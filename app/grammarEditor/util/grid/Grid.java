package org.fleen.forsythia.app.grammarEditor.util.grid;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.util.UI;
import org.fleen.geom_Kisrhombille.GK;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;

/*
 * interactive ui with a kisrhombille grid
 * we transform the perspective : pan and zoom
 * we capture vertex clicks
 * 
  HOW GRID UI WORKS
  
  2 layers : Background and foreground.
  
  Background is image that changes rarely
    grid is probably it. just grid
    Grid image is constant over everything but zoom, so we cache that. Shift it around for various pans.
  
  Foreground is image that changes often
    strokes, vertices, areas
    jigtarget, sections
  
  Cache Background
  
  Don't cache foreground
  
  on paint we copy Background to viewport and render foreground on top of it.
  
  ----
  
  pan by leftclick-drag
  
  zoom by shift-leftclick-verticaldrag
  
  shall we do a pan and zoom preview animation?
  coarse, minimal rendering of grid panning, zooming ??
 
 */
@SuppressWarnings("serial")
public abstract class Grid extends JPanel{
  
  public GridRenderer gridrenderer=new GridRenderer();
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Grid(){
    super();
    setDoubleBuffered(true);
    addMouseListener(ML0);
    addMouseMotionListener(ML0);
    setFocusable(true);
    initGeometryCache();}
  
  /*
   * ################################
   * CURSOR
   * Our convention : 
   * Circle means we're addressing vertices. 
   * Square means we're doing something other than addressing vertices, like selecting sections.  
   * ################################
   */
  
  private static final int
    CURSORMODE_AMBIGUOUS=-1,
    CURSORMODE_CIRCLE=0,
    CURSORMODE_SQUARE=1;
  
  private int cursormode=CURSORMODE_AMBIGUOUS;
  
  public void setCursorCircle(){
    if(cursormode!=CURSORMODE_CIRCLE){
      cursormode=CURSORMODE_CIRCLE;
      CursorCircle.setCursor(this);}}
  
  public void setCursorSquare(){
    if(cursormode!=CURSORMODE_SQUARE){
      cursormode=CURSORMODE_SQUARE;
      CursorSquare.setCursor(this);}}
  
  /*
   * ################################
   * IMAGE
   * Background image is seldom changed
   * Foreground image is often changed
   * Cache background
   * Don't cache foreground
   * Draw foreground on background 
   * 
   * note the background image placement
   * we offset it for viewcenter. mod because the pattern repeats
   * pad the edges, and offset to accomodate that, so we can shfit it around without causing gaps
   * ################################
   */
  
  public void paint(Graphics g){
    super.paint(g);
    int w=getWidth(),h=getHeight();
    if((w<=0)||(h<=0))return;
    BufferedImage i=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2=i.createGraphics();
    try{
      gridrenderer.render(
        g2,
        w,
        h,
        viewscale,
        viewcenterx,
        viewcentery);
      paintOverlay(
        g2,
        w,
        h,
        viewscale,
        viewcenterx,
        viewcentery);
      ((Graphics2D)g).drawImage(i,null,null);
    }catch(Exception x){
      System.out.println("grid paint failed");
      x.printStackTrace();}}
  
  public void paintOverlay(Graphics2D g,GridViewDef d){
    paintOverlay(g,d.w,d.h,d.scale,d.centerx,d.centery);}
  
  protected abstract void paintOverlay(
    Graphics2D g,int w,int h,double scale,double viewcenterx,double viewcentery);
  
  /*
   * ################################
   * VIEW TRANSFORM GEOMETRY
   * ################################
   */
  
  private static final double 
    VIEWSCALE_DEFAULT=32.0,//looks nice
    VIEWSCALE_MIN=8.0,//any smaller is impractical. Max is dependent on view dimensions
    VIEWCENTERX_DEFAULT=0,//origin
    VIEWCENTERY_DEFAULT=0;
  
  //we use this for interacting with some java 2d stuff
  //we keep it so we don't have to keep reconstructing it
  private Point2D.Double ptemp0=new Point2D.Double();
  
  public double 
    viewscale=VIEWSCALE_DEFAULT,
    viewcenterx=VIEWCENTERX_DEFAULT,
    viewcentery=VIEWCENTERY_DEFAULT;
  
  public void setViewScaleDefault(){
    gridrenderer.invalidateTileImage();
    viewscale=VIEWSCALE_DEFAULT;}
  
  public void setViewCenterDefault(){
    viewcenterx=VIEWCENTERX_DEFAULT;
    viewcentery=VIEWCENTERY_DEFAULT;}
  
  public void setTransformDefaults(){
    setViewScaleDefault();
    setViewCenterDefault();}
  
  //our param is a factor multiplied by the previous scale.
  private void adjustViewScale(double factor){
    clearGeometryCache();
    gridrenderer.invalidateTileImage();
    double vsm=getViewScaleMax();
    viewscale*=(factor);
    if(viewscale<VIEWSCALE_MIN)viewscale=VIEWSCALE_MIN;
    if(viewscale>vsm)viewscale=vsm;}
  
  //our params are deltas added to previous center xy
  private void adjustViewCenter(double dx,double dy){
    clearGeometryCache();
    viewcenterx+=dx;
    viewcentery+=dy;}
  
  private double getViewScaleMax(){
    return Math.max(getWidth(),getHeight());}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * Convert grid geometry to view geometry
   * ++++++++++++++++++++++++++++++++
   */
  
  public double[] getViewPoint(double x,double y){
    ptemp0.setLocation(x,y);
    getGridToViewTransform().transform(ptemp0,ptemp0);
    return new double[]{ptemp0.x,ptemp0.y};}
  
  public AffineTransform getGridToViewTransform(){
    AffineTransform t=new AffineTransform();
    t.translate(getWidth()/2,getHeight()/2);
    t.scale(viewscale,-viewscale);
    t.translate(-viewcenterx,-viewcentery);
    return t;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * Convert view geometry to grid geometry
   * ++++++++++++++++++++++++++++++++
   */
  
  public double[] getGridPoint(double x,double y){
    ptemp0.setLocation(x,y);
    getViewToGridTransform().transform(ptemp0,ptemp0);
    return new double[]{ptemp0.x,ptemp0.y};}
  
  public AffineTransform getViewToGridTransform(){
    AffineTransform t=null;
    try{
      t=getGridToViewTransform().createInverse();
    }catch(Exception x){
      x.printStackTrace();}
    return t;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * CENTER AND FIT
   * ++++++++++++++++++++++++++++++++
   */
  
  public void centerAndFit(){
    gridrenderer.invalidateTileImage();
    KPolygon polygon=getHostPolygon();
    //if there's nothing to center and fit then just set defaults
    if(polygon==null||polygon.isEmpty()){
      setTransformDefaults();
      return;}
    //do scale
    Rectangle2D.Double polygonbounds=UIGridUtil.getPolygonBounds2D(polygon);
    double 
      viewwidth=getWidth(),
      viewheight=getHeight(),
      whratioview=viewwidth/viewheight,
      whratiopbounds=polygonbounds.width/polygonbounds.height;
    if(whratioview>whratiopbounds){
      viewscale=(viewheight-(UI.GRID_CENTERANDFITVIEWMARGIN*2.0))/polygonbounds.height;
    }else{
      viewscale=(viewwidth-(UI.GRID_CENTERANDFITVIEWMARGIN*2.0))/polygonbounds.width;}
    if(viewscale<VIEWSCALE_MIN)viewscale=VIEWSCALE_MIN;
    double vsm=getViewScaleMax();
    if(viewscale>vsm)viewscale=vsm;
    //do center
    viewcenterx=polygonbounds.getCenterX();
    viewcentery=polygonbounds.getCenterY();}
  
  //we center and fit on a polygon. it could be null, empty, complete or incomplete.
  protected abstract KPolygon getHostPolygon();
  
  /*
   * ################################
   * MOUSE
   * leftclick vertex to touch point/area/vertex
   * leftclickdrag to pan
   * shift+leftclickdrag (vertically) to zoom
   * doubleclick to center and fit
   * ---
   * image changes on mouse action
   * on leftclickanddrag (pan or zoom) invalidate background and foreground and repaint
   * on leftclick (touch vertex, touch area) fire modeltouched event. refresh handled therein
   * ---
   * when the mouse moves close to a vertex we set vertexmode, when it moves away we set sectionmode
   * ################################
   */
 
  private static final int MINDRAGDIST=10; 
  private int mousedownx,mousedowny;
  
  private MouseAdapter ML0=new MouseAdapter(){
    
    public void mousePressed(MouseEvent e){
      requestFocusInWindow();
      mousedownx=e.getX();
      mousedowny=e.getY();}
    
    public void mouseReleased(MouseEvent e){
      int b=e.getButton(),x=e.getX(),y=e.getY();
      if(b==MouseEvent.BUTTON1&&!e.isConsumed()){
        //center and fit on left doubleclick
        if(e.getClickCount()==2) {
          e.consume();
          centerAndFit();
          repaint();
          return;}
        int dx=x-mousedownx,dy=y-mousedowny;
        boolean shift=e.isShiftDown();
        if(Math.abs(dx)<MINDRAGDIST&&Math.abs(dy)<MINDRAGDIST&&!shift){
          doMouseTouch(x,y);
        }else{
          if(shift){
            doMouseZoom(dy);
          }else{
            doMousePan(dx,dy);}}}}
    
    public void mouseMoved(MouseEvent e){
      doConditionalSampleForMouseMoved(e.getX(),e.getY());}};
  
  private void doMouseTouch(int x,int y){
    mouseTouched(
      getViewPoint(latestsamplepoint[0],latestsamplepoint[1]),//in view geometry
      latestsamplevertex);}
  
  private void doMouseZoom(double dy){
    double z=1.0+dy/getHeight();
    adjustViewScale(z*z);
    repaint();}
  
  private void doMousePan(int dx,int dy){
    adjustViewCenter(-dx/viewscale,dy/viewscale);
    repaint();}
  
  /*
   * ################################
   * MOUSE MOVE SAMPLING
   * When we move the mouse we sample it's location, testing how close it is to a vertex
   * ################################
   */
  
  private static final long MOUSEMOVESAMPLEPERIOD=50;
  
  private long lastsample=-1;
  private double[] latestsamplepoint;
  private KVertex latestsamplevertex;
  
  public double[] getLatestSamplePoint(){
    return latestsamplepoint;}
  
  public KVertex getLatestSampleVertex(){
    return latestsamplevertex;}
  
  private void doConditionalSampleForMouseMoved(int x,int y){
    long t=System.currentTimeMillis();
    if(t-lastsample<MOUSEMOVESAMPLEPERIOD)return;
    lastsample=t;
    latestsamplepoint=getGridPoint(x,y);
    latestsamplevertex=GK.getStandardVertex(
      latestsamplepoint[0],
      latestsamplepoint[1],
      UI.VERTEX_CLOSENESS_MARGIN/viewscale);
    if(latestsamplevertex==null){
      mouseMovedFarFromVertex(latestsamplepoint);
    }else{
      mouseMovedCloseToVertex(latestsamplevertex);}}
  
  /*
   * ################################
   * ABSTRACT MOUSE ACTION
   * ################################
   */
  
  protected abstract void mouseTouched(double[] p,KVertex v);
  
  protected abstract void mouseMovedCloseToVertex(KVertex v);
  
  protected abstract void mouseMovedFarFromVertex(double[] p);
  
  /*
   * ################################
   * GEOMETRY CACHE
   * ################################
   */
  
  GeometryCache geometrycache=null;
  
  private void initGeometryCache(){
    geometrycache=new GeometryCache(this);}
  
  public void clearGeometryCache(){
    geometrycache.clear();}
  
  public GeometryCache getGeometryCache(){
    return geometrycache;}
  
}
