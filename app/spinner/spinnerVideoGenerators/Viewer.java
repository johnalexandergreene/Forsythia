package org.fleen.forsythia.app.spinner.spinnerVideoGenerators;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import org.fleen.forsythia.app.spinner.FSLAFGenerator;

@SuppressWarnings("serial")
public class Viewer extends JPanel{
  
  FSLAFGenerator gen;
  
  public Viewer(FSLAFGenerator gen){
    this.gen=gen;}
  
  private static final AffineTransform NICEOFFSET=AffineTransform.getTranslateInstance(700,10);
  
  public void paint(Graphics g){
    super.paint(g);
    if(gen==null||gen.frame==null)return;
    Graphics2D g2=(Graphics2D)g;
    //
    if(FSLAFGenerator.TEST)paintChainForTest(g2);
    g2.drawImage(gen.frame,NICEOFFSET,null);
    if(FSLAFGenerator.TEST)paintFrameForTest(g2);
    }
  
  private void paintChainForTest(Graphics2D g){
    AffineTransform t=new AffineTransform(NICEOFFSET);
    t.concatenate(AffineTransform.getTranslateInstance(-gen.viewportposition,0));
    g.drawImage(gen.chain.getDebugImage(),t,null);}
  
  private void paintFrameForTest(Graphics2D g){
    AffineTransform 
      t=new AffineTransform(g.getTransform()),
      told=g.getTransform();
    t.concatenate(NICEOFFSET);
    g.setTransform(t);
    g.setStroke(new BasicStroke(2.0f));
    g.setPaint(Color.green);
    g.drawRect(0,0,gen.viewportwidth,gen.viewportheight);
    g.setTransform(told);}
  
}
