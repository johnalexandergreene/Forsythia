package org.fleen.forsythia.app.compositionGenerator.generators.fc0020_poster_18x30_redblackwhite;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.app.compositionGenerator.colorMap.ColorMap;
import org.fleen.forsythia.app.compositionGenerator.colorMap.ColorMapGen;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.util.tree.TreeNode;

/*
 * use egg tag to distinguish nesting levels
 * at each nesting level use half of the palette, randomly
 * dupe colors by sig, for symmetry
 * this looks good with strokes
 */
@SuppressWarnings("serial")
public class CM_SymmetricChaos_EggLevelBW extends HashMap<FPolygon,Color> implements ColorMapGen,ColorMap{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public CM_SymmetricChaos_EggLevelBW(ForsythiaComposition composition,Color[] palette){
    map(composition,palette);}
  
  public CM_SymmetricChaos_EggLevelBW(){}
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public ColorMap getColorMap(){
    return new CM_SymmetricChaos_EggLevelBW(composition,palette);}
  
  public void setPalette(Color[] p){
    palette=p;}
  
  public Color[] getPalette(){
    return palette;}
  
  public void setComposition(ForsythiaComposition c){
    composition=c;}
  
  public ForsythiaComposition getComposition(){
    return composition;}
  
  /*
   * ################################
   * MAPPING METHODS
   * ################################
   */
  
  ForsythiaComposition composition;
  Color[] palette;
  
  public void map(ForsythiaComposition composition,Color[] palette){
    clear();
    this.composition=composition;
    this.palette=palette;
    doPolygonColors();}
  
  public void regenerate(){
    map(composition,palette);}
  
  /*
   * ################################
   * POLYGON COLOR SELECTION
   * ################################
   */
  
  public Color getColor(FPolygon p){
    return get(p);}
  
  private void doPolygonColors(){
    Random rnd=new Random();
    Map<FPolygonSignature,Color> colorbysig=new HashMap<FPolygonSignature,Color>();
    Iterator<TreeNode> i=composition.getLeafPolygonIterator();
    FPolygon p;
    Color c;
    while(i.hasNext()){
      p=(FPolygon)i.next();
      c=getColor(p,rnd,colorbysig);
      put(p,c);}}
  
  private Color getColor(FPolygon polygon,Random rnd,Map<FPolygonSignature,Color> colorbysig){
    FPolygonSignature sig=polygon.getSignature();
    Color color=colorbysig.get(sig);
    if(color==null){
      int 
        eggdepth=getEggDepth(polygon),
        colorindex=eggdepth%2;
      color=palette[colorindex];
      colorbysig.put(sig,color);}
    return color;}
  
  private static final String EGGTAG="egg";
  
  private int getEggDepth(TreeNode node){
    int c=0;
    TreeNode n=node;
    FPolygon p;
    while(n!=null){
      if(n instanceof FPolygon){
        p=(FPolygon)n;
        if(p.hasTags(EGGTAG))
          c++;}
      n=n.getParent();}
    return c;}

}
