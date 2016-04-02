package org.fleen.forsythia.app.simpleGenerator;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{

  private static final long serialVersionUID=581500866418502553L;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  ImagePanel(SimpleGenerator sampler){
    this.sampler=sampler;}
  
  /*
   * ################################
   * SAMPLER
   * ################################
   */
  
  SimpleGenerator sampler;
  
  /*
   * ################################
   * PAINT
   * ################################
   */
  
  public void paint(Graphics g){
    super.paint(g);
    if(sampler==null||sampler.image==null)return;
    Graphics2D g2=(Graphics2D)g;
    g2.drawImage(sampler.image,null,null);}

}
