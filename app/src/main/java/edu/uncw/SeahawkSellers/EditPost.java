package edu.uncw.SeahawkSellers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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


public class EditPost extends AppCompatActivity {
    private static final String TAG = "EditPost";
    private static final String ITEM = "Items";
    private FirebaseAuth mAuth;
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private EditText titleEdit;
    private EditText descriptionEdit;
    private EditText priceEdit;
    private ItemRecyclerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpost);
        Intent intent = getIntent();
        mAuth = FirebaseAuth.getInstance();
        titleEdit = findViewById(R.id.title_display);
        descriptionEdit = findViewById(R.id.description_display);
        priceEdit = findViewById(R.id.price_display);
        String title = intent.getStringExtra("TITLE");
        String description = intent.getStringExtra("DESCRIPTION");
        String price = intent.getStringExtra("PRICE");
        titleEdit.setText(title);
        descriptionEdit.setText(description);
        priceEdit.setText(price);
    }
    public void editItem(View v) {
        if (!validateForm()) {
            return;
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String user= currentUser.getEmail();
        String title = titleEdit.getText().toString();
        String description = descriptionEdit.getText().toString();
        String price = priceEdit.getText().toString();

        Item newItem = new Item(title, description, price, user);

        Toast.makeText(this, "Updating " + title, Toast.LENGTH_SHORT).show();
        mDb.collection(ITEM)
                .add(newItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Item updated with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating item", e);
                    }
                });
        Intent intent = new Intent(EditPost.this, HomePage.class);
        startActivity(intent);
    }

    private boolean validateForm() {
        boolean valid = true;

        String title = titleEdit.getText().toString();
        if (TextUtils.isEmpty(title)) {
            titleEdit.setError("Required.");
            valid = false;
        } else {
            titleEdit.setError(null);
        }

        String description = descriptionEdit.getText().toString();
        if (TextUtils.isEmpty(description)) {
            descriptionEdit.setError("Required.");
            valid = false;
        } else {
            descriptionEdit.setError(null);
        }

        String price = priceEdit.getText().toString();
        if (TextUtils.isEmpty(price)) {
            priceEdit.setError("Required.");
            valid = false;
        } else {
            priceEdit.setError(null);
        }

        return valid;
    }




}