package org.fleen.forsythia.app.spinner.core;

import java.util.List;
import java.util.Random;

import org.fleen.forsythia.app.compositionGenerator.colorMap.CM_SymmetricChaos_EggLevelDoublePaletteSplit;
import org.fleen.forsythia.app.compositionGenerator.colorMap.ColorMap;
import org.fleen.forsythia.core.composition.FGridRoot;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.FMetagon;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KPolygon;

/*
 * A single node in a chain of nodes
 * connects to 0..2 other nodes, prior and next
 * refers to a ForsythiaComposition with a rectangular root.
 * 
 */
public class Stripe_ForsythiaCompositionOLD implements Stripe{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Stripe_ForsythiaCompositionOLD(StripeChainWithMovingViewport chain){
    this.chain=chain;
    initComposition();
    initColorMap();}
  
  /*
   * ################################
   * CHAIN
   * The stripe chain that this stripe is a part of
   * ################################
   */
  
  StripeChainWithMovingViewport chain;
  
  /*
   * ################################
   * COMPOSITION
   * The rectangular forsythia composition that defines the geometry of this stripe.
   *   Ie, the proportions of the rectangle and the internal geometry
   * Get a random rectangular metagon
   * Get a random anchor
   * From those create a polygon
   * use that as the root polygon for a composition
   * compose nice geometry for that composition 
   * ################################
   */
  
  public ForsythiaComposition composition;
  
  private void initComposition(){
    //get the root polygon
    FMetagon rootmetagon=chain.generator.getRandomRectangularMetagon();
    KPolygon p0=rootmetagon.getPolygon();
    List<KAnchor> anchors=rootmetagon.getAnchorOptions(p0);
    KAnchor a=anchors.get(new Random().nextInt(anchors.size()));
    FPolygon rootpolygon=new FPolygon(rootmetagon,a);
    //grid
    FGridRoot rootgrid=new FGridRoot();
    //create the composition and compose it up
    composition=new ForsythiaComposition();
    composition.setGrammar(chain.generator.grammar);
    composition.initTree(rootgrid,rootpolygon);
    chain.generator.composer.compose(composition,getScaledDetailLimit(rootpolygon));}
  
  /*
   * The unscaled height of our rectangular compositions varies, 
   *   therefore the detail limit varies, 
   *   therefore it needs to be scaled accordingly.
   */
  private double getScaledDetailLimit(FPolygon polygon){
    return chain.generator.detaillimit*polygon.getDPolygon().getBounds().height;}
  
  /*
   * ################################
   * COLOR MAP
   * Colors mapped to composition leaf polygons 
   * ################################
   */
  
  public ColorMap colormap;
  
  private void initColorMap(){
    colormap=new CM_SymmetricChaos_EggLevelDoublePaletteSplit(composition,chain.generator.palette);}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  public double getImageScale(){
    double a=composition.getRootPolygon().getDPolygon().getBounds().height;
    double b=chain.generator.viewportheight/a;
    return b;}

  public int getImageX(){
    int 
      a=chain.indexOf(this),
      sum=0;
    for(int i=0;i<a;i++)
      sum+=chain.get(i).getImageWidth();
    return sum;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * IMAGE WIDTH
   * ++++++++++++++++++++++++++++++++
   */
  
  private int imagewidth=-1;
  
  public int getImageWidth(){
    if(imagewidth==-1)
      initImageWidth();
    return imagewidth;}
  
  private void initImageWidth(){
    double
      s=getImageScale(),
      w=composition.getRootPolygon().getDPolygon().getBounds().width;
    imagewidth=(int)(s*w);}
    
}
