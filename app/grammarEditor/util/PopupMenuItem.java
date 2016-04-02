package org.fleen.forsythia.app.grammarEditor.util;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;


@SuppressWarnings("serial")
public abstract class PopupMenuItem extends JMenuItem{
  
  public PopupMenuItem(String s,boolean enabled){
    super(s);
    setBackground(Color.orange);
    setForeground(Color.black);
    setEnabled(enabled);
    setFont(UI.POPUPMENUFONT);
    addActionListener(
      new ActionListener(){
        public void actionPerformed(ActionEvent e){
          doThing();}});}
  
  public PopupMenuItem(String s){
    this(s,true);}
  
  protected abstract void doThing();

}
