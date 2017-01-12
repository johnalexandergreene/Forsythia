package org.fleen.forsythia.app.grammarEditor.editor_Jig.gridOverlayPainter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
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
  private static final double 
    GRIDOVERLAYPAINTER_GLYPHINSET=12,
    GRIDOVERLAYPAINTER_GLYPHGAP=20;
  //in terms of gap
  private static final int 
    GRIDOVERLAYPAINTER_GLYPHIDEALARROWLENGTH=4;
  
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
      GRIDOVERLAYPAINTER_GLYPHINSET,
      GRIDOVERLAYPAINTER_GLYPHGAP,
      GRIDOVERLAYPAINTER_GLYPHIDEALARROWLENGTH);
    renderGlyphs(graphics,glyphsystemmodel);}
  
  private void renderGlyphs(Graphics2D graphics,GlyphSystemModel glyphsystemmodel){
    testFoo(graphics,glyphsystemmodel);
    
  }
  
  private void testFoo(Graphics2D graphics,GlyphSystemModel glyphsystemmodel){
    //TEST
    
    graphics.setStroke(UI.GRID_DRAWINGSTROKE);
    graphics.setPaint(UI.EDITORCREATEJIG_EDITSECTIONSGLYPHSTROKECOLOR);
    Path2D path=getPath2D(glyphsystemmodel.pathmodel);
    graphics.draw(path);
    Ellipse2D dot;
    for(GlyphPathModelPoint p:glyphsystemmodel.pathmodel){
      //
      if(p.type==GlyphPathModel.PTYPE_FIRST)
        graphics.setPaint(Color.red);
      else if(p.type==GlyphPathModel.PTYPE_CORNER)
        graphics.setPaint(Color.blue);
      else if(p.type==GlyphPathModel.PTYPE_RETICULATION)
        graphics.setPaint(Color.green);
      else if(p.type==GlyphPathModel.PTYPE_LAST)
        graphics.setPaint(Color.magenta);
      else
        graphics.setPaint(Color.gray);
      
      if(p.arrowstart||p.arrowend)
        graphics.setPaint(Color.black);
      
      //
      dot=new Ellipse2D.Double(p.x-5,p.y-5,10,10);
      graphics.fill(dot);}}
  
  private Path2D getPath2D(GlyphPathModel pathmodel){
    Path2D path2d=new Path2D.Double();
    GlyphPathModelPoint p=pathmodel.get(0);
    path2d.moveTo(p.x,p.y);
    int s=pathmodel.size();
    for(int i=1;i<s;i++){
      p=pathmodel.get(i);
      path2d.lineTo(p.x,p.y);}
    return path2d;}
  

  
  
}
