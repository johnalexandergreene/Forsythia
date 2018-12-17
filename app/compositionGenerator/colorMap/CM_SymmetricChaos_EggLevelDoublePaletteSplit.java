package org.fleen.forsythia.app.compositionGenerator.colorMap;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

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
public class CM_SymmetricChaos_EggLevelDoublePaletteSplit extends HashMap<FPolygon,Color> implements ColorMap{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public CM_SymmetricChaos_EggLevelDoublePaletteSplit(ForsythiaComposition composition,Color[] palette){
    initPalettes(palette);
    doPolygonColors(composition);}
  
  /*
   * ################################
   * POLYGON COLOR SELECTION
   * ################################
   */
  
  public Color getColor(FPolygon p){
    return get(p);}
  
  private void doPolygonColors(ForsythiaComposition composition){
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
        colorindex;
      //even level
      if(eggdepth%2==0){
        colorindex=rnd.nextInt(palette0.length);
        color=palette0[colorindex];
      //odd level
      }else{
        colorindex=rnd.nextInt(palette1.length);
        color=palette1[colorindex];}
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
  
  /*
   * ################################
   * PALETTE
   * we split the param palette
   * half for polygons with even eggdepth and the other half for odd.
   * ################################
   */
  
  private Color[] palette0,palette1;
  
  private void initPalettes(Color[] palette){
    int a=palette.length/2;
    palette0=new Color[a];
    palette1=new Color[palette.length-a];
    for(int i=0;i<a;i++)
      palette0[i]=palette[i];
    for(int i=a;i<palette.length;i++)
      palette1[i-a]=palette[i];}

  @Override
  public void map(ForsythiaComposition composition,Color[] palette){
    // TODO Auto-generated method stub
    
  }

  @Override
  public void regenerate(){
    // TODO Auto-generated method stub
    
  }

}
