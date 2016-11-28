package org.fleen.forsythia.app.strobe;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class UI extends JFrame{
  
  private static final long serialVersionUID=8413646909208773971L;

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  UI(Strobe strobe){
    this.strobe=strobe;
    setBounds(400,50,100,100);
    Container c=this.getContentPane();
    int 
      xfat=100-c.getHeight(),
      yfat=100-c.getWidth();
    
    //1080i is 1920 horizontal and 540 vertical pixels
    
    
    setBounds(400,50,1280+xfat,720+yfat);
    setVisible(true);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    
  }
  
  
  /*
   * ################################
   * STROBE
   * ################################
   */
  
  Strobe strobe;
  
  /*
   * ################################
   * IMAGE
   * ################################
   */
  
  public void paint(Graphics g){
    if(strobe.image!=null){
      Graphics2D h=(Graphics2D)getContentPane().getGraphics();
      h.drawImage(strobe.image,null,null);}}

}
