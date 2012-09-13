
package com.kii.launcher;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class KiiTextView extends TextView {
    
    private final Typeface fontNormal = Typeface.createFromAsset(getContext().getAssets(), "fonts/Helvetica-normal.ttf");
    private final Typeface fontBold   = Typeface.createFromAsset(getContext().getAssets(), "fonts/Helvetica-bold.ttf");
    
    public KiiTextView( Context context ) {
    
        super(context);
    }
    
    public KiiTextView( Context context, AttributeSet attrs, int defStyle ) {
    
        super(context, attrs, defStyle);
        
    }
    
    public KiiTextView( Context context, AttributeSet attrs ) {
    
        super(context, attrs);
    }
    
    @Override
    public void setTypeface( Typeface tf, int style ) {
    
        if (style == Typeface.BOLD) {
            super.setTypeface(fontBold, style);
        } else {
            super.setTypeface(fontNormal, style);
        }
    }
    
}
