/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package Navigateur;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_LEQUAL;
import static com.jogamp.opengl.GL.GL_LINEAR;
import static com.jogamp.opengl.GL.GL_NICEST;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static com.jogamp.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHT1;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_POSITION;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;


import java.io.IOException;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureIO;

import Blueprint.Room;

@SuppressWarnings("serial")
public class NavigateurView extends GLCanvas implements GLEventListener{
	private GLU glu;

	private NavigateurModel model;


	// ** Constructeur par default */
	public NavigateurView(NavigateurModel model) {
		this.model = model;
		this.addGLEventListener(this);
	}

	// ----- implement methods declared in GLEventListener -----

	

	/**
	 * Called back before the OpenGL context is destroyed. Release resource such as buffers.
	 * */
	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	/**
	 * Called back immediately. after the OpenGL context is initialized. Can be used 
	 * to perform one-time initialization. Run only once.
	 * */
	@Override
	public void init(GLAutoDrawable drawable) {
	      GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
	      glu = new GLU();                         // get GL Utilities
	      gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
	      gl.glClearDepth(1.0f);      // set clear depth value to farthest
	      gl.glEnable(GL_DEPTH_TEST); // enables depth testing
	      gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
	      gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
	      gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
		
	}
	/** 
	 * Called back by the animator to perform rendering
	 * */
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
		gl.glLoadIdentity();  // reset the model-view matrix

		// Rotate up and down to look up and down
		gl.glRotatef(model.getLookUpAngle(), 1.0f, 0, 0);

		// Player at headingY, Rotate the scene by -headingY instead (add 360 to get
		// positive angle)
		gl.glRotatef(360.0f - model.getHeadingY(), 0, 1.0f, 0);

		// Player is at (posX, 0, posZ), Translate the scene to (-posX, 0, -posZ)
		gl.glTranslatef(-model.getPosX(), -model.getWalkBias() - 0.5f, -model.getPosZ());
		if(model.textureFileName!=null){
			try {
				
				// Create a OpenGL Texture object from (URL, mipmap, file suffix)
				// Use URL so that can read from JAR and disk file.
				//System.out.println(model.textureFileName);
				//System.out.println(model.textureFileType);
				model.texture = TextureIO.newTexture(
						getClass().getClassLoader().getResource(model.textureFileName), // relative to project root 
						false, model.textureFileType);

				// Use linear filter for texture if image is larger than the original texture
				gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
				// Use linear filter for texture if image is smaller than the original texture
				gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

				// Texture image flips vertically. Shall use TextureCoords class to retrieve
				// the top, bottom, left and right coordinates, instead of using 0.0f and 1.0f.
				TextureCoords textureCoords = model.texture.getImageTexCoords();
				model.textureTop = textureCoords.top();
				model.textureBottom = textureCoords.bottom();
				model.textureLeft = textureCoords.left();
				model.textureRight = textureCoords.right();
			} catch (GLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		// Lighting
		if (model.getIsLigntOn()) {
			gl.glEnable(GL_LIGHTING);
		} else {
			gl.glDisable(GL_LIGHTING);
		}
		if(model.texture!=null){
			
			// Enables this texture's target in the current GL context's state.
			model.texture.enable(gl);
			// same as gl.glEnable(texture.getTarget());
			// gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
			// Binds this texture to the current GL context.
			model.texture.bind(gl);  // same as gl.glBindTexture(texture.getTarget(), texture.getTextureObject());
			// ----- creer des objets -----
		}
		// first room

		Room r=new Room();
		if(model.filename!=null){
			try {
				r.read(model.filename);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(model.texture!=null){
				r.draw(gl, model.textureTop, model.textureBottom, model.textureLeft,model.textureRight);
				//System.out.println("1235465");
			}
			else{
				r.draw(gl);


			}
			// r.draw(gl);
		}

	}
	/**
	 * Call-back handler for window re-size event. Also called when the drawable is
	 * first set to visible.
	 * */
	 @Override
	   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	      GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

	      if (height == 0) height = 1;   // prevent divide by zero
	      float aspect = (float)width / height;

	      // Set the view port (display area) to cover the entire window
	      gl.glViewport(0, 0, width, height);

	      // Setup perspective projection, with aspect ratio matches viewport
	      gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
	      gl.glLoadIdentity();             // reset projection matrix
	      glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar

	      // Enable the model-view transform
	      gl.glMatrixMode(GL_MODELVIEW);
	      gl.glLoadIdentity(); // reset
	   }

}