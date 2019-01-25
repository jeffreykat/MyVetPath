package com.myvetpath.myvetpath;

import android.view.View;

public interface CustomSubClickListener {
    public void onSubClick(View v, int position);
    public boolean onSubLongClick(View v, int position);
}