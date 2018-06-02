// INotificationBinder.aidl
package srdjan.usorac.chatapplication;

// Declare any non-default types here with import statements
import srdjan.usorac.chatapplication.INotificationCallback;

interface INotificationBinder {

    void setCallback(in INotificationCallback callback);
}
