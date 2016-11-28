package org.fleen.forsythia.junk.simpleRenderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.geom_2D.DPoint;

public class FSR_EggLevelSplitPaletteWithStrokesAndGeneratedColors extends ForsythiaSimpleRenderer_Abstract{
  
  private static final long serialVersionUID=6251642864782975431L;

  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  public FSR_EggLevelSplitPaletteWithStrokesAndGeneratedColors(float strokewidth){
    super();
    this.strokewidth=strokewidth;
    initColorPairs();}
  
  /*
   * ################################
   * COLOR
   * use black stroke
   * use or palette mother for the 2 color pairs
   * ################################
   */
  
  private Color strokecolor=Color.black;
  private Color[] color0,color1;
  
  private Map<FPolygonSignature,Color> polygoncolors=new Hashtable<FPolygonSignature,Color>();
  
  public Color getColor(FPolygon polygon){
    FPolygonSignature sig=polygon.getSignature();
    Color color=polygoncolors.get(sig);
    if(color==null){
      int 
        eggdepth=getTagDepth(polygon,"egg"),
        colorindex;
      //even level
      if(eggdepth%2==0){
        colorindex=rnd.nextInt(color0.length);
        color=color0[colorindex];
      //odd level
      }else{
        colorindex=rnd.nextInt(color1.length);
        color=color1[colorindex];}
      polygoncolors.put(sig,color);}
    return color;}
  
  private void initColorPairs(){
    color0=new Color[]{getRandomGoodColor(),getRandomGoodColor()};
    color1=new Color[]{getRandomGoodColor(),getRandomGoodColor()};}
  
  private Color getRandomGoodColor(){
    Color c=new Color(64+rnd.nextInt(12)*16,64+rnd.nextInt(12)*16,64+rnd.nextInt(12)*16);
    return c;}
  
//  this.color0=color0;
//  this.color1=color1;
//  this.strokecolor=strokecolor;
  
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
  
  protected void render(ForsythiaComposition forsythia,Graphics2D graphics,AffineTransform transform){
    Path2D path;
    //FILL POLYGONS
    Color color;
    for(FPolygon polygon:forsythia.getLeafPolygons()){
      color=getColor(polygon);
      graphics.setPaint(color);
      path=getPath(polygon);
      graphics.fill(path);}
    //STROKE POLYGONS
    graphics.setPaint(strokecolor);
    graphics.setStroke(createStroke());
    for(FPolygon polygon:forsythia.getLeafPolygons()){
      path=getPath(polygon);
      graphics.draw(path);}
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
