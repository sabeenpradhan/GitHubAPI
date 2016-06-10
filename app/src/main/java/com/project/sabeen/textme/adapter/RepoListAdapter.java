package com.project.sabeen.textme.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.sabeen.textme.R;
import com.project.sabeen.textme.fragments.ContributorFragment;
import com.project.sabeen.textme.model.GitRepoItems;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Adapter for displaying Most Starred Repository that were created in last week
 * Uses Recycle View
 * Created by sabeen on 6/3/16.
 */

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.RepoViewHolder> {
    private List<GitRepoItems> gitRepositories;
    private Context context;
//  for setting selected item's position
    private Integer selectedPos=-1;

    public class RepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, description, updated, language, staggers, fork;
        public RelativeLayout relativeLayout;

        public RepoViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.desc);
            updated = (TextView) view.findViewById(R.id.updated);
            language = (TextView) view.findViewById(R.id.language);
            staggers = (TextView) view.findViewById(R.id.stagazers);
            fork = (TextView) view.findViewById(R.id.fork);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.repoRelativeLayout);

        }

        /**
         * On Click listener For Repository List
         * Fetches Contributor's URL Selected Repository and  replaces starred repository fragment
         * with contributor fragment if it is smart phone else replaces it's own container
         * @param view
         */
        @Override
        public void onClick(View view) {
//          notifying item click change
            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);

            GitRepoItems gitRepoItems = gitRepositories.get(getLayoutPosition());

//          putting contributor url in bundle to be used in contributor fragment
            Bundle bundle = new Bundle();
            bundle.putString("url", gitRepoItems.getContributersUrl());

            ContributorFragment contributorFragment = new ContributorFragment();
            contributorFragment.setArguments(bundle);

            FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//          checking if contributor container is null, if null repository container is filled with contributor fragment
//          else uses it's own container
            if (((Activity) context).findViewById(R.id.contrib_container) != null) {
                fragmentTransaction.replace(R.id.contrib_container, contributorFragment);
            } else {
                fragmentTransaction.replace(R.id.repo_container, contributorFragment).addToBackStack(null);
            }

            fragmentTransaction.commit();
        }
    }

    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_items, parent, false);
        return new RepoViewHolder(itemView);
    }

    public RepoListAdapter(List<GitRepoItems> gitRepositories, Context context) {
        this.gitRepositories = gitRepositories;
        this.context = context;
    }


    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {

        GitRepoItems gitRepoItems = gitRepositories.get(position);

//      For Displaying Updated Date in format Updated on Month Name Day
        Date date = gitRepoItems.getUpdated();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer month = calendar.get(Calendar.MONTH);
        Integer day = calendar.get(Calendar.DAY_OF_MONTH);
        String updated = "Updated on " + getMonthForInt(month) + " " + day.toString();

        holder.title.setText(gitRepoItems.getFullName());
        holder.description.setText(gitRepoItems.getDescription());
        holder.updated.setText(updated);
        holder.language.setText(gitRepoItems.getLanguage());
        holder.staggers.setText(gitRepoItems.getStaggers().toString());
        holder.fork.setText(gitRepoItems.getForks().toString());
        holder.itemView.setSelected(selectedPos==position);
//      for changing selected items color
        if(selectedPos==position){
            holder.relativeLayout.setBackgroundColor(Color.WHITE);
        }else{
            holder.relativeLayout.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    /**
     * @param num
     * @return Months Name
     */
    private String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    @Override
    public int getItemCount() {
        return gitRepositories.size();
    }
}
