package srdjan.usorac.chatapplication;

public class Contact {
    private String mUserName;
    private String mFirstName;
    private String mLastName;

    public Contact(String mUserName, String mFirstName, String mLastName) {
        this.mUserName = mUserName;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }
}
