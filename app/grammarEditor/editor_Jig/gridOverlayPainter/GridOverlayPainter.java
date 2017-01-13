package org.fleen.forsythia.app.grammarEditor.editor_Jig.gridOverlayPainter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.Iterator;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.Editor_Jig;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.JigSectionEditingModel;
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
  
  //TODO thse should be params in UI
  private static final double GRIDOVERLAYPAINTER_GLYPHINSET=12;
  
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
    GlyphSystemModel glyphsystemmodel=new GlyphSystemModel(
      focuspolygon,
      GRIDOVERLAYPAINTER_GLYPHINSET);
    if(glyphsystemmodel.isValid())
      renderGlyphs(graphics,glyphsystemmodel);}
  
  private void renderGlyphs(Graphics2D graphics,GlyphSystemModel glyphsystemmodel){
    //render v0 dot
    renderV0Dot(graphics,glyphsystemmodel);
    //render arrow shaft
    graphics.setStroke(UI.GRID_DRAWINGSTROKE);
    graphics.setPaint(UI.EDITORCREATEJIG_EDITSECTIONSGLYPHSTROKECOLOR);
    Path2D path =getPath2D(glyphsystemmodel.glyphpath);
    graphics.draw(path);
    //render arrow head
    renderArrowHead(graphics,glyphsystemmodel);}
    
  private static final double V0DOTRADIUS=0.6;
  
  private void renderV0Dot(Graphics2D graphics,GlyphSystemModel glyphsystemmodel){
    double dotradius=V0DOTRADIUS*GRIDOVERLAYPAINTER_GLYPHINSET;
    DPoint pv0=glyphsystemmodel.getV0DotPoint();
    graphics.setPaint(UI.EDITORCREATEJIG_EDITSECTIONSGLYPHSTROKECOLOR);
    Ellipse2D dot=new Ellipse2D.Double(pv0.x-dotradius,pv0.y-dotradius,dotradius*2,dotradius*2);
    graphics.fill(dot);}
  
  //in terms of inset
  private static final double ARROWLENGTH=2.5,ARROWWIDTH=1.2;
  
  private void renderArrowHead(Graphics2D graphics,GlyphSystemModel glyphsystemmodel){
    graphics.setPaint(UI.EDITORCREATEJIG_EDITSECTIONSGLYPHSTROKECOLOR);
    DPoint 
      p0=glyphsystemmodel.glyphpath.get(glyphsystemmodel.glyphpath.size()-2),
      p1=glyphsystemmodel.glyphpath.get(glyphsystemmodel.glyphpath.size()-1);
    double forward=p0.getDirection(p1);
    DPoint 
      forewardpoint=p1.getPoint(forward,ARROWLENGTH*GRIDOVERLAYPAINTER_GLYPHINSET),
      leftpoint=p1.getPoint(GD.normalizeDirection(forward-GD.HALFPI),ARROWWIDTH*GRIDOVERLAYPAINTER_GLYPHINSET/2),
      rightpoint=p1.getPoint(GD.normalizeDirection(forward+GD.HALFPI),ARROWWIDTH*GRIDOVERLAYPAINTER_GLYPHINSET/2);
    Path2D triangle=new Path2D.Double();
    triangle.moveTo(leftpoint.x,leftpoint.y);
    triangle.lineTo(forewardpoint.x,forewardpoint.y);
    triangle.lineTo(rightpoint.x,rightpoint.y);
    triangle.closePath();
    graphics.fill(triangle);}
  
  private Path2D getPath2D(List<DPoint> points){
    Path2D path2d=new Path2D.Double();
    DPoint p=points.get(0);
    path2d.moveTo(p.x,p.y);
    int s=points.size();
    for(int i=1;i<s;i++){
      p=points.get(i);
      path2d.lineTo(p.x,p.y);}
    return path2d;}
  

  
  
}
