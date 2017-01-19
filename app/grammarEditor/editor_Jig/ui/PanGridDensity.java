package org.fleen.forsythia.app.grammarEditor.editor_Jig.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;

@SuppressWarnings("serial")
public class PanGridDensity extends JPanel{
  
  public JLabel lblgriddensity;
  JButton btngriddensityincrement,btngriddensitydecrement;

  public PanGridDensity(){
    
    setBackground(new Color(204, 153, 255));
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    
    lblgriddensity = new JLabel("Grid Density = 000");
    add(lblgriddensity);
    lblgriddensity.setFont(new Font("Dialog", Font.BOLD, 16));
    
    Component horizontalStrut = Box.createHorizontalStrut(8);
    add(horizontalStrut);
    
    btngriddensityincrement = new JButton("+");
    add(btngriddensityincrement);
    btngriddensityincrement.setFont(new Font("Dialog", Font.BOLD, 16));
    btngriddensityincrement.setBackground(new Color(255, 204, 255));
    btngriddensityincrement.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        if(btngriddensityincrement.isEnabled())
          GE.ge.editor_jig.gridDensity_Increment();}});
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(8);
    add(horizontalStrut_1);
    
    btngriddensitydecrement = new JButton("-");
    add(btngriddensitydecrement);
    btngriddensitydecrement.setBackground(new Color(255, 204, 255));
    btngriddensitydecrement.setFont(new Font("Dialog", Font.BOLD, 16));
    btngriddensitydecrement.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        if(btngriddensitydecrement.isEnabled())
          GE.ge.editor_jig.gridDensity_Decrement();}});
    
    Component horizontalStrut_2 = Box.createHorizontalStrut(8);
    add(horizontalStrut_2);
    
  }
  
  public void setEnabled(boolean a){
    setVisible(a);}
  
}
