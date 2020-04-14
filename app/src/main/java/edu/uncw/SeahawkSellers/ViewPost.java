package edu.uncw.SeahawkSellers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ViewPost extends AppCompatActivity {
    private TextView titleDisplay;
    private TextView descriptionDisplay;
    private TextView priceDisplay;
    private TextView sellerDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpost);
        Intent intent = getIntent();
        titleDisplay = findViewById(R.id.title_display);
        descriptionDisplay = findViewById(R.id.description_display);
        priceDisplay = findViewById(R.id.price_display);
        sellerDisplay = findViewById(R.id.seller_display);
        String title = intent.getStringExtra("TITLE");
        String description = intent.getStringExtra("DESCRIPTION");
        String price = intent.getStringExtra("PRICE");
        String seller = intent.getStringExtra("SELLER");
        titleDisplay.setText(title);
        descriptionDisplay.setText(description);
        priceDisplay.setText("$" +price);
        sellerDisplay.setText(seller);
    }
}
