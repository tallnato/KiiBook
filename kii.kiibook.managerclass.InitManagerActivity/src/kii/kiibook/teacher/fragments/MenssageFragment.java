
package kii.kiibook.teacher.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


import java.util.ArrayList;

import kii.kiibook.managerclass.utils.ItemListView;
import kii.kiibook.teacher.R;
import kii.kiibook.teacher.adapters.AdapterListView;

public class MenssageFragment extends Fragment implements OnItemClickListener {
    
    private LayoutInflater          inflater;
    private ListView                listBox;
    private ArrayList<ItemListView> itens;
    private AdapterListView         adapterListView;
    private String                  turma;
    private Dialog                  dialog;
    private ListView                listBoxMessage;
    private ArrayList<ItemListView> itensMsg;
    private AdapterListView         adapterListViewMsg;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        View view = inflater.inflate(R.layout.menssage_fragment, container, false);
        
        this.inflater = inflater;
        
        // Pega a referencia do ListView
        listBox = (ListView) view.findViewById(R.id.listBoxMessage);
        
        // Define o Listener quando alguem clicar no item.
        listBox.setOnItemClickListener(this);
        
        // Pega a referencia do ListView
        listBoxMessage = (ListView) view.findViewById(R.id.listMessages);
        
        // Define o Listener quando alguem clicar no item.
        listBoxMessage.setOnItemClickListener(this);
        
        createListView();
        return view;
    }
    
    private void createListView() {
    
        // Criamos nossa lista que preenchera o ListView
        itens = new ArrayList<ItemListView>();
        String[] listClass = getResources().getStringArray(R.array.list_boxes);
        
        for (String str : listClass) {
            
            ItemListView item1 = new ItemListView(str);
            itens.add(item1);
        }
        itensMsg = new ArrayList<ItemListView>();
        listClass = getResources().getStringArray(R.array.list_messages);
        
        for (String str : listClass) {
            
            ItemListView item1 = new ItemListView(str);
            itensMsg.add(item1);
        }
        
        // Cria o adapter
        adapterListView = new AdapterListView(getActivity(), itens);
        adapterListViewMsg = new AdapterListView(getActivity(), itensMsg);
        
        // Define o Adapter
        listBox.setAdapter(adapterListView);
        listBoxMessage.setAdapter(adapterListViewMsg);
        
        // Cor quando a lista é selecionada para ralagem.
        listBox.setCacheColorHint(Color.TRANSPARENT);
        listBoxMessage.setCacheColorHint(Color.TRANSPARENT);
    }

    public void onItemClick( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
    
        // Pega o item que foi selecionado.
        ItemListView item = adapterListView.getItem(arg2);
        
    }
}
