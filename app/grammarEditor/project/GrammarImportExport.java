package org.fleen.forsythia.app.grammarEditor.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JFileChooser;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.grammar.ForsythiaGrammar;

/*
 * converts the project grammar into a serialized forsythia grammar and saves it somewhere.
 * in azalea processes we can convert the forsythia grammar into something more appropriate.
 */
public class GrammarImportExport{
  
  /*
   * ################################
   * IMPORT
   * ################################
   */
  
  public static final void importGrammar(){
    File path=getGrammarFile();
    importGrammar(path);}
  
  public static final void importGrammar(File path){
    System.out.println("importing grammar from file:"+path);
    if(path==null)return;
    ForsythiaGrammar fg=null;
    try{
      fg=extractForsythiaGrammarFromFile(path);
    }catch(Exception x){
      x.printStackTrace();}
    GE.focusgrammar=new ProjectGrammar(fg);
    System.out.println("imported : "+fg);
    initFocusElementsForNewGrammar();}
  
  private static final void initFocusElementsForNewGrammar(){
    if(GE.focusgrammar.hasMetagons()){
      GE.focusmetagon=GE.focusgrammar.getMetagon(0);
      if(GE.focusmetagon!=null)
        GE.focusjig=GE.focusmetagon.getProtoJig(0);
    }else{
      GE.focusmetagon=null;
      GE.focusjig=null;}}

  private static final File getGrammarFile(){
    JFileChooser fc=new JFileChooser();
    fc.setCurrentDirectory(GE.getLocalDir());
    int r=fc.showOpenDialog(GE.uimain);
    if(r!=JFileChooser.APPROVE_OPTION)
      return null;
    return fc.getSelectedFile();}
  
  private static final ForsythiaGrammar extractForsythiaGrammarFromFile(File file){
    FileInputStream fis;
    ObjectInputStream ois;
    ForsythiaGrammar fg=null;
    try{
      fis=new FileInputStream(file);
      ois=new ObjectInputStream(fis);
      fg=(ForsythiaGrammar)ois.readObject();
      ois.close();
    }catch(Exception e){
      System.out.println("#^#^# EXCEPTION IN EXTRACT GRAMMAR FROM FILE FOR IMPORT #^#^#");
      e.printStackTrace();
      return null;}
    return fg;}
  
  /*
   * ################################
   * EXPORT
   * ################################
   */
  
  static final String FILE_NAME_SUFFIX="grammar";
  
  public static final void exportGrammar(){
    File f=getExportFile(FILE_NAME_SUFFIX);
    if(f==null)return;
    ForsythiaGrammar g=GE.focusgrammar.getForsythiaGrammar();
    if(g==null)return;
    writeExportFile(g,f);}
  
  private static final File getExportFile(String suffix){
    JFileChooser fc=new JFileChooser();
    fc.setCurrentDirectory(GE.focusgrammar.grammarfilepath);
    if(fc.showSaveDialog(GE.uimain)!=JFileChooser.APPROVE_OPTION)return null;
    GE.focusgrammar.grammarfilepath=fc.getSelectedFile();
    return GE.focusgrammar.grammarfilepath;}
  
  private static final void writeExportFile(Serializable grammar,File file){
    FileOutputStream fos;
    ObjectOutputStream oot;
    try{
      fos=new FileOutputStream(file);
      oot=new ObjectOutputStream(fos);
      oot.writeObject(grammar);
      oot.close();
    }catch(IOException ex){
      System.out.println("%-%-% EXCEPTION IN EXPORT GRAMMAR %-%-%");
      ex.printStackTrace();}}
  
}
