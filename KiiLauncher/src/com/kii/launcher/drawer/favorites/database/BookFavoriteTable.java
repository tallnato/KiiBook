
package com.kii.launcher.drawer.favorites.database;

import com.kii.database.IAppTableDB;
import com.kii.launcher.drawer.apps.database.AppsListTable;

public class BookFavoriteTable implements IAppTableDB {
    
    public static final String  TABLE_NAME      = "bookFavorite";
    public static final String  COLUMN_ID       = "_id";
    public static final String  COLUMN_Is       = "_id";
    
    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_ID + " integer primary key, FOREIGN KEY("
                                                                                + COLUMN_ID + ") REFERENCES " + AppsListTable.TABLE_NAME
                                                                                + "(" + AppsListTable.COLUMN_ID + ") ON DELETE CASCADE );";
    
    @Override
    public String getTableName() {
    
        return TABLE_NAME;
    }
    
    @Override
    public String getCreationSql() {
    
        return DATABASE_CREATE;
    }
}
