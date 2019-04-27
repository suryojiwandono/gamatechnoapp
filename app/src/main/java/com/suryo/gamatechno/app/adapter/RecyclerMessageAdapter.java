package com.suryo.gamatechno.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.model.Message;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Message> messages;
    private String fromUserId;

    public RecyclerMessageAdapter(Context context, List<Message> messages, String fromUserId) {
        this.context = context;
        this.messages = messages;
        this.fromUserId = fromUserId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycler_message, parent, false);
        v.setTag(viewType);
        return new ViewHolderContent(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderContent holders = (ViewHolderContent) holder;
        Message message = messages.get(position);
        holders.textFullname.setText(message.fromUsername);
        holders.textMessage.setText(message.messageContent);
        holders.textTimestamp.setText(message.timestamp);

        if (message.messageStatus.equals("PENDING")) {
            holders.imageStatus.setImageResource(R.drawable.ic_access_time_black_24dp);
            holders.imageStatus.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            holders.imageStatus.setImageResource(R.drawable.ic_check_black_24dp);
            holders.imageStatus.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        if (message.fromUserId.equals(fromUserId)) {
            setGravity(holders, Gravity.END);
            holders.layoutView.setBackgroundResource(R.drawable.bg_chat);
        } else {
            setGravity(holders, Gravity.START);
            holders.layoutView.setBackgroundResource(R.drawable.bg_chat_own);
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
        LinearLayout layoutView;
        @BindView(R.id.layout_header)
        LinearLayout layoutHeader;

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
