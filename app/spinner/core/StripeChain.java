package org.fleen.forsythia.app.spinner.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator.stripeChain.Stripe;
import org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator.stripeChain.Stripe_ForsythiaComposition;
import org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator.stripeChain.Stripe_Insert;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.util.tree.TreeNode;

/*
 * A chain of stripe nodes
 */
@SuppressWarnings("serial")
public class StripeChain extends LinkedList<Stripe>{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public StripeChain(SpinnerOLD generator){
    this.generator=generator;}
  
  public StripeChain(SpinnerOLD generator,List<Stripe> stripes){
    this(generator);
    addAll(stripes);}
  
  /*
   * ################################
   * GENERATOR
   * The fleen spinner looping animation frames generator
   *   associated with this stripechain
   * ################################
   */
  
  public SpinnerOLD generator;
  
  /*
   * ################################
   * CHAIN MODIFICATION
   * invalidate image when we do this
   * ################################
   */
  
  public void addRandomForsythiaCompositionStripeToEnd(){
    invalidateImage();
    Stripe s=new Stripe_ForsythiaComposition(this);
    generator.stripewidthsum+=s.getImageWidth();
    add(s);}
  
  public void addInsertStripe(String path){
    if(path==null)return;
    invalidateImage();
    Stripe s=new Stripe_Header(this,path);
    generator.stripewidthsum+=s.getImageWidth();
    add(s);}
  
  /*
   * note that these stripe lengths were accounted for at the beginning of 
   * the loop, when they were added using the other stripe adding methods.
   * Therefor we do no add their lengths to the stripewidthsum again. 
   */
  public void addTerminusStripesToEndForFinishingUp(List<Stripe> stripes){
    invalidateImage();
    addAll(stripes);}
  
  /*
   * if the first stripe has moved entirely outside the viewport then remove it
   * TODO
   *   cache image width and image x
   *   invalidate image x on chain edit
   */
  public void conditionallyRemoveFirstStripe(){
    Stripe stripe=getFirst();
    if(stripe.getImageX()+stripe.getImageWidth()+generator.edgerange-1<generator.viewportposition){
      invalidateImage();
      generator.viewportposition-=stripe.getImageWidth();
      removeFirst();}}
  
  /*
   * ################################
   * IMAGE
   * Render all of the stripes together as one continuous image
   * ################################
   */
  
  private static final double STROKETHICKNESS=2.0;
  
  public static final HashMap<RenderingHints.Key,Object> RENDERING_HINTS=
      new HashMap<RenderingHints.Key,Object>();
    
  static{
    RENDERING_HINTS.put(
      RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    RENDERING_HINTS.put(
      RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    RENDERING_HINTS.put(
      RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_DEFAULT);
    RENDERING_HINTS.put(
      RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    RENDERING_HINTS.put(
      RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    RENDERING_HINTS.put(
      RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY); 
    RENDERING_HINTS.put(
      RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);}
  
  BufferedImage image=null;
  
  private void invalidateImage(){
    image=null;
    debugimage=null;}
  
  public int getImageWidth(){
    int a=0;
    for(Stripe s:this)
      a+=s.getImageWidth();
    return a;}
  
  public BufferedImage getImage(){
    if(image==null)
      createImage();
    return image;}
  
  private void createImage(){
    image=new BufferedImage(getImageWidth(),generator.viewportheight,BufferedImage.TYPE_INT_RGB);
    Graphics2D g=image.createGraphics();
    g.setPaint(Color.black);
    g.fillRect(0,0,image.getWidth(),image.getHeight());
    g.setRenderingHints(RENDERING_HINTS);
    renderPolygonFill(g);
    renderInsert(g);
    renderPolygonStroke(g);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * FOR DEBUG IMAGE
   * The debug image is the whole stripechain
   * blockfill with red for terminus stripes, blue for nonterminus stripes and white for insert stripes 
   * polygons rendered with black strokes
   * ++++++++++++++++++++++++++++++++
   */
  
  private BufferedImage debugimage=null;
  
  public BufferedImage getDebugImage(){
    if(debugimage==null)
      initDebugImage();
    return debugimage;}
  
  private void initDebugImage(){
    debugimage=new BufferedImage(getImageWidth(),generator.viewportheight,BufferedImage.TYPE_INT_RGB);
    Graphics2D g=debugimage.createGraphics();
    g.setPaint(Color.white);
    g.fillRect(0,0,image.getWidth(),image.getHeight());
    g.setRenderingHints(RENDERING_HINTS);
    renderStripeBlockfill(g);
    renderPolygonStroke(g);}
  
  /*
   * ################################
   * STRIPE IMAGE GEOM
   * ################################
   */
  
  private AffineTransform getStripeFCImageTransform(Stripe_ForsythiaComposition stripe){
    //get all the relevant metrics
    Rectangle2D.Double compositionbounds=((Stripe_ForsythiaComposition)stripe).composition.getRootPolygon().getDPolygon().getBounds();
    double
      cbwidth=compositionbounds.getWidth(),
      cbheight=compositionbounds.getHeight(),
      cbxmin=compositionbounds.getMinX(),
      cbymin=compositionbounds.getMinY();
    AffineTransform transform=new AffineTransform();
    //scale
    double scale=stripe.getImageScale();
    transform.scale(scale,-scale);//flip y for proper cartesian orientation
    //offset
    double
      xoff=((stripe.getImageWidth()/scale-cbwidth)/2.0)-cbxmin,
      yoff=-(((generator.viewportheight/scale+cbheight)/2.0)+cbymin);
    transform.translate(xoff+stripe.getImageX()/scale,yoff);
    //
    return transform;}
  
  /*
   * ################################
   * RENDER METHODS
   * ################################
   */
  
  /*
   * ++++++++++++++++++++++++++++++++
   * RENDER POLYGON FILL
   * ++++++++++++++++++++++++++++++++
   */
  
  private void renderPolygonFill(Graphics2D g){
    for(Stripe stripe:this)
      if(stripe instanceof Stripe_ForsythiaComposition)
        renderPolygonFill(g,(Stripe_ForsythiaComposition)stripe);}
  
  private void renderPolygonFill(Graphics2D g,Stripe_ForsythiaComposition stripe){
    AffineTransform 
      told=g.getTransform(),
      t=new AffineTransform(told);
    t.concatenate(getStripeFCImageTransform(stripe));
    g.setTransform(t);
    //
    Iterator<TreeNode> i=((Stripe_ForsythiaComposition)stripe).composition.getLeafPolygonIterator();
    FPolygon p;
    Color color;
    while(i.hasNext()){
      p=(FPolygon)i.next();
      color=((Stripe_ForsythiaComposition)stripe).colormap.getColor(p);
      g.setPaint(color);
      g.fill(p.getDPolygon().getPath2D());}
    g.setTransform(told);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * RENDER POLYGON STROKE
   * ++++++++++++++++++++++++++++++++
   */
  
  private void renderPolygonStroke(Graphics2D g){
    for(Stripe stripe:this)
      if(stripe instanceof Stripe_ForsythiaComposition)
        renderPolygonStroke(g,(Stripe_ForsythiaComposition)stripe);}
  
  private void renderPolygonStroke(Graphics2D g,Stripe_ForsythiaComposition stripe){
    AffineTransform 
      told=g.getTransform(),
      t=new AffineTransform(told);
    t.concatenate(getStripeFCImageTransform(stripe));
    g.setTransform(t);
    //
    Iterator<TreeNode> i=((Stripe_ForsythiaComposition)stripe).composition.getLeafPolygonIterator();
    FPolygon p;
    g.setPaint(Color.black);
    g.setStroke(createStroke((float)(STROKETHICKNESS/getStripeFCImageTransform(stripe).getScaleX())));
    while(i.hasNext()){
      p=(FPolygon)i.next();
      g.draw(p.getDPolygon().getPath2D());}
    g.setTransform(told);}
  
  private Stroke createStroke(float strokewidth){
    Stroke stroke=new BasicStroke(strokewidth,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,0,null,0);
    return stroke;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * RENDER STRIPE BLOCK FILL
   * fill up the whole stripe rectangle with a single color
   * used for debug
   * ++++++++++++++++++++++++++++++++
   */
  
  private void renderStripeBlockfill(Graphics2D g){
    for(Stripe stripe:this)
      if(stripe instanceof Stripe_ForsythiaComposition)
        renderStripeBlockfill(g,(Stripe_ForsythiaComposition)stripe);}

  private void renderStripeBlockfill(Graphics2D g,Stripe_ForsythiaComposition stripe){
    AffineTransform 
      told=g.getTransform(),
      t=new AffineTransform(told);
    t.concatenate(getStripeFCImageTransform(stripe));
    g.setTransform(t);
    //
    if(generator.terminus.contains(stripe)){
      g.setPaint(Color.red);
    }else{
      g.setPaint(Color.blue);}
    g.fill(((Stripe_ForsythiaComposition)stripe).composition.getRootPolygon().getDPolygon().getPath2D());
    //
    g.setTransform(told);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * RENDER INSERT
   * Just paint the image
   * ++++++++++++++++++++++++++++++++
   */
  
  private void renderInsert(Graphics2D g){
    for(Stripe stripe:this)
      if(stripe instanceof Stripe_Header)
        renderInsert(g,(Stripe_Header)stripe);}
  
  private void renderInsert(Graphics2D g,Stripe_Header stripe){
    AffineTransform t=AffineTransform.getTranslateInstance(stripe.getImageX(),0);
    g.drawImage(stripe.image,t,null);}
  
  
}
