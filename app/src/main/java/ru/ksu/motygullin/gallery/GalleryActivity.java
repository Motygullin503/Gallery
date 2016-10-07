package ru.ksu.motygullin.gallery;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.ksu.motygullin.gallery.R;
import ru.ksu.motygullin.gallery.network.RxVk;

public class GalleryActivity extends AppCompatActivity {
    ProgressBar mProgress;
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        final GalleryAdapter mGalleryAdapter = new GalleryAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mProgress = (ProgressBar) findViewById(R.id.loading_view);
        mRecyclerView.setAdapter(mGalleryAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        showLoading();

        RxVk api = new RxVk();
        api.getFriends(new RxVk.RxVkListener<List<VkFriend>>() {
            @Override
            public void requestFinished(List<VkFriend> requestResult) {
                mGalleryAdapter.setFriendsList(requestResult);
                showFriends();
            }
        });


    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
    }

    private void showFriends() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }

    private class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

        private List<VkFriend> friends;
        private Context mContext;

        public void setFriendsList(@Nullable List<VkFriend> friendsList) {
            friends = friendsList;
            notifyDataSetChanged();
        }


        private GalleryAdapter(@NonNull Context context) {
            this.mContext = context;
        }


        @Override
        public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
            return new GalleryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(GalleryViewHolder holder, int position) {

            final VkFriend friend = friends.get(position);
            Glide.with(mContext).load(friend.getSmallPhotoUrl()).fitCenter().into(holder.avatar);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PhotoActivity.class);
                    intent.putExtra("max_photo", friend.getMaxPhotoUrl());
                    v.getContext().startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return friends.size();
        }

        public class GalleryViewHolder extends RecyclerView.ViewHolder {

            ImageView avatar;
            View view;

            public GalleryViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                avatar = (ImageView) itemView.findViewById(R.id.list_item);
            }

        }
    }
}
