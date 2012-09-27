
package kii.kiibook.ClassMode.adapters_items;

import objects.Data;

import java.util.ArrayList;

public class ItemListViewHistorical {
    
    private Data      data;
    private ArrayList list;
    
    public ItemListViewHistorical( Data data, ArrayList list ) {
    
        this.data = data;
        this.list = list;
    }
    
    public Data getData() {
    
        return data;
    }
    
    public ArrayList getList() {
    
        return list;
    }
    
    public void setData( Data data ) {
    
        this.data = data;
    }
    
    public void setList( ArrayList list ) {
    
        this.list = list;
    }
    
}
