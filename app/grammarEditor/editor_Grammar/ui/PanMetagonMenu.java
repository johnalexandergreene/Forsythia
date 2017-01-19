package org.fleen.forsythia.app.grammarEditor.editor_Grammar.ui;

import java.util.List;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.project.ProjectMetagon;
import org.fleen.forsythia.app.grammarEditor.util.ElementMenu;
import org.fleen.forsythia.app.grammarEditor.util.ElementMenuItem;
import org.fleen.forsythia.app.grammarEditor.util.UI;


@SuppressWarnings("serial")
public class PanMetagonMenu extends ElementMenu{

  /**
   * Create the panel.
   */
  public PanMetagonMenu(){
    super(UI.ELEMENTMENU_OVERVIEWMETAGONSROWS);}

  protected void doPopupMenu(int x,int y){}
  
  protected List<? extends ElementMenuItem> getItems(){
    if(GE.ge==null||GE.ge.focusgrammar==null)return null;
    return GE.ge.focusgrammar.metagons;}
  
  protected boolean isFocusItem(ElementMenuItem i){
    return i==GE.ge.focusmetagon;}
  
  protected void setFocusItem(ElementMenuItem i){
    GE.ge.editor_grammar.setFocusMetagon((ProjectMetagon)i);}
  
  protected void doDoubleclick(ElementMenuItem i){
    GE.ge.editor_grammar.editMetagon();}

}
