package org.fleen.forsythia.app.strobe;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.geom_2D.rasterMap.Cell;
import org.fleen.geom_2D.rasterMap.Presence;
import org.fleen.util.tree.TreeNode;

public class R1{

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  R1(Strobe strobe){
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
  
  /*
   * viewport def is the definition of our viewport rectangle. Our window onto the composition
   * visiblepolygondetailsizefloor and visiblepolygondetailsizeceiling define the min and max detail size
   *   of visible polygons. For a polygon to be visible it must have detail size in that range.
   *   polygon color intensity is greatest in the center of that range, least on the edge.
   * 
   */
  public BufferedImage getImage(int frameindex){
    ViewportDef viewportdef=strobe.getViewportDef();
    image=new BufferedImage(viewportdef.width,viewportdef.height,BufferedImage.TYPE_INT_RGB);
    for(Cell c:strobe.polygonrastermap)
      image.setRGB(c.x,c.y,getPixelColor(c,frameindex).getRGB());
    return image;}
  
  /*
   * ################################
   * PIXEL COLOR
   * ################################
   */
  
  /*
   * get the sum of all presence intensities : intensitysum
   * 
   */
  private Color getPixelColor(Cell c,int frameindex){
    //get intensity sum
    double intensitysum=0;
    for(Presence p:c.presences)
      intensitysum+=p.intensity;
    //get normalized intensity for each presence and sum weighted r g b color components
    int r=0,g=0,b=0;
    Color color;
    double nf;//normalization factor
    for(Presence p:c.presences){
      nf=p.intensity/intensitysum;
      color=getPolygonColor((FPolygon)p.polygon.object,frameindex);
      r+=(int)(color.getRed()*nf);
      if(r>255){
        System.out.println("R too big : "+r);
        r=255;}
      if(r<0){
        System.out.println("R too small : "+r);
        r=0;}
      g+=(int)(color.getGreen()*nf);
      if(g>255){
        System.out.println("G too big : "+g);
        g=255;}
      if(g<0){
        System.out.println("G too small : "+g);
        g=0;}
      b+=(int)(color.getBlue()*nf);
      if(b>255){
        System.out.println("B too big : "+b);
        b=255;}
      if(b<0){
        System.out.println("B too small : "+b);
        b=0;}}
    //
    return new Color(r,g,b);}
  
  /*
   * ################################
   * POLYGON COLOR
   * ################################
   */
  
  Random rnd=new Random();
  
  //SPLIT PALETTE
  //TODO clean this up $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
  
//  //that good early palette "compelling" or whatever
//  private Color[] 
//    color0={new Color(236,208,120),new Color(217,91,67)},
//    color1={new Color(192,41,66),new Color(83,119,122)};
  
//  orange yellow - red purple
//  private Color[] 
//    color0={new Color(255,128,0),new Color(255,255,0)},
//    color1={new Color(255,0,0),new Color(255,0,255)};
  

//  //orange yellow - red blue
//private Color[] 
//  color0={new Color(255,128,0),new Color(255,255,0)},
//  color1={new Color(255,0,0),new Color(255,0,255)};
  
//  //orange white - red blue
//private Color[] 
//  color0={new Color(255,128,0),new Color(255,255,255)},
//  color1={new Color(255,0,0),new Color(0,0,255)};
  
  //orange red - cyan yellow
//private Color[] 
//  color0={new Color(255,128,0),new Color(255,0,0)},
//  color1={new Color(0,255,255),new Color(255,255,0)};
  
  
//  //orange yellow - green blue
//  private Color[] 
//    color0={new Color(255,128,0),new Color(255,255,0)},
//    color1={new Color(0,255,0),new Color(0,0,255)};
  
  //michael orange blue palette
  private Color[] 
      color0={new Color(191,100,45),new Color(221,194,149)},
      color1={new Color(88,171,168),new Color(0,71,138)};
  
  
  //melon ball suprise
//private Color[] 
//  color1={new Color(209,242,165),new Color(239,250,180)},
//  color0={new Color(255,159,128),new Color(245,105,145)};
  
  //strong rainbow
//  private Color[] 
//    color0={new Color(255,0,0),new Color(0,255,0)},
//    color1={new Color(0,0,255),new Color(255,0,255)};
  
  //pastel red blue, yellow white
//  private Color[] 
//    color0={new Color(255,128,128),new Color(128,128,255)},
//    color1={new Color(255,255,128),new Color(255,255,255)};
  
  //red orange yellow white
//  private Color[] 
//      color0={new Color(255,0,0),new Color(255,255,0)},
//      color1={new Color(255,128,0),new Color(255,255,255)};
  
  //most of mystery machine
//private Color[] 
//    color0={new Color(247,120,37),new Color(211,206,61)},
//    color1={new Color(241,239,165),new Color(96,185,154)};
  
  //grayscale
//  private Color[] 
//      color0={new Color(0,0,0),new Color(64,64,64)},
//      color1={new Color(255,255,255),new Color(192,192,192)};
  
  private Color[] allcolors={color0[0],color0[1],color1[0],color1[1]};
  
//  static final double LOWNOISELEVEL=0.2,HIGHNOISELEVEL=0.05;//kinda thick
//  static final double LOWNOISELEVEL=0.06,HIGHNOISELEVEL=0.01;
  static final double LOWNOISELEVEL=0.00,HIGHNOISELEVEL=0.00;
  
  /*
   * get color by sig
   * 
   * we do a bit of noise
   * 
   */
  public Color getPolygonColor(FPolygon polygon,int frameindex){
    Color color;
    double noise=rnd.nextDouble();
    if(noise<HIGHNOISELEVEL){
      //do high noise
      color=allcolors[rnd.nextInt(allcolors.length)];
    }else if(noise<LOWNOISELEVEL){
      //do low noise
      BlinkPattern bp=getBlinkPattern(polygon);
      if(rnd.nextBoolean())
        color=bp.color0;
      else
        color=bp.color1;
    }else{
      //do regular polygon color
      BlinkPattern bp=getBlinkPattern(polygon);
      color=bp.getColor(frameindex);}
    //
    return color;}
  
  Map<FPolygonSignature,BlinkPattern> blinkpatternbysig=new HashMap<FPolygonSignature,BlinkPattern>();
  
  /*
   * chorus by sig
   * color pair by tag depth
   */
  private BlinkPattern getBlinkPattern(FPolygon polygon){
    BlinkPattern blinkpattern=blinkpatternbysig.get(polygon.getSignature());
    if(blinkpattern==null){
      blinkpattern=createBlinkPattern(polygon);
      blinkpatternbysig.put(polygon.getSignature(),blinkpattern);}
    return blinkpattern;}
  
  private BlinkPattern createBlinkPattern(FPolygon polygon){
    int eggdepth=getTagDepth(polygon,"egg");
    BlinkPattern bp;
    if(eggdepth%2==0)
      bp=new BlinkPattern(color0[0],color0[1]);
    else
      bp=new BlinkPattern(color1[0],color1[1]);
    return bp;}
  
  private int getTagDepth(TreeNode node,String tag){
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
