
package kii.kiibook.ClassMode.adapters_items;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterListViewEvents extends BaseAdapter {
    
    private final LayoutInflater    mInflater;
    private final ArrayList<String> itens;
    private ListView                list;
    private final Context           context;
    
    public AdapterListViewEvents( Context context, ArrayList arrayList ) {
    
        this.context = context;
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
    
    public String getItem( int position ) {
    
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
        String item = itens.get(position);
        
        // infla o layout para podermos preencher os dados
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setText(item);
        
        return textView;
    }
}
