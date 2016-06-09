package com.project.sabeen.textme.service;

import com.project.sabeen.textme.model.Contributor;
import com.project.sabeen.textme.model.ContributorUrlList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Interface for using Retrofit for Git API Call
 * For Getting List of Contributors URL
 * Created by sabeen on 6/3/16.
 */

public interface ContributorService {

    @GET
    Call<List<ContributorUrlList>> listContributorsUrl(@Url String url);



    @GET
    Call<Contributor> listContributor(@Url String url);
}
