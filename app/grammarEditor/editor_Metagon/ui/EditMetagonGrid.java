package org.fleen.forsythia.app.grammarEditor.editor_Metagon.ui;

import java.awt.Graphics2D;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Metagon.Editor_Metagon;
import org.fleen.forsythia.app.grammarEditor.editor_Metagon.overlayPainter.EMGridOverlayPainter;
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

public class EditMetagonGrid extends Grid{
  
  private static final long serialVersionUID=5572475027375310727L;

  EMGridOverlayPainter overlaypainter=new EMGridOverlayPainter();

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
    List<KPolygon> p=GE.ge.editor_metagon.editedmetagon.getGraph().getDisconnectedGraph().getPolygons();
    if(p.isEmpty())return null;
    return p.get(0);}
  
  /*
   * ################################
   * MOUSE MOVE
   * ################################
   */

  static final int 
    MOUSEMOVE_VERTEXNEAR=0,
    MOUSEMOVE_VERTEXFAR=1;
  
  int mousemove;
  
  /*
   * test the vertex 
   * if the vertex is not colinear with the vertex that we are connecting to (if the previous vertex isn't null) then we should 
   * indicate that with an invalid flag or something 
   */
  protected void mouseMovedCloseToVertex(KVertex v){
    mousemove=MOUSEMOVE_VERTEXNEAR;
    if(GE.ge.editor_metagon.mode==Editor_Metagon.MODE_CREATE){
      //test the hovered vertex and connectedhead for colinearity
      //not colinear
      if(GE.ge.editor_metagon.connectedhead!=null&&v!=null&&!v.isColinear(GE.ge.editor_metagon.connectedhead)){
        setCursorX();
      //colinear  
      }else{
        setCursorCircle();}
    //touching sections modes gets a square cursor 
    }else{//MODE_CREATE_B || MODE_RETOUCH
      setCursorX();}}

  protected void mouseMovedFarFromVertex(double[] p){
    mousemove=MOUSEMOVE_VERTEXFAR;
    setCursorX();}
  
  /*
   * ################################
   * MOUSE CLICK
   * ################################
   */

  //TODO clean up
  protected void mouseTouched(double[] p,KVertex v){
    boolean valid=true;
    if(GE.ge.editor_metagon.connectedhead!=null&&v!=null&&!v.isColinear(GE.ge.editor_metagon.connectedhead))
      valid=false;
    if(valid)GE.ge.editor_metagon.touchVertex(v);}
  
}
