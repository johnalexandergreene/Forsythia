package org.fleen.forsythia.util.simpleComposer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JFileChooser;

import org.fleen.forsythia.composition.FPolygon;
import org.fleen.forsythia.composition.FPolygonSignature;
import org.fleen.forsythia.composition.ForsythiaComposition;
import org.fleen.forsythia.grammar.FMetagon;
import org.fleen.forsythia.grammar.ForsythiaGrammar;

/*
 * given a grammar and a listener, compose forsythia structure.
 * We can create a new composition from scratch and we can also add to an existing forsythia composition
 * 
 * The composition process goes like this :
 * 
 * for each leaf polygon : target
 *   get a jig
 *   create new nodes (grid and polygons) using jig and target
 *  
 * 
 */
public abstract class ForsythiaSimpleComposer_Abstract implements ForsythiaSimpleComposer,Serializable{
  
  private static final long serialVersionUID=-2006956324022097134L;
  
  /*
   * ################################
   * PROCESS CONTROL
   * Note that the way we've got it going here we can create a new composition from scratch and we can also build upon an existing composition
   * ################################
   */
  
  public ForsythiaComposition compose(ForsythiaGrammar grammar){
    ForsythiaComposition composition=initComposition(grammar);
    build(composition);
    return composition;}
  
  public ForsythiaComposition compose(ForsythiaComposition composition){
    build(composition);
    return composition;}
  
  /*
   * ################################
   * BUILD
   * ################################
   */
  
  //handy reference
  protected int buildcycleindex;
  
  private void build(ForsythiaComposition composition){
    boolean creatednodes=true;
    buildcycleindex=0;
    while(creatednodes){
      buildcycleindex++;
      creatednodes=createNodes(composition);}}
    
  /*
   * try to create nodes.
   * if nodes were created then return true
   * if nodes were not created then return false
   */
  protected abstract boolean createNodes(ForsythiaComposition composition);
  
  /*
   * ################################
   * INIT COMPOSITION
   * ################################
   */
  
  private ForsythiaComposition initComposition(ForsythiaGrammar grammar){
    ForsythiaComposition composition=new ForsythiaComposition();
    composition.setGrammar(grammar);
    FPolygon rootpolygon=createRootPolygon(grammar);
    composition.initTree(rootpolygon);
    return composition;}
  
  /*
   * look for metagons tagged root
   * if we can't find one then pick any metagon
   */
  private FPolygon createRootPolygon(ForsythiaGrammar grammar){
    List<FMetagon> metagons=grammar.getMetagons();
    if(metagons.isEmpty())
      throw new IllegalArgumentException("this grammar has no metagons");
    List<FMetagon> rootmetagons=new ArrayList<FMetagon>();
    for(FMetagon m:metagons)
      if(m.hasTag("root"))
        rootmetagons.add(m);
    FMetagon m;
    if(!rootmetagons.isEmpty())
      m=rootmetagons.get(new Random().nextInt(rootmetagons.size()));
    else
      m=metagons.get(new Random().nextInt(metagons.size()));
    FPolygon p=new FPolygon(m);
    return p;}
  
  /*
   * ################################
   * POLYGON CAPPING UTILITY
   * mark a polygon as capped
   * this means that the polygon will not be further cultivated
   *   if encountered in the cultivation cycle we skip it
   * ################################
   */
  
  private Set<FPolygonSignature> capped=new HashSet<FPolygonSignature>();
  
  protected void cap(FPolygon polygon){
    capped.add(polygon.getSignature());}
  
  protected boolean isCapped(FPolygon polygon){
    return capped.contains(polygon.getSignature());}
  
  protected void flushCappedSet(){
    capped.clear();}
  
  /*
   * ################################
   * SERIALIZATION UTIL
   * export this instance to a file
   * ################################
   */
  
  public void serialize(){
    File f=getExportFile();
    writeExportFile(this,f);}
  
  public void serialize(String dirpath){
    File f=new File(dirpath+"/"+this.getClass().getSimpleName());
    writeExportFile(this,f);}
  
  private File getExportFile(){
    JFileChooser fc=new JFileChooser();
    fc.setCurrentDirectory(null);
    if(fc.showSaveDialog(null)!=JFileChooser.APPROVE_OPTION)return null;
    return fc.getSelectedFile();}
  
  private void writeExportFile(Serializable composer,File file){
    FileOutputStream fos;
    ObjectOutputStream oot;
    try{
      fos=new FileOutputStream(file);
      oot=new ObjectOutputStream(fos);
      oot.writeObject(composer);
      oot.close();
    }catch(IOException ex){
      System.out.println("%-%-% EXCEPTION IN SERIALIZE %-%-%");
      ex.printStackTrace();}}
  
}
