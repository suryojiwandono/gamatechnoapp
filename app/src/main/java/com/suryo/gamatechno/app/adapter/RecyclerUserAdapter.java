package com.suryo.gamatechno.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.model.MUser;
import com.suryo.gamatechno.app.others.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<MUser> mUsers;
    private Response.OnRecyclerItemClick onRecyclerItemClick;

    public RecyclerUserAdapter(Context context, List<MUser> mUsers, Response.OnRecyclerItemClick onRecyclerItemClick) {
        this.context = context;
        this.mUsers = mUsers;
        this.onRecyclerItemClick = onRecyclerItemClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycler_users, parent, false);
        v.setTag(viewType);
        return new ViewHolderContent(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderContent holders = (ViewHolderContent) holder;
        MUser mUser = mUsers.get(position);
        holders.textName.setText(mUser.fullname);
        holders.textEmail.setText(mUser.email);
        holders.textDistance.setText(mUser.distance);
        holders.textLocation.setText(mUser.location);
        holders.imageOnline.setVisibility(mUser.isOnline == 1 ? View.VISIBLE : View.GONE);
//        RequestOptions options = new RequestOptions().centerCrop().placeholder(R.mipmap.ic_launcher).error(R.drawable.ic_broken_image_black_24dp);
//        Glide.with(context).load(mUser.flag).apply(options).into(holders.imageFlag);
        String[] temp = mUser.photo.split(Constants.http);
        if (temp.length == 3) {
            Picasso.get()
                    .load(Constants.http.concat(temp[2]))
                    .placeholder(R.drawable.image_animation)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(holders.imagePhoto);
        } else holders.imagePhoto.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person_black_48dp));


        Picasso.get()
                .load(mUser.flag)
                .placeholder(R.drawable.image_animation)
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(holders.imageFlag);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolderContent extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.text_name)
        TextView textName;
        @BindView(R.id.text_email)
        TextView textEmail;
        @BindView(R.id.text_location)
        TextView textLocation;
        @BindView(R.id.text_distance)
        TextView textDistance;
        @BindView(R.id.image_photo)
        ImageView imagePhoto;
        @BindView(R.id.image_online)
        ImageView imageOnline;
        @BindView(R.id.image_flag)
        ImageView imageFlag;

        private View itemLayoutView;

        ViewHolderContent(View itemLayoutView) {
            super(itemLayoutView);
            ButterKnife.bind(this, itemLayoutView);
            this.itemLayoutView = itemLayoutView;
            this.itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == itemLayoutView) {
                onRecyclerItemClick.onClick(view, getAdapterPosition());
            }
        }
    }
}
