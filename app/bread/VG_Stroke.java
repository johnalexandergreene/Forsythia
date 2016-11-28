package org.fleen.forsythia.app.bread;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.util.List;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.geom_2D.DPoint;

public class VG_Stroke implements Voice_Graphics2D{

  public void paint(FPolygon polygon,Graphics2D graphics,double time){
    Path2D path=getPath(polygon);
    Stroke stroke=new BasicStroke(0.012f,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,0,null,0);
    graphics.setStroke(stroke);
    graphics.setPaint(Color.black);
    graphics.draw(path);}
  
  private Path2D getPath(FPolygon polygon){
    Path2D.Double path=new Path2D.Double();
    List<DPoint> points=polygon.getDPolygon();
    DPoint p=points.get(0);
    path.moveTo(p.x,p.y);
    for(int i=1;i<points.size();i++){
      p=points.get(i);
      path.lineTo(p.x,p.y);}
    path.closePath();
    return path;}

}
