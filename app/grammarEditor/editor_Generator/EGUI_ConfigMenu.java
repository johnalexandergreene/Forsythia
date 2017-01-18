package org.fleen.forsythia.app.grammarEditor.editor_Generator;

import javax.swing.JPopupMenu;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.util.PopupMenuItem;

@SuppressWarnings("serial")
class EGUI_ConfigMenu extends JPopupMenu{
  
  EGUI_ConfigMenu(){
    add(configuregenerator);
    add(configurecompositionexporter);
    add(configuregrammar);}
  
  private PopupMenuItem configuregenerator=new PopupMenuItem("Generator",true){
    protected void doThing(){
      GE.ge.editor_generator.openGeneratorConfigurator();}};
      
  private PopupMenuItem configurecompositionexporter=new PopupMenuItem("Exporter",true){
    protected void doThing(){
      GE.ge.editor_generator.openCompositionExporterConfigurator();}};
          
  private PopupMenuItem configuregrammar=new PopupMenuItem("Grammar",true){
    protected void doThing(){
      GE.ge.editor_generator.openGrammarConfigurator();}};

}
