package com.project.sabeen.textme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * Model For Contributor Url List
 * Extends RealmObject for using Realm Database
 * Created by sabeen on 6/4/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContributorUrlList extends RealmObject{
    @JsonProperty("url")
    @PrimaryKey
    private String url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
