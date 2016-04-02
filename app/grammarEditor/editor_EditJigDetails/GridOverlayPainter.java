package org.fleen.forsythia.app.grammarEditor.editor_EditJigDetails;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.util.UI;
import org.fleen.forsythia.app.grammarEditor.util.textures.Texture;
import org.fleen.geom_2D.DCircle;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.GD;
import org.fleen.geom_Kisrhombille.KVertex;

/*
 * fill sections
 *   color sections by chorus index
 *   paint fill texture by type
 *   render anchor glyphs
 *   render type and chorus index glyphs
 * stroke sections
 *   focus is white, unfocus is black (or whatever colors)
 * render vertices
 *   focus is white, unfocus is black
 */
public class GridOverlayPainter{
  
  void paint(Graphics2D graphics,int w,int h,double scale,double centerx,double centery){
    graphics.setRenderingHints(UI.RENDERING_HINTS);
    JigDetailsModel model=GE.editor_editjigdetails.model;
    fillSections(graphics,model);
    strokeSections(graphics,model);}
  
  /*
   * ################################
   * FILL SECTIONS
   * ################################
   */
  
  private void fillSections(Graphics2D graphics,JigDetailsModel model){
    Path2D path;
    Color color;
    Texture[] textures=Texture.getTextures();
    for(JigSectionDetailsModel m:model.getSectionModels()){
      path=m.getPolygonPath();
      //fill by chorus index
      color=UI.EJD_SECTIONFILLCHORUSINDICES[m.productchorusindex%UI.EJD_SECTIONFILLCHORUSINDICES.length];
      graphics.setPaint(color);
      graphics.fill(path);
      //fill with texture by section type
      textures[m.producttype%textures.length].apply(graphics,path,UI.EJD_TEXTUREALPHA);
      //render anchor glyphs
      renderAnchorGlyphs(graphics,m);
      //render control icons
      if(m.isFocus())
        renderTCCircles(graphics,m);}}
  
  private void renderTCCircles(Graphics2D g,JigSectionDetailsModel sm){
    JigSectionDetailsModelTCIconCircles cic=sm.getTCIconCircles();
    if(cic.failed())return;
    //product type control circle
    g.setPaint(Color.red);
    DCircle c=cic.getCircle_ProductType(); 
    Ellipse2D.Double e=new Ellipse2D.Double(
      c.x-c.r,
      c.y-c.r,
      2*c.r,
      2*c.r);
    g.fill(e);
    g.setPaint(Color.black);
    g.setFont(new Font(null,Font.BOLD,24));
    g.drawString(sm.getTypeIDString(),(float)c.x-12,(float)c.y+12);
    //do chorus index control circle
    g.setPaint(Color.blue);
    c=cic.getCircle_ChorusIndex(); 
    e=new Ellipse2D.Double(
      c.x-c.r,
      c.y-c.r,
      2*c.r,
      2*c.r);
    g.fill(e);
    g.setPaint(Color.black);
    g.setFont(new Font(null,Font.BOLD,24));
    g.drawString(String.valueOf(sm.productchorusindex),(float)c.x-12,(float)c.y+12);}
  
  static final double VERTEXICONSPAN=12;
  
  /*
   * if the section is focus 
   *   put icons by all vertices, distinguish v0 by color
   *   draw twist arrows
   * if the section is nonfocus
   *   put icon by v0
   *   draw twist arrows
   */
  private void renderAnchorGlyphs(Graphics2D graphics,JigSectionDetailsModel model){
    JigSectionDetailsModelVertexIconCircles vic=model.getVertexIconCircles();
    KVertex v0=model.getV0();
    //always render the v0
    renderVertexIconCircle(graphics,vic.getCircle(v0),true);
    //if the section is focus then render the other v0 options as well
    Set<KVertex> v0options=model.getV0Options();
    v0options.remove(v0);
    if(model.isFocus())
      for(KVertex v:v0options)
        renderVertexIconCircle(graphics,vic.getCircle(v),false);
    //render twist arrows
    renderTwistArrows(graphics,vic.getCircles(),Color.black,model);}
  
  private void renderVertexIconCircle(Graphics2D g,DCircle c,boolean fill){
    g.setPaint(Color.black);
    Ellipse2D e=new Ellipse2D.Double(
      c.x-c.r,
      c.y-c.r,
      2*c.r,
      2*c.r);
    if(fill){
      g.fill(e);
    }else{
      g.setStroke(UI.GRID_DRAWINGSTROKE);
      g.draw(e);}}
  
  private void renderTwistArrows(Graphics2D g,List<DCircle> vertexiconcircles,Color color,JigSectionDetailsModel model){
    int inext,s=vertexiconcircles.size();
    double[] p0,p1;
    for(int i=0;i<s;i++){
      inext=i+1;
      if(inext==s)inext=0;
      p0=vertexiconcircles.get(i).getCenter();
      p1=vertexiconcircles.get(inext).getCenter();
      renderTwistArrow(p0,p1,g);}}
  
//  private void renderTwistArrows(Graphics2D graphics,List<Circle2D> vertexiconcircles,Color color,JigSectionDetailsModel model){
//    boolean isclockwise=model.isClockwise();
//    int inext,s=vertexiconcircles.size();
//    double[] p0,p1;
//    if(isclockwise){
//      for(int i=0;i<s;i++){
//        inext=i+1;
//        if(inext==s)inext=0;
//        p0=vertexiconcircles.get(i).getCenter();
//        p1=vertexiconcircles.get(inext).getCenter();
//        renderTwistArrow(p0,p1,graphics);}
//    }else{
//      for(int i=0;i<s;i++){
//        inext=i+1;
//        if(inext==s)inext=0;
//        p1=vertexiconcircles.get(i).getCenter();
//        p0=vertexiconcircles.get(inext).getCenter();
//        renderTwistArrow(p0,p1,graphics);}}}
  
  private static final double 
    TWISTARROWPAD=20,//distance at ends between the sequence and the vertex points
    TWISTARROWLENGTH=30,
    TWISTARROWBLADELENGTH=10,
    TWISTARROWSEQMINDIST=TWISTARROWLENGTH+TWISTARROWPAD*2;
  
  /*
   * draw an arrow indicating direction of index+ traversal
   */
  private void renderTwistArrow(double[] p0,double[] p1,Graphics2D g){
    double disp0p1=GD.getDistance_PointPoint(p0[0],p0[1],p1[0],p1[1]);
    if(disp0p1<TWISTARROWSEQMINDIST)return;
    double dirp0p1=GD.getDirection_PointPoint(p0[0],p0[1],p1[0],p1[1]);
    double dis0=(disp0p1-TWISTARROWLENGTH)/2;
    double[] 
      //tail and head of arrow
      parrowtail=GD.getPoint_PointDirectionInterval(p0[0],p0[1],dirp0p1,dis0),
      parrowhead=GD.getPoint_PointDirectionInterval(parrowtail[0],parrowtail[1],dirp0p1,TWISTARROWLENGTH);
    double 
      dirp0p1reverese=GD.normalizeDirection(dirp0p1+GD.PI),
      darrowblade0=GD.normalizeDirection(dirp0p1reverese+GD.PI/4),
      darrowblade1=GD.normalizeDirection(dirp0p1reverese-GD.PI/4);
    double[] 
      parrowblade0=GD.getPoint_PointDirectionInterval(parrowhead[0],parrowhead[1],darrowblade0,TWISTARROWBLADELENGTH),
      parrowblade1=GD.getPoint_PointDirectionInterval(parrowhead[0],parrowhead[1],darrowblade1,TWISTARROWBLADELENGTH);
    //draw head
    g.setStroke(UI.GRID_DRAWINGSTROKE);
    g.setPaint(Color.black);
    Path2D path=new Path2D.Double();
    path.moveTo(parrowblade0[0],parrowblade0[1]);
    path.lineTo(parrowhead[0],parrowhead[1]);
    path.lineTo(parrowblade1[0],parrowblade1[1]);
    g.draw(path);
    //draw body
    path.reset();
    path.moveTo(parrowtail[0],parrowtail[1]);
    path.lineTo(parrowhead[0],parrowhead[1]);
    g.draw(path);}
  
  
  /*
   * ################################
   * STROKE SECTIONS
   * ################################
   */
  
  private void strokeSections(Graphics2D g,JigDetailsModel model){
    //do lines
    g.setStroke(UI.GRID_DRAWINGSTROKE);
    FocusableModelElement focus=model.getFocusElement();
    for(FocusableModelElement e:model.getSectionModels())
      if(e!=focus)
        strokePolygon(g,e,Color.black);
    strokePolygon(g,focus,Color.white);
    //do vertices
    List<KVertex> 
      focusvertices=new ArrayList<KVertex>(focus.getKPolygon()),
      nonfocusvertices=new ArrayList<KVertex>();
    for(FocusableModelElement e:model.getSectionModels())
      nonfocusvertices.addAll(e.getKPolygon());
    nonfocusvertices.removeAll(focusvertices);
    List<DPoint> 
      focuspoints=GE.editor_editjigdetails.getGrid().getGeometryCache().getPoint2Ds(focusvertices),
      nonfocuspoints=GE.editor_editjigdetails.getGrid().getGeometryCache().getPoint2Ds(nonfocusvertices);
    renderVertices(g,focuspoints,Color.white);
    renderVertices(g,nonfocuspoints,Color.black);}
  
  private void strokePolygon(Graphics2D g,FocusableModelElement e,Color c){
    g.setPaint(c);
    Path2D path=e.getPolygonPath();
    g.draw(path);}
  
  private void renderVertices(Graphics2D g,List<DPoint> points,Color color){
    g.setPaint(color);
    Ellipse2D.Double e;
    double r=UI.GRID_DEFAULTVERTEXSPAN/2;
    for(DPoint p:points){
      e=new Ellipse2D.Double(p.x-r,p.y-r,2*r,2*r);
      g.fill(e);}}
  
}
