package sweng888.edu.psu.locationsandmaps;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

public class MyReceiverActivity extends BroadcastReceiver {

        private static final String MAP_TAG = "MAP_TAG";

        public static final String MAP_BROADCAST = "sweng888.edu.psu.locationsandmaps.action.MAP_BROADCAST";

        public static final int CHANNEL_ID = 1;
        public static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
        public static final String CHANNEL_DESCRIPTION = "BROADCAST MAP CHANNEL";
        public static final String CHANNEL_NAME = "MAPS";

        private NotificationManager notificationManager;
        private Notification.Builder builder;

        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("BROADCAST", "onReceive");

            // Gather the information / params from the Intent.
            Coordinates mapLocation =  (Coordinates) intent.getSerializableExtra(BroadcastMapsActivity.LOCATION_PARAMS);

            // It assumes the highest and lowest latitude on Earth as being, respectively, 90 and -90.
            // Any points between -23 and 23 is considered as CENTRAL HEMISPHERE, as it is close
            // to the Equator.
            String hemisphere = getHemisphere(mapLocation.getLatitude());


            if (hemisphere.equals("NORTH") || hemisphere.equals("SOUTH") || hemisphere.equals("SOUTH")){

                // Create an instance of the NotificationManager
                // It call the static function from(Context) to get a NotificationManagerCompat object, and then call
                // one of its methods to post or cancel notifications
                notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                // If you are using the Android API above then 26, you should use another constructor.
                builder = new Notification.Builder(context, CHANNEL_NAME);

                // Set up the notification Title and Text.
                builder.setSmallIcon(R.drawable.broadcast);
                builder.setContentTitle(mapLocation.getPlace());
                builder.setContentText(Double.isNaN(mapLocation.getLatitude())
                        || Double.isNaN(mapLocation.getLongitude())
                        ? "Location Unknown" :
                        "Located at the " + hemisphere +
                                ", with coordinates (lat, lng): "+
                                mapLocation.getLatitude()+", "+mapLocation.getLongitude());

                // Set the notification channel
                notificationManager.createNotificationChannel(getNotificationChannel());

                // Set the notification ID, and passing as parameter the Notification.Builder.
                notificationManager.notify(CHANNEL_ID, builder.build());

            }else
                Log.d(MAP_TAG, String.valueOf(R.string.location_out));

        }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationChannel getNotificationChannel(){

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_NAME, CHANNEL_DESCRIPTION, CHANNEL_IMPORTANCE);
        notificationChannel.setDescription(CHANNEL_DESCRIPTION);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.enableVibration(true);
        notificationChannel.setShowBadge(true);

        return notificationChannel;
    }

    private String getHemisphere(Double latitude){

        String hemisphere = "";
        boolean isCentralHemisphere = (latitude < 23 && latitude > -23);
        boolean isNorthHemisphere = (latitude > 23 && latitude <= 90);
        boolean isSouthHemisphere = (latitude < -23 && latitude >= -90);

        if(isCentralHemisphere)
            hemisphere = "CENTRAL";
        else{
            if (isNorthHemisphere) hemisphere = "NORTH";
            else{
                if (isSouthHemisphere)
                    hemisphere = "SOUTH";
            }
        }

        return hemisphere;
    }


}







