
package com.kii.launcher.drawer.favorites.database;

import com.kii.database.IAppTableDB;

public class BookFavoriteTable implements IAppTableDB {
    
    public static final String  TABLE_NAME      = "bookFavorite";
    public static final String  COLUMN_ID       = "_id";
    public static final String  COLUMN_NAME     = "name";
    public static final String  COLUMN_PATH     = "path";
    
    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_ID + " integer primary key, " + COLUMN_NAME
                                                                                + " text not null, " + COLUMN_PATH + " text not null );";
    
    @Override
    public String getTableName() {
    
        return TABLE_NAME;
    }
    
    @Override
    public String getCreationSql() {
    
        return DATABASE_CREATE;
    }
}
