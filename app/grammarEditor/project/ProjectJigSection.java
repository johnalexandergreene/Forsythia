package org.fleen.forsythia.app.grammarEditor.project;

import java.awt.geom.Path2D;
import java.util.List;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.model.JigSectionEditingModel;
import org.fleen.forsythia.core.grammar.JigSection;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_Kisrhombille.GK;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KMetagon;
import org.fleen.geom_Kisrhombille.KPolygon;

public class ProjectJigSection{
  
  /*
   * ################################
   * INIT
   * ################################
   */
  
  //create
  public ProjectJigSection(ProjectJig owner,JigSectionEditingModel jigsectioneditingmodel){
    this.owner=owner;
    initForCreate(jigsectioneditingmodel);}
  
  //import
  public ProjectJigSection(ProjectJig owner,JigSection jigsection){
    this.owner=owner;
    initForImport(jigsection);}
  
  /*
   * ################################
   * OWNER
   * the jig that uses this section (among others) to do its jigging
   * ################################
   */
  
  public ProjectJig owner;
  
  /*
   * ################################
   * METAGON
   * The metagon that this section uses, along with an anchor, to create a polygon
   * ################################
   */
  
  public ProjectMetagon metagon;
  
  /*
   * ################################
   * CHORUS INDEX
   * ################################
   */
  
  private int chorusindex=0;
  
  public int getChorusIndex(){
    return chorusindex;}
  
  public void setChorusIndex(int i){
    chorusindex=i;}
    
  /*
   * ################################
   * TAGS
   * ################################
   */
  
  public String tags="";
  
  public void setTags(List<String> tags){
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
   * ANCHOR
   * ################################
   */
  
  public List<KAnchor> anchors;
  public int anchorindex;
  
  public KAnchor getAnchor(){
    return anchors.get(anchorindex);}
  
  /*
   * ################################
   * INIT FOR IMPORT
   * ################################
   */
  
  /*
   * at this point the grammar should be populated with all the metagons we need
   * seek a match for the imported metagon
   * if none found then exception
   */
  private void initForImport(JigSection jigsection){
    metagon=GE.ge.focusgrammar.getMetagon(jigsection.productmetagon);
    if(metagon==null)throw new IllegalArgumentException("metagon in jigsection not found in focus grammar");
    //
    anchors=metagon.kmetagon.getAnchorOptions(metagon.kmetagon.getPolygon(jigsection.productanchor));
    anchorindex=getAnchorIndex(jigsection.productanchor,anchors);
    //
    chorusindex=jigsection.productchorusindex;
    //
    setTags(jigsection.tags.getTags());}
  
  private int getAnchorIndex(KAnchor testanchor,List<KAnchor> anchors){
    for(int i=0;i<anchors.size();i++)
      if(anchors.get(i).equals(testanchor))
        return i;
    throw new IllegalArgumentException("anchor not in list");}
  
  /*
   * ################################
   * INIT FOR CREATE
   * ################################
   */
  
  private void initForCreate(JigSectionEditingModel jigsectioneditingmodel){
    metagon=jigsectioneditingmodel.metagon;
    anchors=jigsectioneditingmodel.anchors;
    anchorindex=jigsectioneditingmodel.anchorindex;
    tags=jigsectioneditingmodel.tags;}
  
  /*
   * ################################
   * PATH2D
   * This is the path 2d of this section's metagon, transformed to fit nicely as 
   * a subimage of the image of its jig
   * We use it to draw the jig image in the overview.
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
      KAnchor anchor=getAnchor();
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
