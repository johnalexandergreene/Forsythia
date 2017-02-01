package org.fleen.forsythia.app.grammarEditor.editor_Generator.ui;

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

public class PanInterval extends JPanel{
  
  private static final long serialVersionUID=-9027881839673058071L;
  
  public JTextField txtinterval;

  public PanInterval(){
    
    setBackground(UI.BUTTON_PURPLE);
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    
    Component horizontalStrut = Box.createHorizontalStrut(8);
    add(horizontalStrut);
    
    JLabel lbljigtag = new JLabel("Interval = ");
    add(lbljigtag);
    lbljigtag.setFont(new Font("Dialog", Font.BOLD, 14));
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(8);
    add(horizontalStrut_3);
    
    txtinterval = new JTextField("1234",6);
    txtinterval.setBackground(UI.BUTTON_YELLOW);
    add(txtinterval);
    txtinterval.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 18));
    txtinterval.setBorder(null);
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(8);
    add(horizontalStrut_1);
    txtinterval.addKeyListener(new KeyAdapter(){
      public void keyReleased(KeyEvent e){
        GE.ge.editor_generator.setInterval(txtinterval.getText());}});
    
  }
}
