package org.fleen.forsythia.app.grammarEditor.editor_Jig;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.editor_Jig.ui.EJ_Grid;

@SuppressWarnings("serial")
public class CJG_UI_OLD extends JPanel{
  
  public JLabel lblgriddensity;
  public EJ_Grid pangrid;
  public JLabel lblinfo;

  public CJG_UI_OLD(){
    setLayout(new BorderLayout(0, 0));
    
    Box hboxtop = Box.createHorizontalBox();
    hboxtop.setBackground(new Color(255, 255, 51));
    hboxtop.setOpaque(true);
    add(hboxtop, BorderLayout.NORTH);
    
    Component horizontalStrut_2 = Box.createHorizontalStrut(20);
    hboxtop.add(horizontalStrut_2);
    
    lblgriddensity = new JLabel("000");
    lblgriddensity.setForeground(new Color(102, 51, 255));
    lblgriddensity.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 24));
    hboxtop.add(lblgriddensity);
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(20);
    hboxtop.add(horizontalStrut_3);
    
    JButton btnincrement = new JButton("+");
    btnincrement.setBackground(new Color(255, 153, 0));
    btnincrement.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.editor_jig.gridDensity_Increment();}});
    btnincrement.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    hboxtop.add(btnincrement);
    
    Component horizontalStrut = Box.createHorizontalStrut(20);
    hboxtop.add(horizontalStrut);
    
    JButton btndecrement = new JButton("-");
    btndecrement.setBackground(new Color(255, 0, 153));
    btndecrement.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.editor_jig.gridDensity_Decrement();}});
    btndecrement.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    hboxtop.add(btndecrement);
    
    JButton btnsave = new JButton("S");
    btnsave.setBackground(new Color(51, 204, 102));
    btnsave.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    btnsave.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.editor_jig.save();}});
    
    Component horizontalGlue = Box.createHorizontalGlue();
    hboxtop.add(horizontalGlue);
    hboxtop.add(btnsave);
    
    Component horizontalStrut_5 = Box.createHorizontalStrut(20);
    hboxtop.add(horizontalStrut_5);
    
    JButton btndiscard = new JButton("X");
    btndiscard.setBackground(new Color(255, 204, 0));
    btndiscard.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    btndiscard.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.editor_jig.quit();}});
    hboxtop.add(btndiscard);
    
    Component horizontalStrut_6 = Box.createHorizontalStrut(20);
    hboxtop.add(horizontalStrut_6);
    
    pangrid = new EJ_Grid();
    add(pangrid, BorderLayout.CENTER);
    
    lblinfo = new JLabel("<no info>");
    lblinfo.setBackground(new Color(255, 153, 204));
    add(lblinfo, BorderLayout.SOUTH);
    
  }
}
