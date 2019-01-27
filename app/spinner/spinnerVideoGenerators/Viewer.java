package org.fleen.forsythia.app.spinner.spinnerVideoGenerators;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Viewer extends JPanel{
  
  public BufferedImage image;
  static final AffineTransform LITTLEOFFSET=AffineTransform.getTranslateInstance(22,22);
  
  public void paint(Graphics g){
    super.paint(g);
    if(image==null)return;
    Graphics2D g2=(Graphics2D)g;
    g2.drawImage(image,LITTLEOFFSET,null);}
  
}
