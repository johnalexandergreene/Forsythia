package org.fleen.forsythia.app.grammarEditor.editor_Generator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;


/*
 * It's just a jpanel with an overidden paint
 */
@SuppressWarnings("serial")
public class EGUI_Viewer extends JPanel{
  
  public EGUI_Viewer(){
    super();}
  
  public void paint(Graphics g){
    try{
      Graphics2D g2d=(Graphics2D)g;
      BufferedImage i=GE.editor_generator.generator.getViewerImage();
      System.out.println("image @ viewer.paint="+i);
      if(i!=null)
        g2d.drawImage(i,null,null);
    }catch(Exception x){
      System.out.println("PAINT FAILED");
      x.printStackTrace();}}
  
}
