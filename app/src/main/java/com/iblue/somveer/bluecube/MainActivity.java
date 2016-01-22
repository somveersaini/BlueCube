package com.iblue.somveer.bluecube;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    TextView tv2;
    private boolean bga = true;
    private boolean sfx = true;
    private Switch sbga = null;
    private Switch ssfx = null;
    SharedPreferences settings;
    private int HIGHSCORE;
    MyGLSurfaceView glview;

    SoundPool sp;
    int cmenu, wel;

    private int backButtonCount = 0;
    private long backButtonPreviousTime = 0;
    private boolean backButtonMessageHasBeenShown = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Go fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        load();

        sp = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        cmenu = sp.load(this, R.raw.cmenu, 1);

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(15, 20, 0, 20);
        row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        row.setBackgroundColor(Color.parseColor("#0d47a1"));

        int width = 50;
        int height = 50;
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
        parms.weight = 1;



        LinearLayout.LayoutParams move_params = new LinearLayout.LayoutParams(100,LinearLayout.LayoutParams.WRAP_CONTENT);
        move_params.weight = 8;

        TextView tvname = new TextView(this);
        tvname.setPadding(10, 0, 40, 6);
        tvname.setTextColor(Color.WHITE);
        tvname.setLayoutParams(move_params);
        tvname.setTextSize(22);
        tvname.setTypeface(Typeface.SERIF);
        tvname.setText("Blue Cube");
        row.addView(tvname);

        ImageView reset = new ImageView(this);
        // exit.setId(R.id.textview1);
        reset.setImageResource(R.drawable.home);
        reset.setLayoutParams(parms);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sfx) {
                    sp.play(cmenu, 1, 1, 0, 0, 1);
                }
                // setSettings();
                finish();
            }
        });
        row.addView(reset);

        ImageView stats = new ImageView(this);
        // exit.setId(R.id.textview1);
        stats.setImageResource(R.drawable.ic_stats);
        stats.setLayoutParams(parms);
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sfx){sp.play(cmenu, 1, 1, 0, 0, 1);}
                logs();
            }
        });
        row.addView(stats);



        ImageView setting = new ImageView(this);
        // exit.setId(R.id.textview1);
        setting.setImageResource(R.drawable.ic_settings);
        setting.setLayoutParams(parms);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sfx){sp.play(cmenu, 1, 1, 0, 0, 1);}
                setSettings();
            }
        });
        row.addView(setting);


        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll.setBackgroundColor(Color.parseColor("#2962ff"));

        ll.addView(row);


      //  LinearLayout.LayoutParams move_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
     //   move_params.setMargins(15, 15, 15, 15);

        tv2 = new TextView(this);
        tv2.setId(R.id.textview2);
        tv2.setTextSize(18);
        tv2.setPadding(0, 30, 0, 20);
       // tv2.setLayoutParams(move_params);
        tv2.setBackgroundColor(Color.parseColor("#0d47a1"));

        tv2.setTextColor(Color.WHITE);
        tv2.setTypeface(Typeface.SERIF);
        tv2.setText("Slide to Solve");

        tv2.setGravity(1);
        ll.addView(tv2);

        glview = new MyGLSurfaceView(this, tv2, HIGHSCORE);
        glview.setId(R.id.glSurface);
        ll.addView(glview);

        TextView tvl = new TextView(this);
        tvl.setTextSize(18);
        tvl.setPadding(0, 20, 0, 10);
        tvl.setBackgroundColor(Color.parseColor("#3f51b5"));
        tvl.setTextColor(Color.WHITE);
        tvl.setTypeface(Typeface.SERIF);
        tvl.setText("Slide to Solve.");
        tvl.setGravity(1);
        ll.addView(tvl);


        setContentView(ll);

    }
    @Override
    public void onBackPressed() {
        final long currentTime = System.currentTimeMillis();
        final long timeDiff = currentTime - backButtonPreviousTime;

        backButtonPreviousTime = currentTime;

        if((timeDiff < constants.BACK_PRESS_DELAY) || (backButtonCount == 0)) {
            backButtonCount++;
        } else {
            backButtonCount = 1;
        }

        if(backButtonCount >= constants.BACK_PRESS_COUNT) {
            this.finish();
            Intent intent = new Intent(this, MainMenu.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);

            startActivity(intent);
        }

        if(!backButtonMessageHasBeenShown) {
            final String msg = "Press back " + constants.BACK_PRESS_COUNT + " times to exit";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            backButtonMessageHasBeenShown = true;
        }
    }
    public void setSettings(){
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View promptsView = inflater.inflate(R.layout.settings, null);
        sbga = (Switch) promptsView.findViewById(R.id.switch1);
        ssfx = (Switch) promptsView.findViewById(R.id.switch2);
        if(bga){ sbga.setChecked(true); }
        if(sfx){  ssfx.setChecked(true); }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        alertDialog.show();
    }
    //saveState
    private  void save(){
        SharedPreferences.Editor editor = settings.edit();
        HIGHSCORE = glview.HighScore;
        editor.putInt("highscore",HIGHSCORE);
        editor.putBoolean("bga", bga);
        editor.putBoolean("sfx", sfx);
        editor.commit();
    }
    //loadState
    private void load(){
        HIGHSCORE = settings.getInt("highscore", 945);
        sfx = settings.getBoolean("sfx",true);
        bga = settings.getBoolean("bga", true);
    }

    public void logs(){
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View promptsView = inflater.inflate(R.layout.logs, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);


        final TextView logs = (TextView) promptsView
                .findViewById(R.id.logs);
        logs.setText("\nHigh Score " + glview.HighScore + " Moves\n" + "Current Moves " + glview.chal + "\n");

        final AlertDialog alertDialog = alertDialogBuilder.create();
        logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        alertDialog.show();
    }
    public void buttonsettings(View view) {
        final int id = view.getId();
       // if(sfx){sp.play(sce, 1, 1, 0, 0, 1);}
        Switch s = (Switch) view;
        boolean isChecked = s.isChecked();
        if(id == R.id.switch1) {
            if(isChecked){
                bga = true;
                glview.bga = true;
               // mp.start();
            }
            else{
               // mp.pause();
                bga = false;
                glview.bga = false;
            }

        } else if(id == R.id.switch2) {
            sfx = isChecked;
            glview.sfx = isChecked;
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        save();
    }

}
