package DAO;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConfiguration {
    //Firebase instances
    private static DatabaseReference databaseReference;
    private static FirebaseAuth firebaseAuth;

    //Reference Singleton
    public static DatabaseReference getFirebaseReference() {
        if (databaseReference == null)
            databaseReference = FirebaseDatabase.getInstance().getReference();
        return databaseReference;
    }

    public static FirebaseAuth getFirebaseAuth() {
        if (firebaseAuth == null)
            firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth;
    }


}
