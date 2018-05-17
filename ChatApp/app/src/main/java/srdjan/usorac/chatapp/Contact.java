package srdjan.usorac.chatapp;

public class Contact {

    public static final String name = "name";

    private int mID;
    private String mUserName;
    private String mFirstName;
    private String mLastName;

    public Contact(int mID, String mUserName, String mFirstName, String mLastName) {
        this.mID = mID;
        this.mUserName = mUserName;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
    }

    public Contact(String mUserName) {
        this.mUserName = mUserName;
    }

    public int getmID() { return mID; }

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
