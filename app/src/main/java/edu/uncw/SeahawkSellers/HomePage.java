package edu.uncw.SeahawkSellers;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.Locale;

public class HomePage extends AppCompatActivity {
    private static final String ITEM = "Items";
    private static final String TAG = "HomePage";
    private FirebaseAuth mAuth;
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private ItemRecyclerAdapter mAdapter;
    private static final String LATITUDE_KEY = "latitude";
    private static final String LONGITUDE_KEY = "longitude";
    private static final String ACCURACY_KEY = "accuracy";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 65535;
    private FusedLocationProviderClient mFusedLocationClient;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String LOCATIONRECORD_COLLECTION = "location-recordings";
    private LocationCallback mLocationCallback;

    private static int REQUEST_CHECK_SETTINGS = 123;

    // This is a configuration object that tells the Google client  how often and to what degree
    // of accuracy you want to receive location updates.
    private LocationRequest mLocationRequest = new LocationRequest()
            .setInterval(300000)
            .setFastestInterval(10000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Intent intent = getIntent();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        final String email = currentUser.getEmail();
        Toast.makeText(HomePage.this, String.format(getResources().getString(R.string.hello), email), Toast.LENGTH_SHORT).show();

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
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
                    editPost( item );
                    String id = mAdapter.getSnapshots().getSnapshot(position).getId();
                    mDb.collection(ITEM).document(id).delete();
                }else{
                    viewPost( item );


                }

            }
        });

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            LocationRecord lr = new LocationRecord(
                                    new Date(location.getTime()),
                                    new GeoPoint(location.getLatitude(), location.getLongitude()),
                                    location.getAccuracy());

                            db.collection(LOCATIONRECORD_COLLECTION)
                                    .add(lr)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "Location Record added with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding LocationRecord", e);
                                        }
                                    });
                        }
                    }
                }
            }
        };

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
    public void editPost(Item item){
        Intent intent = new Intent(HomePage.this, EditPost.class);
        String title= item.getTitle();
        String description= item.getDescription();
        String price= item.getPrice();
        intent.putExtra("TITLE", title);
        intent.putExtra("DESCRIPTION", description);
        intent.putExtra("PRICE", price);
        startActivity(intent);
    }
    public void viewPost(Item item){
        Intent intent = new Intent(HomePage.this, ViewPost.class);
        String title= item.getTitle();
        String description= item.getDescription();
        String price= item.getPrice();
        String seller= item.getSeller();
        intent.putExtra("TITLE", title);
        intent.putExtra("DESCRIPTION", description);
        intent.putExtra("PRICE", price);
        intent.putExtra("SELLER", seller);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createLocationRequest();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "I can record the location now!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Allow location permission in settings to use app.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    protected void createLocationRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (ContextCompat.checkSelfPermission(HomePage.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(HomePage.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Toast.makeText(HomePage.this, "I need permission to access location in order to record locations.", Toast.LENGTH_SHORT).show();
                    } else {
                        ActivityCompat.requestPermissions(HomePage.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                } else {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback,
                            null /* Looper */);
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(HomePage.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            // Regardless of whether the user fixed the issue or not, try again.
            createLocationRequest();

            // This could be really annoying for the user...
            // In reality, you should check the resultCode for success. If not successful, you
            // should degrade the functionality.
        }
    }
}


