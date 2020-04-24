package edu.uncw.SeahawkSellers;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ViewPost extends AppCompatActivity {
    private TextView titleDisplay;
    private TextView descriptionDisplay;
    private TextView priceDisplay;
    private TextView sellerDisplay;
    private String seller;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpost);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        titleDisplay = findViewById(R.id.title_display);
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        titleDisplay.setTypeface(boldTypeface);
        descriptionDisplay = findViewById(R.id.description_display);
        priceDisplay = findViewById(R.id.price_display);
        sellerDisplay = findViewById(R.id.seller_display);
        title = intent.getStringExtra("TITLE");
        String description = intent.getStringExtra("DESCRIPTION");
        String price = intent.getStringExtra("PRICE");
        seller = intent.getStringExtra("SELLER");
        titleDisplay.setText(title);
        descriptionDisplay.setText(description);
        priceDisplay.setText("USD $" +price);
        sellerDisplay.setText(seller);
    }

    public void contactSeller(View v){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {seller});
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        startActivity(intent);
    }
}
