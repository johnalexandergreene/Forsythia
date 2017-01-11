package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.Iterator;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.graph.GEdge;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.graph.GVertex;
import org.fleen.forsythia.app.grammarEditor.util.UI;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.GD;
import org.fleen.geom_Kisrhombille.KPolygon;

public class GridOverlayPainter{
  
  public void paint(Graphics2D graphics,int w,int h,double scale,double centerx,double centery){
    graphics.setRenderingHints(UI.RENDERING_HINTS);
    GE.editor_jig.model.viewgeometrycache.update(w,h,scale,centerx,centery);
    if(GE.editor_jig.mode==Editor_Jig.MODE_EDITSECTIONS){
      fillSectionsForEditSections(graphics);
      strokePolygonEdgesForEditSections(graphics);
      renderGlyphsForEditSections(graphics);
    }else{//GE.editor_jig.mode==Editor_Jig.MODE_EDITGEOMETRY
      fillSectionsForEditGeometry(graphics);
      strokeGraphEdgesForEditGeometry(graphics);
      renderVertices(graphics);}}
  
  
  private void strokePolygonEdgesForEditSections(Graphics2D graphics){
    graphics.setStroke(UI.GRID_DRAWINGSTROKE);
    graphics.setPaint(UI.EDITORCREATEJIG_EDITSECTIONSSTROKECOLOR);
    Path2D path;
    for(JigSectionEditingModel m:GE.editor_jig.model.sections){
      if(m==GE.editor_jig.focussection)continue;
      path=GE.editor_jig.model.viewgeometrycache.getPath(m.getPolygon());
      graphics.draw(path);}
    //focus
    graphics.setPaint(UI.EDITORCREATEJIG_EDITSECTIONSGLYPHSTROKECOLOR);
    path=GE.editor_jig.model.viewgeometrycache.getPath(GE.editor_jig.focussection.getPolygon());
    graphics.draw(path);}
  
  
  private void fillSectionsForEditSections(Graphics2D graphics){
    int colorindex;
    Color color;
    Path2D path;
    for(JigSectionEditingModel m:GE.editor_jig.model.sections){
      colorindex=m.chorus;
      color=UI.EJD_SECTIONFILLCHORUSINDICES[colorindex%UI.EJD_SECTIONFILLCHORUSINDICES.length];
      path=GE.editor_jig.model.viewgeometrycache.getPath(m.getPolygon());
      graphics.setPaint(color);
      graphics.fill(path);}}
  
  private void fillSectionsForEditGeometry(Graphics2D graphics){
    Color color;
    Path2D path;
    for(KPolygon m:GE.editor_jig.model.rawgraph.getDisconnectedGraph().getUndividedPolygons()){
      color=UI.EDITORCREATEJIG_HOSTMETAGONFILLCOLOR;
      path=GE.editor_jig.model.viewgeometrycache.getPath(m);
      graphics.setPaint(color);
      graphics.fill(path);}}
  
  private void strokeGraphEdgesForEditGeometry(Graphics2D graphics){
    graphics.setStroke(UI.GRID_DRAWINGSTROKE);
    graphics.setPaint(UI.EDITORCREATEJIG_EDITGEOMETRYSTROKECOLOR);
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
  
  private void renderVertices(Graphics2D graphics){
    renderDefaultVertices(graphics,UI.EDITORCREATEJIG_EDITGEOMETRYSTROKECOLOR);
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
    graphics.setStroke(UI.EDITORCREATEJIG_HEADVERTEXDECORATIONSTROKE);
    int span=UI.EDITORCREATEJIG_HEADVERTEXDECORATIONSPAN;
    double[] p;
    if(GE.editor_jig.connectedhead!=null){
      p=GE.editor_jig.model.viewgeometrycache.getPoint(GE.editor_jig.connectedhead);
      graphics.setPaint(UI.EDITORCREATEJIG_CONNECTEDHEADVERTEXDECORATIONCOLOR);
      graphics.drawOval(((int)p[0])-span/2,((int)p[1])-span/2,span,span);
    }else if(GE.editor_jig.unconnectedhead!=null){
      p=GE.editor_jig.model.viewgeometrycache.getPoint(GE.editor_jig.unconnectedhead);
      graphics.setPaint(UI.EDITORCREATEJIG_UNCONNECTEDHEADVERTEXDECORATIONCOLOR);
      graphics.drawOval(((int)p[0])-span/2,((int)p[1])-span/2,span,span);}}
  
  /*
   * ################################
   * RENDER GLYPHS FOR EDIT SECTIONS
   * The focus polygon gets a system of glyphs within its edge that indicate
   * the form of the anchor. That is, v0 and twist.
   * we indicate vertex0 with a dot
   * we indicate twist with a course of arrows 
   * ################################
   */
  
  /*
   * get polygon points
   * get inward points
   * devise all end points for arrows
   *   we divide each side into 3 parts
   *   the ends get half a corner-arrow
   *   the middle get a straight-arrow
   *   v0 gets a dot and an arrow growing from it
   * render dot and arrows
   */
  private void renderGlyphsForEditSections(Graphics2D graphics){
    System.out.println("render glyphs");
    DPolygon focuspolygon=GE.editor_jig.model.viewgeometrycache.getDPolygon(GE.editor_jig.focussection.getPolygon());
    boolean clockwise=focuspolygon.getTwist();
    DPolygon innerpolygon=getInnerPolygon(focuspolygon,clockwise);
    //TEST
    
    graphics.setStroke(UI.GRID_DRAWINGSTROKE);
    graphics.setPaint(UI.EDITORCREATEJIG_EDITSECTIONSGLYPHSTROKECOLOR);
    Path2D path=innerpolygon.getPath2D();
    graphics.draw(path);
    DPoint v0=innerpolygon.get(0);
    Ellipse2D dot=new Ellipse2D.Double(v0.x-5,v0.y-5,10,10);
    graphics.fill(dot);
    
  }
  
  private DPolygon getInnerPolygon(DPolygon outerpolygon,boolean clockwise){
    int s=outerpolygon.size(),iprior,inext;
    DPolygon innerpolygon=new DPolygon(s);
    DPoint p,pprior,pnext,pinner;
    for(int i=0;i<s;i++){
      iprior=i-1;
      if(iprior==-1)iprior=s-1;
      inext=i+1;
      if(inext==s)
      inext=0;
      p=outerpolygon.get(i);
      pprior=outerpolygon.get(iprior);
      pnext=outerpolygon.get(inext);
      pinner=getInnerPoint(pprior,p,pnext,clockwise);
      innerpolygon.add(pinner);}
    return innerpolygon;}
  
  static final double DESIREDINWARDDISTANCE=12;
  
  private DPoint getInnerPoint(DPoint p0,DPoint p1,DPoint p2,boolean clockwise){
    double angle,dir;
    if(clockwise){
      angle=GD.getAngle_3Points(p0.x,p0.y,p1.x,p1.y,p2.x,p2.y);
      dir=GD.getDirection_3Points(p0.x,p0.y,p1.x,p1.y,p2.x,p2.y);
    }else{  
      angle=GD.getAngle_3Points(p2.x,p2.y,p1.x,p1.y,p0.x,p0.y);
      dir=GD.getDirection_3Points(p2.x,p2.y,p1.x,p1.y,p0.x,p0.y);}
    if(angle>GD.PI)
      angle=GD.PI2-angle;
    double dis=DESIREDINWARDDISTANCE/(GD.sin(angle/2));
    DPoint innerpoint=p1.getPoint(dir,dis);
    return innerpoint;}
  
  
}
