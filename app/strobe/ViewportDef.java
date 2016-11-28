package org.fleen.forsythia.app.strobe;

import java.awt.geom.AffineTransform;

import org.fleen.geom_2D.DPoint;

/*
 * a rectangular viewport
 */
public class ViewportDef{
  
  ViewportDef(int width,int height,DPoint center,double scale,double forward){
    this.width=width;
    this.height=height;
    this.center=center;
    this.scale=scale;
    this.forward=forward;}
  
  public int width,height;//in pixels, assumedly
  public DPoint center;
  public double 
    scale,
    forward;//aka north, up, whatever
  
  public AffineTransform getTransform(){
    AffineTransform t=new AffineTransform();
    t.translate(width/2,height/2);
    t.rotate(-forward);
    t.translate(-center.x/scale,center.y/scale);
    t.scale(1/scale,-1/scale);
    return t;}

}
