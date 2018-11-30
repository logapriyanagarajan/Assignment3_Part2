package sweng888.edu.psu.locationsandmaps;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class BroadcastMapsActivity extends AppCompatActivity {

    public static final String LOCATION_PARAMS = "LOCATION";


    private EditText mEditTextLat;
    private EditText mEditTextLon;
    private EditText mEditTextLocation;

    private Button mBtnClick;

    Coordinates coordinates = null;

    public IntentFilter intentFilter = null;
    public MyReceiverActivity myReceiverActivity = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinates);

        mEditTextLat = (EditText) findViewById(R.id.editTextLat);
        mEditTextLon = (EditText) findViewById(R.id.editTextLong);
        mEditTextLocation = (EditText) findViewById(R.id.editTextLocation);


        mBtnClick = (Button) findViewById(R.id.btnNavigation);

        mBtnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "****************************in click*****************");

                Double lat = Double.valueOf(mEditTextLat.getText().toString());
                Double lng = Double.valueOf(mEditTextLon.getText().toString());
                LatLng latLng = new LatLng(lat,lng);

                Coordinates c = CoordinatesHelper.getAddressFromLatLgn(getApplicationContext(),latLng);

                Intent broadcastIntent = new Intent("sweng888.edu.psu.locationsandmaps.action.MAP_BROADCAST");
                broadcastIntent.putExtra(LOCATION_PARAMS, c);
                sendBroadcast(broadcastIntent);

                Intent intent = new  Intent(BroadcastMapsActivity.this, MapsActivity.class);
                intent.putExtra(LOCATION_PARAMS, c);
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
        // Register the Broadcast Receiver.
        //registerReceiver(myReceiverActivity, intentFilter);
    }

    @Override
    protected void onStop() {
        // Unregister the Broadcast Receiver
        unregisterReceiver(myReceiverActivity);
        super.onStop();
    }
}
