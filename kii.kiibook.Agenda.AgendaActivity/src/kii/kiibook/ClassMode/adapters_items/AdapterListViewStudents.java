
package kii.kiibook.ClassMode.adapters_items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kii.kiibook.Student.R;

public class AdapterListViewStudents extends BaseAdapter {
    
    private final LayoutInflater                 mInflater;
    private final ArrayList<ItemListViewStudent> itens;
    
    public AdapterListViewStudents( Context context, ArrayList<ItemListViewStudent> arrayList ) {
    
        // Itens que preencheram o listview
        itens = arrayList;
        // responsavel por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
    }
    
    /**
     * Retorna a quantidade de itens
     * 
     * @return
     */
    public int getCount() {
    
        return itens.size();
    }
    
    /**
     * Retorna o item de acordo com a posicao dele na tela.
     * 
     * @param position
     * @return
     */
    
    public ItemListViewStudent getItem( int position ) {
    
        return itens.get(position);
    }
    
    /**
     * Sem implementação
     * 
     * @param position
     * @return
     */
    public long getItemId( int position ) {
    
        return position;
    }
    
    public View getView( int position, View view, ViewGroup parent ) {
    
        // Pega o item de acordo com a posção.
        ItemListViewStudent item = itens.get(position);
        
        // infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.itemlistview_students, null);
        
        // atravez do layout pego pelo LayoutInflater, pegamos cada id
        // relacionado
        // ao item e definimos as informações.
        ((TextView) view.findViewById(R.id.classmode_name)).setText(item.getName());
        return view;
    }
}
