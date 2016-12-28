package org.fleen.forsythia.app.grammarEditor.editor_Jig.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class PanJigTag extends JPanel{
  
  public JTextField txtjigtag;

  public PanJigTag(){
    
    setBackground(new Color(255, 204, 255));
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    
    Box verticalBox = Box.createVerticalBox();
    add(verticalBox);
    
    Box horizontalboxtop = Box.createHorizontalBox();
    verticalBox.add(horizontalboxtop);
    
    Component rigidArea = Box.createRigidArea(new Dimension(300, 4));
    horizontalboxtop.add(rigidArea);
    
    Box horizontalboxmid = Box.createHorizontalBox();
    verticalBox.add(horizontalboxmid);
    
    Component horizontalStrut = Box.createHorizontalStrut(4);
    horizontalboxmid.add(horizontalStrut);
    
    JLabel lbljigtag = new JLabel("Jig Tags =");
    lbljigtag.setFont(new Font("Dialog", Font.BOLD, 14));
    horizontalboxmid.add(lbljigtag);
    
    
    
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(4);
    horizontalboxmid.add(horizontalStrut_3);
    
    txtjigtag = new JTextField();
    txtjigtag.setText("rabbit squirrel fox");
    txtjigtag.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 18));
    txtjigtag.setBorder(null);
    horizontalboxmid.add(txtjigtag);
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(4);
    horizontalboxmid.add(horizontalStrut_1);
    
    Box horizontalboxbottom = Box.createHorizontalBox();
    verticalBox.add(horizontalboxbottom);
    
    Component rigidArea_1 = Box.createRigidArea(new Dimension(44, 4));
    horizontalboxbottom.add(rigidArea_1);
    
  }
}
