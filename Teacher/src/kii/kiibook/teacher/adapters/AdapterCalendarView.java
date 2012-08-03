
package kii.kiibook.teacher.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import kii.kiibook.managerclass.objects.TextElem;
import kii.kiibook.managerclass.utils.HourCalendarListView;
import kii.kiibook.teacher.R;

public class AdapterCalendarView extends BaseAdapter {
    
    private final LayoutInflater                  mInflater;
    private final ArrayList<HourCalendarListView> itens;
    private final Context                         context;
    
    public AdapterCalendarView( Context context, ArrayList<HourCalendarListView> arrayList ) {
    
        itens = arrayList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }
    

    public int getCount() {
    
        return itens.size();
    }
    

    public HourCalendarListView getItem( int position ) {
    
        return itens.get(position);
    }
    

    public long getItemId( int position ) {
    
        return position;
    }
    

    public View getView( int position, View view, ViewGroup parent ) {
    
        HourCalendarListView item = itens.get(position);
        LinearLayout listElem;
        
        /*
        if (view == null) {
            view = mInflater.inflate(R.layout.hour_item_calendar, null);
            listElem = (LinearLayout) view.findViewById(R.id._calendar_hour_elements);
        } else {
            listElem = (LinearLayout) view.findViewById(R.id._calendar_hour_elements);
            for (int index = 0; index < listElem.getChildCount(); index++) {
                TextView text = (TextView) listElem.getChildAt(index);
                text.setText("");
            }
        }*/
        
        view = mInflater.inflate(R.layout.hour_item_calendar, null);
        listElem = (LinearLayout) view.findViewById(R.id._calendar_hour_elements);
        ((TextView) view.findViewById(R.id.text_hour)).setText(item.getHour());
        
        Iterator<TextElem> it = item.getElemInHour().iterator();
        if (item.getElemInHour().size() != 0) {
            while (it.hasNext()) {
                TextView elemsInHour = new TextView(context);
                TextElem elem = it.next();
                elemsInHour.setText(elem.getText());
                elemsInHour.setTextColor(elem.getColof());
                listElem.addView(elemsInHour);
            }
        }
        return view;
    }
}
