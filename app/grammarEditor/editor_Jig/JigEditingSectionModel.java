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
  

}
