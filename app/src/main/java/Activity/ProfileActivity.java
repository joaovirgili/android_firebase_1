package Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joaovirgili.projetofirebase1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import Classes.User;

public class ProfileActivity extends AppCompatActivity {

    ConstraintLayout userProfileLayout;
    ProgressBar userProfileProgress;

    TextView textName;
    TextView textEmail;
    TextView textLastName;
    Button btnLogout;
    ImageView imageUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;

    AlertDialog.Builder logoutAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userProfileLayout = findViewById(R.id.userProfileLayout);
        userProfileProgress = findViewById(R.id.userProfileProgress);

        textName = findViewById(R.id.textNameProfile);
        textLastName = findViewById(R.id.textLastNameProfile);
        textEmail = findViewById(R.id.textEmailProfile);
        imageUser = findViewById(R.id.userImageProfile);

        btnLogout = findViewById(R.id.btnLogoutProfile);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("user");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutAlert.show();
            }
        });

        logoutAlert = new AlertDialog.Builder(ProfileActivity.this);
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
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("login")) {
            Snackbar.make(findViewById(R.id.userProfileLayout), "Login efetuado com sucesso", Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        logoutAlert.show();
    }

    private void logoutUser() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userProfileLayout.setVisibility(View.VISIBLE);
                userProfileProgress.setVisibility(View.GONE);

                Iterator usersIterator = dataSnapshot.getChildren().iterator();
                while (usersIterator.hasNext()) {
                    DataSnapshot data = (DataSnapshot) usersIterator.next();

                    if (data != null && data.child("email").getValue().toString().equals(currentUser.getEmail())) {
                        User user = new User(data.child("id").getValue().toString(),
                                data.child("email").getValue().toString(),
                                data.child("firstName").getValue().toString(),
                                data.child("lastName").getValue().toString(),
                                data.child("profileImage").getValue().toString()
                        );
                        textName.setText(user.getFirstName());
                        textLastName.setText(user.getLastName());
                        textEmail.setText(user.getEmail());

                        byte[] decodedString = Base64.decode(user.getProfileImage(), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageUser.setImageBitmap(decodedByte);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

}
