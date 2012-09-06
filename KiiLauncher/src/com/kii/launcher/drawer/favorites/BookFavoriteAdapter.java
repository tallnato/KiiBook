
package com.kii.launcher.drawer.favorites;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.launcher.R;
import com.kii.launcher.drawer.favorites.database.BookFavoriteDataSource;
import com.kii.launcher.drawer.util.LibraryItem;

import java.io.File;
import java.util.List;

public class BookFavoriteAdapter {
    
    private final Context                context;
    private List<BookFavoriteItem>       objects;
    private final String                 title;
    private final BookFavoriteDataSource booksDataSource;
    
    private final int                    NUM_HORIZONTAL_BOOKS;
    
    public BookFavoriteAdapter( Context context ) {
    
        this.context = context;
        title = context.getResources().getString(R.string.drawer_menu_library);
        booksDataSource = new BookFavoriteDataSource(context);
        
        booksDataSource.open();
        objects = booksDataSource.getAllBooks();
        booksDataSource.close();
        
        NUM_HORIZONTAL_BOOKS = context.getResources().getInteger(R.integer.drawer_favorites_books_horizontal_count);
        
    }
    
    public String getTitle() {
    
        return title;
    }
    
    public void add( BookFavoriteItem item ) {
    
        if (!objects.contains(item)) {
            booksDataSource.open();
            booksDataSource.createFavoriteBook(item.getLibraryItem());
            objects = booksDataSource.getAllBooks();
            booksDataSource.close();
        }
    }
    
    public boolean isEmpty() {
    
        return objects.isEmpty();
    }
    
    public void remove( BookFavoriteItem item ) {
    
        if (objects.contains(item)) {
            booksDataSource.open();
            booksDataSource.deleteFavoriteBook(item.getId());
            objects = booksDataSource.getAllBooks();
            booksDataSource.close();
        }
    }
    
    public View getView( View convertView, ViewGroup parent ) {
    
        convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_kii_drawer_favorites_item, parent, false);
        
        TextView section = (TextView) convertView.findViewById(R.id.fragment_kii_drawer_favorites_section_title);
        TableLayout list = (TableLayout) convertView.findViewById(R.id.fragment_kii_drawer_favorites_section_list);
        
        section.setText(title);
        
        TableRow row = new TableRow(context);
        
        for (int i = 0; i < objects.size(); i++) {
            
            BookFavoriteItem item = objects.get(i);
            final LibraryItem book = item.getLibraryItem();
            
            LinearLayout ll = (LinearLayout) ((Activity) context).getLayoutInflater().inflate(
                                            R.layout.fragment_kii_drawer_favorites_library_iconlayout, list, false);
            
            final ImageView iv = (ImageView) ll.findViewById(R.id.fragment_kii_drawer_favorites_library_iconlayout_image);
            TextView tv = (TextView) ll.findViewById(R.id.fragment_kii_drawer_favorites_library_iconlayout_name);
            
            if (book.getIcon() != null) {
                iv.setImageDrawable(book.getIcon());
            } else {
                iv.setImageResource(android.R.color.darker_gray);
            }
            tv.setText(book.getName());
            
            ll.setTag(item);
            iv.setTag(item);
            
            ll.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick( View view ) {
                
                    BookFavoriteItem li = (BookFavoriteItem) view.getTag();
                    
                    Toast.makeText(context, "abrir o livro " + li.getLibraryItem().getName() + " ...", Toast.LENGTH_SHORT).show();
                }
            });
            
            ll.setOnLongClickListener(new OnLongClickListener() {
                
                @Override
                public boolean onLongClick( View view ) {
                
                    ClipData data = ClipData.newPlainText("", "");
                    DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(iv);
                    
                    view.startDrag(data, shadowBuilder, iv.getTag(), 0);
                    
                    return true;
                }
            });
            
            new Thread(new Runnable() {
                
                @Override
                public void run() {
                
                    File bookDir = new File(book.getPath());
                    if (!bookDir.exists() && !bookDir.isDirectory()) {
                        // remove cenas
                    }
                    
                    File image = new File(book.getPath() + "/cover.jpeg");
                    if (!image.exists()) {
                        image = new File(book.getPath() + "/cover.jpg");
                    } else if (!image.exists()) {
                        image = new File(book.getPath() + "/cover.png");
                    }
                    
                    Drawable icon = Drawable.createFromPath(image.getAbsolutePath());
                    if (icon != null) {
                        iv.setImageDrawable(icon);
                    }
                    
                }
            }).run();
            
            row.addView(ll, new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            if ((i + 1) % NUM_HORIZONTAL_BOOKS == 0) {
                list.addView(row);
                row = new TableRow(context);
            }
        }
        list.addView(row);
        
        return convertView;
    }
    
    private class BookImageRetriver extends AsyncTask<BookFavoriteItem, BookFavoriteItem, Void> {
        
        @Override
        protected Void doInBackground( BookFavoriteItem... params ) {
        
            return null;
        }
        
        @Override
        protected void onProgressUpdate( BookFavoriteItem... progress ) {
        
        }
        
        @Override
        public void onPostExecute( Void result ) {
        
        }
        
    }
}
