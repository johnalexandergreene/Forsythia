package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.Iterator;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.graph.GEdge;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.graph.GVertex;
import org.fleen.forsythia.app.grammarEditor.util.UI;
import org.fleen.geom_Kisrhombille.KPolygon;

public class GridOverlayPainter{
  
  public void paint(Graphics2D graphics,int w,int h,double scale,double centerx,double centery){
    graphics.setRenderingHints(UI.RENDERING_HINTS);
    GE.editor_jig.model.viewgeometrycache.update(w,h,scale,centerx,centery);
    //render for edit sections mode
    if(GE.editor_jig.geometrylock){
      fillSections(graphics);
      renderGraphEdges(graphics,Color.white);//TODO these should be UI colors
      renderVertices(graphics,Color.white);
    //render for edit geometry mode
    }else{
      renderGraphEdges(graphics,Color.black);
      renderVertices(graphics,Color.black);
    }}
  
  private void fillSections(Graphics2D graphics){
    int colorindex;
    Color color;
    Path2D path;
    for(JigSectionEditingModel m:GE.editor_jig.model.sections){
      colorindex=m.chorus;
      color=UI.EJD_SECTIONFILLCHORUSINDICES[colorindex%UI.EJD_SECTIONFILLCHORUSINDICES.length];
      path=GE.editor_jig.model.viewgeometrycache.getPath(m.getPolygon());
      graphics.setPaint(color);
      graphics.fill(path);}}
  
//  private void fillSections(Graphics2D graphics){
//    
//    List<KPolygon> polygons=GE.editor_jig.model.getSectionPolygons();
//    int colorindex;
//    Color color;
//    Path2D path;
//    for(KPolygon polygon:polygons){
//      colorindex=GE.editor_jig.model.getSectionModel(polygon).chorus;
//      color=UI.EJD_SECTIONFILLCHORUSINDICES[colorindex%UI.EJD_SECTIONFILLCHORUSINDICES.length];
//      path=GE.editor_jig.model.viewgeometrycache.getPath(polygon);
//      graphics.setPaint(color);
//      graphics.fill(path);}}
  
  /*
   * ################################
   * RENDER GRAPH EDGES
   * ################################
   */
  
  private void renderGraphEdges(Graphics2D graphics,Color color){
    graphics.setStroke(UI.GRID_DRAWINGSTROKE);
    graphics.setPaint(color);
    
    Iterator<GEdge> i=GE.editor_jig.model.rawgraph.edges.iterator();
    GEdge e;
    double[] p0,p1;
    Path2D path=new Path2D.Double();
    //System.out.println("edge count:"+Q.editor_createjig.graph.getEdgeCount());
    while(i.hasNext()){
      e=i.next();
      p0=GE.editor_jig.model.viewgeometrycache.getPoint(e.v0.kvertex);
      p1=GE.editor_jig.model.viewgeometrycache.getPoint(e.v1.kvertex);
      path.reset();
      path.moveTo(p0[0],p0[1]);
      path.lineTo(p1[0],p1[1]);
      graphics.draw(path);}}
  
  /*
   * ################################
   * RENDER VERTICES
   * ################################
   */
  
  private void renderVertices(Graphics2D graphics,Color color){
    renderDefaultVertices(graphics,color);
    renderHeadDecorations(graphics);}
  
  private void renderDefaultVertices(Graphics2D graphics,Color color){
    graphics.setPaint(color);
    float span=UI.GRID_DEFAULTVERTEXSPAN;
    double[] p;
    Ellipse2D dot=new Ellipse2D.Double();
    for(GVertex v:GE.editor_jig.model.rawgraph.vertices){
      p=GE.editor_jig.model.viewgeometrycache.getPoint(v.kvertex);
      dot.setFrame(p[0]-span/2,p[1]-span/2,span,span);
      graphics.fill(dot);}}
  
  private void renderHeadDecorations(Graphics2D graphics){
    graphics.setStroke(UI.EDITORCREATEPROTOJIG_HEADVERTEXDECORATIONSTROKE);
    int span=UI.EDITORCREATEPROTOJIG_HEADVERTEXDECORATIONSPAN;
    double[] p;
    if(GE.editor_jig.connectedhead!=null){
      p=GE.editor_jig.model.viewgeometrycache.getPoint(GE.editor_jig.connectedhead);
      graphics.setPaint(UI.EDITORCREATEPROTOJIG_CONNECTEDHEADVERTEXDECORATIONCOLOR);
      graphics.drawOval(((int)p[0])-span/2,((int)p[1])-span/2,span,span);
    }else if(GE.editor_jig.unconnectedhead!=null){
      p=GE.editor_jig.model.viewgeometrycache.getPoint(GE.editor_jig.unconnectedhead);
      graphics.setPaint(UI.EDITORCREATEPROTOJIG_UNCONNECTEDHEADVERTEXDECORATIONCOLOR);
      graphics.drawOval(((int)p[0])-span/2,((int)p[1])-span/2,span,span);}}
  
//  /*
//   * if the section is focus 
//   *   put icons by all vertices, distinguish v0 by color
//   *   draw twist arrows
//   * if the section is nonfocus
//   *   put icon by v0
//   *   draw twist arrows
//   */
//  private void renderAnchorGlyphs(Graphics2D graphics,JigSectionDetailsModel model){
//    JigSectionDetailsModelVertexIconCircles vic=model.getVertexIconCircles();
//    KVertex v0=model.getV0();
//    //always render the v0
//    renderVertexIconCircle(graphics,vic.getCircle(v0),true);
//    //if the section is focus then render the other v0 options as well
//    Set<KVertex> v0options=model.getV0Options();
//    v0options.remove(v0);
//    if(model.isFocus())
//      for(KVertex v:v0options)
//        renderVertexIconCircle(graphics,vic.getCircle(v),false);
//    //render twist arrows
//    renderTwistArrows(graphics,vic.getCircles(),Color.black,model);}
//  
//  private void renderVertexIconCircle(Graphics2D g,DCircle c,boolean fill){
//    g.setPaint(Color.black);
//    Ellipse2D e=new Ellipse2D.Double(
//      c.x-c.r,
//      c.y-c.r,
//      2*c.r,
//      2*c.r);
//    if(fill){
//      g.fill(e);
//    }else{
//      g.setStroke(UI.GRID_DRAWINGSTROKE);
//      g.draw(e);}}
//  
//  private void renderTwistArrows(Graphics2D g,List<DCircle> vertexiconcircles,Color color,JigSectionDetailsModel model){
//    int inext,s=vertexiconcircles.size();
//    double[] p0,p1;
//    for(int i=0;i<s;i++){
//      inext=i+1;
//      if(inext==s)inext=0;
//      p0=vertexiconcircles.get(i).getCenter();
//      p1=vertexiconcircles.get(inext).getCenter();
//      renderTwistArrow(p0,p1,g);}}
  
  
}
