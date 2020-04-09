package edu.uncw.SeahawkSellers;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView mNameLabel;
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private ItemRecyclerAdapter mAdapter;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private List<Item> itemList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Intent intent = getIntent();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mNameLabel = findViewById(R.id.hello);
        mNameLabel.setText(String.format(getResources().getString(R.string.hello), currentUser.getEmail()));

        mDb.collection("items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            itemList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item item = document.toObject(Item.class);
                                itemList.add(item);
                            }
                        } else {
                            Log.w(TAG, "Error getting items: ", task.getException());
                        }

                    }
                });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        Query query = mDb.collection(ITEMS)
                .orderBy("createdTime", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();

        mAdapter = new ItemRecyclerAdapter(options, new ItemRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Item item = mAdapter.getSnapshots().getSnapshot(position).toObject(Item.class);
                String id = mAdapter.getSnapshots().getSnapshot(position).getId();
                mDb.collection(ITEMS).document(id).delete();
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

        public void signOut(View view) {
            mAuth.signOut();
            Intent intent = new Intent(HomePage.this, MainActivity.class);
            startActivity(intent);
        }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        GlideApp.with(this).pauseAllRequests();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        GlideApp.with(this).resumeRequests();
    }



}
