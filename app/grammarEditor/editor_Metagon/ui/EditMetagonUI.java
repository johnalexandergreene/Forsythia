package org.fleen.forsythia.app.grammarEditor.editor_Metagon.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.util.UI;
import org.fleen.util.ui.WrapLayout;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
import javax.swing.Box;

public class EditMetagonUI extends JPanel{
  
  private static final long serialVersionUID=3242479197632226748L;
  
  public JPanel pantop;
  public EditMetagonGrid pangrid;
  public JLabel lblinfo;
  
  public JButton btnquit;
  public JButton btnsave;
  
  public PanMetagonTags panmetagontag;
  
  public EditMetagonUI(){
    setLayout(new BorderLayout(0, 0));
    
    pantop = new JPanel();
    add(pantop, BorderLayout.NORTH);
    WrapLayout layouttop=new WrapLayout();
    layouttop.setAlignment(FlowLayout.LEFT);
    pantop.setLayout(layouttop);
    
    btnquit = new JButton("Quit");
    btnquit.setBackground(UI.BUTTON_GREEN);
    btnquit.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.ge.editor_metagon.quit();}});
    pantop.add(btnquit);
    
    btnsave = new JButton("Save");
    btnsave.setBackground(UI.BUTTON_GREEN);
    btnsave.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.ge.editor_metagon.save();}});
    pantop.add(btnsave);
    
    Component horizontalStrut = Box.createHorizontalStrut(20);
    pantop.add(horizontalStrut);
    
    panmetagontag = new PanMetagonTags();
    pantop.add(panmetagontag);
    
    pangrid = new EditMetagonGrid();
    add(pangrid, BorderLayout.CENTER);
    
    JPanel panbot = new JPanel();
    add(panbot, BorderLayout.SOUTH);
    
    lblinfo = new JLabel("polygon count, geometry validity, etc... foo foo foo");
    panbot.add(lblinfo);
    
  }
}
