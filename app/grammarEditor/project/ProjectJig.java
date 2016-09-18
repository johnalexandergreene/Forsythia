package org.fleen.forsythia.app.grammarEditor.project;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.util.ElementMenuItem;
import org.fleen.forsythia.grammar.Jig;
import org.fleen.forsythia.grammar.JigSection;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KYard;

/*
 * This is a project jig
 * it contains a jig definition in terms amenable to our project
 * we can construct one of these in 2 ways
 *   via the jig creating editors
 *   via import
 * 
 * ProjectJig creation goes like this
 *   hit the createjig button. This takes us to the projectjig geometry creating editor 
 *     Specify geometry by drawing stuff. 
 *     When we are satisfied with the geometry, save it. This takes us to the projectjig details editor 
 *       Now we have a projectjig with all the details set to default values.
 *       edit details : tags, anchor options, chorus indices, section types
 *       
 */
public class ProjectJig implements ElementMenuItem{
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  //create
  //invoked by the jig geometry creating editor
  //we specify just geometry stuff, details are set to default values
  public ProjectJig(
    ProjectMetagon targetmetagon,
    int griddensity,
    List<KPolygon> sectionpolygons,
    List<KYard> sectionyards){
    this.targetmetagon=targetmetagon;
    this.griddensity=griddensity;
    initSectionsForCreate(sectionpolygons,sectionyards);
//    initTypes();
    initSectionProductChorusIndices();}
  
  //import
  public ProjectJig(ProjectMetagon targetmetagon,Jig jig){
    this.targetmetagon=targetmetagon;
    this.griddensity=jig.griddensity;
    setTagsForImport(jig.getTags());
    initSectionsForImport(jig);}
  
//  /*
//   * ################################
//   * TYPE
//   * Access jig type here
//   * We init types for this jig and all sections via geometric analysis.
//   * Jig type is immutable but in some cases (when section roles are ambiguous) section types can be changed.
//   * ################################
//   */
//  
//  private int type;
//  
//  public int getJigType(){
//    return type;}
//  
//  private void initTypes(){
//    KPolygon targetpolygon=targetmetagon.kmetagon.getPolygon(getGridDensity(),true);
//    //get section polygons, map to sections
//    List<KPolygon> sectionpolygons=new ArrayList<KPolygon>(sections_polygon.size());
//    KPolygon p;
//    Map<ProjectJigSection_Polygon,KPolygon> polygonbysection=new Hashtable<ProjectJigSection_Polygon,KPolygon>();
//    for(ProjectJigSection_Polygon s:sections_polygon){
//      p=s.productmetagon.kmetagon.getPolygon(s.getProductAnchor());
//      polygonbysection.put(s,p);
//      sectionpolygons.add(p);}
//    //get types from analysis
//    JigSystemAnalysis a=F.getJigSystemAnalysis(targetpolygon,sectionpolygons);
//    type=a.jigtype;
//    for(ProjectJigSection_Polygon s:sections_polygon)
//      s.setProductType(a.sectiontypes.get(polygonbysection.get(s)));}
  
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
  
  public List<ProjectJigSection_Polygon> sections_polygon=new ArrayList<ProjectJigSection_Polygon>();

  //returns true of any of our sections uses the specified metagon for its polygonal product
  public boolean usesForProduct(ProjectMetagon m){
    for(ProjectJigSection_Polygon ppjs:sections_polygon)
      if(ppjs.productmetagon==m)
        return true;
    return false;}
  
  /*
   * At this point the project metagons list should be fully populated
   * Any metagon found in a jig (target or product) should be found in that list
   * if it isn't then we have an exception 
   */
  public void initSectionsForImport(Jig jig){
    ProjectJigSection_Polygon pjspolygon;
    for(JigSection js:jig.sections){
      if(js instanceof JigSection){
         pjspolygon=new ProjectJigSection_Polygon(this,(JigSection)js);
         sections_polygon.add(pjspolygon);}}
    ((ArrayList<ProjectJigSection_Polygon>)sections_polygon).trimToSize();}
  
  private void initSectionsForCreate(List<KPolygon> sectionpolygons,List<KYard> sectionyards){
    for(KPolygon p:sectionpolygons)
      sections_polygon.add(new ProjectJigSection_Polygon(this,p));}
  
  /*
   * init to all different. no chorussing
   */
  private void initSectionProductChorusIndices(){
    int i=0;
    for(ProjectJigSection_Polygon s:sections_polygon){
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
   * projectprotojig gets graphically rendered in one place: Overview bottom element menu. As a big icon.
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
