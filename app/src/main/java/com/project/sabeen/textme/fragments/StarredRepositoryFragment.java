package com.project.sabeen.textme.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.sabeen.textme.R;
import com.project.sabeen.textme.adapter.RepoListAdapter;
import com.project.sabeen.textme.model.GitRepoItems;
import com.project.sabeen.textme.model.GitRepositories;
import com.project.sabeen.textme.service.GitRepositoryService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class StarredRepositoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private RepoListAdapter repoListAdapter;
    List<GitRepoItems> gitRepoItemsList;
    private Realm realm;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repository, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.starSwipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRepoListFromNW();
            }
        });

        gitRepoItemsList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.repo);
        repoListAdapter = new RepoListAdapter(gitRepoItemsList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(repoListAdapter);

//      for using Realm Database
        realm = Realm.getInstance(getActivity().getApplicationContext());

        loadRepoListFromNW();
        loadFromDB();

        return view;
    }

    /**
     * Load Repository List From Retrofit API Call
     */
    private void loadRepoListFromNW() {
        Call<GitRepositories> repos = listRepositories();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        repos.enqueue(new Callback<GitRepositories>() {
            @Override
            public void onResponse(Call<GitRepositories> call, Response<GitRepositories> response) {
                try {
                    realm.beginTransaction();
//                  Creates new record or updates if it already exits
                    realm.copyToRealmOrUpdate(response.body().getGitRepoItems());
                    realm.commitTransaction();
                    loadFromDB();
                    repoListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<GitRepositories> call, Throwable t) {
                Log.d("Repo error", t.getMessage());
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    /**
     * List Repositories using Retrofit
     *
     * @return call for making asynchronous Http request
     */

    private Call<GitRepositories> listRepositories() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        GitRepositoryService service = retrofit.create(GitRepositoryService.class);

//      For calculating date of 7 days back
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());

        return service.listRepos("created:>" + formatted, "stars", "desc");
    }


    /**
     * Loading Git Repositories From Database
     */
    private void loadFromDB() {
        gitRepoItemsList.clear();
        RealmResults<GitRepoItems> results =
                realm.where(GitRepoItems.class).findAll();
        for (GitRepoItems gitRepoItems : results) {
            gitRepoItemsList.add(gitRepoItems);
        }

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();

        }
        swipeRefreshLayout.setRefreshing(false);
    }


}
