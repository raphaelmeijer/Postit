package rma.postit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import rma.postit.helper.FirebaseConnector;
import rma.postit.helper.Globals;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginButtonClicked(View v){
        EditText username = findViewById(R.id.email_field);
        EditText password = findViewById(R.id.password_field);

        ProgressDialog pd = Globals.getLoadingDialog(this );
        pd.show();

        FirebaseConnector
                .getInstance()
                .signInWithEmail(username.getText().toString(), password.getText().toString())
                .addOnCompleteListener((task) ->{
                    pd.dismiss();
                    if( task.isSuccessful() ){
                        // we are logged in, proceed!
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        // HIHI
                    } else {
                        // we have failed, show error
                        Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    public void onRegisterButtonClicked(View v){
        EditText username = findViewById(R.id.email_field);
        EditText password = findViewById(R.id.password_field);

        ProgressDialog pd = Globals.getLoadingDialog(this );
        pd.show();

        FirebaseConnector
                .getInstance()
                .signUpWithEmail(username.getText().toString(), password.getText().toString())
                .addOnCompleteListener((task)->{
                    pd.dismiss();
                    if( task.isSuccessful() ){
                        // we are logged in proceed!
                        // create basic stuff :)
                        FirebaseConnector.getInstance().createBaseUserInfo();
                    } else{
                        // we have failed, show error
                        Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
