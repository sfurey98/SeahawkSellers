package edu.uncw.SeahawkSellers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomePage extends AppCompatActivity {
    private static final String ITEM = "Items";
    private static final String TAG = "HomePage";
    private FirebaseAuth mAuth;
    private TextView mNameLabel;
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private ItemRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Intent intent = getIntent();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mNameLabel = findViewById(R.id.hello);
        mNameLabel.setText(String.format(getResources().getString(R.string.hello), currentUser.getEmail()));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        Query query = mDb.collection(ITEM);
        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();

        mAdapter = new ItemRecyclerAdapter(options, new ItemRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Item item = mAdapter.getSnapshots().getSnapshot(position).toObject(Item.class);
                String id = mAdapter.getSnapshots().getSnapshot(position).getId();
                mDb.collection(ITEM).document(id).delete();
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    public void signOut(View view) {
        mAuth.signOut();
        Intent intent = new Intent(HomePage.this, MainActivity.class);
        startActivity(intent);
    }

    public void newPost(View view) {
        Intent intent = new Intent(HomePage.this, NewPost.class);
        startActivity(intent);
    }

}
