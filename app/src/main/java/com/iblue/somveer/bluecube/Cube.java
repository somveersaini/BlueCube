package com.iblue.somveer.bluecube;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Stack;

public class Cube {
	private FloatBuffer vertexBuffer;  // Buffer for vertex-array
	Stack<float[]> colorStack ;
	private int numFaces = 6;

    float[] blue = {0.0f, 0f, 1.0f, 1f};
    public float[][] precolors = {  // Colors of the 6 faces
            {0.118f, 0.4f, 1f, 1.0f},  // 0. orange
            {1.0f, 0.0f, 1.0f, 1.0f},  // 1. violet
            {0.0f, 1.0f, 0.0f, 1.0f},  // 2. green
            {0.7f, 0.2f, 1.0f, 1.0f},  // 3. blue
            {1.0f, 0.0f, 0.0f, 1.0f},  // 4. red
            {1.0f, 1.0f, 0.0f, 1.0f}   // 5. yellow
    };

	public static float[][] colors = {  // Colors of the 6 faces
			{0.118f, 0.4f, 1f, 1.0f},  // 0. orange
			{1.0f, 0.0f, 1.0f, 1.0f},  // 1. violet
			{0.0f, 1.0f, 0.0f, 1.0f},  // 2. green
			{0.7f, 0.2f, 1.0f, 1.0f},  // 3. blue
			{1.0f, 0.0f, 0.0f, 1.0f},  // 4. red
			{1.0f, 1.0f, 0.0f, 1.0f}   // 5. yellow
	};

	private float[] vertices = {  // Vertices of the 6 faces
			// FRONT
			-1.0f, -1.0f,  1.0f,  // 0. left-bottom-front
			1.0f, -1.0f,  1.0f,  // 1. right-bottom-front
			-1.0f,  1.0f,  1.0f,  // 2. left-top-front
			1.0f,  1.0f,  1.0f,  // 3. right-top-front
			// BACK
			1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
			-1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
			1.0f,  1.0f, -1.0f,  // 7. right-top-back
			-1.0f,  1.0f, -1.0f,  // 5. left-top-back
			// LEFT
			-1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
			-1.0f, -1.0f,  1.0f,  // 0. left-bottom-front
			-1.0f,  1.0f, -1.0f,  // 5. left-top-back
			-1.0f,  1.0f,  1.0f,  // 2. left-top-front
			// RIGHT
			1.0f, -1.0f,  1.0f,  // 1. right-bottom-front
			1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
			1.0f,  1.0f,  1.0f,  // 3. right-top-front
			1.0f,  1.0f, -1.0f,  // 7. right-top-back
			// TOP
			-1.0f,  1.0f,  1.0f,  // 2. left-top-front
			1.0f,  1.0f,  1.0f,  // 3. right-top-front
			-1.0f,  1.0f, -1.0f,  // 5. left-top-back
			1.0f,  1.0f, -1.0f,  // 7. right-top-back
			// BOTTOM
			-1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
			1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
			-1.0f, -1.0f,  1.0f,  // 0. left-bottom-front
			1.0f, -1.0f,  1.0f   // 1. right-bottom-front
	};



	// Constructor - Set up the buffers
	public Cube() {
		colorStack = new Stack<float[]>();
        colors = precolors;
		// Setup vertex-array buffer. Vertices in float. An float has 4 bytes
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder()); // Use native byte order
		vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
		vertexBuffer.put(vertices);         // Copy data into buffer
		vertexBuffer.position(0);           // Rewind
	}

	// Draw the shape
	public void draw(GL10 gl) {
		gl.glFrontFace(GL10.GL_CCW);    // Front face in counter-clockwise orientation
		gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
		gl.glCullFace(GL10.GL_BACK);    // Cull the back face (don't display)

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		// Render all the faces
		for (int face = 0; face < numFaces; face++) {
			gl.glColor4f(colors[face][0], colors[face][1], colors[face][2], colors[face][3]);
			// Draw the primitive from the vertex-array directly
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, face*4, 4);
		}
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);
	}



	public void setColor( boolean color) {
        float[] temp = new float[4];
            if (color) {
                temp = colors[1];
                colorStack.push(temp);
                colors[1] = blue;

            } else {
                temp = colorStack.pop();
                colors[1] = temp;
            }
    }
    public boolean checkBlue(float[] color){
        for(int i = 0; i< 4; i++){
            if(color[i] != blue[i]){
                return  false;
            }
        }

        return  true;
    }
    public int numColorFaces(){
        return colorStack.size();
    }
	public void updateColors(int direction){

		float[][] temp = new float[6][4];


		if(direction == constants.RIGHT){
			temp[0] = colors[2];
			temp[1] = colors[3];
			temp[2] = colors[1];
			temp[3] = colors[0];
			temp[4] = colors[4];
			temp[5] = colors[5];
		}
		if(direction == constants.LEFT){
			temp[0] = colors[3];
			temp[1] = colors[2];
			temp[2] = colors[0];
			temp[3] = colors[1];
			temp[4] = colors[4];
			temp[5] = colors[5];


		}
		if(direction == constants.UP){
			temp[0] = colors[5];
			temp[1] = colors[4];
			temp[4] = colors[0];
			temp[5] = colors[1];
			temp[2] = colors[2];
			temp[3] = colors[3];
		}
		if(direction == constants.DOWN){
			temp[0] = colors[4];
			temp[1] = colors[5];
			temp[4] = colors[1];
			temp[5] = colors[0];
			temp[2] = colors[2];
			temp[3] = colors[3];
		}
		colors = temp;
	}
    public boolean getDownColor(){
        if(checkBlue(colors[1])){
            return  true;
        }else {
            return  false;
        }
    }
}