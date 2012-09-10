
package kii.kiibook.Student.adapters_items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import objects.TextElem;

import java.util.ArrayList;
import java.util.Iterator;

import kii.kiibook.Agenda.NextEventsListView;
import kii.kiibook.Student.R;

public class AdapterCalendarView extends BaseAdapter {
    
    private final LayoutInflater                mInflater;
    private final ArrayList<NextEventsListView> itens;
    private final Context                       context;
    
    public AdapterCalendarView( Context context, ArrayList<NextEventsListView> arrayList ) {
    
        itens = arrayList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }
    
    public int getCount() {
    
        return itens.size();
    }
    
    public NextEventsListView getItem( int position ) {
    
        return itens.get(position);
    }
    
    public long getItemId( int position ) {
    
        return position;
    }
    
    public View getView( int position, View view, ViewGroup parent ) {
    
        NextEventsListView item = itens.get(position);
        LinearLayout listElem;
        
        view = mInflater.inflate(R.layout.hour_item_calendar, null);
        listElem = (LinearLayout) view.findViewById(R.id._calendar_hour_elements);
        ((TextView) view.findViewById(R.id.text_hour)).setText(item.getHour());
        
        Iterator<TextElem> it = item.getElemInHour().iterator();
        if (item.getElemInHour().size() != 0) {
            while (it.hasNext()) {
                TextView elemsInHour = new TextView(context);
                TextElem elem = it.next();
                elemsInHour.setText(elem.getText());
                elemsInHour.setBackgroundColor(elem.getColof());
                listElem.addView(elemsInHour);
            }
        }
        return view;
    }
}
