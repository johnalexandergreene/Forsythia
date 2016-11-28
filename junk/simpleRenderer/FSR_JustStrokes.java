package org.fleen.forsythia.junk.simpleRenderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.ForsythiaComposition;

public class FSR_JustStrokes extends ForsythiaSimpleRenderer_Abstract{
 
  private static final long serialVersionUID=-7451576959261610292L;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public FSR_JustStrokes(){
    super();
    strokecolor=STROKECOLOR_DEFAULT;}
  
  public FSR_JustStrokes(float strokewidth,Color strokecolor){
    super();
    this.strokecolor=strokecolor;
    this.strokewidth=strokewidth;}
  
  /*
   * ################################
   * COLOR
   * a nice random symmetric palette that goes well wit hthe black strokes
   * ################################
   */
  
  private static final Color STROKECOLOR_DEFAULT=new Color(0,0,0);
  private Color strokecolor;
  
  /*
   * ################################
   * STROKE
   * ################################
   */
  
  private static final float STROKEWIDTH_DEFAULT=0.01f;
  private float strokewidth=STROKEWIDTH_DEFAULT;
  
  private Stroke createStroke(){
    Stroke stroke=new BasicStroke(strokewidth,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,0,null,0);
    return stroke;}
  
  /*
   * ################################
   * RENDER 
   * ################################
   */
  
  protected void render(ForsythiaComposition forsythia,Graphics2D graphics,AffineTransform transform){
    Path2D path;
    graphics.setPaint(strokecolor);
    graphics.setStroke(createStroke());
    for(FPolygon polygon:forsythia.getLeafPolygons()){
      path=getPath2D(polygon);
      graphics.draw(path);}}
  
}
