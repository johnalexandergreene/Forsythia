package org.fleen.forsythia.app.grammarEditor.editor_Jig.ui;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.Editor_Jig;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.JigSectionEditingModel;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.gridOverlayPainter.GridOverlayPainter;
import org.fleen.forsythia.app.grammarEditor.util.grid.Grid;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;

/*
 * GRID
 * when panning we repaint
 * when zooming we repaint
 * when changing grid density we init the graph then clear the geometry cache then repaint
 * when touching vertices we repaint
 * when touching sections we repaint 
 * 
 * 
 */

@SuppressWarnings("serial")
public class EJ_Grid extends Grid{
  
  GridOverlayPainter overlaypainter=new GridOverlayPainter();

  protected void paintOverlay(Graphics2D g,int w,int h,double scale,double centerx,double centery){
    try{
      overlaypainter.paint(
        g,
        w,
        h,
        scale,
        centerx,
        centery);
     }catch(Exception x){}}

  protected KPolygon getHostPolygon(){
    return GE.editor_jig.getScaledHostPolygon();}
  
  /*
   * ################################
   * MOUSE
   * We monitor the distance of the mouse from any vertices
   * when we're close we're in touch vertices mode
   * when we're far we're in touch sections mode
   * ################################
   */

  private static final int 
    MOUSEMODE_TOUCHVERTEX=0,
    MOUSEMODE_TOUCHSECTION=1;
  
  private int mousemode;

  //TODO clean up
  protected void mouseTouched(double[] p,KVertex v){
    if(GE.editor_jig.mode==Editor_Jig.MODE_EDITGEOMETRY&&mousemode==MOUSEMODE_TOUCHVERTEX){
      boolean valid=true;
      if(GE.editor_jig.connectedhead!=null&&v!=null&&!v.isColinear(GE.editor_jig.connectedhead))
        valid=false;
      if(valid)GE.editor_jig.touchVertex(v);
    }else{
      GE.editor_jig.touchSection(getSection(p));}}

  /*
   * test the vertex 
   * if the vertex is not colinear with the vertex that we are connecting to (if the previous vertex isn't null) then we should 
   * indicate that with an invalid flag or something 
   */
  protected void mouseMovedCloseToVertex(KVertex v){
    boolean valid=true;
    if(GE.editor_jig.connectedhead!=null&&v!=null&&!v.isColinear(GE.editor_jig.connectedhead))
      valid=false;
    if(valid)
      setCursorCircle();
    else
      setCursorX();
    mousemode=MOUSEMODE_TOUCHVERTEX;}

  protected void mouseMovedFarFromVertex(double[] p){
    setCursorSquare();
    mousemode=MOUSEMODE_TOUCHSECTION;}
  
  private JigSectionEditingModel getSection(double[] p){
    Path2D path;
    for(JigSectionEditingModel m:GE.editor_jig.model.sections){
      path=GE.editor_jig.model.viewgeometrycache.getPath(m.getPolygon());
      if(path.contains(p[0],p[1]))
        return m;}
    return null;}
  
}
