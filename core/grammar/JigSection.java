package org.fleen.forsythia.core.grammar;

import java.io.Serializable;

import org.fleen.forsythia.core.Forsythia;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.ForsythiaTreeNode;
import org.fleen.geom_2D.DPolygon;
import org.fleen.geom_Kisrhombille.KAnchor;
import org.fleen.util.tag.TagManager;

/*
 * This holds general params for creating a forsythia shape tree node
 * within the node-creating system of a Jig
 * See JigSection interface for details
 */
public class JigSection implements Serializable,Forsythia{
  
  private static final long serialVersionUID=-7594787438281531096L;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public JigSection(FMetagon productmetagon,KAnchor productanchor,int productchorusindex,String[] producttags){
    this.productmetagon=productmetagon;
//    this.productanchor=new KAnchor(productanchor);//isolate it
    this.productanchor=productanchor;
    this.productchorusindex=productchorusindex;
    this.tags=new TagManager(producttags);}
  
  /*
   * ################################
   * PRODUCT METAGON
   * the metagon for the produced polygon
   * target.parentgrid + metagon + anchor = polygon 
   * ################################
   */
  
  //the metagon for the generated polygon
  public FMetagon productmetagon;
  
  /*
   * ################################
   * PRODUCT ANCHOR
   * the anchor for the produced polygon
   * target.parentgrid + metagon + anchor = polygon  
   * ################################
   */
  
  //metagon + anchor = polygon
  public KAnchor productanchor;
  
  /*
   * ################################
   * PRODUCT CHORUS INDEX
   * The produced polygon gets a chorus index
   * chorus index denotes the polygon's role in the jig's produced system of polygons
   * polygons with the same chorus index are assumed to be of the same shape and scale, are treated alike 
   * in the tree generation logic, producing symmetries in the pattern
   * polygons with the same chorus index are not required to have the same chorus index, however.
   * It's up to the designer.
   * ################################
   */
  
  public int productchorusindex;
  
  /*
   * ################################
   * TAGS
   * Used by tree production logic
   * These are passed to the produced polygon 
   * ################################
   */
  
  public TagManager tags;
  
  /*
   * ################################
   * CREATE POLYGON NODE
   * Used in the jig to create a new node for the forsythia composition tree
   * ################################
   */
  
  public ForsythiaTreeNode createNode(){
    ForsythiaTreeNode b=new FPolygon(productmetagon,productanchor,productchorusindex,tags.getTags());
    return b;}
  
  /*
   * ################################
   * TEST POLYGON
   * for jig system analysis and such
   * ################################
   */
  
  private DPolygon testpolygon=null;
  
  public DPolygon getTestPolygon(){
    if(testpolygon==null)
      initTestPolygon();
    return testpolygon;}
  
  private void initTestPolygon(){
    FPolygon p=new FPolygon(productmetagon,productanchor);
    testpolygon=p.getDPolygon();}
  
  /*
   * ################################
   * GENERAL PURPOSE OBJECT
   * ################################
   */
  
  public Object gpobject;
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public String toString(){
    StringBuffer a=new StringBuffer();
    a.append("["+getClass().getSimpleName()+" ");
    a.append("product="+productmetagon+" ");
    a.append("anchor="+productanchor+" ");
    a.append("index="+productchorusindex+" ");
    a.append("tags="+tags.toString()+"]");
    return a.toString();}
  
}
