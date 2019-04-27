package com.suryo.gamatechno.app.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.others.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogMessage extends Dialog implements View.OnClickListener {
    @BindView(R.id.layout_title)
    LinearLayout layoutTitle;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_message)
    TextView textMessage;
    @BindView(R.id.text_message_detail)
    TextView textMessageDetail;
    @BindView(R.id.image_close)
    ImageView imageClose;
    @BindView(R.id.button_left)
    Button buttonLeft;
    @BindView(R.id.button_middle)
    Button buttonMiddle;
    @BindView(R.id.button_right)
    Button buttonRight;

    private Activity activity;
    private String message, messageDetail;

    public DialogMessage(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageDetail(String messageDetail) {
        this.messageDetail = messageDetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_message);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this);

        onInit();

        if (message == null || message.equals("")) textMessage.setVisibility(View.GONE);
        else textMessage.setText(message);
        if (messageDetail == null || messageDetail.equals(""))
            textMessageDetail.setVisibility(View.GONE);
        else textMessageDetail.setText(messageDetail);

        imageClose.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLeft) {
            if (onButtonLeftListener != null) onButtonLeftListener.onClick(null);
        } else if (view == buttonMiddle) {
            if (onButtonMiddleListener != null) onButtonMiddleListener.onClick(null);
        } else if (view == buttonRight) {
            if (onButtonRightListener != null) onButtonRightListener.onClick(null);
        }
//        else if (view == imageClose) {
//            if (onButtonLeftListener != null) onButtonLeftListener.onClick();
//            if (onButtonMiddleListener != null) onButtonMiddleListener.onClick();
//            if (onButtonRightListener != null) onButtonRightListener.onClick();
//        }
        dismiss();
    }

    private Response.OnButtonListener onButtonLeftListener, onButtonRightListener, onButtonMiddleListener;
    private CharSequence titleButtonLeft, titleButtonRight, titleButtonMiddle;
    private int buttonColorLeft, buttonColorRight, buttonColorMiddle;
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setButtonMiddle(CharSequence text) {
        this.titleButtonMiddle = text;
        this.buttonColorMiddle = Constants.ColorButton.BLACK;
    }

    public void setButtonMiddle(CharSequence text, int buttonColorMiddle) {
        this.titleButtonMiddle = text;
        this.buttonColorMiddle = buttonColorMiddle;
    }

    public void setButtonLeft(CharSequence text) {
        this.titleButtonLeft = text;
        this.buttonColorLeft = Constants.ColorButton.BLACK;
    }

    public void setButtonLeft(CharSequence text, int buttonColorLeft) {
        this.titleButtonLeft = text;
        this.buttonColorLeft = buttonColorLeft;
    }

    public void setButtonRight(CharSequence text) {
        this.titleButtonRight = text;
        this.buttonColorRight = Constants.ColorButton.BLACK;
    }

    public void setButtonRight(CharSequence text, int buttonColorRight) {
        this.titleButtonRight = text;
        this.buttonColorRight = buttonColorRight;
    }

    public void setButtonLeft(CharSequence text, Response.OnButtonListener onButtonListener) {
        this.titleButtonLeft = text;
        this.onButtonLeftListener = onButtonListener;
        this.buttonColorLeft = Constants.ColorButton.BLACK;
    }

    public void setButtonMiddle(CharSequence text, Response.OnButtonListener onButtonListener) {
        this.titleButtonMiddle = text;
        this.onButtonMiddleListener = onButtonListener;
        this.buttonColorMiddle = Constants.ColorButton.BLACK;
    }

    public void setButtonRight(CharSequence text, Response.OnButtonListener onButtonListener) {
        this.titleButtonRight = text;
        this.onButtonRightListener = onButtonListener;
        this.buttonColorRight = Constants.ColorButton.BLACK;
    }

    public void setButtonRight(CharSequence text, Response.OnButtonListener onButtonListener, int color) {
        this.titleButtonRight = text;
        this.onButtonRightListener = onButtonListener;
        this.buttonColorRight = color;
    }

    public void setButtonMiddle(CharSequence text, Response.OnButtonListener onButtonListener, int color) {
        this.titleButtonMiddle = text;
        this.onButtonMiddleListener = onButtonListener;
        this.buttonColorMiddle = color;
    }

    public void setButtonLeft(CharSequence text, Response.OnButtonListener onButtonListener, int color) {
        this.titleButtonLeft = text;
        this.onButtonLeftListener = onButtonListener;
        this.buttonColorLeft = color;
    }

    private void onInit() {
        if (title == null) layoutTitle.setVisibility(View.GONE);
        else textTitle.setText(title);

        if (titleButtonRight != null) {
            buttonRight.setText(titleButtonRight);
            buttonRight.setVisibility(View.VISIBLE);
            switch (buttonColorRight) {
                case Constants.ColorButton.BLACK:
                    buttonRight.setBackgroundResource(R.drawable.button_dialog_black_right);
                    break;
                case Constants.ColorButton.RED:
                    buttonRight.setBackgroundResource(R.drawable.button_dialog_red_right);
                    break;
                case Constants.ColorButton.BLUE:
                    buttonRight.setBackgroundResource(R.drawable.button_dialog_blue_right);
                    break;
            }
        } else buttonRight.setVisibility(View.GONE);

        if (titleButtonLeft != null) {
            buttonLeft.setText(titleButtonLeft);
            buttonLeft.setVisibility(View.VISIBLE);
            switch (buttonColorLeft) {
                case Constants.ColorButton.BLACK:
                    buttonLeft.setBackgroundResource(R.drawable.button_dialog_black_left);
                    break;
                case Constants.ColorButton.RED:
                    buttonLeft.setBackgroundResource(R.drawable.button_dialog_red_left);
                    break;
                case Constants.ColorButton.BLUE:
                    buttonLeft.setBackgroundResource(R.drawable.button_dialog_blue_left);
                    break;
            }
        } else buttonLeft.setVisibility(View.GONE);

        if (titleButtonMiddle != null) {
            buttonMiddle.setText(titleButtonMiddle);
            buttonMiddle.setVisibility(View.VISIBLE);
            switch (buttonColorMiddle) {
                case Constants.ColorButton.BLACK:
                    if (titleButtonRight != null || titleButtonLeft != null)
                        buttonMiddle.setBackgroundResource(R.drawable.button_dialog_black_middle);
                    else
                        buttonMiddle.setBackgroundResource(R.drawable.button_dialog_black);
                    break;
                case Constants.ColorButton.RED:
                    if (titleButtonRight != null || titleButtonLeft != null)
                        buttonMiddle.setBackgroundResource(R.drawable.button_dialog_red_middle);
                    else
                        buttonMiddle.setBackgroundResource(R.drawable.button_dialog_red);
                    break;
                case Constants.ColorButton.BLUE:
                    if (titleButtonRight != null || titleButtonLeft != null)
                        buttonMiddle.setBackgroundResource(R.drawable.button_dialog_blue_middle);
                    else
                        buttonMiddle.setBackgroundResource(R.drawable.button_dialog_blue);
                    break;
            }
        } else buttonMiddle.setVisibility(View.GONE);

        buttonLeft.setOnClickListener(this);
        buttonMiddle.setOnClickListener(this);
        buttonRight.setOnClickListener(this);
        imageClose.setOnClickListener(this);
    }
}

