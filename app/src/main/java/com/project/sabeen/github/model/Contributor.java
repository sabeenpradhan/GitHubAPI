package com.project.sabeen.github.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Model For Contributor
 * Extends RealmObject for using Realm Database
 * Created by sabeen on 6/4/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contributor extends RealmObject{

    @PrimaryKey
    private  String login;
    private String name;

    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty("html_url")

    private String htmlUrl;


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    @JsonIgnore
    private String searchUrl;
    @JsonIgnore
    public String getSearchUrl() {
        return searchUrl;
    }
    @JsonIgnore
    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }
}
