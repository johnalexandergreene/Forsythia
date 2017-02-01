package org.fleen.forsythia.app.grammarEditor;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class UIMain extends JFrame{
  
  private static final long serialVersionUID=-8290517235964873539L;

  private static final int 
    DEFAULTWIDTH=1024,
    DEFAULTHEIGHT=768;
  
  public JPanel paneditor;
  
//  private JPanel contentPane;

  /*
   * TEST
   */
  public static void main(String[] args){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          UIMain frame=new UIMain();
          frame.setVisible(true);
          frame.setTitle("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        }catch(Exception e){
          e.printStackTrace();}}});}

  /*
   * ################################
   * INIT
   * ################################
   */
  public UIMain(){
    addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
        GE.ge.term();}});
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    //
    setTitle(GE.APPNAME);
    Dimension ss=Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(new Rectangle(
      (ss.width-DEFAULTWIDTH)/2,
      (ss.height-DEFAULTHEIGHT)/2,
      DEFAULTWIDTH,
      DEFAULTHEIGHT));
//    setExtendedState(UIMain.MAXIMIZED_BOTH);
    setVisible(true);
    
    JPanel contentPane=new JPanel();
    contentPane.setBorder(new EmptyBorder(5,5,5,5));
    setContentPane(contentPane);
    
    paneditor = new JPanel();
    GroupLayout gl_contentPane = new GroupLayout(contentPane);
    gl_contentPane.setHorizontalGroup(
      gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(paneditor, GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
    );
    gl_contentPane.setVerticalGroup(
      gl_contentPane.createParallelGroup(Alignment.LEADING)
        .addComponent(paneditor, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
    );
    paneditor.setLayout(new CardLayout(0, 0));
    contentPane.setLayout(gl_contentPane);
    
  }
}
