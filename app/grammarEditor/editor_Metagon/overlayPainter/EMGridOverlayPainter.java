package org.fleen.forsythia.app.grammarEditor.editor_Metagon.overlayPainter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Metagon.Editor_Metagon;
import org.fleen.forsythia.app.grammarEditor.util.UI;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.GD;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.graph.GEdge;
import org.fleen.geom_Kisrhombille.graph.GVertex;

public class EMGridOverlayPainter{
  
  public void paint(Graphics2D graphics,int w,int h,double scale,double centerx,double centery){
    graphics.setRenderingHints(UI.RENDERING_HINTS);
    GE.ge.editor_metagon.editedmetagon.metagoneditorgeometrycache.update(w,h,scale,centerx,centery);
    renderGraph(graphics);}
  
  /*
   * ################################
   * RENDER JIG MODEL FOR EDIT GEOMETRY MODE
   * ################################
   */
  
  private void renderGraph(Graphics2D graphics){
    try{
//      fillSections_EditGeometry(graphics);
      strokeGraphEdges_EditGeometry(graphics);
      renderVertices_EditGeometry(graphics);
    }catch(Exception x){
      x.printStackTrace();}}
  
  private void fillSections_EditGeometry(Graphics2D graphics){
    Color color;
    Path2D path;
    for(KPolygon m:GE.ge.editor_metagon.editedmetagon.graph.getDisconnectedGraph().getUndividedPolygons()){
      color=UI.EDITJIG_EDITGEOMETRY_HOSTMETAGONFILLCOLOR;
      path=GE.ge.editor_metagon.editedmetagon.metagoneditorgeometrycache.getPath(m);
      graphics.setPaint(color);
      graphics.fill(path);}}
  
  private void strokeGraphEdges_EditGeometry(Graphics2D graphics){
    graphics.setStroke(UI.GRID_DRAWINGSTROKE);
    graphics.setPaint(UI.EDITJIG_EDITGEOMETRY_STROKECOLOR);
    Iterator<GEdge> i=GE.ge.editor_metagon.editedmetagon.graph.edges.iterator();
    GEdge e;
    double[] p0,p1;
    Path2D path=new Path2D.Double();
    while(i.hasNext()){
      e=i.next();
      p0=GE.ge.editor_metagon.editedmetagon.metagoneditorgeometrycache.getPoint(e.v0.kvertex);
      p1=GE.ge.editor_metagon.editedmetagon.metagoneditorgeometrycache.getPoint(e.v1.kvertex);
      path.reset();
      path.moveTo(p0[0],p0[1]);
      path.lineTo(p1[0],p1[1]);
      graphics.draw(path);}}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * RENDER VERTICES
   * ++++++++++++++++++++++++++++++++
   */
  
  private void renderVertices_EditGeometry(Graphics2D graphics){
    renderDefaultVertices(graphics,UI.EDITJIG_EDITGEOMETRY_STROKECOLOR);
    renderHeadDecorations(graphics);}
  
  private void renderDefaultVertices(Graphics2D graphics,Color color){
    graphics.setPaint(color);
    float span=UI.GRID_DEFAULTVERTEXSPAN;
    double[] p;
    Ellipse2D dot=new Ellipse2D.Double();
    for(GVertex v:GE.ge.editor_metagon.editedmetagon.graph.vertices){
      p=GE.ge.editor_metagon.editedmetagon.metagoneditorgeometrycache.getPoint(v.kvertex);
      dot.setFrame(p[0]-span/2,p[1]-span/2,span,span);
      graphics.fill(dot);}}
  
  private void renderHeadDecorations(Graphics2D graphics){
    graphics.setStroke(UI.EDITJIG_EDITGEOMETRY_HEADVERTEXDECORATIONSTROKE);
    int span=UI.EDITJIG_EDITGEOMETRY_HEADVERTEXDECORATIONSPAN;
    double[] p;
    if(GE.ge.editor_metagon.connectedhead!=null){
      p=GE.ge.editor_metagon.editedmetagon.metagoneditorgeometrycache.getPoint(GE.ge.editor_metagon.connectedhead);
      graphics.setPaint(UI.EDITJIG_EDITGEOMETRY_CONNECTEDHEADVERTEXDECORATIONCOLOR);
      graphics.drawOval(((int)p[0])-span/2,((int)p[1])-span/2,span,span);
    }else if(GE.ge.editor_metagon.unconnectedhead!=null){
      p=GE.ge.editor_metagon.editedmetagon.metagoneditorgeometrycache.getPoint(GE.ge.editor_metagon.unconnectedhead);
      graphics.setPaint(UI.EDITJIG_EDITGEOMETRY_UNCONNECTEDHEADVERTEXDECORATIONCOLOR);
      graphics.drawOval(((int)p[0])-span/2,((int)p[1])-span/2,span,span);}}
  
}
