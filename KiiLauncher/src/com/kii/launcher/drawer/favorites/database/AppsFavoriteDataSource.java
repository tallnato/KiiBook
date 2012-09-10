
package com.kii.launcher.drawer.favorites.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.kii.database.DBDataSource;
import com.kii.launcher.drawer.apps.database.AppsListDataSource;
import com.kii.launcher.drawer.apps.database.AppsListTable;
import com.kii.launcher.drawer.favorites.AppFavoriteItem;

import java.util.ArrayList;
import java.util.List;

public class AppsFavoriteDataSource {
    
    // Database fields
    private SQLiteDatabase     database;
    private final DBDataSource dbHelper;
    
    public AppsFavoriteDataSource( Context context ) {
    
        dbHelper = new DBDataSource(context);
    }
    
    public void open() throws SQLException {
    
        database = dbHelper.getWritableDatabase();
    }
    
    public void close() {
    
        dbHelper.close();
    }
    
    public void createFavoriteApp( int id ) {
    
        ContentValues values = new ContentValues();
        values.put(AppFavoriteTable.COLUMN_ID, id);
        
        database.insert(AppFavoriteTable.TABLE_NAME, null, values);
    }
    
    public void deleteFavoriteApp( int id ) {
    
        database.delete(AppFavoriteTable.TABLE_NAME, AppFavoriteTable.COLUMN_ID + " = " + id, null);
    }
    
    public List<AppFavoriteItem> getAllApps() {
    
        List<AppFavoriteItem> list = new ArrayList<AppFavoriteItem>();
        Cursor cursor;
        try {
            String MY_QUERY = "SELECT * FROM " + AppsListTable.TABLE_NAME + " A INNER JOIN " + AppFavoriteTable.TABLE_NAME + " F ON F."
                                            + AppFavoriteTable.COLUMN_ID + " = A." + AppsListTable.COLUMN_ID + " ORDER BY A."
                                            + AppsListTable.COLUMN_LABEL;
            
            cursor = database.rawQuery(MY_QUERY, null);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return list;
        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(new AppFavoriteItem(AppsListDataSource.cursorToPackagePermissions(cursor)));
                cursor.moveToNext();
            }
        }
        
        cursor.close();
        
        return list;
    }
}
