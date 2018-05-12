package srdjan.usorac.chatapplication;

public class User {

    private String sessionID;
    private String username;

    public User(String sessionID, String username) {
        this.sessionID = sessionID;
        this.username = username;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getUsername() {
        return username;
    }
}
