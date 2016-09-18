package org.fleen.forsythia.app.grammarEditor.project;

import java.awt.geom.Path2D;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.grammar.JigSection;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_Kisrhombille.GK;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KMetagon;
import org.fleen.geom_Kisrhombille.KPolygon;

/*
 * Wraps a protojigsection
 * immutable except for tags
 */
public class ProjectJigSection_Polygon implements ProjectJigSection{
  
  /*
   * ################################
   * INIT
   * ################################
   */
  
  //create
  public ProjectJigSection_Polygon(ProjectJig owner,KPolygon polygon){
    this.owner=owner;
    initGeometryForCreate(polygon);}
  
  //import
  public ProjectJigSection_Polygon(ProjectJig owner,JigSection jigsection){
    this.owner=owner;
    setTagsForImport(jigsection.tags.getTags());
    initGeometryForImport(jigsection);}
  
  /*
   * ################################
   * OWNER
   * the jig that uses this section (among others) to do its jigging
   * ################################
   */
  
  public ProjectJig owner;
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  public ProjectMetagon productmetagon;
  private List<KAnchor> anchoroptions;
  private int productanchorindex=0;
  
  public KAnchor getProductAnchor(){
    return anchoroptions.get(productanchorindex);}
  
  public List<KAnchor> getProductAnchorOptions(){
    return anchoroptions;}
  
  public int getProductAnchorIndex(){
    return productanchorindex;}
  
  public void setProductAnchorIndex(int i){
    productanchorindex=i;}
  
  /*
   * at this point the grammar should be populated with all the metagons we need
   * seek a match for the imported metagon
   * if none found then exception
   */
  private void initGeometryForImport(JigSection jigsection){
    productmetagon=GE.focusgrammar.getMetagon(jigsection.productmetagon);
    if(productmetagon==null)throw new IllegalArgumentException("metagon in jigsection not found in focus grammar");
    anchoroptions=productmetagon.kmetagon.getAnchorOptions(productmetagon.kmetagon.getPolygon(jigsection.productanchor));
    productanchorindex=getAnchorIndex(jigsection.productanchor,anchoroptions);
    productchorusindex=jigsection.productchorusindex;}
  
  private int getAnchorIndex(KAnchor testanchor,List<KAnchor> anchors){
    for(int i=0;i<anchors.size();i++)
      if(anchors.get(i).equals(testanchor))
        return i;
    throw new IllegalArgumentException("anchor not in list");}
  
  /*
   * init section geometry using a kpolygon derived from editor_createprotojig.graph
   * Find a metagon in the focusgrammar that fits polygon. If none such exists then create one
   * Use that metagon as our product
   * Derive all possible anchors for the polygon. Pick an arbitrary one for default.
   */
  private void initGeometryForCreate(KPolygon polygon){
    //clean up the polygon
    polygon.removeRedundantColinearVertices();
    //glean metagon and anchors
    //try matching an existant metagon
    initMetagonAndAnchorsByMatchingExistantMetagon(polygon);
    //if that failed then create a new metagon
    if(productmetagon==null)
      initMetagonAndAnchorsByCreatingNewMetagon(polygon);}
  
  /*
   * Test polygon agains existant grammar metagons
   * if we find one that fits then return it
   * if we fail to find a match then return null
   */
  private void initMetagonAndAnchorsByMatchingExistantMetagon(KPolygon polygon){
    SEEKFIT:for(ProjectMetagon m:GE.focusgrammar.metagons){
      anchoroptions=m.kmetagon.getAnchorOptions(polygon);
      if(anchoroptions!=null){
        productmetagon=m;
        break SEEKFIT;}}}
  
  private void initMetagonAndAnchorsByCreatingNewMetagon(KPolygon polygon){
    productmetagon=new ProjectMetagon(
      GE.focusgrammar,
      new KPolygon(polygon),
      "");
    GE.focusgrammar.addMetagon(productmetagon);
    anchoroptions=productmetagon.kmetagon.getAnchorOptions(polygon);}
  
  /*
   * ################################
   * PRODUCT TYPE
   * ################################
   */
  
  private int producttype=1;
  
  public int getProductType(){
    return producttype;}
  
  public void setProductType(int i){
    producttype=i;}
  
  /*
   * ################################
   * PRODUCT CHORUS INDEX
   * ################################
   */
  
  private int productchorusindex=0;
  
  public int getProductChorusIndex(){
    return productchorusindex;}
  
  public void setProductChorusIndex(int i){
    productchorusindex=i;}
    
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
   * PATH2D
   * This is the path 2d of this section's metagon, transformed to fit nicely as 
   * a subimage of the image of it's jig
   * We use it to draw the jig image in the overview. 
   * TODO are we using this for the JigEditor image too?
   * ################################
   */
  
  private Path2D.Double path2dforjigimagecomponent=null;
  
  public Path2D.Double getPath2DForJigImageComponent(){
    if(path2dforjigimagecomponent==null)
      initImagePath2DForJigImageComponent();
    return path2dforjigimagecomponent;}
  
  private void initImagePath2DForJigImageComponent(){
    path2dforjigimagecomponent=null;
    try{
      KMetagon p1=new KMetagon(owner.targetmetagon.kpolygon);
      KAnchor anchor=getProductAnchor();
      if(anchor.twist==GK.TWIST_NEGATIVE)
        p1.reverseDeltas();
      KPolygon p0=p1.getPolygon(anchor.v0,anchor.v1);
      DPolygon points=p0.getDefaultPolygon2D();
      path2dforjigimagecomponent=new Path2D.Double();
      DPoint p=points.get(0);
      path2dforjigimagecomponent.moveTo(p.x,p.y);
      for(int i=1;i<points.size();i++){
        p=points.get(i);
        path2dforjigimagecomponent.lineTo(p.x,p.y);}
      path2dforjigimagecomponent.closePath();
    }catch(Exception e){
      //fail on exception
      path2dforjigimagecomponent=null;}}
  
}
