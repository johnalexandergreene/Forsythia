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
import org.fleen.forsythia.app.grammarEditor.editor_Jig.Editor_Jig;

@SuppressWarnings("serial")
public class PanMode extends JPanel{
  
  public JButton btnmode;

  public PanMode(){
    
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
    
    btnmode = new JButton("MODE FOO");
    btnmode.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.editor_jig.toggleMode();}});
    btnmode.setFont(new Font("Dialog", Font.BOLD, 12));
    horizontalboxmid.add(btnmode);
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(4);
    horizontalboxmid.add(horizontalStrut_3);
    
    Box horizontalboxbottom = Box.createHorizontalBox();
    verticalBox.add(horizontalboxbottom);
    
    Component rigidArea_1 = Box.createRigidArea(new Dimension(44, 4));
    horizontalboxbottom.add(rigidArea_1);}
  
  /*
   * ################################
   * mode
   * display color and test depending on mode
   * ################################
   */
  
  private static final Color 
    BACKGROUND_MODEEDITSECTIONS=new Color(128,255,128),
    BACKGROUND_MODEEDITGEOMETRY=new Color(255,255,128);
  
  public void setMode(int mode){
    if(mode==Editor_Jig.MODE_EDITSECTIONS){
      btnmode.setText("MODE = EDIT SECTIONS");
      btnmode.setBackground(BACKGROUND_MODEEDITSECTIONS);
    }else{//mode==MODE_EDITGEOMETRY)
      btnmode.setText("MODE = EDIT GEOMETRY");
      btnmode.setBackground(BACKGROUND_MODEEDITGEOMETRY);}}
  

  
  
  
  
  
}
