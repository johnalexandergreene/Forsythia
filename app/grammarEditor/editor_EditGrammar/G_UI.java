package org.fleen.forsythia.app.grammarEditor.editor_EditGrammar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;

import org.fleen.forsythia.app.grammarEditor.GE;

@SuppressWarnings("serial")
public class G_UI extends JPanel{
  
  public MetagonMenu panmetagonmenu;
  public JigMenu panjigmenu;
  public JLabel lblmetagoninfo,lbljiginfo,lblinfo_grammarpath;
 
  /**
   * Create the panel.
   */
  public G_UI(){
    
    panmetagonmenu = new MetagonMenu();
    panmetagonmenu.setBorder(new LineBorder(new Color(169, 169, 169)));
    
    panjigmenu = new JigMenu();
    panjigmenu.setBorder(new LineBorder(new Color(169, 169, 169)));
    
    lblinfo_grammarpath = new JLabel("/foo/bar/grammar_1234");
    lblinfo_grammarpath.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 12));
    
    Box horizontalBox = Box.createHorizontalBox();
    horizontalBox.setOpaque(true);
    horizontalBox.setBackground(new Color(153, 204, 255));
    
    Box horizontalBox_1 = Box.createHorizontalBox();
    horizontalBox_1.setOpaque(true);
    horizontalBox_1.setBackground(new Color(255, 255, 153));
    
    Box horizontalBox_2 = Box.createHorizontalBox();
    horizontalBox_2.setOpaque(true);
    horizontalBox_2.setBackground(new Color(255, 255, 153));
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(
      groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(panjigmenu, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
            .addComponent(lblinfo_grammarpath, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
            .addComponent(horizontalBox, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
            .addComponent(horizontalBox_2, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
            .addComponent(panmetagonmenu, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
            .addComponent(horizontalBox_1, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE))
          .addContainerGap())
    );
    groupLayout.setVerticalGroup(
      groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup()
          .addComponent(horizontalBox, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(horizontalBox_2, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(panmetagonmenu, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(horizontalBox_1, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(panjigmenu, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
          .addPreferredGap(ComponentPlacement.RELATED)
          .addComponent(lblinfo_grammarpath, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
    );
    
    lblmetagoninfo = new JLabel("MET:123 ISO:456 JIGLESS:789 ");
    lblmetagoninfo.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 14));
    horizontalBox_2.add(lblmetagoninfo);
    lblmetagoninfo.setForeground(new Color(0, 0, 139));
    
    Component horizontalGlue_1 = Box.createHorizontalGlue();
    horizontalBox_2.add(horizontalGlue_1);
    
    JButton btncreatemetagon = new JButton("Create");
    btncreatemetagon.setToolTipText("create metagon");
    btncreatemetagon.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.editor_grammar.createMetagon();}});
    btncreatemetagon.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 20));
    btncreatemetagon.setBackground(new Color(255, 204, 102));
    btncreatemetagon.setMargin(new Insets(0,0,0,0));
    horizontalBox_2.add(btncreatemetagon);
    
    Component horizontalStrut_1 = Box.createHorizontalStrut(32);
    horizontalBox_2.add(horizontalStrut_1);
    
    JButton btndiscardmetagon = new JButton("Discard");
    btndiscardmetagon.setToolTipText("discard metagton");
    btndiscardmetagon.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.editor_grammar.discardMetagon();}});
    btndiscardmetagon.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 20));
    btndiscardmetagon.setBackground(new Color(204, 0, 51));
    btndiscardmetagon.setMargin(new Insets(0,0,0,0));
    btndiscardmetagon.setIcon(null);
    horizontalBox_2.add(btndiscardmetagon);
    
    Component horizontalStrut_3 = Box.createHorizontalStrut(8);
    horizontalBox_2.add(horizontalStrut_3);
    
    lbljiginfo = new JLabel("JIGS 123");
    lbljiginfo.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 14));
    horizontalBox_1.add(lbljiginfo);
    lbljiginfo.setForeground(new Color(0, 0, 139));
    
    Component horizontalGlue = Box.createHorizontalGlue();
    horizontalBox_1.add(horizontalGlue);
    
    JButton btncreatejig = new JButton("Create");
    btncreatejig.setToolTipText("create jig");
    btncreatejig.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.editor_grammar.createProtoJig();}});
    btncreatejig.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 20));
    btncreatejig.setBackground(new Color(255, 204, 102));
    btncreatejig.setMargin(new Insets(0,0,0,0));
    horizontalBox_1.add(btncreatejig);
    
    Component horizontalStrut = Box.createHorizontalStrut(32);
    horizontalBox_1.add(horizontalStrut);
    
    JButton btndiscardjig = new JButton("Discard");
    btndiscardjig.setToolTipText("discard jig");
    btndiscardjig.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.editor_grammar.discardProtoJig();}});
    btndiscardjig.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 20));
    btndiscardjig.setBackground(new Color(204, 0, 51));
    btndiscardjig.setMargin(new Insets(0,0,0,0));
    horizontalBox_1.add(btndiscardjig);
    
    Component horizontalStrut_2 = Box.createHorizontalStrut(8);
    horizontalBox_1.add(horizontalStrut_2);
    
    Component horizontalStrut_7 = Box.createHorizontalStrut(20);
    horizontalBox.add(horizontalStrut_7);
    
    JButton btnimportgrammar = new JButton("Import");
    btnimportgrammar.setToolTipText("import grammar");
    btnimportgrammar.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 20));
    btnimportgrammar.setBackground(new Color(102, 153, 255));
    btnimportgrammar.setMargin(new Insets(0,0,0,0));
    horizontalBox.add(btnimportgrammar);
    btnimportgrammar.addMouseListener(new MouseAdapter(){
      public void mouseClicked(MouseEvent e){
        GE.editor_grammar.importGrammar();}});
    
    Component horizontalStrut_4 = Box.createHorizontalStrut(20);
    horizontalBox.add(horizontalStrut_4);
    
    final JButton btnexportgrammar = new JButton("Export");
    btnexportgrammar.setToolTipText("export grammar");
    btnexportgrammar.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 20));
    btnexportgrammar.setBackground(new Color(102, 153, 255));
    btnexportgrammar.setMargin(new Insets(0,0,0,0));
    horizontalBox.add(btnexportgrammar);
    btnexportgrammar.addMouseListener(new MouseAdapter(){
      public void mouseClicked(MouseEvent e){
        GE.editor_grammar.exportGrammar();}});
    
    Component horizontalStrut_5 = Box.createHorizontalStrut(20);
    horizontalBox.add(horizontalStrut_5);
    
    JButton btnnew = new JButton("New");
    btnnew.setToolTipText("new grammar");
    btnnew.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        GE.editor_grammar.createNewGrammar();}});
    btnnew.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 20));
    btnnew.setBackground(new Color(102, 153, 255));
    btnnew.setMargin(new Insets(0,0,0,0));
    horizontalBox.add(btnnew);
    
    Component horizontalGlue_2 = Box.createHorizontalGlue();
    horizontalBox.add(horizontalGlue_2);
    
    JButton btngenerator = new JButton("Generate");
    btngenerator.setToolTipText("streamer");
    btngenerator.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e){
        GE.setEditor(GE.editor_generator);}});
    btngenerator.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 20));
    btngenerator.setBackground(new Color(204, 0, 255));
    btngenerator.setMargin(new Insets(0,0,0,0));
    horizontalBox.add(btngenerator);
    
    Component horizontalStrut_6 = Box.createHorizontalStrut(32);
    horizontalBox.add(horizontalStrut_6);
    
    setLayout(groupLayout);}
}
