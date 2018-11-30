package sweng888.edu.psu.locationsandmaps;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

public class BroadcastMapsActivity extends AppCompatActivity {

    private EditText mEditTextLat;
    private EditText mEditTextLon;
    private EditText mEditTextLocation;

    private Button mBtnClick;

    Coordinates coordinates = null;

    public static final String LOCATION_PARAMS = "LOCATION";

    public IntentFilter intentFilter = null;
    public MyReceiverActivity myReceiverActivity = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinates);

        mEditTextLat = (EditText) findViewById(R.id.editTextLat);
        mEditTextLon = (EditText) findViewById(R.id.editTextLong);
        mEditTextLocation = (EditText) findViewById(R.id.editTextLocation);


        mBtnClick = (Button) findViewById(R.id.btnClick);

        mBtnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lat = (mEditTextLat.getText().toString().trim());
                String lon = (mEditTextLon.getText().toString().trim());
                String place = mEditTextLocation.getText().toString();
                LatLng latLng = new LatLng(Double.valueOf(lat), Double.valueOf(lon));

                Coordinates maplocation = CoordinatesHelper.getAddressFromLatLgn(getApplicationContext(), latLng);

                Intent broadcastIntent = new Intent("sweng888.edu.psu.locationsandmaps.action.");
                broadcastIntent.putExtra(LOCATION_PARAMS, maplocation.getPlace());
                sendBroadcast(broadcastIntent);

                Intent intent = new  Intent(BroadcastMapsActivity.this, MapsActivity.class);
                intent.putExtra("LATITUDE", mEditTextLat.getText().toString().trim());
                intent.putExtra("LONGITUDE", mEditTextLon.getText().toString().trim());
                intent.putExtra("LOCATION", mEditTextLocation.getText().toString().trim());
                startActivity(intent);


            }
        });

    }
    private void setUpBroadcastReceiver(){
        intentFilter = new IntentFilter(MyReceiverActivity.MAP_BROADCAST);
        myReceiverActivity = new MyReceiverActivity();
        registerReceiver(myReceiverActivity, intentFilter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        setUpBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(myReceiverActivity);
        super.onStop();
    }
}


