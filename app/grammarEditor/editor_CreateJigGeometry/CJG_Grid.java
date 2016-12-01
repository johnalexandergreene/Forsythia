package org.fleen.forsythia.app.grammarEditor.editor_CreateJigGeometry;

import java.awt.Graphics2D;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.util.grid.Grid;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;

@SuppressWarnings("serial")
public class CJG_Grid extends Grid{
  public CJG_Grid() {
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
    return GE.editor_createjig.getHostPolygon();}

  protected void mouseTouched(double[] p,KVertex v){
    GE.editor_createjig.touchGrid(p,v);}

  protected void mouseMovedCloseToVertex(KVertex v){//TODO highlight vertex
    GE.editor_createjig.initMouseMode_VERTEXNEAR();}

  protected void mouseMovedFarFromVertex(double[] p){
    GE.editor_createjig.initMouseMode_VERTEXFAR();}
  
}
