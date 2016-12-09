/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package Modeleur;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JFileChooser;

import Blueprint.Room;
import Blueprint.Wall;

/**
 * class ModeleurController 
 * celui-ci dirige tous les instructions de fonctionnalite de modeleur.
 * */
public class ModeleurController implements ActionListener, MouseListener, MouseMotionListener {
	/** ModeleurModel qui fournit tous les variables et instances, ainsi que les methodes de fonctionnalite */
	ModeleurModel mm;
	
	public ModeleurController(ModeleurModel mm) {
		this.mm = mm;
	}

	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		boolean selected=false;
		GridBagConstraints g = new GridBagConstraints();
		
		if(mm.mode==1){
			for (Wall w : mm.room.getWalls()){
				w.getV1().select(x, y);
				if(w.getV1().isSelected()){
					mm.toolbar.removeAll();
					
					mm.toolbar.add(mm.optsVertex, BorderLayout.CENTER);
					mm.toolbar.add(mm.save, BorderLayout.SOUTH);
					mm.toolbar.validate();
					mm.toolbar.repaint();
					
					selected=true;
				}
				w.getV2().select(x, y);
				if(w.getV2().isSelected()){
					selected=true;
				}
				w.select(x, y);
				if(w.isSelected()){
					mm.toolbar.removeAll();
					
					mm.bVertex.addActionListener(this);
					mm.bDoor.addActionListener(this);
					mm.bWindow.addActionListener(this);
					
					mm.toolbar.add(mm.optsWall, BorderLayout.CENTER);
					mm.toolbar.add(mm.save, BorderLayout.SOUTH);
					mm.toolbar.validate();
					mm.toolbar.repaint();
					
					selected=true;
				}
				if(w.getOpen()!=null){
					w.getOpen().getV1().select(x, y);
					if(w.getOpen().getV1().isSelected()){
						selected=true;
					}
					w.getOpen().getV2().select(x, y);
					if(w.getOpen().getV2().isSelected()){
						selected=true;
					}
				}
				mm.graph.repaint();
				
				if(!selected){
					mm.toolbar.removeAll();
					mm.toolbar.add(mm.optsRoom, BorderLayout.CENTER);
					mm.toolbar.add(mm.save, BorderLayout.SOUTH);
					mm.toolbar.validate();
					mm.toolbar.repaint();
				}
			}
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		float x = e.getX();
		float y = e.getY();
		
		if(mm.mode==1){
			for (Wall w : mm.room.getWalls()){
				if(w.isSelected()){
					float[] list=w.move(x, y);
					mm.room.nextWall(w).getV1().move(list[2], list[3]);
					mm.room.lastWall(w).getV2().move(list[0], list[1]);
					mm.graph.repaint();
				}	
				else if (w.getV1().isSelected()){
					w.getV1().move(x-mm.r, y-mm.r);
					mm.graph.repaint();
				}
				else if (w.getV2().isSelected()){
					w.getV2().move(x-mm.r, y-mm.r);
					mm.graph.repaint();
				}
				else if (w.getOpen() != null){
					float[] l=w.moveOpen(x, y);
					float r=w.ratioOpen(x, y);
					if(w.getOpen().getV1().isSelected()){
						w.getOpen().getV1().move(l[0]-mm.r, l[1]-mm.r);
						w.getOpen().setR1(r);
					}
					else if (w.getOpen().getV2().isSelected()){
						w.getOpen().getV2().move(l[0]-mm.r, l[1]-mm.r);
						w.getOpen().serR2(r);
					}
					mm.graph.repaint();
				}
			}
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		GridBagConstraints g = new GridBagConstraints();
		
		if (source == mm.bSave && mm.mode==1){
			mm.saveDia.setVisible(true);
			final String dir = mm.saveDia.getDirectory();
			final String file = mm.saveDia.getFile();
			mm.saveDia.dispose();
			if (dir != null && file != null) {
				try {
					mm.room.write(dir+file);
					Room room2= new Room();
					room2.read(dir+file);
				} catch (IOException exception){
					System.out.println("cant even!");
				}
			}
		} else if (source == mm.bRoom){
			mm.mode=1;
			mm.toolbar.removeAll();
			
			mm.bRectangle.addActionListener(this);
			mm.bHexagon.addActionListener(this);
			mm.bOctogon.addActionListener(this);
			
			mm.toolbar.add(mm.optsRoom, BorderLayout.CENTER);
			mm.toolbar.add(mm.save, BorderLayout.SOUTH);
			mm.toolbar.validate();
			mm.toolbar.repaint();
			mm.graph.validate();
			mm.graph.repaint();
		} else if (source == mm.bCorridor ){
			mm.mode=2;
			mm.graph.validate();
			mm.graph.repaint();
		} else if (source == mm.bRectangle && mm.mode==1){
			mm.room = new Room(4,"Rectangle");
			mm.graph.repaint();
		} else if (source == mm.bHexagon && mm.mode==1){
			mm.room = new Room(6,"Hexagone");
			mm.graph.repaint();
		} else if (source == mm.bOctogon && mm.mode==1){
			mm.room = new Room(8,"Octogone");
			mm.graph.repaint();
		} else if (source == mm.bVertex && mm.mode==1){
			mm.room.addVertex();
			mm.graph.repaint();
			
			mm.toolbar.removeAll();
			mm.toolbar.add(mm.optsVertex, BorderLayout.CENTER);
			mm.toolbar.add(mm.save, BorderLayout.SOUTH);
			mm.toolbar.validate();
			mm.toolbar.repaint();
			
		} else if (source == mm.bDoor && mm.mode==1){
			mm.room.addDoor("Door");
			mm.graph.repaint();
		} else if (source == mm.bWindow && mm.mode==1){
			mm.room.addWindow("Window");
			mm.graph.repaint();
		} else if (source == mm.bDelVertex && mm.mode==1){
			mm.room.delVertex();
			mm.graph.repaint();
			
			mm.toolbar.removeAll();
			mm.toolbar.add(mm.optsRoom, BorderLayout.CENTER);
			mm.toolbar.add(mm.save, BorderLayout.SOUTH);
			mm.toolbar.validate();
			mm.toolbar.repaint();
			mm.graph.validate();
			mm.graph.repaint();
		}
	}

}
