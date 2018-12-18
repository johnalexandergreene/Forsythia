package org.fleen.forsythia.core.composition;

import java.util.ArrayList;
import java.util.List;

import org.fleen.forsythia.core.grammar.FMetagon;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_2D.GD;
import org.fleen.geom_2D.IncircleCalculator;
import org.fleen.geom_Kisrhombille.GK;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.geom_Kisrhombille.KGrid;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KPoint;
import org.fleen.util.tag.TagManager;
import org.fleen.util.tag.Tagged;
import org.fleen.util.tree.TreeNode;

/*
 * NODE POLYGON
 * A polygon specified by a metagon, 2 vertices, a twist and a chorus index
 * the polygonmodel specifies relative vertex locations
 * the vertices specify the first 2 vertices in the polygon, scale and orientation
 * twist specifies polygon path orientation clockwise/counterclockwise
 * chorus index is for local child grouping
 * from this we derive a loop of vertices which specify a polygon 
 * real 2d points are derived from the compounded local grid, which is the first grid node in this polygon's ancestry
 */
public class FPolygon extends ForsythiaTreeNode implements Tagged{
  
  private static final long serialVersionUID=7403675520824450721L;
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  public FPolygon(FMetagon metagon,KAnchor anchor,int chorusindex,List<String> tags){
    this.metagon=metagon;
    this.anchor=anchor;
    this.chorusindex=chorusindex;
    
    //get tags from metagon and jig section
    setTags(metagon.getTags());
    tagmanager.addTags(tags);
    
    initVertices();}
  
  /*
   * returns a default form for use in the grammar editing tool or whatever
   */
  public FPolygon(FMetagon metagon){
    this.metagon=metagon;
    KPolygon p=metagon.getPolygon();
    anchor=new KAnchor(p.get(0),p.get(1),true);
    chorusindex=0;
    initVertices();}
  
  /*
   * for test
   */
  public FPolygon(FMetagon metagon,KAnchor anchor){
    this.metagon=metagon;
    this.anchor=anchor;
    chorusindex=0;
    initVertices();}
  
  /*
   * ################################
   * TREE STRUCTURE STUFF
   * ################################
   */
  
  /*
   * if this polygon's parent is the root grid then this is the root polygon and its depth is 1
   */
  public boolean isRootPolygon(){
    return getDepth()==1;}
  
  public FPolygon getPolygonParent(){
    if(isRootPolygon())return null;
    return (FPolygon)getAncestor(2);}
  
  public List<FPolygon> getPolygonChildren(){
    //if this polygon is a leaf then we have no polygon children
    if(isLeaf())return new ArrayList<FPolygon>(0);
    //refer to this polygons's grid child, refer to that grid's children
    List<? extends TreeNode> children=getChild().getChildren();
    //gather those chold nodes into a list
    List<FPolygon> polygons=new ArrayList<FPolygon>(children.size());
    for(TreeNode n:children)
      polygons.add((FPolygon)n);
    return polygons;}
  
  /*
   * ################################
   * GEOM
   * ################################
   */
  
  public FMetagon metagon;
  public KAnchor anchor;
  
  /*
   * ++++++++++++++++++++++++++++++++
   * KVERTICES
   * ++++++++++++++++++++++++++++++++
   */
  
  private KPoint[] vertices;
  
  public KPoint[] getVertices(){
    return vertices;}
  
  public int getVertexCount(){
    return vertices.length;}
  
  /*
   * vertex intervals
   * specified in the case of (v0,v1)
   * in the other cases they are (modelinterval[n]/modelinterval[0])*distance(v0,v1) 
   */
  private void initVertices(){
    int vectorcount=metagon.vectors.length;
    vertices=new KPoint[vectorcount+2];
    double localbaseinterval=getLocalBaseInterval(),distance;
    vertices[0]=anchor.v0;
    vertices[1]=anchor.v1;
    int direction=getLocalBaseForeward(),delta;
    for(int i=0;i<vectorcount;i++){
      distance=metagon.vectors[i].relativeinterval*localbaseinterval;
      delta=metagon.vectors[i].directiondelta;
      if(!anchor.twist)delta*=-1;
      direction=(direction+delta+12)%12;
      vertices[i+2]=new KPoint(GK.getVertex_VertexVector(vertices[i+1].coors,direction,distance));
      if(vertices[i+2].coors[3]==-1)
        throw new IllegalArgumentException(
          "BAD GEOMETRY. "+vertices[i+1]+" , direction:"+direction+" distance:"+distance);}}
  
  public int getLocalBaseForeward(){
    int f=GK.getDirection_VertexVertex(
      anchor.v0.getAnt(),anchor.v0.getBat(),anchor.v0.getCat(),anchor.v0.getDog(),
      anchor.v1.getAnt(),anchor.v1.getBat(),anchor.v1.getCat(),anchor.v1.getDog());
    if(f==GK.DIRECTION_NULL)
      throw new IllegalArgumentException("local foreward is direction null "+anchor.v0+" "+anchor.v1);
    return f;}
  
  public double getLocalBaseInterval(){
    return anchor.v0.getDistance(anchor.v1);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * DPOLYGON
   * 2d polygon
   * This FPolygon's KVertex geometry translated into 2D geometry
   * we derive any foreward, scale and translation context from ancestry
   * ++++++++++++++++++++++++++++++++
   */
  
  private DPolygon dpolygon=null;
  
  public DPolygon getDPolygon(){
    if(dpolygon==null)
      initDPolygon();
    dpolygon.object=this;//because it is very handy
    return dpolygon;}
  
  private void initDPolygon(){
    KPoint[] v=getVertices();
    int s=v.length;
    dpolygon=new DPolygon(s);
    //get the local kgrid
    //if there is none it's because were doing some kind of test thing
    //  in that case create a test grid. the default.
    FGrid fag=getFirstAncestorGrid();
    KGrid grid;
    if(fag!=null){
      grid=fag.getLocalKGrid();
    }else{
      grid=new KGrid();}
    //
    for(int i=0;i<s;i++)
      dpolygon.add(new DPoint(grid.getPoint2D(v[i])));}
  
  private double[][] getDPolygonAsDoubleArray(){
    List<DPoint> vp2d=getDPolygon();
    int s=vp2d.size();
    double[][] b=new double[s][2];
    DPoint p;
    for(int i=0;i<s;i++){
      p=vp2d.get(i);
      b[i]=new double[]{p.x,p.y};}
    return b;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * DETAIL SIZE
   * The diameter of the incircle
   * TODO this should be in DPolygon
   * ++++++++++++++++++++++++++++++++
   */
  
  private Double detailsize=null;
  
  public double getDetailSize(){
    if(detailsize==null)
      detailsize=IncircleCalculator.getIncircle(getDPolygon()).r*2;
    return detailsize;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * PERIMETER AND AREA
   * TODO this should be in DPolygon
   * ++++++++++++++++++++++++++++++++
   */
  
  double perimeter2d=-1,area2d=-1;
  
  public double getPerimeter2D(){
    if(perimeter2d==-1)
      initPerimeterAndArea();
    return perimeter2d;}
  
  public double getArea2D(){
    if(area2d==-1)
      initPerimeterAndArea();
    return area2d;}
  
  private void initPerimeterAndArea(){
    double[][] a=getDPolygonAsDoubleArray();
    perimeter2d=GD.getPerimeter(a);
    area2d=GD.getAbsArea2D(a);}
  
  /*
   * ################################
   * SIGNATURE
   * This is a way of characterizing a bubble's context. 
   * It's a little sloppy (chorus indices don't necessarily denote similar contexts) but 
   * it works nice for symmetry. 
   * ################################
   */
  
//  private FPolygonSignature signature=null;
  private FPolygonSignature.SigComponent signaturecomponent=null;
  
  public FPolygonSignature getSignature(){
//    if(signature==null)
    FPolygonSignature signature=new FPolygonSignature(this);
    return signature;}
  
  public FPolygonSignature.SigComponent getSignatureComponent(){
    if(signaturecomponent==null)
      signaturecomponent=new FPolygonSignature.SigComponent(this);
    return signaturecomponent;}
  
  /*
   * ################################
   * CHORUS INDEX
   * siblings of similar geometry are often grouped for parallel treatment by giving them identical chorus indices
   * chorus indices through the ancestry of a polygon (a list of chorus indices) uniquely ids this FNPolygon's geometric context
   * ################################
   */
  
  public int chorusindex;
  
  public int getChorusIndex(){
    return chorusindex;}
  
  /*
   * ################################
   * TAGS
   * ################################
   */
  
  private TagManager tagmanager=new TagManager();
  
  public void setTags(String... tags){
    tagmanager.setTags(tags);}
  
  public void setTags(List<String> tags){
    tagmanager.setTags(tags);}
  
  public List<String> getTags(){
    return tagmanager.getTags();}
  
  public boolean hasTags(String... tags){
    return tagmanager.hasTags(tags);}
  
  public boolean hasTags(List<String> tags){
    return tagmanager.hasTags(tags);}
  
  public void addTags(String... tags){
    tagmanager.addTags(tags);}
  
  public void addTags(List<String> tags){
    tagmanager.addTags(tags);}
  
  public void removeTags(String... tags){
    tagmanager.removeTags(tags);}
  
  public void removeTags(List<String> tags){
    tagmanager.removeTags(tags);}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public String toString(){
    return getClass().getSimpleName()+"["+hashCode()+"]"+tagmanager;}

}
