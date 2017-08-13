package org.fleen.forsythia.app.bread.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.app.bread.VG_Stripe;
import org.fleen.forsythia.app.bread.Voice_Graphics2D;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.junk.simpleRenderer.ForsythiaSimpleRenderer_Abstract;

public class Renderer002_Stripetest extends Renderer_Abstract{
  
  private static final long serialVersionUID=6251642864782975431L;
  
  //TODO better
  public double time;

  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  public Renderer002_Stripetest(Color backgroundcolor,int margin){
    super(backgroundcolor,margin);}
  
  public Renderer002_Stripetest(){}
  
  /*
   * ################################
   * RENDER
   * ################################
   */
  
  Random rnd=new Random();
  
  protected void render(ForsythiaComposition forsythia,Graphics2D graphics,AffineTransform transform){
    Map<FPolygonSignature,Voice_Graphics2D> voices=new HashMap<FPolygonSignature,Voice_Graphics2D>();
    List<FPolygon> polygons=forsythia.getLeafPolygons();
    Voice_Graphics2D voice;
    for(FPolygon polygon:polygons){
      voice=getVoice(polygon,voices);
      voice.paint(polygon,graphics);}}
        
  private Voice_Graphics2D getVoice(FPolygon polygon,Map<FPolygonSignature,Voice_Graphics2D> voices){
    FPolygonSignature signature=polygon.getSignature();
    Voice_Graphics2D voice=voices.get(signature);
    if(voice==null){
      voice=createVoice(polygon);
      voices.put(signature,voice);}
    return voice;}
  
  Random r=new Random();
  
  private Voice_Graphics2D createVoice(FPolygon polygon){
    Voice_Graphics2D voice=new VG_Stripe();
    ((VG_Stripe)voice).time=time;
    return voice;}
  
}
