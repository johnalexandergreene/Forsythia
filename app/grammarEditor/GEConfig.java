package org.fleen.forsythia.app.grammarEditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.fleen.forsythia.app.grammarEditor.compositionExporter.CompositionExportConfig;
import org.fleen.forsythia.app.grammarEditor.generator.GeneratorConfig;
import org.fleen.forsythia.app.grammarEditor.project.GrammarImportExport;
import org.fleen.forsythia.grammar.ForsythiaGrammar;

/*
 * Quasar configuration object
 * 
 * paths to grammar import, grammar export
 * get grammar for init
 * path to composition export
 * window state
 * 
 * configs for generator, composition exporter..
 * 
 */

public class GEConfig implements Serializable{

  private static final long serialVersionUID=6610105776650927620L;
  
  static final String FILENAME="Q.config";
  
  /*
   * ################################
   * SAVE AND LOAD THIS CONFIG
   * ################################
   */
  
  //write FQ.mainconfig to local dir
  public static final void save(){
//    Q.config.generatorconfig.flush();
//    //
//    String pathtoconfig=Q.getLocalDir().getPath()+"/"+FILENAME;
//    System.out.println("writing config : "+pathtoconfig);
//    FileOutputStream fos;
//    ObjectOutputStream oot;
//    File file=new File(pathtoconfig);
//    try{
//      fos=new FileOutputStream(file);
//      oot=new ObjectOutputStream(fos);
//      oot.writeObject(Q.config);
//      oot.close();
//    }catch(IOException ex){
//      System.out.println("Exception in write config");
//      ex.printStackTrace();}
//    
  }
  
  //get the config file from the local dir
  //create a new MainConfig object if load fails
  public static final void load(){
    String pathtoconfig=GE.getLocalDir().getPath()+"/"+FILENAME;
    System.out.println("reading config : "+pathtoconfig);
    try{
      FileInputStream a=new FileInputStream(pathtoconfig);
      ObjectInputStream b=new ObjectInputStream(a);
      GE.config=(GEConfig)b.readObject();
      b.close();
    }catch(Exception e){
      System.out.println("Read config returned null");}
    //if load failed then create new MainConfig object using all defaults
    if(GE.config==null)GE.config=new GEConfig();
    //finally, init
    GE.config.init();}
  
  /*
   * ################################
   * INIT
   * ################################
   */
  
  void init(){
    initGrammar();
    //init whatever
  }
  
  /*
   * ################################
   * GRAMMAR
   * ################################
   */
  
  static final String DEFAULTGRAMMARNAME="samplegrammar0000";
  
  //path to the serialized file version of the grammar presently in focus
  private File grammarfilepath=null;
  
  private void initGrammar(){
    File pathtodefaultgrammarfile=initDefaultGrammarFile();
    //if grammarfilepath is null then use the path to the default
    if(grammarfilepath==null)grammarfilepath=pathtodefaultgrammarfile;
    //import the grammar file indicated by grammarfilepath and set that to our working grammar 
    GrammarImportExport.importGrammar(grammarfilepath);}
  
  /*
   * create our default grammar file at appdir/samplegrammar0000
   * don't check for its existence or validate its noncorruptedness or anything, 
   * just create it every time at startup. 
   * It's easy and makes 100% sure that we have a working default grammar.
   */
  private File initDefaultGrammarFile(){
    //get the default grammar
    ForsythiaGrammar fg=null;
    try{
      InputStream a=GE.class.getResourceAsStream("assets/"+DEFAULTGRAMMARNAME);
      ObjectInputStream b=new ObjectInputStream(a);
      fg=(ForsythiaGrammar)b.readObject();
      b.close();
    }catch(Exception e){
      e.printStackTrace();}
    //write it
    String path=GE.getLocalDir().getPath()+"/"+DEFAULTGRAMMARNAME;
    FileOutputStream fos;
    ObjectOutputStream oot;
    File file=new File(path);
    try{
      fos=new FileOutputStream(file);
      oot=new ObjectOutputStream(fos);
      oot.writeObject(fg);
      oot.close();
    }catch(IOException ex){
      ex.printStackTrace();}
    return file;}
  
  /*
   * ################################
   * GENERATOR CONFIG
   * ################################
   */
  
  private GeneratorConfig generatorconfig=null;
  
  public GeneratorConfig getGeneratorConfig(){
    if(generatorconfig==null)generatorconfig=new GeneratorConfig();
    return generatorconfig;}
  
  /*
   * ################################
   * IMPORT/EXPORT GRAMMAR PATH
   * ################################
   */
  
  /*
   * ################################
   * COMPOSITION EXPORT CONFIG
   * ################################
   */
  
  CompositionExportConfig compositionexportconfig=null;
  
  public CompositionExportConfig getCompositionExportConfig(){
    if(compositionexportconfig==null)
      compositionexportconfig=new CompositionExportConfig();
    return compositionexportconfig;}
  
}
