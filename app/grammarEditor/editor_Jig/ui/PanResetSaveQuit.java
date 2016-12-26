package org.fleen.forsythia.app.grammarEditor.editor_Jig.ui;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Dimension;
import javax.swing.JLabel;

@SuppressWarnings("serial")
class PanResetSaveQuit extends JPanel{

  public PanResetSaveQuit(){
    setBackground(new Color(204, 153, 102));
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
    
    JButton btnreset = new JButton("RESET");
    horizontalboxmid.add(btnreset);
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(8);
    horizontalboxmid.add(horizontalStrut_1);
    
    JButton btnsave = new JButton("SAVE");
    horizontalboxmid.add(btnsave);
    
    Component horizontalStrut_2 = Box.createHorizontalStrut(8);
    horizontalboxmid.add(horizontalStrut_2);
    
    JButton btnquit = new JButton("QUIT");
    horizontalboxmid.add(btnquit);
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(4);
    horizontalboxmid.add(horizontalStrut_3);
    
    Box horizontalboxbottom = Box.createHorizontalBox();
    verticalBox.add(horizontalboxbottom);
    
    Component rigidArea_1 = Box.createRigidArea(new Dimension(44, 4));
    horizontalboxbottom.add(rigidArea_1);
    
  }

}
