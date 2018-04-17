package rtrk.pnrs.databaseexample;


public class Student {

    private String mFirstName;
    private String mLastName;
    private String mIndex;

    public Student(String firstName, String lastName, String index) {
        mFirstName = firstName;
        mLastName = lastName;
        mIndex = index;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getIndex() {
        return mIndex;
    }
}
