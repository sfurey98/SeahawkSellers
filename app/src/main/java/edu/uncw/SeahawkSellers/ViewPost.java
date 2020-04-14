package edu.uncw.SeahawkSellers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewPost extends AppCompatActivity {
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
        titleEdit = findViewById(R.id.title_edit);
        descriptionEdit = findViewById(R.id.description_edit);
        priceEdit = findViewById(R.id.price_edit);
        String title = intent.getStringExtra("TITLE");
        String description = intent.getStringExtra("DESCRIPTION");
        String price = intent.getStringExtra("PRICE");
        titleEdit.setText(title);
        descriptionEdit.setText(description);
        priceEdit.setText(price);
    }
}
