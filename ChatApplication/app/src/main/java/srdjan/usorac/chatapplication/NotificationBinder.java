package srdjan.usorac.chatapplication;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

public class NotificationBinder extends INotificationBinder.Stub {

    INotificationCallback Callback;
    NotificationCaller Caller;

    @Override
    public void setCallback(INotificationCallback callback) throws RemoteException {
        Callback = callback;
        Caller = new NotificationCaller();
        Caller.start();
    }

    public void stop() {
        Caller.stop();
    }

    private class NotificationCaller implements Runnable {

        public static final long DELAY = 5000L;

        private Handler handler = null;
        private boolean mRun = true;

        @Override
        public void run() {

            if (!mRun) {
                return;
            }
            try {
                Callback.onCallbackCall();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            handler.postDelayed(this, DELAY);
        }

        public void start() {
            handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(this, DELAY);
            mRun = true;
        }

        public void stop() {
            mRun = false;
            handler.removeCallbacks(this);
        }
    }
}
