
package com.kii.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kii.launcher.drawer.apps.database.AppsListTable;
import com.kii.launcher.drawer.apps.database.KiiListTable;
import com.kii.launcher.drawer.favorites.database.AppFavoriteTable;
import com.kii.launcher.drawer.favorites.database.BookFavoriteTable;

public class DBDataSource extends SQLiteOpenHelper {
    
    private static final String      DATABASE_NAME    = "KiiLauncher.db";
    private static final int         DATABASE_VERSION = 2;
    
    private static final IAppTableDB list[]           = { new AppsListTable(), new KiiListTable(), new AppFavoriteTable(),
                                    new BookFavoriteTable() };
    
    public DBDataSource( Context context ) {
    
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate( SQLiteDatabase database ) {
    
        database.execSQL("PRAGMA foreign_keys=ON;");
        for (IAppTableDB i : list) {
            database.execSQL(i.getCreationSql());
        }
    }
    
    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
    
        Log.w(AppsListTable.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
                                        + ", which will destroy all old data");
        for (IAppTableDB i : list) {
            db.execSQL("DROP TABLE IF EXISTS " + i.getTableName());
        }
        onCreate(db);
    }
}
