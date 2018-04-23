package cn.gotohope.forgive.pojo;

public interface BestTransfer {
    String json();
    String getType();
    String getGameId();

    User getUser();

    default int getScore() {
        throw new RuntimeException("Wrong call! Please check type of the object.");
    }
    default float getVelocity() {
        throw new RuntimeException("Wrong call! Please check type of the object.");
    }
}
