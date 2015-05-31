package edu.mit.voicesurvey.androidapplication.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Calendar;

import edu.mit.voicesurvey.androidapplication.R;
import edu.mit.voicesurvey.androidapplication.controllers.startup.SplashScreen;

public class NotifyService extends Service {
    public NotifyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static void setAlarms(Context context) {
        Intent intent = new Intent(context, NotifyService.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        //https://developer.android.com/training/scheduling/alarms.html
        //https://developer.android.com/training/notify-user/build-notification.html
        //http://stackoverflow.com/questions/23440251/how-to-repeat-notification-daily-on-specific-time-in-android-through-background
        //http://www.banane.com/2014/05/07/simple-example-of-scheduled-self-clearing-android-notifications/
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8); // TODO: allow users to choose if / when to be reminded
        calendar.set(Calendar.MINUTE, 5);
        //alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }

    @Override
    public void onCreate() {
        Intent intent = new Intent(this, SplashScreen.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.brain_1732b)
                        .setContentTitle("VoiceUp")
                        .setContentText("Complete your daily survey!")
                        .setContentIntent(contentIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(0, mBuilder.build());
    }
}
