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
public class CM_SymmetricChaos extends HashMap<FPolygon,Color> implements ColorMap{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public CM_SymmetricChaos(ForsythiaComposition composition,Color[] palette){
    this.palette=palette;
    doPolygonColors(composition);}
  
  /*
   * ################################
   * POLYGON COLOR SELECTION
   * ################################
   */
  
  private Color[] palette;
  
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
      int colorindex=rnd.nextInt(palette.length);
      color=palette[colorindex];
      colorbysig.put(sig,color);}
    return color;}

  @Override
  public void map(ForsythiaComposition composition,Color[] palette){
    // TODO Auto-generated method stub
    
  }

  @Override
  public void regenerate(){
    // TODO Auto-generated method stub
    
  }
  

}
