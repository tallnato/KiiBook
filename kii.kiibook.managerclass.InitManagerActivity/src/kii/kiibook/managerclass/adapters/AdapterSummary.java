
package kii.kiibook.managerclass.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import objects.MediaBook;
import objects.Summary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kii.kiibook.teacher.R;

public class AdapterSummary extends ArrayAdapter<Summary> {
    
    private final int           layoutResourceId;
    private final Context       context;
    private final List<Summary> objects;
    private Summary             slave;
    private MediaBook           book;
    
    public AdapterSummary( Context context, int viewResourceId, ArrayList<Summary> arrayList ) {
    
        super(context, viewResourceId, arrayList);
        
        layoutResourceId = viewResourceId;
        this.context = context;
        this.objects = arrayList;
        
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        View row = convertView;
        
        slave = getItem(position);
        
        Log.e("", slave.toString());
        
        Iterator<MediaBook> it = slave.getListMedia().iterator();
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            LinearLayout container = (LinearLayout) row.findViewById(R.id.container_links);
            LinearLayout link;
            
            while (it.hasNext()) {
                book = it.next();
                final String name = book.getName();
                final int page = book.getPage();
                
                link = new LinearLayout(row.getContext());
                link.setOrientation(LinearLayout.HORIZONTAL);
                
                TextView point = new TextView(row.getContext());
                point.setText("o");
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                llp.setMargins(0, 0, 5, 0); // llp.setMargins(left, top, right,
                                            // bottom);
                point.setLayoutParams(llp);
                link.addView(point);
                
                TextView view = new TextView(row.getContext());
                view.setText(name + " - " + book.getCap());
                view.setTextColor(row.getResources().getColor(android.R.color.holo_blue_dark));
                view.setOnClickListener(new OnClickListener() {
                    
                    public void onClick( View v ) {
                    
                        Toast.makeText(context, "Livro aberto pelo KiiReader", Toast.LENGTH_SHORT).show();
                        broadcastOpenBook(context, name, page, true);
                        
                    }
                });
                link.addView(view);
                container.addView(link);
            }
            
        }
        
        TextView date = (TextView) row.findViewById(R.id.textView_date_summary);
        TextView text = (TextView) row.findViewById(R.id.textView_desc_summary_item);
        TextView number = (TextView) row.findViewById(R.id.textView_number_summary);
        
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String data = format.format(slave.getDate());
        
        date.setText(data);
        text.setText(slave.getText());
        number.setText("SUMARIO Nº" + slave.getNumber());
        
        return row;
    }
    
    public static void broadcastOpenBook( Context context, String bookName, int page, boolean openOnTop ) {
    
        Log.d("LINK", "broadcastOpenBook");
        Intent i = new Intent("KIIREADER_OPEN_BOOK");
        i.putExtra("NAME", bookName);
        i.putExtra("PAGE", page);
        i.putExtra("ONTOP", openOnTop);
        context.sendBroadcast(i);
    }
}
