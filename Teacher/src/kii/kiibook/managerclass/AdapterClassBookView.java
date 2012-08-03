package kii.kiibook.managerclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import kii.kiibook.managerclass.utils.ItemListView;
import kii.kiibook.teacher.R;


public class AdapterClassBookView extends BaseAdapter {
    
    private final LayoutInflater mInflater;
    private final ArrayList      itens;
    
    public AdapterClassBookView( Context context, ArrayList arrayList ) {
    
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

    public ItemListView getItem( int position ) {
    
        return (ItemListView) itens.get(position);
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
        ItemListView item = (ItemListView) itens.get(position);
        // infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.item_classbook, null);
        
        // atravez do layout pego pelo LayoutInflater, pegamos cada id
        // relacionado
        // ao item e definimos as informações.
        //((TextView) view.findViewById(R.id.text)).setText(item.getTexto());
        //ImageView image = ((ImageView) view.findViewById(R.id.imagemview));
        //image.setImageResource(item.getIconeRid());
        
        return view;
    }
}
