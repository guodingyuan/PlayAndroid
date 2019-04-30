package com.gdy.playandroid.mvp.bean;

import java.util.List;

public class LoginData {

    private List<String> chapterTops;
    private List<String> collectIds;
    private String email;
    private String icon;
    private Integer id;
    private String password;
    private String token;
    private Integer type;
    private String username;

    public List<String> getChapterTops() {
        return chapterTops;
    }

    public void setChapterTops(List<String> chapterTops) {
        this.chapterTops = chapterTops;
    }

    public List<String> getCollectIds() {
        return collectIds;
    }

    public void setCollectIds(List<String> collectIds) {
        this.collectIds = collectIds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
