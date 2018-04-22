package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class LoginActivity extends AppCompatActivity  {

    private FirebaseAuth firebaseAuth;

    private EditText edtEmail;
    private EditText edtPassword;
    private ProgressBar progressLogin;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        edtEmail = findViewById(R.id.inputEmail);
        edtPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        progressLogin = findViewById(R.id.progressLogin);
        TextView tvSignUp = findViewById(R.id.textRegister);

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
        startActivity(new Intent(LoginActivity.this, ProfileActivity.class).putExtra("login", true));
    }

}
