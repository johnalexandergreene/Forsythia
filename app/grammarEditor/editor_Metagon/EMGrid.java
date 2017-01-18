package org.fleen.forsythia.app.grammarEditor.editor_Metagon;

import java.awt.Graphics2D;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.util.grid.Grid;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;

@SuppressWarnings("serial")
public class EMGrid extends Grid{
  
  private EMGridOverlayPainter overlaypainter=new EMGridOverlayPainter();

  protected void paintOverlay(Graphics2D g,int w,int h,
    double scale,double centerx,double centery){
    try{
      overlaypainter.paint(
        g,
        w,
        h,
        scale,
        centerx,
        centery);
     }catch(Exception x){}}
  
  protected KPolygon getHostPolygon(){
    return GE.ge.editor_metagon.workingpolygon;}

  protected void mouseTouched(double[] p,KVertex v){
    GE.ge.editor_metagon.touchVertex(v);}

  protected void mouseMovedCloseToVertex(KVertex v){}

  protected void mouseMovedFarFromVertex(double[] p){}

}
