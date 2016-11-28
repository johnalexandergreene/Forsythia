package org.fleen.forsythia.app.drifter;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.fleen.geom_2D.GD;

public class DrifterWindow extends JFrame{
  
  private static final long serialVersionUID=8413646909208773971L;

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  DrifterWindow(Drifter drifter){
    this.drifter=drifter;
    setBounds(400,50,100,100);
    Container c=this.getContentPane();
    int 
      xfat=100-c.getHeight(),
      yfat=100-c.getWidth();
    
    //1080i is 1920 horizontal and 540 vertical pixels
    
    
    setBounds(400,50,1280+xfat,720+yfat);
    setVisible(true);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    
    addKeyListener(new KL());
    
    
  }
  
  private static final double 
    UNSCALEDPANINCREMENT=4.0,
    ZOOMINCREMENT=0.99;
  
  
  private class KL extends KeyAdapter{
    public void keyTyped(KeyEvent e) {
      String s=String.valueOf(e.getKeyChar());
      System.out.println("foo  "+s);
      if(s.equals("w")){
        System.out.println("north");
        drifter.vpcentery+=(UNSCALEDPANINCREMENT*drifter.vpscale);
        drifter.render();
      }else if(s.equals("a")){
        System.out.println("west");
        drifter.vpcenterx-=(UNSCALEDPANINCREMENT*drifter.vpscale);
        drifter.render();
      }else if(s.equals("s")){
        System.out.println("south");
        drifter.vpcentery-=(UNSCALEDPANINCREMENT*drifter.vpscale);
        drifter.render();
      }else if(s.equals("d")){
        System.out.println("east");
        drifter.vpcenterx+=(UNSCALEDPANINCREMENT*drifter.vpscale);
        drifter.render();
      }else if(s.equals("+")){
        System.out.println("zoom in");
        drifter.vpscale*=ZOOMINCREMENT;
        drifter.render();
      }else if(s.equals("-")){
        System.out.println("zoom out");
        drifter.vpscale/=ZOOMINCREMENT;
        drifter.render();
      }else if(s.equals("z")){
        System.out.println("rotate ccw");
        drifter.vpforward=GD.normalizeDirection(drifter.vpforward-GD.PI/6);
        drifter.render();
      }else if(s.equals("x")){
        System.out.println("rotate cw");
        drifter.vpforward=GD.normalizeDirection(drifter.vpforward+GD.PI/6);
        drifter.render();
      }
    }
  }
  
  /*
   * ################################
   * DRIFTER
   * ################################
   */
  
  Drifter drifter;
  
  /*
   * ################################
   * IMAGE
   * ################################
   */
  
  public void paint(Graphics g){
    if(drifter.image!=null){
      Graphics2D h=(Graphics2D)getContentPane().getGraphics();
      h.drawImage(drifter.image,null,null);}}

}
