package com.myvetpath.myvetpath;

import android.view.View;

public interface CustomSubClickListener {
    void onSubClick(View v, long Master_ID);
    boolean onSubLongClick(View v, int position);
}