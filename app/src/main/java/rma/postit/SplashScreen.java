package rma.postit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rma.postit.helper.FirebaseConnector;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        checkAndLoadNextActivity();
    }

    private void checkAndLoadNextActivity() {
        if( FirebaseConnector.getInstance().isUserLoggedIn() ){
            // SHOW MAIN! :D
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // SHOW LOGIN
            Intent intent = new Intent( this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
