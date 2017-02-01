package org.fleen.forsythia.app.grammarEditor.editor_Grammar.ui;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.Editor_Jig;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.gridOverlayPainter.GridOverlayPainter;
import org.fleen.forsythia.app.grammarEditor.project.jig.ProjectJig;
import org.fleen.forsythia.app.grammarEditor.project.jig.ProjectJigSection;
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

public class GridEditMetagon extends Grid{
  
  private static final long serialVersionUID=5154407826012214745L;

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
    int d=1;
    ProjectJig jig=GE.ge.editor_jig.editedjig;
    if(jig!=null)d=jig.griddensity;
    return GE.ge.focusmetagon.kmetagon.getScaledPolygon(d);}
  
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
    if(GE.ge.editor_jig.mode==Editor_Jig.MODE_CREATE_A){
      //test the hovered vertex and connectedhead for colinearity
      //not colinear
      if(GE.ge.editor_jig.connectedhead!=null&&v!=null&&!v.isColinear(GE.ge.editor_jig.connectedhead)){
        setCursorX();
      //colinear  
      }else{
        setCursorCircle();}
    //touching sections modes gets a square cursor 
    }else{//MODE_CREATE_B || MODE_RETOUCH
      setCursorSquare();}}

  protected void mouseMovedFarFromVertex(double[] p){
    mousemove=MOUSEMOVE_VERTEXFAR;
    if(GE.ge.editor_jig.mode==Editor_Jig.MODE_CREATE_A){
      setCursorX();
    }else{
      setCursorSquare();}}
  
  /*
   * ################################
   * MOUSE CLICK
   * ################################
   */

  //TODO clean up
  protected void mouseTouched(double[] p,KVertex v){
    if(GE.ge.editor_jig.mode==Editor_Jig.MODE_CREATE_A&&mousemove==MOUSEMOVE_VERTEXNEAR){
      boolean valid=true;
      if(GE.ge.editor_jig.connectedhead!=null&&v!=null&&!v.isColinear(GE.ge.editor_jig.connectedhead))
        valid=false;
      if(valid)GE.ge.editor_jig.touchVertex(v);
    }else{
      GE.ge.editor_jig.touchSection(getSection(p));}}
  
  private ProjectJigSection getSection(double[] p){
    Path2D path;
    for(ProjectJigSection m:GE.ge.editor_jig.editedjig.sections){
      path=GE.ge.editor_jig.editedjig.jigeditorgeometrycache.getPath(m.getPolygon());
      if(path.contains(p[0],p[1]))
        return m;}
    return null;}
  
}
