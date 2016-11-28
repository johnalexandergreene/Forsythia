package org.fleen.forsythia.app.bread;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.junk.simpleRenderer.ForsythiaSimpleRenderer_Abstract;

public class Renderer003_Stripetest extends ForsythiaSimpleRenderer_Abstract{
  
  private static final long serialVersionUID=6251642864782975431L;
  
  //TODO better
  public double time;

  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  public Renderer003_Stripetest(Color backgroundcolor,int margin){
    super(backgroundcolor,margin);}
  
  public Renderer003_Stripetest(){}
  
  /*
   * ################################
   * RENDER
   * ################################
   */
  
  Random rnd=new Random();
  static final Color[] COLORS={new Color(255,0,0,64),new Color(255,255,0,64),new Color(0,255,255,64)};
  Map<FPolygonSignature,Voice_Graphics2D> voices=new HashMap<FPolygonSignature,Voice_Graphics2D>();
  
  protected void render(ForsythiaComposition forsythia,Graphics2D graphics,AffineTransform transform){
    List<FPolygon> polygons=forsythia.getPolygons();
    Collections.sort(polygons,PolygonDepthComparator);
    Voice_Graphics2D voice;
    for(FPolygon polygon:polygons){
      voice=getVoice(polygon);
      voice.paint(polygon,graphics,time);}}
  
  private Comparator<FPolygon> PolygonDepthComparator=new Comparator<FPolygon>(){
    public int compare(FPolygon p0,FPolygon p1){
      int d0=p0.getDepth(),d1=p1.getDepth();
      if(d0==d1){
        return 0;
      }else if(d0>d1){
        return 1;
      }else{
        return -1;}}};
        
  private Voice_Graphics2D getVoice(FPolygon polygon){
    FPolygonSignature signature=polygon.getSignature();
    Voice_Graphics2D voice=voices.get(signature);
    if(voice==null){
      Color color=COLORS[rnd.nextInt(COLORS.length)];
      voice=createVoice(polygon,color);
      voices.put(signature,voice);}
    return voice;}
  
  Random r=new Random();
  
  private Voice_Graphics2D createVoice(FPolygon polygon,Color color){
    VG_Stripe voice=new VG_Stripe();
    voice.color=color;
    return voice;}
  
}
