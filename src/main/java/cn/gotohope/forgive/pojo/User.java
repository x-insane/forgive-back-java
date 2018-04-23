package cn.gotohope.forgive.pojo;

import java.io.Serializable;

public class User implements Serializable {
    private String phone, password, nickname, description;

    @Override
    public String toString() {
        return "User{" +
                "phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getPhone() {
        return phone;
    }
    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }
    public String getPassword() {
        return password;
    }
    public User setPassword(String password) {
        this.password = password;
        return this;
    }
    public String getNickname() {
        return nickname;
    }
    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }
    public String getDescription() {
        return description;
    }
    public User setDescription(String description) {
        this.description = description;
        return this;
    }
}
