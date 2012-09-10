
package com.kii.launcher.wall.popups;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.kii.launcher.R;

public class WallPopUp extends PopupWindow {
    
    public WallPopUp( Context context, int layoutResource, View clickView, int width, int height ) {
    
        super(((Activity) context).getLayoutInflater().inflate(layoutResource, null, false), width, height, false);
        
        int showX, showY;
        int viewLocation[] = new int[2];
        Point screenSize = new Point();
        
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        display.getSize(screenSize);
        
        setAnimationStyle(R.style.WallPopUpAnimation);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        
        clickView.getLocationOnScreen(viewLocation);
        
        showX = viewLocation[0] - screenSize.x / 2;
        showY = 0 - screenSize.y / 2 + 60 + height / 2;
        
        showAtLocation(clickView.getRootView(), Gravity.CENTER, showX, showY);
    }
}
