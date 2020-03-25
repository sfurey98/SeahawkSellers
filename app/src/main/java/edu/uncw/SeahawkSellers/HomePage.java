package edu.uncw.SeahawkSellers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView mNameLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Intent intent = getIntent();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mNameLabel = findViewById(R.id.hello);
        mNameLabel.setText(String.format(getResources().getString(R.string.hello), currentUser.getEmail()));
    }

        public void signOut(View view) {
            mAuth.signOut();
            Intent intent = new Intent(HomePage.this, MainActivity.class);
            startActivity(intent);
        }
}
