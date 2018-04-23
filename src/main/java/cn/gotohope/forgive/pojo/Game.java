package cn.gotohope.forgive.pojo;

import com.google.gson.annotations.Expose;

public class Game {
    private String id;
    private String name;
    private String type = "";

    @Expose(serialize = false, deserialize = false)
    private BestTransfer best;

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", getType='" + type + '\'' +
                ", best=" + best +
                '}';
    }

    public String getId() {
        return id;
    }
    public Game setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }
    public Game setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }
    public Game setType(String type) {
        this.type = type;
        return this;
    }

    public BestTransfer getBest() {
        return best;
    }
    public Game setBest(BestTransfer best) {
        this.best = best;
        return this;
    }
}
