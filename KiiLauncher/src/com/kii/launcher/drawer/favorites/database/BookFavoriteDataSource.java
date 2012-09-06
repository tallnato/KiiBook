
package com.kii.launcher.drawer.favorites.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.kii.database.DBDataSource;
import com.kii.launcher.drawer.favorites.BookFavoriteItem;
import com.kii.launcher.drawer.util.LibraryItem;

import java.util.ArrayList;
import java.util.List;

public class BookFavoriteDataSource {
    
    // Database fields
    private SQLiteDatabase     database;
    private final DBDataSource dbHelper;
    
    public BookFavoriteDataSource( Context context ) {
    
        dbHelper = new DBDataSource(context);
    }
    
    public void open() throws SQLException {
    
        database = dbHelper.getWritableDatabase();
    }
    
    public void close() {
    
        dbHelper.close();
    }
    
    public void createFavoriteBook( String label, String path ) {
    
        ContentValues values = new ContentValues();
        values.put(BookFavoriteTable.COLUMN_NAME, label);
        values.put(BookFavoriteTable.COLUMN_PATH, path);
        
        database.insert(BookFavoriteTable.TABLE_NAME, null, values);
    }
    
    public void createFavoriteBook( LibraryItem item ) {
    
        ContentValues values = new ContentValues();
        values.put(BookFavoriteTable.COLUMN_NAME, item.getName());
        values.put(BookFavoriteTable.COLUMN_PATH, item.getPath());
        
        database.insert(BookFavoriteTable.TABLE_NAME, null, values);
    }
    
    public void deleteFavoriteBook( int id ) {
    
        database.delete(BookFavoriteTable.TABLE_NAME, BookFavoriteTable.COLUMN_ID + " = " + id, null);
    }
    
    public List<BookFavoriteItem> getAllBooks() {
    
        List<BookFavoriteItem> list = new ArrayList<BookFavoriteItem>();
        Cursor cursor;
        try {
            String MY_QUERY = "SELECT * FROM " + BookFavoriteTable.TABLE_NAME;
            
            cursor = database.rawQuery(MY_QUERY, null);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return list;
        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursorToLibraryItem(cursor));
                cursor.moveToNext();
            }
        }
        
        cursor.close();
        
        return list;
    }
    
    public static BookFavoriteItem cursorToLibraryItem( Cursor cursor ) {
    
        return new BookFavoriteItem(cursor.getInt(0), new LibraryItem(cursor.getString(1), cursor.getString(2)));
    }
}
