package org.fleen.forsythia.app.grammarEditor.editor_Metagon.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.util.ui.WrapLayout;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
import javax.swing.Box;

public class EditMetagonUI extends JPanel{
  
  private static final long serialVersionUID=-8825258503202494056L;
  
  public JPanel pantop;
  public EditMetagonGrid pangrid;
  public JLabel lblinfo;
  
  public JButton btnquit;
  public JButton btnsave;
  public JButton btnmode;
  
  public PanJigTags panjigtag;
  
  public EditMetagonUI(){
    setLayout(new BorderLayout(0, 0));
    
    pantop = new JPanel();
    add(pantop, BorderLayout.NORTH);
    WrapLayout layouttop=new WrapLayout();
    layouttop.setAlignment(FlowLayout.LEFT);
    pantop.setLayout(layouttop);
    
    btnquit = new JButton("Quit");
    btnquit.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.ge.editor_jig.quit();}});
    pantop.add(btnquit);
    
    btnsave = new JButton("Save");
    btnsave.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.ge.editor_jig.save();}});
    pantop.add(btnsave);
    
    Component horizontalStrut = Box.createHorizontalStrut(20);
    pantop.add(horizontalStrut);
    
    panjigtag = new PanJigTags();
    pantop.add(panjigtag);
    
    pangrid = new EditMetagonGrid();
    add(pangrid, BorderLayout.CENTER);
    
    JPanel panbot = new JPanel();
    add(panbot, BorderLayout.SOUTH);
    
    lblinfo = new JLabel("polygon count, geometry validity, etc... foo foo foo");
    panbot.add(lblinfo);
    
  }
}
