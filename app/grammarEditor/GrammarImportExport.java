package org.fleen.forsythia.app.grammarEditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JFileChooser;

import org.fleen.forsythia.app.grammarEditor.project.ProjectGrammar;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;

public class GrammarImportExport implements Serializable{
  
  private static final long serialVersionUID=796695767401225914L;
  
  /*
   * ################################
   * IMPORT EXPORT DIR
   * ################################
   */
  
  private File importexportdir=null;
  
  private File getImportExportDir(){
    if(importexportdir==null||!importexportdir.isDirectory())
      importexportdir=GE.getLocalDir();
    return importexportdir;}
  
  /*
   * ################################
   * IMPORT
   * ################################
   */
  
  public void importGrammar(){
    File path=getGrammarFile();
    importGrammar(path);}
  
  public void importGrammar(File path){
    System.out.println("importing grammar from file:"+path);
    if(path==null)return;
    ForsythiaGrammar fg=null;
    try{
      fg=extractForsythiaGrammarFromFile(path);
    }catch(Exception x){
      x.printStackTrace();}
    GE.ge.focusgrammar=new ProjectGrammar(fg,path.getName());
    initFocusElementsForNewGrammar();}
  
  private void initFocusElementsForNewGrammar(){
    if(GE.ge.focusgrammar.hasMetagons()){
      GE.ge.focusmetagon=GE.ge.focusgrammar.getMetagon(0);
      if(GE.ge.focusmetagon!=null)
        GE.ge.focusjig=GE.ge.focusmetagon.getJig(0);
    }else{
      GE.ge.focusmetagon=null;
      GE.ge.focusjig=null;}}

  private File getGrammarFile(){
    JFileChooser fc=new JFileChooser();
    fc.setCurrentDirectory(getImportExportDir());
    int r=fc.showOpenDialog(GE.ge.getUIMain());
    if(r!=JFileChooser.APPROVE_OPTION)
      return null;
    File selectedfile=fc.getSelectedFile();
    importexportdir=fc.getCurrentDirectory();
    return selectedfile;}
  
  private ForsythiaGrammar extractForsythiaGrammarFromFile(File file){
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
  
  public void exportGrammar(){
    exportGrammar(GE.ge.focusgrammar);}
  
  public void exportGrammar(ProjectGrammar grammar){
    File path=getExportFile();
    if(path==null)return;
    exportGrammar(grammar,path);}
  
  public void exportGrammar(ProjectGrammar grammar,File path){
    ForsythiaGrammar g=grammar.getForsythiaGrammar();
    if(g==null)return;
    writeExportFile(g,path);}
  
  private File getExportFile(){
    JFileChooser fc=new JFileChooser();
    fc.setCurrentDirectory(getImportExportDir());
    if(fc.showSaveDialog(GE.ge.getUIMain())!=JFileChooser.APPROVE_OPTION)return null;
    File path=fc.getSelectedFile();
    importexportdir=fc.getCurrentDirectory();
    return path;}
  
  private void writeExportFile(Serializable grammar,File file){
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
