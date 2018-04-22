package com.example.joaovirgili.projetofirebase1.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.example.joaovirgili.projetofirebase1.Classes.User;

public class AddInformationActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    ImageView imageUpload;
    Bitmap profileImage;
    ProgressBar progressImageUpload;
    String profileImageUrl;

    TextView firstName;
    TextView lastName;

    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("user");
        firebaseAuth = FirebaseAuth.getInstance();

        imageUpload = findViewById(R.id.imageUpload);
        profileImage = null;
        profileImageUrl = "";
        progressImageUpload = findViewById(R.id.progressImageUpload);

        firstName = findViewById(R.id.inputFirstName);
        lastName = findViewById(R.id.inputLastName);

        btnSave = findViewById(R.id.btnSaveProfile);

        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });

        checkExtras();
    }

    private void checkExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("firstLogin") && extras.getBoolean("firstLogin")) {
            Snackbar.make(findViewById(R.id.infoLayout), "Conta criada, adicione mais informações ao cadsatro.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void saveUserInformation() {
        final String displayFirstName = firstName.getText().toString();
        final String displayLastName = lastName.getText().toString();
        if (displayFirstName.isEmpty()) {
            firstName.requestFocus();
            firstName.setError("Campo requerido.");
        }
        else if (displayLastName.isEmpty()) {
            lastName.requestFocus();
            lastName.setError("Campo requerido.");
        } else {
            progressImageUpload.setVisibility(View.VISIBLE);
            user = firebaseAuth.getCurrentUser();
            if (user != null) {
                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(displayFirstName).build();

                user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            insertIntoDatabase(displayFirstName, displayLastName, profileImage);
                            progressImageUpload.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Informações salvas.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddInformationActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                    }
                });
            }
        }
    }

    private void insertIntoDatabase(String firstName, String lastName, Bitmap uriProfileImage) {
        //Convert bitmap to insert into database (base64)
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        uriProfileImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String profileImageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

        //Create user id in database
        String id = databaseReference.push().getKey();

        //Insert user into database
        if (firebaseAuth.getCurrentUser() != null) {
            User user = new User(id, firebaseAuth.getCurrentUser().getEmail(), firstName, lastName, profileImageBase64);
            databaseReference.child(id).setValue(user);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uriImage = data.getData();
            try {
                profileImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImage);
                imageUpload.setImageBitmap(profileImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE);
    }
}