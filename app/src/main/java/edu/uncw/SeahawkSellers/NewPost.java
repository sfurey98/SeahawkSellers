package edu.uncw.SeahawkSellers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class NewPost extends AppCompatActivity {
    private static final String TAG = "NewPost";
    private static final String ITEM = "Item";
    private FirebaseAuth mAuth;
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private EditText titleEdit;
    private EditText descriptionEdit;
    private EditText priceEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpost);
        Intent intent = getIntent();
        mAuth = FirebaseAuth.getInstance();
        titleEdit = findViewById(R.id.title_edit);
        descriptionEdit = findViewById(R.id.description_edit);
        priceEdit = findViewById(R.id.price_edit);
    }
    public void addItem(View v) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String user= currentUser.getEmail().toString();
        String title = titleEdit.getText().toString();
        String description = descriptionEdit.getText().toString();
        String price = priceEdit.getText().toString();

        Item newItem = new Item(title, description, price, user);

        Toast.makeText(this, "Adding " + title, Toast.LENGTH_SHORT).show();
        mDb.collection(ITEM)
                .add(newItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Item added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding item", e);
                    }
                });
        Intent intent = new Intent(NewPost.this, HomePage.class);
        startActivity(intent);
    }




}
