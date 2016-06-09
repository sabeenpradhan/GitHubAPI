package com.project.sabeen.textme;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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

/**
 * Activity for loading Starred Repository that was created last week
 * Uses RepoListAdapter for displaying Repository List in Recycle View
 * Created by sabeen on 6/3/16.
 */
public class StarredRepoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RepoListAdapter repoListAdapter;
    List<GitRepoItems> gitRepoItemsList;
    private Realm realm;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starred_repo);


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.about_me);
        dialog.setTitle("Title...");


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.starSwipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRepoListFromNW();
            }
        });
        gitRepoItemsList = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("TextMe GitHub");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

//      For Using Realm Database
        realm = Realm.getInstance(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.repo);
        repoListAdapter = new RepoListAdapter(gitRepoItemsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(repoListAdapter);
        loadRepoListFromNW();
        loadFromDB();
    }

    /**
     * Load Repository List From Retrofit API Call
     */
    private void loadRepoListFromNW(){
        Call<GitRepositories> repos = listRepositories();
        progressDialog = new ProgressDialog(this);
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
                }catch (Exception e){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Call<GitRepositories> call, Throwable t) {
                Log.d("Repo error",t.getMessage());
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
        }

    /**
     *List Repositories using Retrofit
     *@return call for making asynchronous Http request
     */
    private Call<GitRepositories> listRepositories(){
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

        return service.listRepos("created:>"+formatted,"stars","desc");
    }

    /**
     * Loading Git Repositories From Database
     */
    private void loadFromDB(){
        gitRepoItemsList.clear();
        RealmResults<GitRepoItems> results =
                realm.where(GitRepoItems.class).findAll();
            for (GitRepoItems gitRepoItems : results) {
                gitRepoItemsList.add(gitRepoItems);
            }
        swipeRefreshLayout.setRefreshing(false);
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.starred_menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//       Handle item selection
        switch (item.getItemId()) {
            case R.id.about:
                dialog.show();
                ImageView close =(ImageView) dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
