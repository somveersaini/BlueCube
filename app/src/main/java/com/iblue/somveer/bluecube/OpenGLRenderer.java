package com.iblue.somveer.bluecube;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

public class OpenGLRenderer implements Renderer {

    float z = -20.0f;
    float angle = 0f;

    public int direction;
    float translatex = 0;
    float translatey = 0;
    int sx = 2;
    int sy = 2;

    private Cube mCube ;
    private Square[] mSquare;

    // Lighting (NEW)
    boolean lightingEnabled = false;   // Is lighting on? (NEW)
    private float[] lightAmbient = {0.8f, 0.8f, 0.8f, 0.8f};
    private float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
    private float[] lightPosition = {2f, -2f, -5f, 0f};
    private boolean blendingEnabled = true;
    private BaseSquare mBase = new BaseSquare();

    public OpenGLRenderer(){
        mCube = new Cube();
        mSquare = new Square[25];
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        gl.glClearColor(0.118f, 0.565f, 1f, 1.0f);  // Set color's clear-value to black
        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);




        // Setup lighting GL_LIGHT1 with ambient and diffuse lights (NEW)
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, lightAmbient, 0);
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, lightDiffuse, 0);
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, lightPosition, 0);
        gl.glEnable(GL10.GL_LIGHT1);   // Enable Light 1 (NEW)
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glEnable(GL10.GL_COLOR_MATERIAL);
        // Setup Blending (NEW)
        //gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);           // Full brightness, 50% alpha (NEW)
        //gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

        mSquare = initSquare();


    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClearColor(0,0,0,0.5f);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glLoadIdentity();
        if (lightingEnabled) {
            gl.glEnable(GL10.GL_LIGHTING);
        } else {
            gl.glDisable(GL10.GL_LIGHTING);
        }
        if (blendingEnabled) {
            gl.glEnable(GL10.GL_BLEND);       // Turn blending on (NEW)
            gl.glDisable(GL10.GL_DEPTH_TEST); // Turn depth testing off (NEW)

        } else {
            gl.glDisable(GL10.GL_BLEND);      // Turn blending off (NEW)
            gl.glEnable(GL10.GL_DEPTH_TEST);  // Turn depth testing on (NEW)
        }

        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, z - 1f);
        mBase.draw(gl);

        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, z);

            float tx[] = {-0.2f, -0.1f, 0, 0.1f, 0.2f };
            float ty[] = {-0.2f, -0.1f, 0, 0.1f, 0.2f };
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 5; j++) {
                    gl.glLoadIdentity();
                    gl.glTranslatef(tx[i] , ty [j] , z);
                    mSquare[(i*5) + j].draw(gl);
                }
            }


            gl.glLoadIdentity(); // Reset the model-view matrix
            if(direction == constants.RIGHT){
                gl.glTranslatef(translatex, translatey, z);
                gl.glRotatef(angle, 0.0f, 1.0f, 0.0f);
            }else if(direction == constants.LEFT){
                gl.glTranslatef(translatex, translatey, z);
                gl.glRotatef(-angle, 0.0f, 1.0f, 0.0f);
            }else if(direction == constants.UP){
                gl.glTranslatef(translatex, translatey, z);
                gl.glRotatef(-angle, 1.0f, 0.0f, 0.0f);

            }else if(direction == constants.DOWN) {
                gl.glTranslatef(translatex, translatey, z);
                gl.glRotatef(angle, 1.0f, 0.0f, 0.0f);
            }else{
                gl.glTranslatef(0, 0, z);
            }

            mCube.draw(gl);
        gl.glRotatex(30,0,1,0);

    }



    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = (float) width / height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 42.0f, ratio, 0.1f, 100.0f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
       // gl.glLoadIdentity();                        // reset the matrix to its default state
        gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7);  // apply the projection matrix
    }

    public Square[] initSquare(){
        Square[] squares = new Square[25];
        int faces = 0;
        boolean color = false;
        Random randomf = new Random();
        float center[] = {0, 0 , -1};
        float order[] = {-4, -2, 0, 2, 4};
        for(int i = 0; i < 5 ; i++){
            for(int j = 0; j < 5; j++) {
                if (faces < 6) {
                    if (randomf.nextBoolean()) {
                        color = true;
                        faces++;
                    }
                }
                center[0] = order[i];
                center[1] = order[j];
                squares[(i*5) + j] = new Square(center, color);
                color = false;
            }
        }

        return squares;
    }

    public void  Update(){
        mCube.updateColors(direction);
        if(direction == constants.LEFT){
            sx--;
        }
        if(direction == constants.RIGHT){
            sx++;
        }
        if(direction == constants.UP){
            sy++;
        }
        if(direction == constants.DOWN){
            sy--;
        }
        int underSquare = underSquare();
        if(underSquare != -1) {
            if (mSquare[underSquare].getColor()) {
                if (!mCube.getDownColor()) {
                    mSquare[underSquare].setColor(false);
                    mCube.setColor(true);
                }
            } else {
                if (mCube.getDownColor()) {
                    mSquare[underSquare].setColor(true);
                    mCube.setColor(false);
                }
            }
        }

    }

    public int ColorFaces(){
        return mCube.colorStack.size();
    }
    public int underSquare(){

        int r = sx*5 + sy;
        if(r < 0 || r > 24){
            r = -1;
        }
        Log.d("underSquare", r+"");
        return  r;
    }


}
