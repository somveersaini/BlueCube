package com.iblue.somveer.bluecube;

/**
 * Created by Samsaini on 10/31/2015.
 */
 import java.nio.ByteBuffer;
 import java.nio.ByteOrder;
 import java.nio.FloatBuffer;
 import javax.microedition.khronos.opengles.GL10;
/*
 * A square drawn in 2 triangles (using TRIANGLE_STRIP). This square has one color.
 */
public class Square {
    float centerx = 0;
    float centery = 0;
    float centerz = 0;
    float R = 1f;
    float G = 1f;
    float B = 1f;

    public boolean color = false;

    private FloatBuffer vertexBuffer;

      // Buffer for vertex-array
    private float[] vertices;


    // Constructor - Setup the vertex buffer
    public Square(float[] center, boolean color) {
        // Setup vertex array buffer. Vertices in float. A float has 4 bytes
        this.color = color;
        vertices = init(center, color);
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
        vertexBuffer.put(vertices);         // Copy data into buffer
        vertexBuffer.position(0);           // Rewind
    }

    // Render the shape

    public void draw(GL10 gl) {
        // Enable vertex-array and define its buffer
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        // Set the current color (NEW)
        gl.glColor4f(R, G,  B, 1.0f);
        // Draw the primitives from the vertex array directly
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    public float[] init(float[] center, boolean color){
        centerx = center[0];
        centery = center[1];
        centerz = center[2];

        float[] vertic = {  // Vertices for the square
                -1.0f+centerx, -1.0f+centery,  0.0f+centerz,  // 0. left-bottom
                1.0f+centerx, -1.0f+centery,  0.0f+centerz,  // 1. right-bottom
                -1.0f+centerx,  1.0f+centery,  0.0f+centerz,  // 2. left-top
                1.0f+centerx,  1.0f+centery,  0.0f+centerz   // 3. right-top
        };

        if(color){
            R = 0;
            G = 0f;
            B = 1;
        }
        return vertic;
    }
    public void setColor(boolean c){

        color = c;
        if(color){
            R = 0f;
            G = 0f;
            B = 1f;
        }else {
            R = 1f;
            G = 1f;
            B = 1f;
        }
    }
    public boolean getColor(){
        return color;
    }
}