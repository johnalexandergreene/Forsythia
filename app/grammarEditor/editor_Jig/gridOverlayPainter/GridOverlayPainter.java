package org.fleen.forsythia.app.grammarEditor.editor_Jig.gridOverlayPainter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.Editor_Jig;
import org.fleen.forsythia.app.grammarEditor.project.jig.ProjectJigSection;
import org.fleen.forsythia.app.grammarEditor.util.UI;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.GD;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.graph.GEdge;
import org.fleen.geom_Kisrhombille.graph.GVertex;

public class GridOverlayPainter implements Serializable{
  
  private static final long serialVersionUID=5607779324837953808L;

  public void paint(Graphics2D graphics,int w,int h,double scale,double centerx,double centery){
    graphics.setRenderingHints(UI.RENDERING_HINTS);
    GE.ge.editor_jig.editedjig.getJigEditorGeometryCache().update(w,h,scale,centerx,centery);
    if(GE.ge.editor_jig.mode==Editor_Jig.MODE_CREATE_B||GE.ge.editor_jig.mode==Editor_Jig.MODE_RETOUCH){
      renderJigModel_EditSections(graphics);
    }else{//GE.editor_jig.mode==Editor_Jig.MODE_EDITGEOMETRY
      renderJigModel_EditGeometry(graphics);}}
  
  /*
   * ################################
   * RENDER JIG MODEL FOR EDIT GEOMETRY MODE
   * ################################
   */
  
  private void renderJigModel_EditGeometry(Graphics2D graphics){
    try{
      fillSections_EditGeometry(graphics);
      strokeGraphEdges_EditGeometry(graphics);
      renderVertices_EditGeometry(graphics);
    }catch(Exception x){
      x.printStackTrace();}}
  
  private void fillSections_EditGeometry(Graphics2D graphics){
    Color color;
    Path2D path;
    for(KPolygon m:GE.ge.editor_jig.editedjig.getGraph().getDisconnectedGraph().getUndividedPolygons()){
      color=UI.EDITJIG_EDITGEOMETRY_HOSTMETAGONFILLCOLOR;
      path=GE.ge.editor_jig.editedjig.getJigEditorGeometryCache().getPath(m);
      graphics.setPaint(color);
      graphics.fill(path);}}
  
  private void strokeGraphEdges_EditGeometry(Graphics2D graphics){
    graphics.setStroke(UI.GRID_DRAWINGSTROKE);
    graphics.setPaint(UI.EDITJIG_EDITGEOMETRY_STROKECOLOR);
    Iterator<GEdge> i=GE.ge.editor_jig.editedjig.getGraph().edges.iterator();
    GEdge e;
    double[] p0,p1;
    Path2D path=new Path2D.Double();
    while(i.hasNext()){
      e=i.next();
      p0=GE.ge.editor_jig.editedjig.getJigEditorGeometryCache().getPoint(e.v0.kvertex);
      p1=GE.ge.editor_jig.editedjig.getJigEditorGeometryCache().getPoint(e.v1.kvertex);
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
    for(GVertex v:GE.ge.editor_jig.editedjig.getGraph().vertices){
      p=GE.ge.editor_jig.editedjig.getJigEditorGeometryCache().getPoint(v.kvertex);
      dot.setFrame(p[0]-span/2,p[1]-span/2,span,span);
      graphics.fill(dot);}}
  
  private void renderHeadDecorations(Graphics2D graphics){
    graphics.setStroke(UI.EDITJIG_EDITGEOMETRY_HEADVERTEXDECORATIONSTROKE);
    int span=UI.EDITJIG_EDITGEOMETRY_HEADVERTEXDECORATIONSPAN;
    double[] p;
    if(GE.ge.editor_jig.connectedhead!=null){
      p=GE.ge.editor_jig.editedjig.getJigEditorGeometryCache().getPoint(GE.ge.editor_jig.connectedhead);
      graphics.setPaint(UI.EDITJIG_EDITGEOMETRY_CONNECTEDHEADVERTEXDECORATIONCOLOR);
      graphics.drawOval(((int)p[0])-span/2,((int)p[1])-span/2,span,span);
    }else if(GE.ge.editor_jig.unconnectedhead!=null){
      p=GE.ge.editor_jig.editedjig.getJigEditorGeometryCache().getPoint(GE.ge.editor_jig.unconnectedhead);
      graphics.setPaint(UI.EDITJIG_EDITGEOMETRY_UNCONNECTEDHEADVERTEXDECORATIONCOLOR);
      graphics.drawOval(((int)p[0])-span/2,((int)p[1])-span/2,span,span);}}
  
  /*
   * ################################
   * RENDER JIG MODEL FOR EDIT SECTIONS MODE
   * ################################
   */
  
  private void renderJigModel_EditSections(Graphics2D graphics){
    fillSections_EditSections(graphics);
    strokePolygonEdges_EditSections(graphics);
    renderGlyphs_EditSections(graphics);}
  
  /*
   * Fill color reflects chorus index. A rainbow.
   * 0 is red, 1 is orangyred and so on 
   */
  private void fillSections_EditSections(Graphics2D graphics){
    int colorindex;
    Color color;
    Path2D path;
    for(ProjectJigSection m:GE.ge.editor_jig.editedjig.sections){
      colorindex=m.chorusindex;
      color=UI.EDITJIG_EDITSECTIONS_SECTIONFILL[colorindex%UI.EDITJIG_EDITSECTIONS_SECTIONFILL.length];
      path=GE.ge.editor_jig.editedjig.getJigEditorGeometryCache().getPath(m.getPolygon());
      graphics.setPaint(color);
      graphics.fill(path);}}
  
  /*
   * focus section gets stroked in one color, all of the unfocus sections get stroked in another
   * colors probably match for glyph
   */
  private void strokePolygonEdges_EditSections(Graphics2D graphics){
    graphics.setStroke(UI.GRID_DRAWINGSTROKE);
    graphics.setPaint(UI.EDITJIG_EDITSECTIONS_UNFOCUSSTROKECOLOR);
    Path2D path;
    for(ProjectJigSection m:GE.ge.editor_jig.editedjig.sections){
      if(m==GE.ge.editor_jig.focussection)continue;
      path=GE.ge.editor_jig.editedjig.getJigEditorGeometryCache().getPath(m.getPolygon());
      graphics.draw(path);}
    //focus
    graphics.setPaint(UI.EDITJIG_EDITSECTIONS_FOCUSSTROKECOLOR);
    path=GE.ge.editor_jig.editedjig.getJigEditorGeometryCache().getPath(GE.ge.editor_jig.focussection.getPolygon());
    graphics.draw(path);}
  
  /*
   * ################################
   * RENDER GLYPHS FOR EDIT SECTIONS
   * The focus polygon gets a system of glyphs within its edge that indicate
   * the form of the anchor. That is, v0 and twist.
   * we indicate vertex0 with a dot
   * we indicate twist with a long bendy arrow 
   * ################################
   */
  
  private void renderGlyphs_EditSections(Graphics2D graphics){
    //get non-focus section polygons
    List<DPolygon> nonfocussections=new ArrayList<DPolygon>();
    for(ProjectJigSection section:GE.ge.editor_jig.editedjig.sections)
      if(section!=GE.ge.editor_jig.focussection)
        nonfocussections.add(GE.ge.editor_jig.editedjig.getJigEditorGeometryCache().getDPolygon(section.getPolygon()));
    //get focus section polygon
    DPolygon focussection=GE.ge.editor_jig.editedjig.getJigEditorGeometryCache().getDPolygon(GE.ge.editor_jig.focussection.getPolygon());
    //render non-focus section polygons
    for(DPolygon nonfocussection:nonfocussections)
      renderGlyphs(graphics,nonfocussection,UI.EDITJIG_EDITSECTIONS_UNFOCUSSTROKECOLOR);
    //render focus section polygon
    renderGlyphs(graphics,focussection,UI.EDITJIG_EDITSECTIONS_FOCUSSTROKECOLOR);}
  
  private void renderGlyphs(Graphics2D graphics,DPolygon polygon,Color color){
    GlyphSystemModel glyphsystemmodel=new GlyphSystemModel(
      polygon,
      UI.EDITJIG_EDITSECTIONS_GLYPHINSET);
    if(glyphsystemmodel.isValid()){
      //render v0 dot
      renderV0Dot(graphics,glyphsystemmodel,color);
      //render arrow shaft
      graphics.setStroke(UI.GRID_DRAWINGSTROKE);
      graphics.setPaint(color);
      Path2D path =getPath2D(glyphsystemmodel.glyphpath);
      graphics.draw(path);
      //render arrow head
      renderArrowHead(graphics,glyphsystemmodel,color);}}
    
  /*
   * ++++++++++++++++++++++++++++++++
   * RENDER V0 DOT
   * ++++++++++++++++++++++++++++++++
   */
  
  private void renderV0Dot(Graphics2D graphics,GlyphSystemModel glyphsystemmodel,Color color){
    double dotradius=UI.EDITJIG_EDITSECTIONS_GLYPHV0DOTRADIUS*UI.EDITJIG_EDITSECTIONS_GLYPHINSET;
    DPoint pv0=glyphsystemmodel.getV0DotPoint();
    graphics.setPaint(color);
    Ellipse2D dot=new Ellipse2D.Double(pv0.x-dotradius,pv0.y-dotradius,dotradius*2,dotradius*2);
    graphics.fill(dot);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * RENDER ARROW HEAD
   * ++++++++++++++++++++++++++++++++
   */
  
  private void renderArrowHead(Graphics2D graphics,GlyphSystemModel glyphsystemmodel,Color color){
    graphics.setPaint(color);
    DPoint 
      p0=glyphsystemmodel.glyphpath.get(glyphsystemmodel.glyphpath.size()-2),
      p1=glyphsystemmodel.glyphpath.get(glyphsystemmodel.glyphpath.size()-1);
    double forward=p0.getDirection(p1);
    DPoint 
      forewardpoint=p1.getPoint(
        forward,
        UI.EDITJIG_EDITSECTIONS_GLYPHARROWLENGTH*UI.EDITJIG_EDITSECTIONS_GLYPHINSET),
      leftpoint=p1.getPoint(
        GD.normalizeDirection(forward-GD.HALFPI),
        UI.EDITJIG_EDITSECTIONS_GLYPHARROWWIDTH*UI.EDITJIG_EDITSECTIONS_GLYPHINSET/2),
      rightpoint=p1.getPoint(
        GD.normalizeDirection(forward+GD.HALFPI),
        UI.EDITJIG_EDITSECTIONS_GLYPHARROWWIDTH*UI.EDITJIG_EDITSECTIONS_GLYPHINSET/2);
    Path2D triangle=new Path2D.Double();
    triangle.moveTo(leftpoint.x,leftpoint.y);
    triangle.lineTo(forewardpoint.x,forewardpoint.y);
    triangle.lineTo(rightpoint.x,rightpoint.y);
    triangle.closePath();
    graphics.fill(triangle);}
  
  //an open path
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
