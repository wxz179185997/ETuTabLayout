package com.etu.tablayout.utils;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import com.etu.tablayout.MessageView;

public class MsgUtils {

    public static void showRedDot(MessageView msgView) {
        showMessageView(msgView, 0);
    }

    public static void showMessageView(MessageView msgView, int number) {
        if (msgView == null) {
            return;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) msgView.getLayoutParams();
        DisplayMetrics metrics = msgView.getResources().getDisplayMetrics();
        msgView.setVisibility(View.VISIBLE);
        if (number <= 0) {
            msgView.setStrokeWidth(0);
            msgView.setText("");

            layoutParams.width = (int) (7 * metrics.density);
            layoutParams.height = (int) (7 * metrics.density);
            layoutParams.setMargins(0,(int) (8 * metrics.density),(int) (16 * metrics.density),0);
            msgView.setLayoutParams(layoutParams);

        } else {
            layoutParams.height = (int) (15 * metrics.density);
            if (number > 0 && number < 10) {
                layoutParams.width = (int) (17 * metrics.density);
                msgView.setText(number + "");
            } else if (number >= 10 && number <= msgView.getMaxNumber()) {
                layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                msgView.setPadding((int) (5 * metrics.density), 0, (int) (5* metrics.density), 0);
                msgView.setText(number + "");
            } else {
                layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                msgView.setPadding((int) (5 * metrics.density), 0, (int) (5 * metrics.density), 0);
                msgView.setText(msgView.getMaxNumber() + "+");
            }
        }
    }

}
