package org.fleen.forsythia.junk.simpleRenderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.List;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.geom_2D.DPoint;

public class FSR_EggLevelWithStrokes extends ForsythiaSimpleRenderer_Abstract{
  
  private static final long serialVersionUID=6251642864782975431L;

  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  public FSR_EggLevelWithStrokes(Color backgroundcolor,int margin,Color[] colors,Color strokecolor,float strokewidth){
    super(backgroundcolor,margin);
    this.colors=colors;
    this.strokecolor=strokecolor;
    this.strokewidth=strokewidth;}
  
  public FSR_EggLevelWithStrokes(Color[] colors,Color strokecolor,float strokewidth){
    super();
    this.colors=colors;
    this.strokecolor=strokecolor;
    this.strokewidth=strokewidth;}
  
  public FSR_EggLevelWithStrokes(){
    super();
    this.colors=COLORS_DEFAULT;
    this.strokecolor=STROKECOLOR_DEFAULT;}
  
  /*
   * ################################
   * COLOR
   * ################################
   */
  
  //mystery machine
  private static final Color[] COLORS_DEFAULT={
    new Color(85,66,54),
    new Color(247,120,37),
    new Color(211,206,61),
    new Color(241,239,165),
    new Color(96,185,154),
    
  };
  
  private static final Color STROKECOLOR_DEFAULT=Color.black;
  
  private Color strokecolor;
  private Color[] colors;
  
  public Color getColor(FPolygon polygon){
    int 
      eggdepth=getTagDepth(polygon,"egg"),
      colorindex=eggdepth%colors.length;
    Color color=colors[colorindex];
    return color;}
  
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
      graphics.draw(path);}}
  
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
