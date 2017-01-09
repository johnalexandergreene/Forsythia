package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import java.util.Collection;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.project.ProjectMetagon;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KPolygon;

public class JigSectionEditingModel{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public JigSectionEditingModel(
    JigEditingModel jigeditingmodel,
    KPolygon rawpolygon){
    if(rawpolygon==null)throw new IllegalArgumentException("null rawpolygon");
    this.jigeditingmodel=jigeditingmodel;
    this.rawpolygon=rawpolygon;}
  
  /*
   * ################################
   * JIG EDITING MODEL
   * The jig editing model of which this jig section editing model is a part
   * ################################
   */
  
  JigEditingModel jigeditingmodel;
  
  /*
   * ################################
   * RAW POLYGON
   * The polygon gleaned from the editing graph
   * Unprocessed, unrefined. Just grabbed off the graph
   * ################################
   */
  
  public KPolygon rawpolygon;
  
  /*
   * ################################
   * ANCHOR
   * From the raw polygon we derive a metagon
   * The metagon + this anchor gives us our cooked polygon
   * ################################
   */
  
  public int anchorindex=0;
  List<KAnchor> anchoroptions=null;
  
  public String getAnchorIndexString(){
    String s="Section Anchor = "+String.format("%03d",anchorindex);
    return s;}
  
  public void incrementAnchor(){
    List<KAnchor> anchoroptions=getAnchorOptions();
    int maxanchor=anchoroptions.size()-1;
    anchorindex++;
    if(anchorindex>maxanchor)
      anchorindex=0;}
  
  private List<KAnchor> getAnchorOptions(){
    if(anchoroptions==null)
      anchoroptions=getCookedMetagon().kmetagon.getAnchorOptions(getCookedPolygon());
    return anchoroptions;}
  
  public KAnchor getAnchor(){
    return getAnchorOptions().get(anchorindex);}
  
  /*
   * ################################
   * CHORUS
   * The chorus index of this section
   * ################################
   */
  
  public int chorus=0;
  
  public String getChorusString(){
    String s="Section Chorus = "+String.format("%03d",chorus);
    return s;}
  
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
   * COOKED POLYGON
   * 
   * this model is associated with a drawn (raw) polygon and also a polygon that is an optimized variant of that drawn polygon
   * 
   * call that optimized polygon the "cooked" polygon
   * 
   * The cooked polygon differs from the drawn polygon like this
   * 
   * the cooked polygon has no redundant colinear vertices (rcv). We remove those.
   * 
   * the cooked polygon is based on a metagon derived from either the drawn polygon, this jig or the working grammar
   *   
   * so the cooked polygon getter delivers one of 3 possible polygons.
   *     1) a polygon from the working grammar
   *     2) a polygon from this jig, defined before this polygon
   *     3) the drawn polygon (with the rcv removed)
   *     
   * The point here is that isomorphic (scale, location, rotation, etc independent) metagons are equal, 
   *   so polygons that use the same metagon are referring to the same metagon.
   * ################################
   */
  
  /*
   * this refers to either a metagon in the grammar, 
   *   a metagon in another section in this jig, 
   *   or a metagon that we created in this section 
   */
  ProjectMetagon cookedmetagon=null;
  
  public ProjectMetagon getCookedMetagon(){
    if(cookedmetagon==null)
      initCookedMetagon();
    return cookedmetagon;}

  public KPolygon getCookedPolygon(){
    ProjectMetagon m=getCookedMetagon();
    KPolygon p=m.kmetagon.getPolygon(getAnchor());
    return p;}
  
  private void initCookedMetagon(){
    KPolygon p=new KPolygon(rawpolygon);
    //clean the raw polygon
    p.removeRedundantColinearVertices();
    //seek its metagon in the grammar
    cookedmetagon=getCookedMetagonFromGrammar(p);
    //seek its metagon locally//TODO it's recursing
//    if(cookedmetagon==null)
//      cookedmetagon=getCookedMetagonFromLocalJig(p);
    //create its metagon
    cookedmetagon=new ProjectMetagon(GE.focusgrammar,p,"");}
  
  /*
   * check the metagons in the project grammar 
   * see if one of them fits the specified polygon. 
   * If a fit is found then return that metagon. 
   * Otherwise return null.
   */
  private ProjectMetagon getCookedMetagonFromGrammar(KPolygon polygon){
    ProjectMetagon fittingmetagon=null;
    SEEKFIT:for(ProjectMetagon pm:GE.focusgrammar.metagons){
      anchoroptions=pm.kmetagon.getAnchorOptions(polygon);
      if(anchoroptions!=null){
        fittingmetagon=pm;
        break SEEKFIT;}}
    return fittingmetagon;}
  
  /*
   * check the cooked metagons in this section's jig's sections  
   * see if one of them fits the specified polygon. 
   * If a fit is found then return that metagon. 
   * Otherwise return null.
   */
  private ProjectMetagon getCookedMetagonFromLocalJig(KPolygon polygon){
    ProjectMetagon fittingmetagon=null,testmetagon;
    SEEKFIT:for(JigSectionEditingModel jsem:jigeditingmodel.sectionmodelbypolygon.values()){
      if(jsem==this)continue;
      testmetagon=jsem.getCookedMetagon();
      anchoroptions=testmetagon.kmetagon.getAnchorOptions(polygon);
      if(anchoroptions!=null){
        fittingmetagon=testmetagon;
        break SEEKFIT;}}
    return fittingmetagon;}
  
  

  

}
