package org.fleen.forsythia.app.bread.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.geom_2D.DPoint;

public class Renderer005 extends Renderer_Abstract{
  
  /*
   * ################################
   * COLOR
   * ################################
   */
  
  private Color strokecolor=Color.black;
  private Color[] 
    color0={new Color(255,0,0),new Color(255,128,0)},
    color1={new Color(255,255,0),new Color(128,255,0)},
    color2={new Color(255,255,0),new Color(0,255,255)};
  
  private Map<FPolygonSignature,Color> polygoncolors=new Hashtable<FPolygonSignature,Color>();
  
  public Color getColor(FPolygon polygon){
    FPolygonSignature sig=polygon.getSignature();
    Color color=polygoncolors.get(sig);
    int eggdepthmod;
    if(color==null){
      int 
        eggdepth=getTagDepth(polygon,"egg"),
        colorindex;
//      System.out.println("eggdepth="+eggdepth);
      eggdepthmod=eggdepth%3;
      if(eggdepthmod==0){
        colorindex=rnd.nextInt(color0.length);
        color=color0[colorindex];
      }else if(eggdepthmod==1){
        colorindex=rnd.nextInt(color1.length);
        color=color1[colorindex];
      }else{
        colorindex=rnd.nextInt(color2.length);
        color=color2[colorindex];}
      polygoncolors.put(sig,color);}
    return color;}
  
  /*
   * ################################
   * STROKE
   * ################################
   */
  
  private float strokewidth;
  
  private Stroke createStroke(){
    Stroke stroke=new BasicStroke(strokewidth,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,0,null,0);
    return stroke;}
  
  /*
   * ################################
   * RENDER
   * ################################
   */
  
  Random rnd=new Random();
  
//  protected void render(ForsythiaComposition forsythia,Graphics2D graphics,AffineTransform transform){
//    Path2D path;
//    //FILL POLYGONS
//    Color color;
//    for(FPolygon polygon:forsythia.getLeafPolygons()){
//      color=getColor(polygon);
//      graphics.setPaint(color);
//      path=getPath(polygon);
//      graphics.fill(path);}
//    //STROKE POLYGONS
//    graphics.setPaint(strokecolor);
//    graphics.setStroke(createStroke());
//    for(FPolygon polygon:forsythia.getLeafPolygons()){
//      path=getPath(polygon);
//      graphics.draw(path);}
//    //
//    polygoncolors.clear();}
  
  protected void render(ForsythiaComposition forsythia,Graphics2D graphics,AffineTransform transform){
    Path2D path;
    //FILL POLYGONS
    Color color;
    for(FPolygon polygon:forsythia.getPolygons()){
      color=getColor(polygon);
      graphics.setPaint(color);
      path=getPath(polygon);
      graphics.fill(path);}
    //STROKE POLYGONS
    graphics.setPaint(strokecolor);
    graphics.setStroke(createStroke());
    for(FPolygon polygon:forsythia.getPolygons()){
      path=getPath(polygon);
//      graphics.draw(path);
      }
    //
    polygoncolors.clear();}
  
  private Path2D getPath(FPolygon polygon){
    Path2D.Double path=new Path2D.Double();
    List<DPoint> points=polygon.getDPolygon();
    DPoint p=points.get(0);
    path.moveTo(p.x,p.y);
    for(int i=1;i<points.size();i++){
      p=points.get(i);
      path.lineTo(p.x,p.y);}
    path.closePath();
    return path;}
  
}
