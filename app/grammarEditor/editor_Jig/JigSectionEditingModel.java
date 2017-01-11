package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import java.util.List;

import org.fleen.forsythia.app.grammarEditor.project.ProjectMetagon;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KPolygon;

public class JigSectionEditingModel{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public JigSectionEditingModel(JigEditingModel jigeditingmodel,JigSectionModelComponents components){
      this.jigeditingmodel=jigeditingmodel;
      metagon=components.metagon;
      anchors=components.anchors;}
  
  /*
   * ################################
   * JIG EDITING MODEL
   * The jig editing model of which this jig section editing model is a part
   * ################################
   */
  
  JigEditingModel jigeditingmodel;
  
  /*
   * ################################
   * METAGON
   * ################################
   */
  
  public ProjectMetagon metagon;
  
  /*
   * ################################
   * ANCHOR
   * From the raw polygon we derive a metagon
   * The metagon + this anchor gives us our cooked polygon
   * ################################
   */
  
  public int anchorindex=0;
  List<KAnchor> anchors=null;
  
  public String getAnchorIndexString(){
    return String.format("%03d",anchorindex);}
  
  public void incrementAnchor(){
    int maxanchor=anchors.size()-1;
    anchorindex++;
    if(anchorindex>maxanchor)
      anchorindex=0;}
  
  public KAnchor getAnchor(){
    return anchors.get(anchorindex);}
  
  /*
   * ################################
   * CHORUS
   * The chorus index of this section
   * ################################
   */
  
  public int chorus=0;
  
  public String getChorusString(){
    return String.format("%03d",chorus);}
  
  public void incrementChorus(){
    int maxchorus=jigeditingmodel.getMaxChorus();
    chorus++;
    if(chorus>maxchorus)
      chorus=0;}
  
  /*
   * ################################
   * TAGS
   * Every section has tags. 
   * In the cultivation process the tags get put on the created polygons
   * ################################
   */
  
  public String tags="";
  
  /*
   * ################################
   * POLYGON
   * ################################
   */
  
  public KPolygon getPolygon(){
    return metagon.kmetagon.getPolygon(getAnchor());}
  
}
