package com.suryo.gamatechno.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.model.Room;
import com.suryo.gamatechno.app.others.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Room> messages;
    private String fromUserId;

    public RecyclerRoomAdapter(Context context, List<Room> messages, String fromUserId) {
        this.context = context;
        this.messages = messages;
        this.fromUserId = fromUserId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycler_room, parent, false);
        v.setTag(viewType);
        return new ViewHolderContent(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderContent holders = (ViewHolderContent) holder;
        Room message = messages.get(position);
        holders.textFullname.setText(message.fromUserName);
        holders.textMessage.setText(message.fromMessage);
        holders.textTimestamp.setText(message.timestamp);

        String[] temp = message.fromUserPhoto.split(Constants.http);
        if (message.fromUserId.equals(fromUserId)) {
            setGravity(holders, Gravity.END);
            holders.imageStatus.setVisibility(View.VISIBLE);
            holders.layoutContent.setBackgroundResource(R.drawable.bg_chat_own);
            holders.imagePhotoLeft.setVisibility(View.GONE);
            holders.imagePhotoRight.setVisibility(View.VISIBLE);
            if (temp.length == 3) {
                Picasso.get()
                        .load(Constants.http.concat(temp[2]))
                        .placeholder(R.drawable.image_animation)
                        .error(R.drawable.ic_broken_image_black_24dp)
                        .into(holders.imagePhotoRight);
            } else holders.imagePhotoRight.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person_black_48dp));
        } else {
            holders.imageStatus.setVisibility(View.GONE);
            setGravity(holders, Gravity.START);
            holders.layoutContent.setBackgroundResource(R.drawable.bg_chat);
            holders.imagePhotoRight.setVisibility(View.GONE);
            holders.imagePhotoLeft.setVisibility(View.VISIBLE);
            if (temp.length == 3) {
                Picasso.get()
                        .load(Constants.http.concat(temp[2]))
                        .placeholder(R.drawable.image_animation)
                        .error(R.drawable.ic_broken_image_black_24dp)
                        .into(holders.imagePhotoLeft);
            } else holders.imagePhotoLeft.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person_black_48dp));
        }
    }

    private void setGravity(ViewHolderContent holder, int gravity) {
        holder.layoutMain.setGravity(gravity);
        holder.layoutView.setGravity(gravity);
        holder.layoutHeader.setGravity(gravity);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (gravity == Gravity.START) params.setMarginEnd(80);
        else params.setMarginStart(80);

        holder.layoutView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolderContent extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.text_fullname)
        TextView textFullname;
        @BindView(R.id.text_message)
        TextView textMessage;
        @BindView(R.id.text_timestamp)
        TextView textTimestamp;
        @BindView(R.id.image_status)
        ImageView imageStatus;
        @BindView(R.id.layout_main)
        LinearLayout layoutMain;
        @BindView(R.id.layout_view)
        RelativeLayout layoutView;
        @BindView(R.id.layout_content)
        LinearLayout layoutContent;
        @BindView(R.id.layout_header)
        LinearLayout layoutHeader;
        @BindView(R.id.image_photo_left)
        ImageView imagePhotoLeft;
        @BindView(R.id.image_photo_right)
        ImageView imagePhotoRight;

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
//                touchListener.onViewClick(view, getAdapterPosition());
            }
        }
    }
}
