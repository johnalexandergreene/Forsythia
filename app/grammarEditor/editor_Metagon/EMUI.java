package org.fleen.forsythia.app.grammarEditor.editor_Metagon;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.fleen.forsythia.app.grammarEditor.GE;

@SuppressWarnings("serial")
public class EMUI extends JPanel{

  public EMGrid grid;
  public JTextField txttags,txtinfo;
  public JButton 
    btnsavemetagon,
    btndiscardmetagon;
  
  /**
   * Create the panel.
   */
  public EMUI(){
    setBackground(Color.LIGHT_GRAY);
    
    grid = new EMGrid();
    grid.setBackground(Color.MAGENTA);
    
    Box horizontalBox = Box.createHorizontalBox();
    
    txtinfo = new JTextField();
    txtinfo.setBackground(new Color(204, 204, 204));
    txtinfo.setEditable(false);
    txtinfo.setFont(new Font("Dialog", Font.BOLD, 16));
    txtinfo.setText("foo");
    txtinfo.setColumns(10);
    
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(
      groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addComponent(txtinfo, GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE)
          .addContainerGap())
        .addComponent(grid, GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addComponent(horizontalBox, GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE)
          .addContainerGap())
    );
    groupLayout.setVerticalGroup(
      groupLayout.createParallelGroup(Alignment.TRAILING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addComponent(horizontalBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
          .addGap(9)
          .addComponent(grid, GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(txtinfo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
          .addGap(7))
    );
    
    txttags = new JTextField();
    horizontalBox.add(txttags);
    txttags.setForeground(new Color(148, 0, 211));
    txttags.setToolTipText("Space delimited tags");
    txttags.setFont(new Font("Dialog", Font.BOLD, 16));
    txttags.setText("");
    txttags.setColumns(10);
    
    JPanel panel = new JPanel();
    panel.setBackground(Color.LIGHT_GRAY);
    panel.setBorder(null);
    FlowLayout flowLayout = (FlowLayout) panel.getLayout();
    flowLayout.setVgap(0);
    flowLayout.setHgap(8);
    horizontalBox.add(panel);
    
    btnsavemetagon = new JButton("Save");
    panel.add(btnsavemetagon);
    btnsavemetagon.setToolTipText("Save metagon");
    btnsavemetagon.setForeground(new Color(255, 255, 255));
    btnsavemetagon.setBackground(new Color(154, 205, 50));
    btnsavemetagon.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 20));
    btnsavemetagon.setMargin(new Insets(0,0,0,0));
    btnsavemetagon.addMouseListener(new MouseAdapter(){
      public void mouseClicked(MouseEvent e){
        GE.ge.editor_metagon.saveMetagonAndReturnToGrammarEditor();}});
    
    btndiscardmetagon = new JButton("X");
    panel.add(btndiscardmetagon);
    btndiscardmetagon.setToolTipText("Discard metagon");
    btndiscardmetagon.setBackground(new Color(240, 128, 128));
    btndiscardmetagon.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 20));
    btndiscardmetagon.setMargin(new Insets(0,0,0,0));
    btndiscardmetagon.addMouseListener(new MouseAdapter(){
      public void mouseClicked(MouseEvent e){
        GE.ge.editor_metagon.discardMetagonAndReturnToGrammarEditor();}});
    
    setLayout(groupLayout);}
}
