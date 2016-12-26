package org.fleen.forsythia.app.grammarEditor.editor_Jig.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;

@SuppressWarnings("serial")
class PanGridDensity extends JPanel{

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
    
    JLabel lblgriddensity = new JLabel("Grid Density = 000");
    lblgriddensity.setFont(new Font("Dialog", Font.BOLD, 16));
    horizontalboxmid.add(lblgriddensity);
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(8);
    horizontalboxmid.add(horizontalStrut_1);
    
    JButton btngriddensityincrement = new JButton("+");
    horizontalboxmid.add(btngriddensityincrement);
    
    Component horizontalStrut_2 = Box.createHorizontalStrut(8);
    horizontalboxmid.add(horizontalStrut_2);
    
    JButton btngriddensitydecrement = new JButton("-");
    btngriddensitydecrement.setFont(new Font("Dialog", Font.BOLD, 12));
    horizontalboxmid.add(btngriddensitydecrement);
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(4);
    horizontalboxmid.add(horizontalStrut_3);
    
    Box horizontalboxbottom = Box.createHorizontalBox();
    verticalBox.add(horizontalboxbottom);
    
    Component rigidArea_1 = Box.createRigidArea(new Dimension(44, 4));
    horizontalboxbottom.add(rigidArea_1);
    
  }
}
