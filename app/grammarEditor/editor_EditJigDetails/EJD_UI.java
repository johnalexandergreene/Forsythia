package org.fleen.forsythia.app.grammarEditor.editor_EditJigDetails;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.fleen.forsythia.app.grammarEditor.GE;

import java.awt.Color;

@SuppressWarnings("serial")
public class EJD_UI extends JPanel{
  
  public JLabel lblfocuselementid;
  public JTextField txttags;
  public EJD_Grid pangrid;
  public JLabel lblinfo;

  public EJD_UI(){
    setLayout(new BorderLayout(0, 0));
    
    Box hboxtop = Box.createHorizontalBox();
    hboxtop.setOpaque(true);
    hboxtop.setBackground(new Color(153, 153, 255));
    add(hboxtop, BorderLayout.NORTH);
    
    Component horizontalStrut_2 = Box.createHorizontalStrut(20);
    hboxtop.add(horizontalStrut_2);
    
    lblfocuselementid = new JLabel("JIG");
    lblfocuselementid.setForeground(new Color(255, 255, 153));
    lblfocuselementid.setBackground(new Color(102, 204, 204));
    lblfocuselementid.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 20));
    hboxtop.add(lblfocuselementid);
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(20);
    hboxtop.add(horizontalStrut_3);
    
    txttags = new JTextField();
    txttags.setBorder(null);
    txttags.setBackground(new Color(255, 204, 204));
    txttags.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 18));
    txttags.setText("<no tags>");
    hboxtop.add(txttags);
    txttags.setColumns(10);
    
    Component horizontalStrut_4 = Box.createHorizontalStrut(20);
    hboxtop.add(horizontalStrut_4);
    
    JButton btnsave = new JButton("S");
    btnsave.setBackground(new Color(255, 153, 51));
    btnsave.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    btnsave.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.editor_editjigdetails.save();}});
    hboxtop.add(btnsave);
    
    Component horizontalStrut_5 = Box.createHorizontalStrut(20);
    hboxtop.add(horizontalStrut_5);
    
    JButton btndiscard = new JButton("X");
    btndiscard.setBackground(new Color(255, 204, 255));
    btndiscard.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 18));
    btndiscard.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.editor_editjigdetails.discard();}});
    hboxtop.add(btndiscard);
    
    Component horizontalStrut_6 = Box.createHorizontalStrut(20);
    hboxtop.add(horizontalStrut_6);
    
    pangrid = new EJD_Grid();
    add(pangrid, BorderLayout.CENTER);
    
    lblinfo = new JLabel("<no info>");
    add(lblinfo, BorderLayout.SOUTH);
    
  }
}
