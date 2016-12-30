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
   * this model is associated with a polygon
   * we offer an idealized and contextualized (cleaned) version of this polygon
   * by idealized we mean
   *   all redundant colinear vertices removed
   *   clockwiseized
   * by contextualized we mean
   *   if the polygon's metagon already exists within the grammar then we use that metagon's polygon
   *   that is to say, we specify a v0
   *   (if the polygon's metagon does not exist within the grammar, and we save this jig, 
   *     then we save that section metagon too, adding it to the grammar) 
   */
  public KPolygon getCleanedPolygon(){
    
  }
  

}
