package com.iblue.somveer.bluecube;

import android.view.View;

public class AboutActivity extends BaseActivity {
    private View gView;

    @Override
    public void onCreate() {
        super.onCreate();
        //getWindow().getAttributes().windowAnimations = R.style.win_animation;

        gView = addView(R.layout.about);
    }


}
