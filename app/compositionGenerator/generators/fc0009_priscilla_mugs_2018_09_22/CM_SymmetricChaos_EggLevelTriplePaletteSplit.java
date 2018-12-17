package org.fleen.forsythia.app.compositionGenerator.generators.fc0009_priscilla_mugs_2018_09_22;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
public class CM_SymmetricChaos_EggLevelTriplePaletteSplit extends HashMap<FPolygon,Color> implements ColorMapGen,ColorMap{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public CM_SymmetricChaos_EggLevelTriplePaletteSplit(ForsythiaComposition composition,Color[] palette){
    map(composition,palette);}
  
  public CM_SymmetricChaos_EggLevelTriplePaletteSplit(){}
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public ColorMap getColorMap(){
    return new CM_SymmetricChaos_EggLevelTriplePaletteSplit(composition,palette);}
  
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
    initSubpalettes();
    doPolygonColors();}
  
  public void regenerate(){
    map(composition,palette);}
  
  /*
   * ################################
   * SUBPALETTES
   * ################################
   */
  
  private Color[] sp0,sp1,sp2;
  
  private void initSubpalettes(){
    int a=palette.length/3;
    sp0=new Color[a];
    sp1=new Color[a];
    sp2=new Color[palette.length-a*2];
    for(int i=0;i<palette.length;i++){
      if(i<a){
        sp0[i]=palette[i];
      }else if(i>=a&&i<a*2){
        sp1[i-a]=palette[i];
      }else{
        sp2[i-a*2]=palette[i];}}
    //randomize the subpalette order
    List<Color[]> r=new ArrayList<Color[]>();
    r.add(sp0);
    r.add(sp1);
    r.add(sp2);
    Collections.shuffle(r,new Random());
    sp0=r.get(0);
    sp1=r.get(1);
    sp2=r.get(2);}
  
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
        colorindex,
        di=eggdepth%3;
      if(di==0){
        colorindex=rnd.nextInt(sp0.length);
        color=sp0[colorindex];
      }else if(di==1){
        colorindex=rnd.nextInt(sp1.length);
        color=sp1[colorindex];
      }else{
        colorindex=rnd.nextInt(sp2.length);
        color=sp2[colorindex];}
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
