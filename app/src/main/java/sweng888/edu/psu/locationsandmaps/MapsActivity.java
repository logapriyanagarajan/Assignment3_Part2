package sweng888.edu.psu.locationsandmaps;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String LOCATION_PARAMS = "LOCATION";

    public GoogleMap mMap;

    private final String LOG_MAP = "GOOGLE_MAPS";

    private LatLng currentLatLng;
    private MapFragment mapFragment;
    private Marker currentMapMarker;

    FirebaseDatabase mAuth = null;
    DatabaseReference databaseReference = null;

    private Handler mHandler = new Handler();
    private Runnable mRunnable;

    private String latitude;
    private String longitude;
    private String location;

    private IntentFilter intentFilter = null;
    private MyReceiverActivity myReceiverActivity = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlemaps);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googlemap);

        mapFragment.getMapAsync(MapsActivity.this);

        intentFilter = new IntentFilter("sweng888.edu.psu.locationsandmaps.action.MAP_BROADCAST");
        myReceiverActivity = new MyReceiverActivity();

        //intentFilter = new IntentFilter("sweng888.edu.psu.locationsandmaps.action.MAP_BROADCAST");
        // myReceiverActivity = new MyReceiverActivity();

        /*mRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "****************in run************************");
                mapFragment.getMapAsync(MapsActivity.this);
            }
        };*/

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        Coordinates coordinates = (Coordinates) intent.getSerializableExtra(BroadcastMapsActivity.LOCATION_PARAMS);

        LatLng latLng = new LatLng(coordinates.getLatitude(),coordinates.getLongitude());

        /*Intent intent = getIntent();
        latitude = intent.getStringExtra("LATITUDE");
        longitude = intent.getStringExtra("LONGITUDE");
        location = intent.getStringExtra("LOCATION");

        Double lat = Double.valueOf(latitude);
        Double lon = Double.valueOf(longitude);
        LatLng latLng = new LatLng(lat,lon);*/

        mMap.addMarker(new MarkerOptions().position(latLng).title(coordinates.getPlace()).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //mapCameraConfiguration(googleMap);
        useMapClickListener(googleMap);
        //useMapLongClickListener(googleMap);
        useMarkerClickListener(googleMap);
        useMarkerDragListener(googleMap);

        createMarkersFromFirebase(mMap);

    }

    public void mapCameraConfiguration(GoogleMap googleMap) {

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(currentLatLng)
                .zoom(14)
                .bearing(0)
                .build();

        // Camera that makes reference to the maps view
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

        googleMap.animateCamera(cameraUpdate, 3000, new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {
                Log.i(LOG_MAP, "googleMap.animateCamera:onFinish is active");
            }

            @Override
            public void onCancel() {
                Log.i(LOG_MAP, "googleMap.animateCamera:onCancel is active");
            }
        });
    }

    public void createCustomMapMarkers(GoogleMap googleMap, LatLng latlng, String title, String snippet) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng) // coordinates2
                .title(title) // location name
                .snippet(snippet); // location description

        triggerBroadcastMessageFromFirebase(latlng,title);

        // Update the global variable (currentMapMarker)
        currentMapMarker = googleMap.addMarker(markerOptions);
    }

    /*public void createCustomMapMarkersfromLocation(LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng).title("hi").snippet("hello");

    }*/


    public void useMapClickListener(final GoogleMap googleMap) {

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latltn) {
                Log.i(LOG_MAP, "setOnMapClickListener");

                if (currentMapMarker != null) {
                    // Remove current marker from the map.
                    currentMapMarker.remove();
                }
                // The current marker is updated with the new position based on the click.
                createCustomMapMarkers(
                        googleMap,
                        new LatLng(latltn.latitude, latltn.longitude),
                        "New Marker",
                        "Listener onMapClick - new position"
                                + "lat: " + latltn.latitude
                                + " lng: " + latltn.longitude);
            }
        });
    }

    public void useMapLongClickListener(final GoogleMap googleMap) {
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Log.i(LOG_MAP, "setOnMapLongClickListener");
                LatLng latlng = new LatLng(latLng.latitude, latLng.longitude);
                Log.d("TAG", "Update the new coordinates2 on long click");
            }

        });
    }


    public void useMarkerClickListener(GoogleMap googleMap) {
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            // If FALSE, when the map should have the standard behavior (based on the android framework)
            // When the marker is clicked, it wil focus / centralize on the specific point on the map
            // and show the InfoWindow. IF TRUE, a new behavior needs to be specified in the source code.
            // However, you are not required to change the behavior for this method.
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i(LOG_MAP, "setOnMarkerClickListener");

                return false;
            }
        });
    }

    public void useMarkerDragListener(GoogleMap googleMap) {
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Log.i(LOG_MAP, "setOnMarkerDragListener");
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });
    }


    public void createMarkersFromFirebase(final GoogleMap googleMap) {

        mAuth = FirebaseDatabase.getInstance();
        databaseReference = mAuth.getReference();

        final MarkerOptions markerOptions = new MarkerOptions();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Coordinates c = dataSnapshot.getValue(Coordinates.class);
                mHandler.postDelayed(mRunnable, 5);
                LatLng latLng = new LatLng(c.getLatitude(), c.getLongitude());

                createCustomMapMarkers(googleMap, latLng, c.getPlace(), "hiii");

            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void triggerBroadcastMessageFromFirebase(LatLng latLng, String place) {
        Coordinates c = new Coordinates();
        c.setLatitude(latLng.latitude);
        c.setLongitude(latLng.longitude);
        c.setPlace(place);

        Intent broadcastIntent = new Intent("sweng888.edu.psu.locationsandmaps.action.MAP_BROADCAST");
        broadcastIntent.putExtra(LOCATION_PARAMS, c);
        sendBroadcast(broadcastIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Register the Broadcast Receiver.
        registerReceiver(myReceiverActivity, intentFilter);
    }

    @Override
    protected void onStop() {
        // Unregister the Broadcast Receiver
        unregisterReceiver(myReceiverActivity);
        super.onStop();
    }
}
