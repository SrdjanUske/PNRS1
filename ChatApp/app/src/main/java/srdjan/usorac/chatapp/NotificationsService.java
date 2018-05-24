package srdjan.usorac.chatapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.IOException;

public class NotificationsService extends Service {

    NotificationCompat.Builder notification;
    public static final int uniqueID = 123456789;
    public static final String SERVICE_URL = HttpHelper.BASE_URL + "/getfromservice";
    private static final String LOG_TAG = "NotificationsService";
    private static final long PERIOD = 5000L;
    private Context context = this;
    private HttpHelper httpHelper;

    private NotificationsRunnable mRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        httpHelper = new HttpHelper();
        mRunnable = new NotificationsRunnable();
        mRunnable.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRunnable.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw null;
    }

    private class NotificationsRunnable implements Runnable {
        private Handler mHandler;
        private boolean mRun = false;

        public NotificationsRunnable() {
            mHandler = new Handler(getMainLooper());
        }

        public void start() {
            mRun = true;
            mHandler.postDelayed(this, PERIOD);
        }

        public void stop() {
            mRun = false;
            mHandler.removeCallbacks(this);
        }

        @Override
        public void run() {
            if (!mRun) {
                return;
            }
            Log.d(LOG_TAG, "Hello from Runnable");

            SharedPreferences preferences = context.getSharedPreferences("MyPreferences", 0);
            String sessionID = preferences.getString("sessionID", null);
            //final boolean success = httpHelper.getFromServiceFromURL(SERVICE_URL, sessionID);
            final boolean success = true;

            if (success) {
                Log.d(LOG_TAG, "Success");

                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivities(getApplicationContext(), 0, new Intent[]{intent}, 0);

                notification = new NotificationCompat.Builder(getApplicationContext());
                notification.setSmallIcon(R.drawable.notification);
                notification.setContentTitle(getResources().getString(R.string.app_name));
                notification.setContentText(getResources().getString(R.string.new_message));
                notification.setContentIntent(pendingIntent);
                notification.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                notification.setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                notificationManager.notify(uniqueID, notification.build());

            }
            else {
                Log.d(LOG_TAG, "Failed");
            }

            mHandler.postDelayed(this, PERIOD);
        }
    }
}
