package com.org.pawpal.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.org.pawpal.R;
import com.org.pawpal.interfaces.OnSendMessageListener;

/**
 * Created by hp-pc on 13-01-2017.
 */

public class SendMessageDialog extends DialogFragment {
    private EditText etMsg;
    private RelativeLayout rlSendMsg;
    private View view;
    private OnSendMessageListener onSendMessageListener;

    public static SendMessageDialog newInstance() {
        return new SendMessageDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.activity_send_message, container, false);
        init();
        return view;
    }

    private void init() {
        etMsg = (EditText)view.findViewById(R.id.et_msg);
        rlSendMsg = (RelativeLayout)view.findViewById(R.id.rl_send_msg);
        rlSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = etMsg.getText().toString();
                if (!message.isEmpty())
                {
                    if (onSendMessageListener != null)
                        onSendMessageListener.onMessageSend(message);
                    dismiss();
                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onSendMessageListener = (OnSendMessageListener)context;
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
