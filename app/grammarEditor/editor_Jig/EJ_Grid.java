package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import java.awt.Graphics2D;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.util.grid.Grid;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;

@SuppressWarnings("serial")
public class EJ_Grid extends Grid{
  public EJ_Grid() {
  }
  
  GridOverlayPainter overlaypainter=new GridOverlayPainter();

  protected void paintOverlay(Graphics2D g,int w,int h,double scale,double centerx,double centery){
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
    return GE.editor_jig.getHostPolygon();}

  protected void mouseTouched(double[] p,KVertex v){
    GE.editor_jig.touchGrid(p,v);}

  protected void mouseMovedCloseToVertex(KVertex v){//TODO highlight vertex
    GE.editor_jig.initMouseMode_TouchVertex();}

  protected void mouseMovedFarFromVertex(double[] p){
    GE.editor_jig.initMouseMode_TouchSection();}
  
}
