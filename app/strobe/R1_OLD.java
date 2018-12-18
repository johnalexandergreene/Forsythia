package org.fleen.forsythia.app.strobe;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.Jig;
import org.fleen.forsythia.core.grammar.forsythiaGrammar_Basic.ForsythiaGrammar_Basic;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.GD;
import org.fleen.geom_2D.rasterMap.Cell;
import org.fleen.geom_2D.rasterMap.Presence;
import org.fleen.geom_2D.rasterMap.RasterMap;
import org.fleen.util.tree.TreeNode;

public class R1_OLD implements Renderer{

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  R1_OLD(Strobe strobe){
    this.strobe=strobe;}
  
  /*
   * ################################
   * STROBE
   * ################################
   */
  
  Strobe strobe;
  
  /*
   * ################################
   * IMAGE
   * ################################
   */
  
  BufferedImage image;
  Graphics2D graphics;
  private static final double GLOWSPAN=1.5;
  
  /*
   * viewport def is the definition of our viewport rectangle. Our window onto the composition
   * visiblepolygondetailsizefloor and visiblepolygondetailsizeceiling define the min and max detail size
   *   of visible polygons. For a polygon to be visible it must have detail size in that range.
   *   polygon color intensity is greatest in the center of that range, least on the edge.
   * 
   */
  public BufferedImage getImage(){
    ViewportDef viewportdef=strobe.getViewportDef();
    double 
      visiblepolygondetailsizefloor=strobe.getVisiblePolygonDetailSizeFloor(),
      visiblepolygondetailsizeceiling=strobe.getVisiblePolygonDetailSizeCeiling();
    //
    Map<DPolygon,Double> detailsizedependentintensityfactors=getDetailSizeDependentIntensityFactors(
        strobe.getVisibleDPolygons(),visiblepolygondetailsizefloor,visiblepolygondetailsizeceiling);
    //
    image=new BufferedImage(viewportdef.width,viewportdef.height,BufferedImage.TYPE_INT_RGB);
    for(Cell c:strobe.polygonrastermap)
      image.setRGB(c.x,c.y,getPixelColor(c,detailsizedependentintensityfactors).getRGB());
    //
    return image;}
  
  /*
   * ################################
   * PIXEL COLOR
   * ################################
   */
  
  private Color getPixelColor(Cell c,Map<DPolygon,Double> detailsizedependentintensityfactors){
    //get intensity sum
    double intensitysum=0,factor;
    for(Presence p:c.presences){
      factor=detailsizedependentintensityfactors.get(p.polygon);
      if(factor>1||factor<0)factor=0;
//        throw new IllegalArgumentException("FACTOR OUTSIDE VALID RANGE : factor="+factor);
      intensitysum+=(p.intensity*detailsizedependentintensityfactors.get(p.polygon));}
    //get normalized intensity for each presence and sum weighted r g b color components
    int r=0,g=0,b=0;
    Color color;
    double normalized;
    for(Presence p:c.presences){
      factor=detailsizedependentintensityfactors.get(p.polygon);
      normalized=(p.intensity*factor)/intensitysum;
      color=getPolygonColor((FPolygon)p.polygon.object);
      r+=(int)(color.getRed()*normalized);
      if(r>255)r=255;
      if(r<0)r=0;
      g+=(int)(color.getGreen()*normalized);
      if(g>255)g=255;
      if(g<0)g=0;
      b+=(int)(color.getBlue()*normalized);
      if(b>255)b=255;
      if(b<0)b=0;}
    //
    return new Color(r,g,b);}
  
  /*
   * ################################
   * POLYGON DETAIL SIZE DEPENDENT INTENSITY
   * polygons at detailsize in the middle of our visible detail size range get max intensity : 1.0
   * towards the edge they get something less
   * ################################
   */
  
  private Map<DPolygon,Double> getDetailSizeDependentIntensityFactors(
    List<DPolygon> polygons,double visiblepolygondetailsizefloor,double visiblepolygondetailsizeceiling){
    Map<DPolygon,Double> detailsizedependentpolygonintensityfactors=new Hashtable<DPolygon,Double>();
    double 
      mean=(visiblepolygondetailsizefloor+visiblepolygondetailsizeceiling)/2,
      range=mean-visiblepolygondetailsizefloor,
      detailsize,deviation,factor;
    for(DPolygon polygon:polygons){
      detailsize=polygon.getDetailSize();
      deviation=Math.abs(mean-detailsize);
      factor=1.0-(deviation/range);
      detailsizedependentpolygonintensityfactors.put(polygon,factor);}
    return detailsizedependentpolygonintensityfactors;}
  
  /*
   * ################################
   * POLYGON COLOR
   * ################################
   */
  
  Random rnd=new Random();
  
  //SPLIT PALETTE
  
  ////that good early palette, use black stroke
//  private Color[] 
//    color0={new Color(236,208,120),new Color(217,91,67)},
//    color1={new Color(192,41,66),new Color(83,119,122)};
  
  //red orange yellow white. use black stroke
  //private Color[] 
  //    color0={new Color(255,0,0),new Color(255,255,0)},
  //    color1={new Color(255,128,0),new Color(255,255,255)};
  
  //red blue, yellow white. use black stroke
  private Color[] 
    color0={new Color(255,128,128),new Color(128,128,255)},
    color1={new Color(255,255,128),new Color(255,255,255)};
  
  //straight black and white. use gray stroke
  //private Color[] 
  //    color0={new Color(0,0,0)},
  //    color1={new Color(255,255,255)};
  
  Map<FPolygonSignature,Color> polygoncolorbysig=new Hashtable<FPolygonSignature,Color>();
  
  public Color getPolygonColor(FPolygon polygon){
    FPolygonSignature sig=polygon.getSignature();
    Color color=polygoncolorbysig.get(sig);
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
      polygoncolorbysig.put(sig,color);}
    return color;}
  
  protected int getTagDepth(TreeNode node,String tag){
    int c=0;
    TreeNode n=node;
    FPolygon p;
    while(n!=null){
      if(n instanceof FPolygon){
        p=(FPolygon)n;
        if(p.hasTag(tag))
          c++;}
      n=n.getParent();}
    return c;}
  
  
}
