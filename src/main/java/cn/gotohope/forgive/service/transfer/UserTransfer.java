package cn.gotohope.forgive.service.transfer;

import cn.gotohope.forgive.pojo.User;

public class UserTransfer implements Transfer {
    private int error = 0;
    private String msg;
    private User user;

    @Override
    public String toString() {
        return "UserTransfer{" +
                "error=" + error +
                ", msg='" + msg + '\'' +
                ", user=" + user +
                '}';
    }

    public int getError() {
        return error;
    }
    public UserTransfer setError(int error) {
        this.error = error;
        return this;
    }

    public String getMsg() {
        return msg;
    }
    public UserTransfer setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public User getUser() {
        return user;
    }
    public UserTransfer setUser(User user) {
        this.user = user;
        return this;
    }
}
