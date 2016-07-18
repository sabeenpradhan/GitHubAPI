package com.project.sabeen.github.service;

import com.project.sabeen.github.model.GitRepositories;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Intefrace for using Retrofit For Git API Call
 * for Getting Most Starred Repositories
 * Created by sabeen on 6/3/16.
 */

public interface GitRepositoryService {


@GET("search/repositories")
    Call<GitRepositories> listRepos(@Query("q") String q, @Query("sort") String sort, @Query("order") String order);


}
