package org.fleen.forsythia.util.simpleRenderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.composition.FPolygon;
import org.fleen.forsythia.composition.FPolygonSignature;
import org.fleen.forsythia.composition.ForsythiaComposition;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.polygonRasterMap.Cell;
import org.fleen.geom_2D.polygonRasterMap.PolygonRasterMap;
import org.fleen.geom_2D.polygonRasterMap.Presence;

public class FSR_EggLevelSplitPaletteWithStrokes_RasterMapped extends ForsythiaSimpleRenderer_Abstract{
  
  private static final long serialVersionUID=6251642864782975431L;

  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  public FSR_EggLevelSplitPaletteWithStrokes_RasterMapped(Color backgroundcolor,int margin,Color[] color0,Color[] color1){
    super(backgroundcolor,margin);
    this.color0=color0;
    this.color1=color1;}
  
  public FSR_EggLevelSplitPaletteWithStrokes_RasterMapped(Color[] color0,Color[] color1){
    super();
    this.color0=color0;
    this.color1=color1;}
  
  /*
   * ################################
   * POLYGON COLOR
   * ################################
   */
  
  Random rnd=new Random();
  private Color[] color0,color1;
  
  private Map<FPolygonSignature,Color> polygoncolors=new Hashtable<FPolygonSignature,Color>();
  
  public Color getPolygonColor(FPolygon polygon){
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
  
  /*
   * ################################
   * RENDER
   * ################################
   */
  
  Map<DPolygon,FPolygon> fpolygonbydpolygon=new Hashtable<DPolygon,FPolygon>();
  
  private static final double GLOWSPAN=1.5;
  
  protected void render(ForsythiaComposition forsythia,Graphics2D graphics,AffineTransform transform){
    int w=image.getWidth(),h=image.getHeight();
    PolygonRasterMap rastermap=new PolygonRasterMap(w,h,transform,GLOWSPAN);
    DPolygon dpolygon;
    for(FPolygon fpolygon:forsythia.getLeafPolygons()){
      dpolygon=fpolygon.getDPolygon();
      fpolygonbydpolygon.put(dpolygon,fpolygon);
      rastermap.castPresence(dpolygon);}
    //
    for(Cell c:rastermap)
      image.setRGB(c.x,c.y,getColor(c).getRGB());}
  
  private Color getColor(Cell c){
    //get intensity sum
    double intensitysum=0;
    for(Presence p:c.presences)
      intensitysum+=p.intensity;
    //get normalized intensity for each presence and sum weighted r g b color components
    int r=0,g=0,b=0;
    Color color;
    double normalized;
    for(Presence p:c.presences){
      normalized=p.intensity/intensitysum;
      color=getPolygonColor(fpolygonbydpolygon.get(p.polygon));
      r+=(int)(color.getRed()*normalized);
      g+=(int)(color.getGreen()*normalized);
      b+=(int)(color.getBlue()*normalized);}
    //
    return new Color(r,g,b);}
  
}
