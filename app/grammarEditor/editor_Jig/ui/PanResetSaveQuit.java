package org.fleen.forsythia.app.grammarEditor.editor_Jig.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;

@SuppressWarnings("serial")
public class PanResetSaveQuit extends JPanel{

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
    btnreset.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.editor_jig.reset();}});
    horizontalboxmid.add(btnreset);
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(8);
    horizontalboxmid.add(horizontalStrut_1);
    
    JButton btnsave = new JButton("SAVE");
    btnsave.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.editor_jig.save();}});
    horizontalboxmid.add(btnsave);
    
    Component horizontalStrut_2 = Box.createHorizontalStrut(8);
    horizontalboxmid.add(horizontalStrut_2);
    
    JButton btnquit = new JButton("QUIT");
    btnquit.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.editor_jig.quit();}});
    horizontalboxmid.add(btnquit);
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(4);
    horizontalboxmid.add(horizontalStrut_3);
    
    Box horizontalboxbottom = Box.createHorizontalBox();
    verticalBox.add(horizontalboxbottom);
    
    Component rigidArea_1 = Box.createRigidArea(new Dimension(44, 4));
    horizontalboxbottom.add(rigidArea_1);
    
  }

}
