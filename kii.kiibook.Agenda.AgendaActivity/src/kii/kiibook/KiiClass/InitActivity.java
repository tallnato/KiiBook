
package kii.kiibook.KiiClass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import kii.kiibook.Student.R;

public class InitActivity extends Activity implements OnClickListener {
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kiiclass_layout);
        RelativeLayout linear = (RelativeLayout) findViewById(R.id.clickme);
        linear.setOnClickListener(this);
    }
    
    public void onClick( View v ) {
    
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    
}
