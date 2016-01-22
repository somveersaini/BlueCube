package com.iblue.somveer.bluecube;

/**
 * Created by Samsaini on 11/3/2015.
 */
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
/*
 * A square drawn in 2 triangles (using TRIANGLE_STRIP). This square has one color.
 */
public class BaseSquare {
    float R = 0f;
    float G=  0f;
    float B = 0f;

    private FloatBuffer vertexBuffer;
    float x = 5.41f;

    // Buffer for vertex-array
    private float[] vertices = {  // Vertices for the square
            -x, -x,  0.0f,  // 0. left-bottom
             x, -x,  0.0f,  // 1. right-bottom
            -x,  x,  0.0f,  // 2. left-top
             x,  x,  0.0f   // 3. right-top
    };


    // Constructor - Setup the vertex buffer
    public BaseSquare() {
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
        gl.glColor4f(R, G,  B, 0.1f);
        // Draw the primitives from the vertex array directly
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }


}