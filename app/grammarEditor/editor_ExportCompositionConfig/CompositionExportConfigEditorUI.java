package org.fleen.forsythia.app.grammarEditor.editor_ExportCompositionConfig;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.fleen.forsythia.app.grammarEditor.GE;

@SuppressWarnings("serial")
public class CompositionExportConfigEditorUI extends JPanel{
  
  JTextField txtexportdirpath;
  JTextField txtsize;
  JCheckBox 
    chkboxvector,
    chkboxraster;

  /**
   * Create the panel.
   */
  public CompositionExportConfigEditorUI(){
    
    chkboxvector = new JCheckBox("Vector");
    chkboxvector.setToolTipText("Export a vector file.");
    chkboxvector.setFont(new Font("Dialog", Font.BOLD, 16));
    
    chkboxraster = new JCheckBox("Raster");
    chkboxraster.setToolTipText("Export a raster file.");
    chkboxraster.setFont(new Font("Dialog", Font.BOLD, 16));
    
    txtexportdirpath = new JTextField();
    txtexportdirpath.setToolTipText("Path to export dir.");
    txtexportdirpath.setBackground(Color.GREEN);
    txtexportdirpath.setFont(new Font("Dialog", Font.BOLD, 16));
    txtexportdirpath.setEditable(false);
    txtexportdirpath.setText("/foo/bar");
    txtexportdirpath.setColumns(10);
    txtexportdirpath.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.editor_compositionexportconfig.gleanExportPathDir();}});
    
    txtsize = new JTextField();
    txtsize.setToolTipText("Maximum size of exported raster image in the form 123x456");
    txtsize.setFont(new Font("Dialog", Font.BOLD, 16));
    txtsize.setText("800x600");
    txtsize.setColumns(10);
    
    JButton btnsavechanges = new JButton("S");
    btnsavechanges.setToolTipText("save shanges");
    btnsavechanges.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.editor_compositionexportconfig.saveChanges();}});
    
    JButton btndiscardchanges = new JButton("D");
    btndiscardchanges.setToolTipText("discard changes");
    btndiscardchanges.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.editor_compositionexportconfig.discardChanges();}});
    
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(
      groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(chkboxvector)
            .addGroup(groupLayout.createSequentialGroup()
              .addComponent(chkboxraster)
              .addPreferredGap(ComponentPlacement.RELATED)
              .addComponent(txtsize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addGroup(groupLayout.createSequentialGroup()
              .addComponent(btnsavechanges)
              .addPreferredGap(ComponentPlacement.RELATED)
              .addComponent(btndiscardchanges))
            .addComponent(txtexportdirpath, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE))
          .addContainerGap())
    );
    groupLayout.setVerticalGroup(
      groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
            .addComponent(btnsavechanges)
            .addComponent(btndiscardchanges))
          .addGap(25)
          .addComponent(txtexportdirpath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addComponent(chkboxvector)
          .addPreferredGap(ComponentPlacement.UNRELATED)
          .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
            .addComponent(chkboxraster)
            .addComponent(txtsize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
          .addContainerGap(148, Short.MAX_VALUE))
    );
    setLayout(groupLayout);

  }
}
