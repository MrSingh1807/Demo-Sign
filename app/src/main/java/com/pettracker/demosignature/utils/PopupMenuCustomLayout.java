package com.pettracker.demosignature.utils;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class PopupMenuCustomLayout {
    private PopupMenuCustomOnClickListener onClickListener;
    private Context context;
    private PopupWindow popupWindow;
    private int rLayoutId;
    private View popupView;

    public PopupMenuCustomLayout(Context context, int rLayoutId, PopupMenuCustomOnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
        this.rLayoutId = rLayoutId;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(rLayoutId, null);
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.setWindowLayoutMode(width,height);
        popupWindow.setElevation(10);

        LinearLayout linearLayout = (LinearLayout) popupView;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View v = linearLayout.getChildAt(i);
            v.setOnClickListener( v1 -> { onClickListener.onClick( v1.getId()); popupWindow.dismiss(); });
        }
    }
    public void setAnimationStyle( int animationStyle) {
        popupWindow.setAnimationStyle(animationStyle);
    }
    public void show() {
        popupWindow.showAtLocation( popupView, Gravity.CENTER, 0, 0);
    }

    public void show( View anchorView, int gravity, int offsetX, int offsetY) {
        popupWindow.showAsDropDown( anchorView, 0, -2 * (anchorView.getHeight()));
    }

    public interface PopupMenuCustomOnClickListener {
        public void onClick(int menuItemId);
    }
}