package org.fleen.forsythia.app.bread;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.List;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.geom_2D.DPoint;

public class VG_Fill implements Voice_Graphics2D{

  public void paint(FPolygon polygon,Graphics2D graphics){
    Path2D path=getPath(polygon);
    graphics.setPaint(Color.yellow);
    graphics.fill(path);}
  
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
