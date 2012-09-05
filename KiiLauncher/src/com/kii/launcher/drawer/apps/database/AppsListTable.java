
package com.kii.launcher.drawer.apps.database;

import com.kii.database.IAppTableDB;

public class AppsListTable implements IAppTableDB {
    
    public static final String  TABLE_NAME      = "appsList";
    public static final String  COLUMN_ID       = "_id";
    public static final String  COLUMN_LABEL    = "label";
    public static final String  COLUMN_PACKAGE  = "packageName";
    public static final String  COLUMN_INTENT   = "intentActivity";
    public static final String  COLUMN_ICON     = "icon";
    
    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_ID + " integer primary key autoincrement, "
                                                                                + COLUMN_LABEL + " text not null, " + COLUMN_PACKAGE
                                                                                + " text not null, " + COLUMN_INTENT + " text not null, "
                                                                                + COLUMN_ICON + " BLOB not null " + ");";
    
    @Override
    public String getTableName() {
    
        return TABLE_NAME;
    }
    
    @Override
    public String getCreationSql() {
    
        return DATABASE_CREATE;
    }
}
