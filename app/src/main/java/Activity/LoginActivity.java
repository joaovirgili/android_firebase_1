package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joaovirgili.projetofirebase1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import DAO.FirebaseConfiguration;

public class LoginActivity extends AppCompatActivity  {

    private FirebaseAuth firebaseAuth;

    private EditText edtEmail;
    private EditText edtPassword;
    private TextView tvSignUp;
    private ProgressBar progressLogin;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        firebaseAuth = FirebaseConfiguration.getFirebaseAuth();
        edtEmail = findViewById(R.id.inputEmail);
        edtPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        progressLogin = findViewById(R.id.progressLogin);
        tvSignUp = findViewById(R.id.textRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (checkInputs()) {
                    progressLogin.setVisibility(View.VISIBLE);
                    btnLogin.setClickable(false);
                    loginValidate(edtEmail.getText().toString(), edtPassword.getText().toString());
                }
            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        verifyUser();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkInputs() {
        if (edtEmail.getText().toString().isEmpty()) {
            edtEmail.requestFocus();
            edtEmail.setError("Campo e-mail requerido.");
            return false;
        }
        if (edtPassword.getText().toString().length() < 6) {
            edtPassword.requestFocus();
            edtPassword.setError("Senha curta, mínimo de 6 caracteres.");
            return false;
        }
        if (edtPassword.getText().toString().isEmpty()) {
            edtPassword.requestFocus();
            edtPassword.setError("Campo senha requerido.");
            return false;
        }
        return true;
    }

    private void loginValidate(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressLogin.setVisibility(View.GONE);
                btnLogin.setClickable(true);
                if (task.isSuccessful()) {
                    loginSucceeded();
                }
                else
                    Toast.makeText(LoginActivity.this, "E-mail ou senha incorretos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            loginSucceeded();
        }
    }

    private void loginSucceeded() {
        Toast.makeText(LoginActivity.this, "Login efetuado com sucesso.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

}
