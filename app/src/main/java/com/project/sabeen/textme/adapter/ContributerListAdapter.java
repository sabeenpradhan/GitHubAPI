package com.project.sabeen.textme.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.sabeen.textme.R;
import com.project.sabeen.textme.model.Contributor;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter for displaying data Top Contributor List
 * Uses Recycler View
 * Created by sabeen on 6/3/16.
 */
public class ContributerListAdapter extends RecyclerView.Adapter<ContributerListAdapter.RepoViewHolder> {
    private List<Contributor> contributorList;

    public class RepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView contributerName, link, login;
        public ImageView avatar;

        public RepoViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            contributerName = (TextView) view.findViewById(R.id.contrib_name);
            link = (TextView) view.findViewById(R.id.contrib_url);
            avatar = (ImageView) view.findViewById(R.id.contrib_pic);
            login = (TextView) view.findViewById(R.id.contrib_login);
        }

        /**
         * On Click listener For Contributor List
         * Fetches Profile Link of Selected Contributor and sends to his page
         *
         * @param view
         */

        @Override
        public void onClick(View view) {
            Contributor contributor = contributorList.get(getLayoutPosition());
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(contributor.getHtmlUrl()));
            view.getContext().startActivity(i);
        }

    }

    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contributer_items, parent, false);
        return new RepoViewHolder(itemView);
    }

    public ContributerListAdapter(List<Contributor> contributorList) {
        this.contributorList = contributorList;
    }


    @Override
    public void onBindViewHolder(final RepoViewHolder holder, int position) {

        final Contributor Contributor = contributorList.get(position);
        holder.contributerName.setText(Contributor.getName());
        holder.link.setText(Contributor.getHtmlUrl());
        holder.login.setText(Contributor.getLogin());
//      For loading image from Contributor's Avatar URL to iImageView
        Picasso.with(holder.avatar.getContext())
                .load(Contributor.getAvatarUrl())
                .resize(200, 200)
                .placeholder(R.drawable.ic_user_placeholder)
                .centerCrop()
                .into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return contributorList.size();
    }
}
