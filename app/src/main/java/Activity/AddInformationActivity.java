package Activity;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joaovirgili.projetofirebase1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import DAO.FirebaseConfiguration;

public class ProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    ImageView imageUpload;
    Uri uriProfileImage;
    ProgressBar progressImageUpload;
    String profileImageUrl;

    TextView firstName;
    TextView lastName;

    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseConfiguration.getFirebaseAuth();

        imageUpload = findViewById(R.id.imageUpload);
        uriProfileImage = null;
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
                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(displayFirstName).setPhotoUri(uriProfileImage).build();

                user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            insertIntoDatabase(displayFirstName, displayLastName, uriProfileImage);
                            progressImageUpload.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Informações salvas.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void insertIntoDatabase(String firstName, String lastName, Uri uriProfileImage) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageUpload.setImageBitmap(bitmap);

                uploadImageToFirebase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void uploadImageToFirebase() {
        final StorageReference profileImageRef = storageReference.child("profilepcis/" + System.currentTimeMillis() +".jpg");
        if (uriProfileImage != null) {
            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

}
