package org.fleen.forsythia.app.grammarEditor.editor_EditJigDetails;

import java.awt.Graphics2D;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.util.grid.Grid;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;

@SuppressWarnings("serial")
public class EJD_Grid extends Grid{
  
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
    return GE.editor_editjigdetails.model.targetpolygon;}

  protected void mouseTouched(double[] p,KVertex v){
    GE.editor_editjigdetails.touchGrid(p);}

  protected void mouseMovedCloseToVertex(KVertex v){}

  protected void mouseMovedFarFromVertex(double[] p){}
  
  public void clearGeometryCache(){
    super.clearGeometryCache();
    if(GE.editor_editjigdetails.model!=null)
      GE.editor_editjigdetails.model.clearGeometryCache();}
  
}
