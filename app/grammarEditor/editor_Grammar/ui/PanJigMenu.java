package org.fleen.forsythia.app.grammarEditor.editor_Grammar.ui;

import java.util.List;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.project.jig.ProjectJig;
import org.fleen.forsythia.app.grammarEditor.util.ElementMenu;
import org.fleen.forsythia.app.grammarEditor.util.ElementMenuItem;
import org.fleen.forsythia.app.grammarEditor.util.UI;

public class PanJigMenu extends ElementMenu{

  private static final long serialVersionUID=3987915843219850992L;

  public PanJigMenu(){
    super(UI.ELEMENTMENU_OVERVIEWJIGSROWS);}

  protected void doPopupMenu(int x,int y){}

  protected List<? extends ElementMenuItem> getItems(){
    if(GE.ge==null||GE.ge.focusmetagon==null)return null;
    return GE.ge.focusmetagon.getJigs();}
  
  protected boolean isFocusItem(ElementMenuItem i){
    return i==GE.ge.focusjig;}

  protected void setFocusItem(ElementMenuItem i){
    GE.ge.editor_grammar.setFocusJig((ProjectJig)i);}
  
  protected void doDoubleclick(ElementMenuItem i){
    GE.ge.editor_grammar.editJig();}

}
