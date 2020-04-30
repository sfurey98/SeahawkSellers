package edu.uncw.SeahawkSellers;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomePage extends AppCompatActivity {
    private static final String ITEM = "Items";
    private static final String TAG = "HomePage";
    private FirebaseAuth mAuth;
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private ItemRecyclerAdapter mAdapter;
    private EditText searchBar;
    private String email;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        email = currentUser.getEmail();
        searchBar= findViewById(R.id.search_edit);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        Query query = mDb.collection(ITEM);
        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();

        mAdapter = new ItemRecyclerAdapter(options, new ItemRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Item item = mAdapter.getSnapshots().getSnapshot(position).toObject(Item.class);
                if (item.getSeller().equals(email)) {
                    String id = mAdapter.getSnapshots().getSnapshot(position).getId();
                    editPost(item, id);
                } else {
                    viewPost(item);
                }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newPost:
                newPost();
                return true;

            case R.id.myPost:
                myPost();
                return true;

            case R.id.logOut:
                signOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(HomePage.this, MainActivity.class);
        startActivity(intent);
    }

    public void newPost() {
        Intent intent = new Intent(HomePage.this, NewPost.class);
        startActivity(intent);
    }

    public void editPost(Item item, String id) {
        Intent intent = new Intent(HomePage.this, EditPost.class);
        String title = item.getTitle();
        String description = item.getDescription();
        String price = item.getPrice();
        intent.putExtra("TITLE", title);
        intent.putExtra("DESCRIPTION", description);
        intent.putExtra("PRICE", price);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

    public void viewPost(Item item) {
        Intent intent = new Intent(HomePage.this, ViewPost.class);
        String title = item.getTitle();
        String description = item.getDescription();
        String price = item.getPrice();
        String seller = item.getSeller();
        intent.putExtra("TITLE", title);
        intent.putExtra("DESCRIPTION", description);
        intent.putExtra("PRICE", price);
        intent.putExtra("SELLER", seller);
        startActivity(intent);
    }

    public void myPost() {
        Intent intent = new Intent(HomePage.this, MyPosts.class);
        startActivity(intent);
    }

    public void search(View v){
        String searched = searchBar.getText().toString().trim();
        if (searched.equals("")){
            Query query = mDb.collection(ITEM);
            FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                    .setQuery(query, Item.class)
                    .build();
            mAdapter.updateOptions(options);
        }
        else {
            Query query = mDb.collection(ITEM).whereEqualTo("title", searched);
            FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                    .setQuery(query, Item.class)
                    .build();
            mAdapter.updateOptions(options);
        }
        recyclerView.setAdapter(mAdapter);
    }

    public void clear(View v){
        searchBar.setText("");
        Query query = mDb.collection(ITEM);
        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();
        mAdapter.updateOptions(options);
        recyclerView.setAdapter(mAdapter);
    }

}


