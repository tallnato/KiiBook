
package kii.kiibook.kiimarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends FragmentActivity implements OnClickListener {
    
    private Fragment            fragment;
    private FragmentTransaction transaction;
    private Button              btnGames;
    private Button              btnBooks;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnGames = (Button) findViewById(R.id.button_games);
        btnBooks = (Button) findViewById(R.id.button_books);
        
        btnGames.setOnClickListener(this);
        btnBooks.setOnClickListener(this);
        
        transaction = getSupportFragmentManager().beginTransaction();
        fragment = new GamesFragment();
        transaction.add(R.id.container, fragment, "frag");
        transaction.commit();
    }
    
    public void onClick( View v ) {
    
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.detach(fragment);
        transaction.remove(fragment);
        
        switch (v.getId()) {
            case R.id.button_games:
                fragment = new GamesFragment();
                btnBooks.setBackground(getResources().getDrawable(R.drawable.shape_color));
                btnGames.setBackground(getResources().getDrawable(R.drawable.shape_color_selected));
                break;
            case R.id.button_books:
                fragment = new BooksFragment();
                btnGames.setBackground(getResources().getDrawable(R.drawable.shape_color));
                btnBooks.setBackground(getResources().getDrawable(R.drawable.shape_color_selected));
                break;
        }
        
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(R.id.container, fragment, "frag");
        transaction.commit();
    }
}
