package cn.gotohope.forgive.pojo;

import java.io.Serializable;

public class GameBest implements Serializable, BestTransfer {
    private String userPhone, gameId;
    private int score;
    private transient User user;

    @Override
    public String toString() {
        return "GameBest{" +
                "userPhone='" + userPhone + '\'' +
                ", gameId='" + gameId + '\'' +
                ", score=" + score +
                '}';
    }

    @Override
    public String json() {
        return String.format("{\"game_id\": \"%s\", \"score\": %d}", gameId, score);
    }

    @Override
    public String getType() {
        return "score";
    }

    public String getUserPhone() {
        return userPhone;
    }
    public GameBest setUserPhone(String userPhone) {
        this.userPhone = userPhone;
        return this;
    }

    @Override
    public String getGameId() {
        return gameId;
    }
    public GameBest setGameId(String gameId) {
        this.gameId = gameId;
        return this;
    }

    @Override
    public int getScore() {
        return score;
    }
    public GameBest setScore(int score) {
        this.score = score;
        return this;
    }

    @Override
    public User getUser() {
        return user;
    }
    public GameBest setUser(User user) {
        this.user = user;
        return this;
    }
}
