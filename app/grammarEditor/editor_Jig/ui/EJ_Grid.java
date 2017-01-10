package org.fleen.forsythia.app.grammarEditor.editor_Jig.ui;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.GridOverlayPainter;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.JigEditingModel;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.JigSectionEditingModel;
import org.fleen.forsythia.app.grammarEditor.util.grid.Grid;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;

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
    return GE.editor_jig.getHostPolygon();}
  
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

  protected void mouseTouched(double[] p,KVertex v){
    if(mousemode==MOUSEMODE_TOUCHVERTEX)
      GE.editor_jig.touchVertex(v);
    else
      GE.editor_jig.touchSection(getSection(p));}

  protected void mouseMovedCloseToVertex(KVertex v){
    setCursorCircle();
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
  
//  private KPolygon getPolygon(double[] p){
//    JigEditingModel m=GE.editor_jig.model;
//    List<KPolygon> undividedpolygons=m.rawgraph.getDisconnectedGraph().getUndividedPolygons();
//    Path2D path;
//    for(KPolygon a:undividedpolygons){
//      path=GE.editor_jig.model.viewgeometrycache.getPath(a);
//      if(path.contains(p[0],p[1]))
//        return a;}
//    return null;}
  
}
