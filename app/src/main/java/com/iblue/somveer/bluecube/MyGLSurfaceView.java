
package com.iblue.somveer.bluecube;

import android.app.AlertDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MyGLSurfaceView extends GLSurfaceView {
    public boolean bga = true;
    public boolean sfx = true;
    public int HighScore;

    private final OpenGLRenderer mRenderer;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    float  x1 = 0, y1 = 0; //for touch events
    float translatex = 0;
    float translatey = 0;
    Context cntxt ;

    float tx[] = {-4.2f, -2.1f, 0, 2.1f, 4.2f };
    SoundPool sp;
    int srow, scol, win;
    TextView txt;
    int colorfaces;
    boolean isCompleted = false;

    int chal = 0;

    public MyGLSurfaceView(Context context, TextView tv, int highScore) {
        super(context);
        cntxt = context;
        txt = tv;
        HighScore = highScore;
        setEGLContextClientVersion(1);
        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new OpenGLRenderer();
        setRenderer(mRenderer);
        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        sp = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        scol = sp.load(context, R.raw.scm, 1);
        srow = sp.load(context, R.raw.scb, 1);
        win = sp.load(context, R.raw.win, 1);
    }


    int sx = 2;
    int sy = 2;

    static boolean unlock = true;
    // Handler for touch event
    public void touch(MotionEvent evt){
        unlock  = false;
        float   x2, y2, dx, dy;
        int direction = 0;

        switch (evt.getAction()) {
            case (MotionEvent.ACTION_DOWN):

                x1 = evt.getX();
                y1 = evt.getY();
                break;
            case (MotionEvent.ACTION_UP):
                x2 = evt.getX();
                y2 = evt.getY();

                dx = x2 - x1;
                dy = y2 - y1;



                float translaterx = 0;
                float translatery = 0f;
                int test = 30;

                if (Math.abs(dx) > test || Math.abs(dy) > test) {


                    // Use dx and dy to determine the direction

                    if (Math.abs(dx) > Math.abs(dy)) {
                        if(sfx) sp.play(srow, 1f, 1f, 0, 0, 1);
                        if (dx > 0) {
                            direction = constants.RIGHT;
                            if(sx == 4){
                                break;
                            }
                            else{
                               translaterx = 0.42f;
                                translatex = tx[sx];
                                sx++;

                            }

                        } else {
                            direction = constants.LEFT;
                            if(sx == 0){
                                break;
                            }
                            else{
                                translaterx = -0.42f;
                                translatex = tx[sx];
                                sx--;
                            }
                        }
                    } else {
                        if (sfx) sp.play(scol, 1f, 1f, 0, 0, 1);
                        if (dy > 0) {
                            direction = constants.DOWN;
                            if(sy == 0){
                                break;
                            }
                            else{
                                translatery = -0.42f;
                                translatey = tx[sy];
                                sy--;
                            }
                        } else {
                            direction = constants.UP;
                            if(sy == 4){
                                break;
                            }
                            else{
                                translatery = 0.42f;
                                translatey = tx[sy];
                                sy++;
                            }
                        }
                    }
                    Log.d("touch up  ", " " + direction + " " + dx + " " + dy + " " + x1 + " " + y1);

                    mRenderer.direction = direction;


                    boolean isRunning = true;
                    float angle = 0f;
                    long previous = System.currentTimeMillis();
                    while (isRunning) {
                        long current = System.currentTimeMillis();
                        if ((current - previous) > 20) {
                            previous = current;
                            angle += 18f;
                            translatex = translatex + translaterx;
                            translatey = translatey + translatery;
                            mRenderer.angle = angle;
                            mRenderer.translatex = translatex;
                            mRenderer.translatey = translatey;

                            requestRender();
                            if (angle == 90) {
                                if(!isCompleted) {
                                    mRenderer.Update();
                                }

                                isRunning = false;
                            }
                        }

                    }

                    chal++;
                    ShowMoves();
                }

        }
        unlock = true;
    }
    @Override
    public boolean onTouchEvent(final MotionEvent evt) {
        if(unlock){
            touch(evt);
            return true;
        }
        return false;  // Event handled
    }

    public void ShowMoves(){
       // MainActivity.showmoves(chal);
        colorfaces = mRenderer.ColorFaces();
        String complete = "";
        if(!isCompleted) {
            if (colorfaces == 6) {
                isCompleted = true;
                if (HighScore > chal) {
                    HighScore = chal;
                    log(1);
                }else {
                    log(2);
                }
                complete = "Completed";
            }
        }
        txt.setText(" Moves  " + chal + "     Blueface  " + colorfaces + "   " + complete);
    }

    public void log(int arg){
        LayoutInflater inflater = (LayoutInflater) cntxt
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View promptsView = inflater.inflate(R.layout.logs, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cntxt);
        alertDialogBuilder.setView(promptsView);


        final TextView logs = (TextView) promptsView
                .findViewById(R.id.logs);
        if (sfx) sp.play(win, 1f, 1f, 0, 0, 1);
        if(arg == 1){
            logs.setText("\nNew\nHigh Score \n" + chal + " Moves\n");
        }else {
            logs.setText("\n Cube Solved\nScore \n"  + chal + " Moves\n");
        }

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






}
