package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joaovirgili.projetofirebase1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    TextView tvEmail;
    TextView tvPassword;
    TextView tvConfirmPassword;
    TextView tvLogin;

    ProgressBar progressRegister;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        tvEmail = findViewById(R.id.inputEmailForm);
        tvEmail.requestFocus();
        tvPassword = findViewById(R.id.inputPasswordForm);
        tvConfirmPassword = findViewById(R.id.inputConfirmPasswordForm);
        tvLogin = findViewById(R.id.textLogin);

        progressRegister = findViewById(R.id.progressRegister);
        btnRegister = findViewById(R.id.btnFormRegister);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInputs()) {
                    btnRegister.setClickable(false);
                    progressRegister.setVisibility(View.VISIBLE);
                    registerUser(tvEmail.getText().toString(), tvPassword.getText().toString());
                }
            }
        });
    }

    private boolean checkInputs() {
        if (tvEmail.getText().toString().isEmpty()) {
            tvEmail.requestFocus();
            tvEmail.setError("Campo e-mail requerido.");
            return false;
        } else if (tvPassword.getText().toString().length() < 6) {
            tvPassword.requestFocus();
            tvPassword.setError("Senha curta, mínimo de 6 caracteres.");
            return false;
        } else if (tvPassword.getText().toString().isEmpty()) {
            tvPassword.requestFocus();
            tvPassword.setError("Campo senha requerido.");
            return false;
        } else if (tvConfirmPassword.getText().toString().isEmpty()) {
            tvConfirmPassword.requestFocus();
            tvConfirmPassword.setError("Campo confirmar senha requerido.");
            return false;
        } else if (!tvConfirmPassword.getText().toString().equals(tvPassword.getText().toString())) {
            tvConfirmPassword.requestFocus();
            tvConfirmPassword.setError("Senhas não conferem.");
            return false;
        }
        return true;
    }

    private void registerUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                btnRegister.setClickable(true);
                progressRegister.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(RegisterActivity.this, AddInformationActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("firstLogin", true));
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException)
                        toast("E-mail já cadastrado.");
                    else if (task.getException() != null)
                        toast(task.getException().getMessage());
                }
            }
        });
    }

    private void toast(String text) {
        Toast.makeText(RegisterActivity.this, text, Toast.LENGTH_SHORT).show();
    }

}
