package org.fleen.forsythia.app.grammarEditor.editor_EditJigDetails;

import java.awt.geom.Path2D;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.project.ProjectJig;
import org.fleen.forsythia.app.grammarEditor.project.ProjectJigSection;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_Kisrhombille.KPolygon;

class JigDetailsModel implements FocusableModelElement{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  JigDetailsModel(ProjectJig projectjig){
    //convert jig to model
    targetpolygon=projectjig.targetmetagon.kmetagon.getPolygon(projectjig.getGridDensity(),true);
    jigtags=projectjig.tags;
    //convert sections
    JigSectionDetailsModel m;
    int i=0;
    for(ProjectJigSection s:projectjig.sections){
      m=new JigSectionDetailsModel(s,i,this);
      modelbysection.put(s,m);
      i++;}}
  
  /*
   * ################################
   * SECTIONS
   * ################################
   */
  
  //section models keyed by section
  Map<ProjectJigSection,JigSectionDetailsModel> modelbysection=
    new Hashtable<ProjectJigSection,JigSectionDetailsModel>();
  
  Collection<JigSectionDetailsModel> getSectionModels(){
    return modelbysection.values();}
  
  int getSectionCount(){
    return modelbysection.keySet().size();}
  
  /*
   * ################################
   * TAGS
   * the tags of this model's jig
   * ################################
   */
  
  String jigtags;
  
  public void setTags(String t){
    jigtags=t;}
  
  public String getTags(){
    return jigtags;}
  
  /*
   * ################################
   * ID STRING
   * ################################
   */
  
  private static final String IDSTRING="JIG";
  
  public String getElementIDString(){
    return IDSTRING; }
  
  /*
   * ################################
   * FOCUS ELEMENT
   * either this or a section
   * ################################
   */
  
  FocusableModelElement 
    focuselement=this,
    priorfocuselement=null;
  
  FocusableModelElement getFocusElement(){
    return focuselement;}
  
  FocusableModelElement getPriorFocusElement(){
    return priorfocuselement;}
  
  boolean focusIsJig(){
    return focuselement==this;}
  
  //set priorfocus to focus
  //set focus to element indicated by specified point
  //if a section contains that point then that section is the focus
  //if no section contains that point then the focus is this jig model
  FocusableModelElement updateFocusElement(double[] p){
    priorfocuselement=focuselement;
    focuselement=this;
    for(JigSectionDetailsModel s:modelbysection.values())
      if(s.getPolygonPath().contains(p[0],p[1])){
        focuselement=s;}
    return focuselement;}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  KPolygon targetpolygon;
  
  //polygon path of target metagon default polygon
  public Path2D getPolygonPath(){
    return GE.editor_editjigdetails.getGrid().getGeometryCache().getPath2D(targetpolygon);}
  
  public KPolygon getKPolygon(){
    return targetpolygon;}
  
  public List<DPoint> getPolygonPoints(){
    return GE.editor_editjigdetails.getGrid().getGeometryCache().getPoint2Ds(targetpolygon);}
  
  void clearGeometryCache(){
    for(JigSectionDetailsModel m:getSectionModels())
      m.clearGeometryCache();
  }
  
  /*
   * ################################
   * EXPORT
   * ################################
   */
  
  void export(ProjectJig jig){
    jig.tags=jigtags;
    JigSectionDetailsModel m;
    for(ProjectJigSection s:jig.sections){
      m=modelbysection.get(s);
      s.setProductAnchorIndex(m.productanchorindex);
      s.setProductChorusIndex(m.productchorusindex);
      s.setProductType(m.producttype);
      s.tags=m.producttags;}}

}
