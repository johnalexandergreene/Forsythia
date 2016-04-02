package org.fleen.forsythia.app.grammarEditor.editor_EditJigDetails;

import java.awt.geom.Path2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fleen.forsythia.Forsythia;
import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.project.ProjectJigSection_Polygon;
import org.fleen.forsythia.app.grammarEditor.project.ProjectMetagon;
import org.fleen.geom_2D.DCircle;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.GD;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;

class JigSectionDetailsModel implements FocusableModelElement{
  
  JigSectionDetailsModel(ProjectJigSection_Polygon jigsection,int sectionindex,JigDetailsModel jigmodel){
    productanchoroptions=jigsection.getProductAnchorOptions();
    producttags=jigsection.tags;
    productmetagon=jigsection.productmetagon;
    productanchorindex=jigsection.getProductAnchorIndex();
    productchorusindex=jigsection.getProductChorusIndex();
    producttype=jigsection.getProductType();
    this.sectionindex=sectionindex;
    this.jigmodel=jigmodel;
    initElementIDString();}
  
  JigDetailsModel jigmodel;
  
  ProjectMetagon productmetagon;
  
  boolean isFocus(){
    return jigmodel.getFocusElement()==this;}
  
  int sectionindex;
  
  /*
   * ################################
   * CHORUS INDEX
   * ################################
   */
  
  int productchorusindex;
  
  //we allow chorus indices in range [0,sectioncount]
  void incrementProductChorusIndex(){
    productchorusindex++;
    if(productchorusindex>jigmodel.getSectionCount())
      productchorusindex=0;}
  
  /*
   * ################################
   * PRODUCT TYPE
   * ################################
   */
  
  int producttype;
  
  //in the rare case we may want to change the type of a section
  //in most cases the automation handles it just fine
  boolean incrementProductType(){
    producttype++;
    if(producttype==3)producttype=0;
    return true;}
  
  /*
   * ################################
   * PRODUCT ANCHOR
   * ################################
   */
  
  List<KAnchor> productanchoroptions;
  
  int productanchorindex;
  
  KAnchor getAnchor(){
    return productanchoroptions.get(productanchorindex);
  }
  
  void flipAnchorTwist(){
    KAnchor a=productanchoroptions.get(productanchorindex);
    Integer f=getAnchorIndex(a.v0,!a.twist);
    if(f!=null)
      productanchorindex=f;
    clearGeometryCache();}
  
  void setAnchorV0(KVertex v0){
    System.out.println("set anchor---");
    KAnchor a=productanchoroptions.get(productanchorindex);
    Integer f=getAnchorIndex(v0,a.twist);
    if(f==null)f=getAnchorIndex(v0,!a.twist);
    if(f!=null)   
      productanchorindex=f;
    clearGeometryCache();}
  
  //return the anchor with the specified v0 and twist
  Integer getAnchorIndex(KVertex v0,boolean twist){
    KAnchor a;
    for(int i=0;i<productanchoroptions.size();i++){
      a=productanchoroptions.get(i);
      if(a.twist==twist&&a.v0.equals(v0))
        return i;}
    return null;}
  
  /*
   * ################################
   * PRODUCT TAGS
   * ################################
   */
  
  String producttags;
  
  public void setTags(String t){
    producttags=t;}
  
  public String getTags(){
    return producttags;}  
  
  /*
   * ################################
   * ID STRINGS
   * TODO get rid of this
   * ################################
   */
  
  String idstring;
  
  public String getElementIDString(){
    return idstring; }
  
  private void initElementIDString(){
    idstring="X";}
  
  public String getTypeIDString(){
    return "X";}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  public KPolygon getKPolygon(){
    return productmetagon.kmetagon.getPolygon(productanchoroptions.get(productanchorindex));}
  
  /*
   * TODO 
   * stop using double[] for points
   */
  public DPolygon getPolygon2D(){
    List<DPoint> p=GE.editor_editjigdetails.getGrid().getGeometryCache().getPoint2Ds(getKPolygon());
    return new DPolygon(p);}
  
  //the section sample metagon polygon path polygon
  public Path2D getPolygonPath(){
    return GE.editor_editjigdetails.getGrid().getGeometryCache().getPath2D(getKPolygon());}
  
  public boolean isClockwise(){
    List<DPoint> vertexpoints=GE.editor_editjigdetails.getGrid().getGeometryCache().getPoint2Ds(getKPolygon());
    return GD.isClockwiseD(vertexpoints);}
  
  public KVertex getV0(){
    return getKPolygon().get(0);}
  
  /*
   * given our list of anchor options, this is the list of v0s that those anchors express
   */
  public Set<KVertex> getV0Options(){
    Set<KVertex> v0options=new HashSet<KVertex>();
    KPolygon p;
    for(KAnchor a:productanchoroptions){
      p=productmetagon.kmetagon.getPolygon(a);
      v0options.add(p.get(0));}
    return v0options;}
  
  void clearGeometryCache(){
    invalidateIconCircles();}
  
  /*
   * ################################
   * ICON CIRCLES
   * For displaying our model we have some circles
   * they represent vertices and certain important values
   * we can interact with the circles in the model display, buttons
   * ################################
   */
 
  JigSectionDetailsModelVertexIconCircles vertexiconcircles=null;
  JigSectionDetailsModelTCIconCircles tciconcircles=null;
  
  JigSectionDetailsModelVertexIconCircles getVertexIconCircles(){
    if(vertexiconcircles==null)
      vertexiconcircles=new JigSectionDetailsModelVertexIconCircles(this);
    return vertexiconcircles;}
 
  JigSectionDetailsModelTCIconCircles getTCIconCircles(){
    if(tciconcircles==null)
      tciconcircles=new JigSectionDetailsModelTCIconCircles(this);
    return tciconcircles;}
  
  void invalidateIconCircles(){
    vertexiconcircles=null;
    tciconcircles=null;}
  
  boolean touchedTIconCircle(double[] p){
    JigSectionDetailsModelTCIconCircles a=getTCIconCircles();
    if(a.failed())return false;
    return a.getCircle_ProductType().contains(p);}
  
  boolean touchedCIconCircle(double[] p){
    JigSectionDetailsModelTCIconCircles a=getTCIconCircles();
    if(a.failed())return false;
    return a.getCircle_ChorusIndex().contains(p);}
  
  KVertex getTouchedVIconCircleVertex(double[] p){
    JigSectionDetailsModelVertexIconCircles a=getVertexIconCircles();
    DCircle c;
    for(KVertex v:getKPolygon()){
      c=a.getCircle(v);
      if(c.contains(p))
        return v;}
    return null;}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public String toString(){
    return getKPolygon().toString();}
  
}
