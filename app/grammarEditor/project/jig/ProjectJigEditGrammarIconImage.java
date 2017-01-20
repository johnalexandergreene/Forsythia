package org.fleen.forsythia.app.grammarEditor.project.jig;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.util.UI;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_Kisrhombille.KMetagon;
import org.fleen.geom_Kisrhombille.KPolygon;

/*
 * a large square icon image rendering a project jig 
 * TODO if the jig is invalid then show it. render all the strokes in red or something.
 */
class ProjectJigEditGrammarIconImage extends BufferedImage{
  
  ProjectJigEditGrammarIconImage(ProjectJig pj,int span){
    super(span,span,BufferedImage.TYPE_INT_RGB);
    //init graphics
    Graphics2D g=createGraphics();
    g.setRenderingHints(UI.RENDERING_HINTS);
    //fill background
    g.setColor(UI.ELEMENTMENU_ICONBACKGROUND);
    g.fillRect(0,0,span,span);
    //glean metrics and transform
    DPolygon hostmetagonpoints=getHostMPPoints(pj);
    Path2D.Double hostmetagonpath=UI.getClosedPath(hostmetagonpoints);
    Rectangle2D bounds=hostmetagonpath.getBounds2D();
    double bw=bounds.getWidth(),bh=bounds.getHeight(),scale;
    int maxpolygonimagespan=span-(UI.ELEMENTMENU_ICONGEOMETRYINSET*2);
    scale=(bw>bh)?maxpolygonimagespan/bw:maxpolygonimagespan/bh;
    AffineTransform t=new AffineTransform();
    t.scale(scale,-scale);//note y flip
    double 
      xoffset=-bounds.getMinX()+(((span-(bw*scale))/2)/scale),
      yoffset=-bounds.getMaxY()-(((span-(bh*scale))/2)/scale);
    t.translate(xoffset,yoffset);
    g.transform(t);
    //render host metagon area
    g.setPaint(UI.ELEMENTMENU_ICON_FILL);
    g.fill(hostmetagonpath);
    //render section strokes
    BasicStroke stroke=new BasicStroke(
      (float)(UI.ELEMENTMENU_ICONPATHSTROKETHICKNESS/scale),
      BasicStroke.CAP_SQUARE,
      BasicStroke.JOIN_ROUND,
      0,null,0);
    g.setStroke(stroke);
    renderSections(g,pj);}

  //host metagon polygon points
  //for overall icon geometry
  private DPolygon getHostMPPoints(ProjectJig j){
    KMetagon m=GE.ge.focusgrammar.getMetagon(j).kmetagon;
    KPolygon p=m.getScaledPolygon(j.getGridDensity());
    return p.getDefaultPolygon2D();}
  
  /*
   * ################################
   * RENDER SECTIONS
   * render polygon path
   * render section index with fill color
   * ################################
   */
  
  void renderSections(Graphics2D g,ProjectJig pj){
    //polygon jig sections
    KPolygon p;
    DPolygon points;
    for(ProjectJigSection section:pj.sections){
      p=section.metagon.kmetagon.getPolygon(section.getAnchor());
      points=p.getDefaultPolygon2D();
      renderSectionPolygon(g,points);}}
  
  private void renderSectionPolygon(Graphics2D g,DPolygon points){
    Path2D.Double path=UI.getClosedPath(points);
    g.setPaint(UI.ELEMENTMENU_ICON_STROKE);
    g.draw(path);}
  
//  private Path2D createPath(KPolygon kp){
//    int pointcount=kp.size();
//    Path2D.Double path=new Path2D.Double();
//    //we set this to be uniform with multiedge path winding rule (yard) (see below)
//    //maybe unnecessary, but don't mess with it.
//    path.setWindingRule(Path2D.WIND_EVEN_ODD);
//    DPolygon points=kp.getDefaultPolygon2D();
//    DPoint p=points.get(0);
//    path.moveTo(p.x,p.y);
//    for(int i=1;i<pointcount;i++){
//      p=points.get(i);
//      path.lineTo(p.x,p.y);}
//    path.closePath();
//    return path;} 
  
}
