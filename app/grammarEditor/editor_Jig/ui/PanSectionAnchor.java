package org.fleen.forsythia.app.grammarEditor.editor_Jig.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;

@SuppressWarnings("serial")
public class PanSectionAnchor extends JPanel{

  JButton btn;
  
  public PanSectionAnchor(){
    
    setBackground(new Color(204, 255, 51));
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
    
    btn = new JButton("SECTION ANCHOR = 000");
    btn.setFont(new Font("Dialog", Font.BOLD, 12));
    btn.addMouseListener(new MouseAdapter(){
      public void mouseClicked(MouseEvent e){
        GE.editor_jig.incrementSectionAnchor();}});
    horizontalboxmid.add(btn);
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(4);
    horizontalboxmid.add(horizontalStrut_3);
    
    Box horizontalboxbottom = Box.createHorizontalBox();
    verticalBox.add(horizontalboxbottom);
    
    Component rigidArea_1 = Box.createRigidArea(new Dimension(44, 4));
    horizontalboxbottom.add(rigidArea_1);
    
  }
  
  public void setText(String a){
    btn.setText(a);}
  
  public void setEnabled(boolean a){
    setVisible(a);
//    btn.setEnabled(a);
    }
  
}
