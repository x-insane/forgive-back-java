package cn.gotohope.forgive.pojo;

import java.io.Serializable;

public class ChallengeBest implements Serializable, BestTransfer {
    private String userPhone, gameId;
    private float velocity;
    private transient User user;

    @Override
    public String toString() {
        return "ChallengeBest{" +
                "userPhone='" + userPhone + '\'' +
                ", gameId='" + gameId + '\'' +
                ", velocity=" + velocity +
                '}';
    }

    @Override
    public String json() {
        return String.format("{\"game_id\": \"%s\", \"velocity\": %.3f}", gameId, velocity);
    }

    @Override
    public String getType() {
        return "velocity";
    }

    public String getUserPhone() {
        return userPhone;
    }
    public ChallengeBest setUserPhone(String userPhone) {
        this.userPhone = userPhone;
        return this;
    }

    @Override
    public String getGameId() {
        return gameId;
    }
    public ChallengeBest setGameId(String gameId) {
        this.gameId = gameId;
        return this;
    }

    @Override
    public float getVelocity() {
        return velocity;
    }
    public ChallengeBest setVelocity(float velocity) {
        this.velocity = velocity;
        return this;
    }

    @Override
    public User getUser() {
        return user;
    }
    public ChallengeBest setUser(User user) {
        this.user = user;
        return this;
    }
}
