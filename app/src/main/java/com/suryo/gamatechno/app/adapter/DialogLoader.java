package com.suryo.gamatechno.app.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.others.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogLoader extends Dialog {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.text_progress)
    TextView textProgress;

    private Activity activity;
    private boolean isStop;
    private String text;

    public DialogLoader(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public void setLabel(String text) {
        this.text = text;
    }

    public void setProgress(final int progress) {
        if (!isStop) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    progressBar.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_bar));
                    progressBar.setIndeterminate(false);
                }
            });
            isStop = true;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(progress);
                textProgress.setText(String.valueOf(progress).concat("%"));
                Utility.Logs.i(String.valueOf(progress).concat("%"));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loader);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setCancelable(false);
//        progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.progress_bar_indeterminate));
        progressBar.setIndeterminate(true);
        textProgress.setText(text != null ? text : "");
    }
}

