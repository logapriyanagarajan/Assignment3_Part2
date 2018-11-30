package sweng888.edu.psu.locationsandmaps;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class MyReceiverActivity extends BroadcastReceiver {

    public static final String CHANNEL_NAME = "MAPS";

    private static final String MAP_TAG = "MAP_TAG";

    public static final String MAP_BROADCAST = "sweng888.edu.psu.locationsandmaps.action.MAP_BROADCAST";

    public static final int CHANNEL_ID = 1;
    public static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
    public static final String CHANNEL_DESCRIPTION = "BROADCAST MAP CHANNEL";


    private NotificationManager notificationManager;
    private Notification.Builder builder;

    @Override
    public void onReceive(Context context, Intent intent) {

        Coordinates c =  (Coordinates) intent.getSerializableExtra(BroadcastMapsActivity.LOCATION_PARAMS);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new Notification.Builder(context, CHANNEL_NAME);


        //String place = mapLocation.getCountry();
        //LatLng lat = new LatLng(19,72);

        builder.setSmallIcon(R.drawable.broadcast);
        builder.setContentTitle(c.getPlace());
        LatLng latLng = new LatLng(c.getLatitude(),c.getLongitude());
        String latlng1 = latLng.toString();
        builder.setContentText(latlng1);

        notificationManager.createNotificationChannel(getNotificationChannel());
        notificationManager.notify(CHANNEL_ID, builder.build());
    }

    private NotificationChannel getNotificationChannel(){

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_NAME, CHANNEL_DESCRIPTION, CHANNEL_IMPORTANCE);
        notificationChannel.setDescription(CHANNEL_DESCRIPTION);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.enableVibration(true);
        notificationChannel.setShowBadge(true);

        return notificationChannel;
    }
}
