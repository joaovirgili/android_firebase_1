package Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joaovirgili.projetofirebase1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import DAO.FirebaseConfiguration;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button btnLogout;

    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;

    AlertDialog.Builder logoutAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = findViewById(R.id.textView);
        btnLogout = findViewById(R.id.btnLogout);

        firebaseAuth = FirebaseConfiguration.getFirebaseAuth();
        currentUser = firebaseAuth.getCurrentUser();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutAlert.show();
            }
        });

        logoutAlert = new AlertDialog.Builder(MainActivity.this);
        logoutAlert.setTitle("Sair");
        logoutAlert.setMessage("Deseja sair?");
        logoutAlert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutUser();
                dialog.dismiss();
            }
        });
        logoutAlert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        logoutAlert.show();
    }

    private void logoutUser() {
        firebaseAuth.signOut();
        finish();
        toast("Logout efetuado com sucesso.");
        startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    private void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        textView.setText(currentUser.getEmail());
    }



}
