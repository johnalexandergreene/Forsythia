package org.fleen.forsythia.composition;

import java.util.ArrayList;
import java.util.List;

import org.fleen.forsythia.Forsythia;
import org.fleen.forsythia.grammar.ForsythiaGrammar;
import org.fleen.forsythia.grammar.FMetagon;
import org.fleen.util.tree.TreeNode;
import org.fleen.util.tree.TreeNodeIterator;

/*
 * A tree of polygons and grids
 * some convenient methods for init and node-access
 * 
 * This is our Forsythia polygon node composition 
 * It manages a tree of nodes : polygons and grids
 * 
 * we init the tree, build it, access it
 */
public class ForsythiaComposition implements Forsythia{
  
  /*
   * ################################
   * GRAMMAR
   * ################################
   */
  
  private ForsythiaGrammar grammar=null;
  
  public void setGrammar(ForsythiaGrammar grammar){
    this.grammar=grammar;}
  
  public ForsythiaGrammar getGrammar(){
    return grammar;}
  
  /*
   * ################################
   * INIT
   * ################################
   */
  
  //create a root grid. We use the default.
  //create a root polygon from the specified metagon. We use the default.
  //set parent-child relationships
  public void initTree(FMetagon rootpolygonmetagon){
    root=new FGridRoot();
    FPolygon p=new FPolygon(rootpolygonmetagon);
    root.setChild(p);
    p.setParent(root);}
  
  //create a root grid. We use the default.
  //set parent-child relationships
  public void initTree(FPolygon rootpolygon){
    root=new FGridRoot();
    root.setChild(rootpolygon);
    rootpolygon.setParent(root);}
  
  public void initTree(FGridRoot grid,FPolygon rootpolygon){
    root=grid;
    root.setChild(rootpolygon);
    rootpolygon.setParent(root);}
  
  /*
   * ################################
   * TREE NODES
   * The root is a grid
   * The nodes are grids and polygons
   * ################################
   */
  
  public FGridRoot root;

  public FGridRoot getRoot(){
    return root;}
  
  public void setRoot(FGridRoot root){
    this.root=root;}

  public TreeNodeIterator getNodeIterator(){
    return new TreeNodeIterator(root);}
  
  public List<ForsythiaTreeNode> getNodes(){
    List<ForsythiaTreeNode> nodes=new ArrayList<ForsythiaTreeNode>();
    TreeNodeIterator i=getNodeIterator();
    while(i.hasNext())
      nodes.add((ForsythiaTreeNode)i.next());
    return nodes;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * GRIDS
   * ++++++++++++++++++++++++++++++++
   */
  
  public TreeNodeIterator getGridIterator(){
    return new GridNodeIterator(root);}
  
  private class GridNodeIterator extends TreeNodeIterator{

    public GridNodeIterator(ForsythiaTreeNode root){
      super(root);}

    protected boolean skip(TreeNode node){
      return !(node instanceof FGrid);}}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * POLYGONS
   * ++++++++++++++++++++++++++++++++
   */
  
  public FPolygon getRootPolygon(){
    FPolygon a=(FPolygon)root.getChild();
    return a;}

  public TreeNodeIterator getPolygonIterator(){
    return new PolygonIterator(root);}
  
  public List<FPolygon> getPolygons(){
    List<FPolygon> polygons=new ArrayList<FPolygon>();
    TreeNodeIterator i=getPolygonIterator();
    while(i.hasNext())
      polygons.add((FPolygon)i.next());
    return polygons;}
  
  public TreeNodeIterator getLeafPolygonIterator(){
    return new LeafPolygonIterator(root);}
  
  public List<FPolygon> getLeafPolygons(){
    List<FPolygon> polygons=new ArrayList<FPolygon>();
    TreeNodeIterator i=getLeafPolygonIterator();
    while(i.hasNext())
      polygons.add((FPolygon)i.next());
    return polygons;}
  
  private class PolygonIterator extends TreeNodeIterator{

    public PolygonIterator(TreeNode root){
      super(root);}

    protected boolean skip(TreeNode node){
      return(!(node instanceof FPolygon));}}
 
  private class LeafPolygonIterator extends TreeNodeIterator{

    public LeafPolygonIterator(ForsythiaTreeNode root){
      super(root);}

    protected boolean skip(TreeNode node){
      return !(node instanceof FPolygon&&node.isLeaf());}}
 
}
