/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package Navigateur;

import java.awt.FileDialog;
import java.awt.MenuItem;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JFrame;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
public class NavigateurModel {
	private static final int FPS = 60;
	protected MenuItem openItem= new MenuItem("Ouvrir");
	protected final JFrame frame = new JFrame();	 
	protected MenuItem closeItem = new MenuItem("Exit"); 
	protected FileDialog openDia=  new FileDialog(frame,"Ouvrir",FileDialog.LOAD);
	protected MenuItem menutexture = new MenuItem("Texture");
	protected FileDialog textureDia = new FileDialog(frame,"Texture",FileDialog.LOAD);
	protected String textureFileName;
	protected String textureFileType;
	
	// un map qui contien le  path d'image et le type d'image 
	protected HashMap<String,String> texture= new HashMap<String,String>();
	protected HashMap<String,Boolean> text= new HashMap<String,Boolean>();
	
	protected float textureTop, textureBottom, textureLeft, textureRight;
	protected GLCanvas canvas = new GLCanvas();
	protected final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
	protected String filename;
	private static float posX = 2;
	private float posZ = 2;
	private float headingY = 0; // heading of player; about y-axis
	private float lookUpAngle =0.0f;

	private float moveIncrement = 0.05f;
	private float turnIncrement = 3.0f; // each turn in degree
	private float lookUpIncrement = 1.0f;

	private float walkBias = 0;
	private float walkBiasAngle = 0;
	private static boolean isLightOn = false;

	/** retourner le coordonnée de X */
	public float getPosX(){
		return posX;
	}
	
	/** retourner le coordonnée de Z */
	public float getPosZ(){
		return posZ;
	}
	
	/** retourner le coordonnée de Y */
	public float getHeadingY(){
		return headingY;
	}
	
	/** retourner le lookupAngle*/
	public float getLookUpAngle(){
		return lookUpAngle;
	}
	
	/** retourner le moveIncrement*/
	public float getMoveIncrement() {
		return moveIncrement;
	}
	
	/** retrouner le TurnIncrement*/
	public float getTurnIncrement(){
		return turnIncrement;
	}
	
	/** retourner le LookUpTncrement*/
	public float getLookUpIncrement(){
		return lookUpIncrement;
	}
	
	/** retourner le WalkBias*/
	public float getWalkBias(){
		return walkBias;
	}
	
	/** retourner le WalkBiasAngle*/ 
	public float getWalkBiasAngle(){
		return walkBiasAngle;
	}
	
	/** méthode de tourner à gauche*/
	public void turnLeft(){
		headingY += turnIncrement;
	}

	/** éèthode de tourner à droit*/
	public void turnRight(){
		headingY -= turnIncrement;
	}
	
	/** l'état de la lumière, retourner True si la lumière est allumé, False si la lumière est étendre*/ 
	public boolean getIsLigntOn(){
		return isLightOn;
	}
	
	/** méthode de allumer la lumière*/ 
	public void turnLight(){
		isLightOn = !isLightOn;
	}
	
	/** méthode de bouger en avant */
	public void moveIn(){
		posX -= (float)Math.sin(Math.toRadians(headingY)) * moveIncrement;
		posZ -= (float)Math.cos(Math.toRadians(headingY)) * moveIncrement;
		walkBiasAngle = (walkBiasAngle >= 359.0f) ? 0.0f : walkBiasAngle + 10.0f;
		walkBias = (float)Math.sin(Math.toRadians(walkBiasAngle)) / 20.0f;
	}
	
	/** méthode de bouger en arrière*/
	public void moveOut() {
		posX += (float)Math.sin(Math.toRadians(headingY)) * moveIncrement;
		posZ += (float)Math.cos(Math.toRadians(headingY)) * moveIncrement;
		walkBiasAngle = (walkBiasAngle <=1.0f) ? 359.0f : walkBiasAngle - 10.0f;
		walkBias = (float)Math.sin(Math.toRadians(walkBiasAngle)) / 20.0f;
	}
	
	public void initTexture(){
		texture.put("images/crate.png", ".png");
		texture.put("images/glass.png", ".png");
		texture.put("images/nehe.png", ".png");		
	}


	/** méthode de regarder en haut*/
	public void lookUp(){
		lookUpAngle -= lookUpIncrement;
	}
	
	/** méthode de regarder en bas */
	public void lookDown() {
		lookUpAngle += lookUpIncrement;
	}
	public void changeCondition(String s){
		text.replace(s,true);
	}
	
	public void initText(){
		Set<String> Key = text.keySet();   
		for(String s : Key){
			if(text.get(s)){
				text.replace(s, false);
			}
		}
	}
}