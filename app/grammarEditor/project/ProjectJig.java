package org.fleen.forsythia.app.grammarEditor.project;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.util.ElementMenuItem;
import org.fleen.forsythia.core.grammar.Jig;
import org.fleen.forsythia.core.grammar.JigSection;
import org.fleen.geom_Kisrhombille.KPolygon;

/*
 * This is a project jig
 * it contains a jig definition in terms amenable to our project
 * we can construct one of these in 2 ways
 *   via jig editor
 *   via import
 *       
 */
public class ProjectJig implements ElementMenuItem{
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  //create
  //invoked by the jig editor
  //we specify just geometry stuff, details are set to default values
  //TODO we gotta handle anchors, chorusindices and tags here too
  public ProjectJig(
    ProjectMetagon targetmetagon,
    int griddensity,
    List<KPolygon> sectionpolygons){
    this.targetmetagon=targetmetagon;
    this.griddensity=griddensity;
    initSectionsForCreate(sectionpolygons);
    initSectionProductChorusIndices();}
  
  //import
  public ProjectJig(ProjectMetagon targetmetagon,Jig jig){
    this.targetmetagon=targetmetagon;
    this.griddensity=jig.griddensity;
    setTagsForImport(jig.getTags());
    initSectionsForImport(jig);}
  
  /*
   * ################################
   * TARGET METAGON
   * ################################
   */ 
  
  public ProjectMetagon targetmetagon;
  
  /*
   * ################################
   * GRID DENSITY AND FISH FACTOR
   * ################################
   */ 
  
  private int griddensity=1;
  
  public int getGridDensity(){
    return griddensity;}
  
  public void setGridDensity(int a){
    if(a<1)a=1;
    griddensity=a;}
  
  public double getFishFactor(){
    return 1.0/(double)griddensity;}
  
  /*
   * ################################
   * SECTIONS
   * ################################
   */
  
  public List<ProjectJigSection> sections=new ArrayList<ProjectJigSection>();

  //returns true of any of our sections uses the specified metagon for its polygonal product
  public boolean usesForProduct(ProjectMetagon m){
    for(ProjectJigSection pjs:sections)
      if(pjs.productmetagon==m)
        return true;
    return false;}
  
  /*
   * At this point the project metagons list should be fully populated
   * Any metagon found in a jig (target or product) should be found in that list
   * if it isn't then we have an exception 
   */
  public void initSectionsForImport(Jig jig){
    ProjectJigSection pjspolygon;
    for(JigSection js:jig.sections){
      if(js instanceof JigSection){
         pjspolygon=new ProjectJigSection(this,(JigSection)js);
         sections.add(pjspolygon);}}
    ((ArrayList<ProjectJigSection>)sections).trimToSize();}
  
  private void initSectionsForCreate(List<KPolygon> sectionpolygons){
    for(KPolygon p:sectionpolygons)
      sections.add(new ProjectJigSection(this,p));}
  
  /*
   * init to all different. no chorussing
   */
  private void initSectionProductChorusIndices(){
    int i=0;
    for(ProjectJigSection s:sections){
      s.setProductChorusIndex(i);
      i++;}}
  
  /*
   * ################################
   * TAGS
   * ################################
   */
  
  public String tags="";
  
  private void setTagsForImport(List<String> tags){
    if(tags.isEmpty()){
      this.tags="";
      return;}
    StringBuffer a=new StringBuffer();
    for(String b:tags)
      a.append(b+" ");
    a.delete(a.length()-1,a.length());
    this.tags=a.toString();}
  
  public String[] getTagsForExport(){
    if(tags.equals(""))return new String[0];
    String[] a=tags.split(" ");
    return a;}
  
  /*
   * ################################
   * IMAGE
   * projectjig gets graphically rendered in one place: Overview bottom element menu. As a big icon.
   * It shares rendering components with the jigeditor.
   * imagepaths is cached and immutable.
   * overviewiconimage is cached. Invalidated on ui resize.
   * ################################
   */
  
  BufferedImage overviewiconimage=null;
  
  //implementation of UIElementMenuElement interface
  public BufferedImage getElementMenuItemIconImage(int span){
    if(overviewiconimage==null)
      overviewiconimage=new ProjectJigEditGrammarIconImage(this,span);
    return overviewiconimage;}
  
  public void invalidateOverviewIconImage(){
    overviewiconimage=null;}
  
}
