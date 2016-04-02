package org.fleen.forsythia.app.grammarEditor.editor_ViewMetagonEditTags;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.util.UI;
import org.fleen.forsythia.app.grammarEditor.util.grid.UIGridUtil;

/*
 * while polygon is open
 *   draw white line with white vertices
 *   v0 is big, vlast is triangle arrowhead
 *   prompt for discard
 * when the polygon is closed
 *   draw in black with black vertices
 *   fill with translucent white
 *   prompt for save/discard 
 */
public class EVMETOverlayPainter{
  
  void paint(Graphics2D graphics,int w,int h,double scale,double centerx,double centery){
    graphics.setRenderingHints(UI.RENDERING_HINTS);
    List<double[]> points=UIGridUtil.convertGridVerticesToViewPoints(GE.focusmetagon.kpolygon,w,h,scale,centerx,centery);
    renderPolygon(graphics,points);}
  
  private void renderPolygon(Graphics2D graphics,List<double[]> points){
    Path2D path=getPathForPolygon(points);
    //fill
    graphics.setPaint(UI.EDITORCREATEMETAGON_FINISHEDMETAGONFILLCOLOR);
    graphics.fill(path);
    //stroke
    graphics.setStroke(UI.GRID_DRAWINGSTROKE);
    graphics.setPaint(UI.GRID_DRAWINGSTROKECOLOR);
    graphics.draw(path);
    //vertices
    int span=UI.GRID_DEFAULTVERTEXSPAN;
    for(double[] p:points)  
      graphics.fillOval(((int)p[0])-span/2,((int)p[1])-span/2,span,span);}
  
  private Path2D getPathForPolygon(List<double[]> points){
    int pointcount=points.size();
    Path2D.Double path=new Path2D.Double();
    double[] p=points.get(0);
    path.moveTo(p[0],p[1]);
    for(int i=1;i<pointcount;i++){
      p=points.get(i);
      path.lineTo(p[0],p[1]);}
    path.closePath();
    return path;}

}
