package org.fleen.forsythia.app.grammarEditor.editor_Jig.ui;

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
import org.fleen.forsythia.app.grammarEditor.util.UI;

public class PanGridDensity extends JPanel{
  
  private static final long serialVersionUID=617916980288078402L;
  
  public JLabel lblgriddensity;
  JButton btngriddensityincrement,btngriddensitydecrement;

  public PanGridDensity(){
    
    setBackground(UI.BUTTON_PURPLE);
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    
    lblgriddensity = new JLabel("Grid Density = 000");
    add(lblgriddensity);
    lblgriddensity.setFont(new Font("Dialog", Font.BOLD, 12));
    
    Component horizontalStrut = Box.createHorizontalStrut(8);
    add(horizontalStrut);
    
    btngriddensityincrement = new JButton("+");
    btngriddensityincrement.setBackground(UI.BUTTON_GREEN);
    add(btngriddensityincrement);
    btngriddensityincrement.setFont(new Font("Dialog", Font.BOLD, 12));
    btngriddensityincrement.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        if(btngriddensityincrement.isEnabled())
          GE.ge.editor_jig.gridDensity_Increment();}});
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(8);
    add(horizontalStrut_1);
    
    btngriddensitydecrement = new JButton("-");
    btngriddensitydecrement.setBackground(UI.BUTTON_GREEN);
    add(btngriddensitydecrement);
    btngriddensitydecrement.setFont(new Font("Dialog", Font.BOLD, 12));
    btngriddensitydecrement.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        if(btngriddensitydecrement.isEnabled())
          GE.ge.editor_jig.gridDensity_Decrement();}});
    
    Component horizontalStrut_2 = Box.createHorizontalStrut(8);
    add(horizontalStrut_2);
    
  }
  
}
