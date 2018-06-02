package srdjan.usorac.chatapplication;

public class Chat {
    private int id;
    private Contact mSender;
    private Contact mReceiver;
    private String mMessage;

    public Chat(int id, Contact mSender, Contact mReceiver, String mMessage) {
        this.id = id;
        this.mSender = mSender;
        this.mReceiver = mReceiver;
        this.mMessage = mMessage;
    }

    public Chat(Contact mSender, Contact mReceiver, String mMessage) {
        this.mSender = mSender;
        this.mReceiver = mReceiver;
        this.mMessage = mMessage;
    }

    public int getId() {
        return id;
    }

    public Contact getmSender() {
        return mSender;
    }

    public Contact getmReceiver() {
        return mReceiver;
    }

    public String getmMessage() {
        return mMessage;
    }
}
