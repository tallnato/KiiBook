
package com.kii.launcher.drawer.apps.database;

import com.kii.database.IAppTableDB;

public class KiiListTable implements IAppTableDB {
    
    public static final String  TABLE_NAME      = "kiiList";
    public static final String  COLUMN_ID       = "_id";
    
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
