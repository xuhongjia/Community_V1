package cn.andrewlu.community.entity;

import com.google.gson.Gson;

public class CheckCode {

    private String account;
    private String code;
    private long lastTime;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
    public String toString(){
    	return new Gson().toJson(this);
    }

}