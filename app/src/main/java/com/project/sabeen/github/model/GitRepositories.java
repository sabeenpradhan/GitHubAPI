package com.project.sabeen.github.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Model For Git Repositories
 * Extends RealmObject for Realm Database
 * Created by sabeen on 6/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitRepositories{
    @JsonProperty("items")
    private  List<GitRepoItems> gitRepoItems;

    public List<GitRepoItems> getGitRepoItems() {
        return gitRepoItems;
    }

    public void setGitRepoItems(List<GitRepoItems> gitRepoItems) {
        this.gitRepoItems = gitRepoItems;
    }
}
