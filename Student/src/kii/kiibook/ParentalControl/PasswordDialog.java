
package kii.kiibook.ParentalControl;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import kii.kiibook.Student.R;

public class PasswordDialog extends Activity {
    
    private PasswordDialog me;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.password_activity);
        
        setFinishOnTouchOutside(false);
        
        me = this;
        
        EditText pass = (EditText) findViewById(R.id.password_activity_password);
        Button ok = (Button) findViewById(R.id.password_activity_bt_ok);
        Button cancel = (Button) findViewById(R.id.password_activity_bt_cancel);
        
        pass.setOnEditorActionListener(new OnEditorActionListener() {
            
            public boolean onEditorAction( TextView v, int actionId, KeyEvent event ) {
            
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    
                    checkPassword(v);
                }
                
                return true;
            }
        });
        
        ok.setOnClickListener(new View.OnClickListener() {
            
            public void onClick( View v ) {
            
                checkPassword(v);
            }
        });
        
        cancel.setOnClickListener(new View.OnClickListener() {
            
            public void onClick( View v ) {
            
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
    
    private boolean checkPassword( View v ) {
    
        EditText pass = (EditText) v.getRootView().findViewById(R.id.password_activity_password);
        if (pass.getText().toString().equals("1234")) {
            setResult(RESULT_OK);
            
            me.finish();
            
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
            
            return false;
        }
    }
}
