package com.project.sabeen.textme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Model For Git Most Starred Repository Items
 * Extends RealmObject for using Realm Database
 * Created by sabeen on 6/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitRepoItems extends RealmObject{


    @JsonProperty("stargazers_count")
    private Integer staggers;
    private Integer forks;

    private String description;
    private String language;

    @PrimaryKey
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("updated_at")
    private Date updated;

    @JsonProperty("contributors_url")
    private String contributersUrl;

    public String getContributersUrl() {
        return contributersUrl;
    }

    public void setContributersUrl(String contributersUrl) {
        this.contributersUrl = contributersUrl;
    }

    public Date getUpdated() {

        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getStaggers() {
        return staggers;
    }

    public void setStaggers(Integer staggers) {
        this.staggers = staggers;
    }

    public Integer getForks() {
        return forks;
    }

    public void setForks(Integer forks) {
        this.forks = forks;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public String getUpdated() {
//        return updated;
//    }
//
//    public void setUpdated(String updated) {
//        this.updated = updated;
//    }
}
