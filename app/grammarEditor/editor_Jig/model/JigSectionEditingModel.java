package org.fleen.forsythia.app.grammarEditor.editor_Jig.model;

import java.util.List;

import org.fleen.forsythia.app.grammarEditor.project.metagon.ProjectMetagon;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KPolygon;

public class JigSectionEditingModel{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public JigSectionEditingModel(
      JigEditingModelForCreate jigeditingmodel,
      JigSectionModelMetagonAndAnchors metagonandanchors,
      int chorusindex){
      this.jigeditingmodel=jigeditingmodel;
      metagon=metagonandanchors.metagon;
      anchors=metagonandanchors.anchors;
      this.chorusindex=chorusindex;}
  
  /*
   * ################################
   * JIG EDITING MODEL
   * The jig editing model of which this jig section editing model is a part
   * ################################
   */
  
  JigEditingModelForCreate jigeditingmodel;
  
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
  public List<KAnchor> anchors=null;
  
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
   * CHORUS INDEX
   * The chorus index of this section
   * ################################
   */
  
  public int chorusindex=0;
  
  public String getChorusString(){
    return String.format("%03d",chorusindex);}
  
  public void incrementChorus(){
    chorusindex=jigeditingmodel.getNextValidChorusIndex(this,chorusindex);}
  
  /*
   * ################################
   * TAGS
   * Every section has tags. 
   * In the cultivation process the tags get put on the created polygons
   * Our tag string here is a bunch of space-delimited tags
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
