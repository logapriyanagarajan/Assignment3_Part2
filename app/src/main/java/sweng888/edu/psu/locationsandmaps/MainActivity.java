package sweng888.edu.psu.locationsandmaps;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase locationDB;
    DatabaseReference databaseReference;

    private Button button = null;

    Button mclick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mclick = (Button) findViewById(R.id.btnClick);

        mclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
                firebaseLoadData();

            }
        });
    }

    public void addData() {

        ArrayList<Coordinates> data = new ArrayList<>();

        data.add(new Coordinates(-25.734968, 134.489563, "Australia"));
        data.add(new Coordinates( 20.5937, 78.9629, "India"));
        data.add(new Coordinates( 25.032412, -77.39811, "Bahamas"));
        data.add(new Coordinates( 35.486703, 101.901875, "China"));
        data.add(new Coordinates( -20.251868, 57.870755, "Mauritius"));
        data.add(new Coordinates(42.504154, 12.646361, "Italy"));

        locationDB = FirebaseDatabase.getInstance();
        databaseReference = locationDB.getReference();
        Toast.makeText(MainActivity.this, "Data Inserted to Database", Toast.LENGTH_LONG).show();

        databaseReference.setValue(data);

    }

    private void firebaseLoadData() {
        locationDB = FirebaseDatabase.getInstance();
        databaseReference = locationDB.getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("TAG", "Printing Database details on Console............................");

                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    Coordinates dataread = i.getValue(Coordinates.class);
                    Log.d("TAG", "PLACE: " + dataread.getPlace() + "  LATITUDE: " + dataread.getLatitude() + "  LONGITUDE: " + dataread.getLongitude());

                    Intent intent = new Intent(MainActivity.this, BroadcastMapsActivity.class);
                    startActivity(intent);


                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}