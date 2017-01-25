package org.fleen.forsythia.app.grammarEditor.editor_Metagon.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.fleen.forsythia.app.grammarEditor.GE;

public class PanMetagonTags extends JPanel{
  
  private static final long serialVersionUID=-1484832312439244645L;
  
  public JTextField txtmetagontags;

  public PanMetagonTags(){
    
    setBackground(new Color(255, 204, 255));
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    
    Component horizontalStrut = Box.createHorizontalStrut(4);
    add(horizontalStrut);
    
    JLabel lbljigtag = new JLabel("MetagonTags=");
    add(lbljigtag);
    lbljigtag.setFont(new Font("Dialog", Font.BOLD, 14));
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(4);
    add(horizontalStrut_3);
    
    txtmetagontags = new JTextField("foo",20);
    add(txtmetagontags);
    txtmetagontags.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 18));
    txtmetagontags.setBorder(null);
    txtmetagontags.addKeyListener(new KeyAdapter(){
      public void keyReleased(KeyEvent e){
        GE.ge.editor_metagon.setMetagonTags(txtmetagontags.getText());}});
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(4);
    add(horizontalStrut_1);
    
  }
}
