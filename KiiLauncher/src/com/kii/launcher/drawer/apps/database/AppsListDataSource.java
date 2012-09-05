
package com.kii.launcher.drawer.apps.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.kii.database.DBDataSource;
import com.kii.launcher.PackagePermissions;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AppsListDataSource {
    
    // Database fields
    private SQLiteDatabase     database;
    private final DBDataSource dbHelper;
    private final String[]     allColumns = { AppsListTable.COLUMN_ID, AppsListTable.COLUMN_LABEL, AppsListTable.COLUMN_PACKAGE,
                                    AppsListTable.COLUMN_INTENT, AppsListTable.COLUMN_ICON };
    
    public AppsListDataSource( Context context ) {
    
        dbHelper = new DBDataSource(context);
    }
    
    public void open() throws SQLException {
    
        database = dbHelper.getWritableDatabase();
    }
    
    public void close() {
    
        dbHelper.close();
    }
    
    public PackagePermissions createPackagePermissions( String label, String packageName, String intent, byte[] icon ) {
    
        ContentValues values = new ContentValues();
        values.put(AppsListTable.COLUMN_LABEL, label);
        values.put(AppsListTable.COLUMN_PACKAGE, packageName);
        values.put(AppsListTable.COLUMN_INTENT, intent);
        values.put(AppsListTable.COLUMN_ICON, icon);
        
        long insertId = database.insert(AppsListTable.TABLE_NAME, null, values);
        Cursor cursor = database.query(AppsListTable.TABLE_NAME, allColumns, AppsListTable.COLUMN_ID + " = " + insertId, null, null, null,
                                        null);
        cursor.moveToFirst();
        PackagePermissions pp = cursorToPackagePermissions(cursor);
        
        cursor.close();
        return pp;
    }
    
    public PackagePermissions createPackagePermissions( String label, String packageName, String intent, Drawable icon ) {
    
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ((BitmapDrawable) icon).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
        
        return createPackagePermissions(label, packageName, intent, stream.toByteArray());
    }
    
    /*public void deleteComment( Comment comment ) {
    
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }*/
    
    public void deletePackage( String packageName ) {
    
        // TODO fix this shit
        int i = database.delete(AppsListTable.TABLE_NAME, AppsListTable.COLUMN_PACKAGE + " = \'" + packageName + "%\'", null);
        System.out.println("Deleted " + i + " icons " + packageName);
    }
    
    public int getCount() {
    
        return (int) DatabaseUtils.queryNumEntries(database, AppsListTable.TABLE_NAME);
    }
    
    public List<PackagePermissions> getAllApps() {
    
        List<PackagePermissions> list = new ArrayList<PackagePermissions>();
        Cursor cursor;
        try {
            cursor = database.query(AppsListTable.TABLE_NAME, allColumns, null, null, null, null, AppsListTable.COLUMN_LABEL);
        }
        catch (SQLException e) {
            return null;
        }
        
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursorToPackagePermissions(cursor));
            cursor.moveToNext();
        }
        
        cursor.close();
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }
    
    public static PackagePermissions cursorToPackagePermissions( Cursor cursor ) {
    
        return new PackagePermissions(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getBlob(4));
    }
}
