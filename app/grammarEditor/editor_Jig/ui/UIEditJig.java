package org.fleen.forsythia.app.grammarEditor.editor_Jig.ui;

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

public class UIEditJig extends JPanel{
  
  private static final long serialVersionUID=8375171748091392216L;
  
  public JPanel pantop;
  public GridEditJig pangrid;
  public JLabel lblinfo;
  
  public JButton btnquit;
  public JButton btnsave;
  public JButton btnmode;
  
  public PanGridDensity pangriddensity;
  public PanJigTags panjigtag;
  public PanSectionTags pansectiontags;
  public JButton btnsectionchorus;
  public JButton btnsectionanchor;
  private Component horizontalStrut_1;
  
  public UIEditJig(){
    setLayout(new BorderLayout(0, 0));
    
    pantop = new JPanel();
    add(pantop, BorderLayout.NORTH);
    WrapLayout layouttop=new WrapLayout();
    layouttop.setAlignment(FlowLayout.LEFT);
    pantop.setLayout(layouttop);
    
    btnquit = new JButton("Quit");
    btnquit.setBackground(UI.BUTTON_RED);
    btnquit.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.ge.editor_jig.quit();}});
    pantop.add(btnquit);
    
    btnsave = new JButton("Save");
    btnsave.setBackground(UI.BUTTON_RED);
    btnsave.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.ge.editor_jig.save();}});
    pantop.add(btnsave);
    
    Component horizontalStrut = Box.createHorizontalStrut(20);
    pantop.add(horizontalStrut);
    
    btnmode = new JButton("Mode=foo");
    btnmode.setBackground(UI.BUTTON_ORANGE);
    btnmode.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.ge.editor_jig.toggleMode();}});
    pantop.add(btnmode);
    
    horizontalStrut_1 = Box.createHorizontalStrut(20);
    pantop.add(horizontalStrut_1);
    
    pangriddensity = new PanGridDensity();
    pantop.add(pangriddensity);
    
    panjigtag = new PanJigTags();
    pantop.add(panjigtag);
    
    btnsectionchorus = new JButton("SectionChorus=000");
    btnsectionchorus.setBackground(UI.BUTTON_GREEN);
    btnsectionchorus.addMouseListener(new MouseAdapter(){
      public void mouseClicked(MouseEvent e){
        GE.ge.editor_jig.incrementSectionChorus();}});
    pantop.add(btnsectionchorus);
    
    btnsectionanchor = new JButton("SectionAnchor=000");
    btnsectionanchor.setBackground(UI.BUTTON_GREEN);
    btnsectionanchor.addMouseListener(new MouseAdapter(){
      public void mouseClicked(MouseEvent e){
        GE.ge.editor_jig.incrementSectionAnchor();}});
    pantop.add(btnsectionanchor);
    
    pansectiontags = new PanSectionTags();
    pantop.add(pansectiontags);
    
    pangrid = new GridEditJig();
    add(pangrid, BorderLayout.CENTER);
    
    JPanel panbot = new JPanel();
    add(panbot, BorderLayout.SOUTH);
    
    lblinfo = new JLabel("polygon count, geometry validity, etc... foo foo foo");
    panbot.add(lblinfo);
    
  }
}
