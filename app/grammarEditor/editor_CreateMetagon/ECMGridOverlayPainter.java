package org.fleen.forsythia.app.grammarEditor.editor_CreateMetagon;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.util.UI;
import org.fleen.forsythia.app.grammarEditor.util.grid.UIGridUtil;

public class ECMGridOverlayPainter{
  
  void paint(Graphics2D graphics,int w,int h,double scale,double centerx,double centery){
    graphics.setRenderingHints(UI.RENDERING_HINTS);
    if(GE.editor_createmetagon.workingpolygon.isEmpty())return;
    List<double[]> points=UIGridUtil.convertGridVerticesToViewPoints(GE.editor_createmetagon.workingpolygon,w,h,scale,centerx,centery);
    if(GE.editor_createmetagon.workingpolygon.finished)
      renderFinishedPolygon(graphics,points);  
    else if(points.size()>1)
      renderOpenVertexSequence(graphics,points);
    else
      renderSingleVertex(graphics,points);}
  
  private void renderFinishedPolygon(Graphics2D graphics,List<double[]> points){
    Path2D path=getPathForPolygon(points);
    //fill
    graphics.setPaint(UI.EDITORCREATEMETAGON_FINISHEDMETAGONFILLCOLOR);
    graphics.fill(path);
    //draw
    graphics.setStroke(UI.GRID_DRAWINGSTROKE);
    graphics.setPaint(UI.GRID_DRAWINGSTROKECOLOR);
    graphics.draw(path);
    //vertices
    int span=UI.GRID_DEFAULTVERTEXSPAN;
    for(double[] p:points)  
      graphics.fillOval(((int)p[0])-span/2,((int)p[1])-span/2,span,span);}
  
  private void renderOpenVertexSequence(Graphics2D graphics,List<double[]> points){
    graphics.setPaint(UI.GRID_DRAWINGSTROKECOLOR);
    Path2D path=getPathForOpenVertexSequence(points);
    //stroke
    graphics.setStroke(UI.GRID_DRAWINGSTROKE);
    graphics.draw(path);
    //vertices
    int 
      s=points.size(),
      span=UI.GRID_DEFAULTVERTEXSPAN;
    double[] p;
    for(int i=0;i<s;i++){  
      p=points.get(i);
      graphics.fillOval(((int)p[0])-span/2,((int)p[1])-span/2,span,span);}}
  
  private void renderSingleVertex(Graphics2D graphics,List<double[]> points){
    int span=UI.GRID_DEFAULTVERTEXSPAN;
    graphics.setPaint(UI.GRID_DRAWINGSTROKECOLOR);
    double[] p=points.get(0);
    graphics.fillOval(((int)p[0])-span/2,((int)p[1])-span/2,span,span);}
  
  //for vertex sequence
  //pointcount guaranteed>=2
  private Path2D getPathForOpenVertexSequence(List<double[]> points){
    int pointcount=points.size();
    Path2D.Double path=new Path2D.Double();
    double[] p=points.get(0);
    path.moveTo(p[0],p[1]);
    for(int i=1;i<pointcount;i++){
      p=points.get(i);
      path.lineTo(p[0],p[1]);}
    return path;}
  
  //for complete polygon
  //pointcount guaranteed >=3
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
