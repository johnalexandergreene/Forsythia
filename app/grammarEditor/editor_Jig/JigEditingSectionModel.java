package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import java.util.List;

import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KPolygon;

public class JigEditingSectionModel{
  
  public JigEditingSectionModel(JigEditingModel jigeditingmodel,KPolygon polygon){
    this.jigeditingmodel=jigeditingmodel;
    this.polygon=polygon;}
  
  JigEditingModel jigeditingmodel;
  public KPolygon polygon;
  public int anchor=0,chorus=0;
  public String tags="";
  
  public String getAnchorString(){
    String s="Section Anchor = "+String.format("%03d",anchor);
    return s;}
  
  public String getChorusString(){
    String s="Section Chorus = "+String.format("%03d",chorus);
    return s;}
  
  public void incrementAnchor(){
    List<KAnchor> anchors=polygon.getKMetagon().getAnchorOptions(polygon);
    int maxanchor=anchors.size()-1;
    anchor++;
    if(anchor>maxanchor)
      anchor=0;}
  
  public void incrementChorus(){
    int maxchorus=jigeditingmodel.getMaxChorus();
    chorus++;
    if(chorus>maxchorus)
      chorus=0;}
  
  /*
   * ################################
   * CLEAN POLYGON
   * this model is associated with a drawn polygon and also a polygon that is an optimized variant of that drawn polygon
   * 
   * call that the "clean" polygon
   * 
   * The clean polygon differs from the drawn polygon like this
   * 
   * the clean polygon has no redundant colinear vertices (rcv). We remove those.
   * 
   * the clean polygon is based on a metagon derived from either the drawn polygon, this jig or the working grammar
   *   
   * so the clean polygon getter delivers one of 3 possible polygons.
   *     1) a polygon from the working grammar
   *     2) a polygon from this jig, defined before this polygon
   *     3) the drawn polygon (with the rcv removed)
   *     
   * The point here is that isomorphic (scale, location, rotation, etc independent) metagons are equal, 
   *   so polygons that use the same metagon are referring to the same metagon.
   * ################################
   */
  
  public KPolygon getCleanPolygon(){
    
  }
  

}
