package org.fleen.forsythia.app.compositionGenerator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class HeadViewer extends JPanel{

  private static final long serialVersionUID=581500866418502553L;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  HeadViewer(HeadAbstract head){
    this.head=head;}
  
  /*
   * ################################
   * HEAD
   * ################################
   */
  
  HeadAbstract head;
  
  /*
   * ################################
   * PAINT
   * ################################
   */
  
  public void paint(Graphics g){
    super.paint(g);
    if(head==null)return;
    BufferedImage i=head.getUIViewerImage();
    if(i==null)return;
    Graphics2D g2=(Graphics2D)g;
    g2.drawImage(i,null,null);}

}
