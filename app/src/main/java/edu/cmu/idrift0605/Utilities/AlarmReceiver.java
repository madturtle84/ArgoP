package edu.cmu.idrift0605.Utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import edu.cmu.idrift0605.HomeActivity;
import edu.cmu.idrift0605.R;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("AlarmReceiver]", "ALAAARMMMMMMMMMMMMMMMMM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Toast.makeText(HomeActivity.getSingleton(),"Time's up!!!", Toast.LENGTH_SHORT).show();
        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        //this will send a notification message
        //ComponentName comp = new ComponentName(context.getPackageName(), HomeActivity.class.getName());
//        ComponentName comp = new ComponentName(context.getPackageName(), AlarmService.class.getName());
//        startWakefulService(context, (intent.setComponent(comp)));
//        setResultCode(Activity.RESULT_OK);


        sendNotification();
    }

    private void sendNotification() {
        Context ctx = HomeActivity.getSingleton();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.icon_parking_notification)
                .setContentTitle("Your parking time is up!!!")
                .setContentText("Click to dismiss the ringtone.");


        /* Define the intent when clicking the notification */
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, new Intent(ctx, HomeActivity.class), 0);
        mBuilder.setContentIntent(contentIntent);


        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        int mId=0;
        mNotificationManager.notify(mId, mBuilder.build());
    }
}