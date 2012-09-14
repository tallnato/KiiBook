
package kii.kiibook.Agenda;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

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
        TextView date = ((TextView) view.findViewById(R.id.text_hour));
        date.setTextSize(16);
        date.setText(item.getHour());
        
        TextView tema = new TextView(context);
        tema.setText(Html.fromHtml("<b>" + item.getEventType() + "</b>"));
        listElem.addView(tema);
        
        TextView elemsInHour = new TextView(context);
        elemsInHour.setText(item.getElemInHour());
        listElem.addView(elemsInHour);
        return view;
    }
}
