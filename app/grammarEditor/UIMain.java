package org.fleen.forsythia.app.grammarEditor;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
//    setTitle(GE.APPNAME);
    Dimension ss=Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(new Rectangle(
      (ss.width-DEFAULTWIDTH)/2,
      (ss.height-DEFAULTHEIGHT)/2,
      DEFAULTWIDTH,
      DEFAULTHEIGHT));
//    setExtendedState(UIMain.MAXIMIZED_BOTH);
    setVisible(true);

    paneditor = new JPanel();
    paneditor.setLayout(new CardLayout(0, 0));
    paneditor.setBorder(new EmptyBorder(5,5,5,5));
    setContentPane(paneditor);
    
  }
}
