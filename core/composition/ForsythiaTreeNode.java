package org.fleen.forsythia.core.composition;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.fleen.forsythia.core.Forsythia;
import org.fleen.util.tree.TreeNode;
import org.fleen.util.tree.TreeNodeIterator;
import org.fleen.util.tree.TreeNodeServices;

/*
 * FORSYTHIA TREE NODE
 * Nodes come in 3 types : Grid, Bubble and Lake
 * Grids comes in 2 subtypes : Root and Transform
 * See the Forsythia interface for details.
 */
public abstract class ForsythiaTreeNode implements TreeNode,Serializable,Forsythia{

  private static final long serialVersionUID=6049549726585045831L;
  
  /*
   * ################################
   * TREENODE
   * ################################
   */
  
  public TreeNodeServices treenodeservices=new TreeNodeServices();
  
  /*
   * ++++++++++++++++++++++++++++++++
   * IMPLEMENTATION OF TreeNode INTERFACE
   */
  
  public TreeNode getParent(){
    return treenodeservices.getParent();}
  
  public void setParent(TreeNode node){
    treenodeservices.setParent(node);}
  
  public List<? extends TreeNode> getChildren(){
    return treenodeservices.getChildren();}
  
  public TreeNode getChild(){
    return treenodeservices.getChild();}
  
  public void setChildren(List<? extends TreeNode> nodes){
    treenodeservices.setChildren(nodes);}
  
  public void setChild(TreeNode node){
    treenodeservices.setChild(node);}
  
  public void addChild(TreeNode node){
    treenodeservices.addChild(node);}
  
  public int getChildCount(){
    return treenodeservices.getChildCount();}
  
  public boolean hasChildren(){
    return treenodeservices.hasChildren();}
  
  public void clearChildren(){
    treenodeservices.clearChildren();}
  
  public void removeChild(TreeNode child){
    treenodeservices.removeChild(child);}
  
  public void removeChildren(Collection<? extends TreeNode> children){
    treenodeservices.removeChildren(children);}
  
  public boolean isRoot(){
    return treenodeservices.isRoot();}
  
  public boolean isLeaf(){
    return treenodeservices.isLeaf();}
  
  public int getDepth(){
    return treenodeservices.getDepth(this);}
  
  public TreeNode getRoot(){
    return treenodeservices.getRoot(this);}
  
  public TreeNode getAncestor(int levels){
    return treenodeservices.getAncestor(this,levels);}
  
  public List<TreeNode> getSiblings(){
    return treenodeservices.getSiblings(this);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * FORSYTHIA SPECIFIC TREE STUFF
   */
  
  /**
   * @return The number of polygons encountered when traversing the tree from this node to the root.
   */
  public int getPolygonDepth(){
    int c=0;
    ForsythiaTreeNode n=this;
    while(n!=null){
      n=n.getFirstAncestorPolygon();
      if(n!=null)c++;}
    return c;}
  
  /**
   * @return The number of grids encountered when traversing the tree from this node to the root.
   */
  public int getGridDepth(){
    int c=0;
    ForsythiaTreeNode n=this;
    while(n!=null){
      n=n.getFirstAncestorGrid();
      if(n!=null){
        c++;}}
    return c;}
  
  public FGrid getFirstAncestorGrid(){
    TreeNode n=treenodeservices.getParent();
    while(!(n instanceof FGrid)){
      if(n==null)return null;
      n=n.getParent();}
    return (FGrid)n;}
  
  public FPolygon getFirstAncestorPolygon(){
    TreeNode n=treenodeservices.getParent();
    while(!(n instanceof FPolygon)){
      if(n==null)return null;
      n=n.getParent();}
    return (FPolygon)n;}
  
  /**
   * @return Leaves of the branch rooted at this node
   */
  @SuppressWarnings("serial")
  public TreeNodeIterator getLeafPolygonIterator(){
    return new TreeNodeIterator(this){
      //if it isn't a leaf AND a polygon then skip it
      public boolean skip(TreeNode node){
        return !((node instanceof FPolygon)&&(node.isLeaf()));}};}
  
  /**
   * @return nodes in the branch rooted at this node
   */
  public TreeNodeIterator getNodeIterator(){
    return new TreeNodeIterator(this);}
  
  /*
   * ################################
   * GENERAL PURPOSE OBJECT
   * ################################
   */
  
  public Object gpobject;
  
//  /*
//   * ################################
//   * GEOMETRY CACHE FLUSH
//   * We cache much geometry data. 
//   * If the root grid node changes then everything dependent upon it changes too
//   * flush and (lazily) recalculate  
//   * ################################
//   */
//  
//  public void flush(){
//    flushLocalGeometryCache();
//    for(TreeNode n:treenodeservices.getChildren())
//      ((ForsythiaTreeNode)n).flush();}
//  
//  protected abstract void flushLocalGeometryCache();
  
}
