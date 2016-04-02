package org.fleen.forsythia.app.grammarEditor.editor_CreateJigGeometry;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.Iterator;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_CreateJigGeometry.graph.GEdge;
import org.fleen.forsythia.app.grammarEditor.editor_CreateJigGeometry.graph.GVertex;
import org.fleen.forsythia.app.grammarEditor.util.UI;
import org.fleen.geom_Kisrhombille.KYard;

public class GridOverlayPainter{
  
  void paint(Graphics2D graphics,int w,int h,double scale,double centerx,double centery){
    graphics.setRenderingHints(UI.RENDERING_HINTS);
    GE.editor_createjig.viewgeometrycache.update(w,h,scale,centerx,centery);
    renderHostMetagonPolygonAndFocusPolygon(graphics);
    renderYards(graphics);
    renderEdges(graphics);
    renderVertices(graphics);}
  
  /*
   * ################################
   * RENDER HOST METAGON POLYGON AND FOCUS POLYGON
   * ################################
   */
  
  private void renderHostMetagonPolygonAndFocusPolygon(Graphics2D graphics){
    Path2D hmppath=GE.editor_createjig.viewgeometrycache.getPath(GE.editor_createjig.getHostPolygon());
    if(GE.editor_createjig.focuselement!=null){
      Path2D fppath=GE.editor_createjig.viewgeometrycache.getPath(GE.editor_createjig.focuselement);  
      Area a0=new Area(hmppath);
      Area a1=new Area(fppath);
      a0.subtract(a1);  
      graphics.setPaint(UI.EDITORCREATEPROTOJIG_HOSTMETAGONFILLCOLOR);
      graphics.fill(a0);  
      graphics.setPaint(UI.EDITORCREATEPROTOJIG_FOCUSSECTIONFILLCOLOR);
      graphics.fill(a1);
    }else{
      graphics.setPaint(UI.EDITORCREATEPROTOJIG_HOSTMETAGONFILLCOLOR);
      graphics.fill(hmppath);}}
  
  /*
   * ################################
   * RENDER YARDS
   * ################################
   */
  
  private void renderYards(Graphics2D graphics){
    graphics.setPaint(new Color(128,128,255,128));
    Path2D path;
    List<KYard> yards=GE.editor_createjig.rawgraph.getDisconnectedGraph().getYards();
    System.out.println("### YARDCOUNT @ PAINTER : "+yards.size());
    int rindex=0;
    for(KYard y:yards){
      System.out.println("rendering yard ::: "+rindex);
      rindex++;
      try{
        path=GE.editor_createjig.viewgeometrycache.getPath(y);
        graphics.fill(path);
      }catch(Exception x){
        System.out.println("EXCEPTION IN RENDER YARDS");
        x.printStackTrace();}}
    }
  
  /*
   * ################################
   * RENDER EDGES
   * ################################
   */
  
  private void renderEdges(Graphics2D graphics){
    graphics.setStroke(UI.GRID_DRAWINGSTROKE);
    graphics.setPaint(UI.GRID_DRAWINGSTROKECOLOR);
    
    //for drawing graphics, disregard otherwise
//    Stroke s=new BasicStroke(8f,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,0,null,0);
//    graphics.setStroke(s);
//    graphics.setPaint(new Color(192,64,64));
    
    
    Iterator<GEdge> i=GE.editor_createjig.rawgraph.edges.iterator();
    GEdge e;
    double[] p0,p1;
    Path2D path=new Path2D.Double();
    //System.out.println("edge count:"+Q.editor_createjig.graph.getEdgeCount());
    while(i.hasNext()){
      e=i.next();
      p0=GE.editor_createjig.viewgeometrycache.getPoint(e.v0.kvertex);
      p1=GE.editor_createjig.viewgeometrycache.getPoint(e.v1.kvertex);
      path.reset();
      path.moveTo(p0[0],p0[1]);
      path.lineTo(p1[0],p1[1]);
      graphics.draw(path);}}
  
  /*
   * ################################
   * RENDER VERTICES
   * ################################
   */
  
  private void renderVertices(Graphics2D graphics){
    renderDefaultVertices(graphics);
    renderHeadDecorations(graphics);}
  
  private void renderDefaultVertices(Graphics2D graphics){
//    System.out.println("vertex count:"+Q.editor_createjig.graph.getVertexCount());
    
    
    graphics.setPaint(UI.GRID_DRAWINGSTROKECOLOR);
    float span=UI.GRID_DEFAULTVERTEXSPAN;
    
//    //for drawing graphics, disregard otherwise
//    span=18;
//    graphics.setPaint(new Color(64,64,64));
    
    
    double[] p;
    Ellipse2D dot=new Ellipse2D.Double();
    for(GVertex v:GE.editor_createjig.rawgraph.vertices){
      p=GE.editor_createjig.viewgeometrycache.getPoint(v.kvertex);
      dot.setFrame(p[0]-span/2,p[1]-span/2,span,span);
      graphics.fill(dot);}}
  
  private void renderHeadDecorations(Graphics2D graphics){
    graphics.setStroke(UI.EDITORCREATEPROTOJIG_HEADVERTEXDECORATIONSTROKE);
    int span=UI.EDITORCREATEPROTOJIG_HEADVERTEXDECORATIONSPAN;
    double[] p;
    if(GE.editor_createjig.connectedhead!=null){
      p=GE.editor_createjig.viewgeometrycache.getPoint(GE.editor_createjig.connectedhead);
      graphics.setPaint(UI.EDITORCREATEPROTOJIG_CONNECTEDHEADVERTEXDECORATIONCOLOR);
      graphics.drawOval(((int)p[0])-span/2,((int)p[1])-span/2,span,span);
    }else if(GE.editor_createjig.unconnectedhead!=null){
      p=GE.editor_createjig.viewgeometrycache.getPoint(GE.editor_createjig.unconnectedhead);
      graphics.setPaint(UI.EDITORCREATEPROTOJIG_UNCONNECTEDHEADVERTEXDECORATIONCOLOR);
      graphics.drawOval(((int)p[0])-span/2,((int)p[1])-span/2,span,span);}}
  
}
