
package com.kii.launcher.drawer.apps.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.kii.database.DBDataSource;
import com.kii.launcher.PackagePermissions;

import java.util.ArrayList;
import java.util.List;

public class KiiListDataSource {
    
    // Database fields
    private SQLiteDatabase     database;
    private final DBDataSource dbHelper;
    
    public KiiListDataSource( Context context ) {
    
        dbHelper = new DBDataSource(context);
    }
    
    public void open() throws SQLException {
    
        database = dbHelper.getWritableDatabase();
    }
    
    public void close() {
    
        dbHelper.close();
    }
    
    public void createKiiApp( int id ) {
    
        ContentValues values = new ContentValues();
        values.put(KiiListTable.COLUMN_ID, id);
        
        database.insert(KiiListTable.TABLE_NAME, null, values);
    }
    
    /*public void deleteComment( Comment comment ) {
    
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }*/
    
    public List<PackagePermissions> getAllApps() {
    
        List<PackagePermissions> list = new ArrayList<PackagePermissions>();
        Cursor cursor;
        try {
            String MY_QUERY = "SELECT * FROM " + AppsListTable.TABLE_NAME + " A INNER JOIN " + KiiListTable.TABLE_NAME + " K ON K."
                                            + KiiListTable.COLUMN_ID + " = A." + AppsListTable.COLUMN_ID + " ORDER BY A."
                                            + AppsListTable.COLUMN_LABEL;
            System.out.println(MY_QUERY);
            
            cursor = database.rawQuery(MY_QUERY, null);
        }
        catch (SQLException e) {
            return null;
        }
        
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(AppsListDataSource.cursorToPackagePermissions(cursor));
            cursor.moveToNext();
        }
        
        cursor.close();
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }
}
