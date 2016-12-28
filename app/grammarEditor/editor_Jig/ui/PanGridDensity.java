package org.fleen.forsythia.app.grammarEditor.editor_Jig.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class PanGridDensity extends JPanel{
  
  public JLabel lblgriddensity;
  JButton btngriddensityincrement,btngriddensitydecrement;

  public PanGridDensity(){
    
    setBackground(new Color(204, 153, 255));
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    
    Box verticalBox = Box.createVerticalBox();
    add(verticalBox);
    
    Box horizontalboxtop = Box.createHorizontalBox();
    verticalBox.add(horizontalboxtop);
    
    Component rigidArea = Box.createRigidArea(new Dimension(44, 4));
    horizontalboxtop.add(rigidArea);
    
    Box horizontalboxmid = Box.createHorizontalBox();
    verticalBox.add(horizontalboxmid);
    
    Component horizontalStrut = Box.createHorizontalStrut(4);
    horizontalboxmid.add(horizontalStrut);
    
    lblgriddensity = new JLabel("Grid Density = 000");
    lblgriddensity.setFont(new Font("Dialog", Font.BOLD, 16));
    horizontalboxmid.add(lblgriddensity);
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(8);
    horizontalboxmid.add(horizontalStrut_1);
    
    btngriddensityincrement = new JButton("+");
    btngriddensityincrement.setBackground(new Color(255, 204, 255));
    btngriddensityincrement.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        if(btngriddensityincrement.isEnabled())
          GE.editor_jig.gridDensity_Increment();}});
    horizontalboxmid.add(btngriddensityincrement);
    
    Component horizontalStrut_2 = Box.createHorizontalStrut(8);
    horizontalboxmid.add(horizontalStrut_2);
    
    btngriddensitydecrement = new JButton("-");
    btngriddensitydecrement.setBackground(new Color(255, 204, 255));
    btngriddensitydecrement.setFont(new Font("Dialog", Font.BOLD, 12));
    btngriddensitydecrement.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        if(btngriddensitydecrement.isEnabled())
          GE.editor_jig.gridDensity_Decrement();}});
    horizontalboxmid.add(btngriddensitydecrement);
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(4);
    horizontalboxmid.add(horizontalStrut_3);
    
    Box horizontalboxbottom = Box.createHorizontalBox();
    verticalBox.add(horizontalboxbottom);
    
    Component rigidArea_1 = Box.createRigidArea(new Dimension(44, 4));
    horizontalboxbottom.add(rigidArea_1);
    
  }
  
  private static final Color 
    BUTTONENABLED=new Color(255,200,255),
    BUTTONDISABLED=new Color(200,200,200);
  
  public void setEnabled(boolean a){
    super.setEnabled(a);
    btngriddensityincrement.setEnabled(a);
    btngriddensitydecrement.setEnabled(a);
    if(a){
      btngriddensityincrement.setBackground(BUTTONENABLED);
      btngriddensitydecrement.setBackground(BUTTONENABLED);
    }else{
      btngriddensityincrement.setBackground(BUTTONDISABLED);
      btngriddensitydecrement.setBackground(BUTTONDISABLED);}}
  
}
