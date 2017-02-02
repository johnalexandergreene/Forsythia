package org.fleen.forsythia.app.grammarEditor.editor_Metagon.ui;

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
import org.fleen.forsythia.app.grammarEditor.util.UI;

public class PanMetagonTags extends JPanel{
  
  private static final long serialVersionUID=1080948326559731578L;
  
  public JTextField txtmetagontags;

  public PanMetagonTags(){
    
    setBackground(UI.BUTTON_RED);
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    
    Component horizontalStrut = Box.createHorizontalStrut(4);
    add(horizontalStrut);
    
    JLabel lbljigtag = new JLabel("MetagonTags=");
    add(lbljigtag);
    lbljigtag.setFont(new Font("Dialog", Font.BOLD, 14));
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(4);
    add(horizontalStrut_3);
    
    txtmetagontags = new JTextField("foo",20);
    txtmetagontags.setBackground(UI.BUTTON_YELLOW);
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
