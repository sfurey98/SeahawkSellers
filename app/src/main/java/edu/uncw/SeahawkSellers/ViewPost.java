package edu.uncw.SeahawkSellers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewPost extends AppCompatActivity {
    private static final String TAG = "ViewPost";
    private static final String ITEM = "Items";
    private FirebaseAuth mAuth;
    private TextView titleEdit;
    private TextView descriptionEdit;
    private TextView priceEdit;
    private TextView sellerEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpost);
        Intent intent = getIntent();
        mAuth = FirebaseAuth.getInstance();
        titleEdit = findViewById(R.id.title_edit);
        descriptionEdit = findViewById(R.id.description_edit);
        priceEdit = findViewById(R.id.price_edit);
        sellerEdit= findViewById(R.id.seller_edit);
        String title = intent.getStringExtra("TITLE");
        String description = intent.getStringExtra("DESCRIPTION");
        String price = intent.getStringExtra("PRICE");
        String seller = intent.getStringExtra("SELLER");
        titleEdit.setText(title);
        descriptionEdit.setText(description);
        priceEdit.setText("$" +price);
        sellerEdit.setText(seller);
    }
}
