
package com.kii.applocker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NotifDialog extends Activity {
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.notif_activity);
        
        setFinishOnTouchOutside(false);
        Button bt = (Button) findViewById(R.id.notif_activity_bt_ok);
        bt.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Intent intent = new Intent();
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_FORWARD_RESULT
                                                | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
                                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                
                startActivity(intent);
                finish();
            }
        });
        
    }
    
    @Override
    public void onBackPressed() {
    
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_FORWARD_RESULT | Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        
        startActivity(intent);
        finish();
    }
}
