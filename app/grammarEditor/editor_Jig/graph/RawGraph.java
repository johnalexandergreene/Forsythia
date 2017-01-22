package org.fleen.forsythia.app.grammarEditor.editor_Jig.graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.geom_Kisrhombille.KPolygon;
import org.fleen.geom_Kisrhombille.KVertex;

/*
 * This is what we build when we draw with editor_Jig
 * It's a bunch of vertices and connections between vertices
 * We edit it. It's for editing. We add and remove vertices and edges.
 * 
 * From this we derive a DisconnectedGraph
 *   which contains 1..n ConnectedGraphs
 *     which contain artifacts like vertices, edges and polygons
 *     
 * We talk to the RawGraph in terms of KVertices
 * Internally we use GVertices (Graph Vertices)
 * 
 * FROM THIS WE DERIVE A DISCONNECTED GRAPH
 * 
 * The DisconnectedGraph is this raw graph analyzed and organized. 
 *  
 * The DisconnectedGraph contains 0..n connected graphs. 
 * each connected graph is composed of 
 *   1..n vertices 
 *   0..n edges
 *   0..n kpolygons
 * 
 * The DisconnectedGraph also contains 0..n Areas (space between nesting ConnectedGraphs)
 * 
 * 
 * 
 * TODO
 * generalize this. stick it in org.fleen.util
 * 
 */
public class RawGraph{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  /*
   * We're creating this graph within a host polygon to define a jig
   * the host polygon is basically immutable. 
   * We can add and remove vertices to it but its form remains the same.
   * We achieve this immutability by adding the polygon to the graph every time we make any other modification.
   * ex : add a vertex, then add hostpolygon. remove a vertex, then add hostpolygon.
   * It's brutish, but its simple and it works. 
   * 
   * 
   * we scale the polygon by griddensity
   * 
   */
  public RawGraph(KPolygon p,int griddensity){
//    hostkpolygon=p.getKMetagon().getPolygon(griddensity,true);
    hostkpolygon=GE.ge.editor_jig.getScaledHostPolygon();
    addHostKPolygon();
    }
  
  /*
   * ################################
   * HOST POLYGON
   * ################################
   */
  
  KPolygon hostkpolygon;
  
  /*
   * ################################
   * EDIT THIS RAW GRAPH
   * This is where we add, remove and connect vertices
   * ################################
   */
  
  public Set<GVertex> vertices=new HashSet<GVertex>();
  public Set<GEdge> edges=new HashSet<GEdge>();
  
  public boolean contains(KVertex v){
    for(GVertex gv:vertices)
      if(gv.kvertex.equals(v))
        return true;
    return false;}
  
  /*
   * create a new gvertex and stick in the set
   */
  public void addVertex(KVertex v){
    invalidateDisconnectedGraph();
    GVertex gv=new GVertex(v);
    vertices.add(gv);}
  
  //remove the vertex and any connected edges
  //if there is no such gvertex associated with the specified, or
  //if the gvertex is immutable, then fail.
  public void removeVertex(KVertex v){
    GVertex gv=getGVertex(v);
    invalidateDisconnectedGraph();
    vertices.remove(gv);
    Iterator<GEdge> i=edges.iterator();
    GEdge edge;
    while(i.hasNext()){
      edge=i.next();
      if(edge.uses(gv)){
        edge.disconnect();
        i.remove();}}}
  
  //connect the specified vertices by creating an edge and setting references
  public void connect(KVertex v0,KVertex v1){
    System.out.println("CONNECT V0 V1");
    invalidateDisconnectedGraph();
    GVertex 
      gv0=getGVertex(v0),
      gv1=getGVertex(v1);
    //create our new edge
    //if it doesn't already exist then set appropriate references and stick it in the edges set
    GEdge e=new GEdge(gv0,gv1);
    if(!edges.contains(e)){
      edges.add(e);
      gv0.edges.add(e);
      gv1.edges.add(e);}}
  
  public void disconnect(KVertex v0,KVertex v1){
    GVertex 
      gv0=getGVertex(v0),
      gv1=getGVertex(v1);
    disconnect(gv0,gv1);}
  
  public void disconnect(GVertex v0,GVertex v1){
    invalidateDisconnectedGraph();
    GEdge e=new GEdge(v0,v1);
    v0.edges.remove(e);
    v1.edges.remove(e);
    edges.remove(e);}
  
  //get gvertex associated with the specified kvertex
  public GVertex getGVertex(KVertex kv){
    GVertex gv0=null;
    for(GVertex gv:vertices)
      if(gv.kvertex.equals(kv)){
        gv0=gv;
        break;}
    if(gv0==null)throw new IllegalArgumentException("specified kvertex is not in this graph : "+kv);
    return gv0;}
  
  /*
   * returns the edge that the specified vertex get crossed by
   * returns null if the vertex doesn't get crossed by any edge 
   */
  public GEdge getCrossingEdge(KVertex v){
    for(GEdge e:edges)
      if(e.crosses(v))
        return e;
    return null;}
  
  private void addHostKPolygon(){
    int s,i0,i1;
    KVertex v0,v1;
    s=hostkpolygon.size();
    for(KVertex v:hostkpolygon)
      addVertex(v);
    for(i0=0;i0<s;i0++){
      i1=i0+1;
      if(i1==s)i1=0;
      v0=hostkpolygon.get(i0);
      v1=hostkpolygon.get(i1);
      connect(v0,v1);}}
  
  /*
   * ################################
   * DISCONNECTED GRAPH
   * It's an orderly, analyzed version of the raw graph
   * We derive it from the raw graph
   * It contains 
   *   1..n ConnectedGraphs
   *   Areas (space between ConnectedGraphs)
   * ConnectedGraphs nest, so we arrange them in 1..n trees
   * ConnectedGraphs contain vertices, edges, polygons
   * ################################
   */
  
  private DisconnectedGraph disconnectedgraph=null;
  
  public DisconnectedGraph getDisconnectedGraph(){
    if(disconnectedgraph==null){
      //this is how we make sure that the host polygon, the polygon that the jig is jigging, persists
      //we just keep adding it. If it's redundant then that ok. If it isn't then whatever hospolygon structure
      //that needs healing gets healed.
//      addHostKPolygon();
      //
      disconnectedgraph=new DisconnectedGraph(this);}
    return disconnectedgraph;}
  
  /*
   * the disconnected graph is an interpretation of the raw graph 
   * so when the raw graph changes we invalidate this disconnected graph
   * because it no longer accurately reflects the raw graph 
   */
  public void invalidateDisconnectedGraph(){
    disconnectedgraph=null;}
  
  /*
   * ################################
   * JIG SYSTEM INFO
   * ################################
   */
  
  public String getInfo(){
    int osc=getDisconnectedGraph().getOpenKVertexSequences().size();
    if(osc>0)return "incomplete";
    List<KPolygon> up=getDisconnectedGraph().getUndividedPolygons();
    if(up.size()<2)return "incomplete";
//    Forsythia.JigSystemAnalysis jsa=Forsythia.getJigSystemAnalysis(hostkpolygon,up);
    return "foo";}
  

}
