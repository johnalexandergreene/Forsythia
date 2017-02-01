package org.fleen.forsythia.app.grammarEditor.util;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.List;

import javax.swing.JPanel;


/*
 * A menu
 * 1..n rows of icons
 * clickdrag side scrolling
 */
public abstract class ElementMenu extends JPanel{
  
  private static final long serialVersionUID=-1864008707499697749L;
  
  private static final long MOUSEDRAGSAMPLEPERIOD=50;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public ElementMenu(int rowcount){
    super();
    this.rowcount=rowcount;
    addMouseListener(new ML0());
    addMouseMotionListener(new ML0());}
  
  /*
   * ################################
   * MOUSE
   * ################################
   */
  
  private static final int MOUSEDRAG_DX_MIN=10; 
  private int mousesamplex;
  private boolean mousedragging=false;
  private long oldtime=-1,newtime,timediff;
  
  private class ML0 extends MouseAdapter implements Serializable{

    private static final long serialVersionUID=2524868473914370627L;

    public void mousePressed(MouseEvent e){
      mousesamplex=e.getX();}

    public void mouseClicked(MouseEvent e){
      if(e.getClickCount()==2){
        e.consume();
        ElementMenuItem i=getItem(e.getX(),e.getY());
        doDoubleclick(i);}}
    
    public void mouseReleased(MouseEvent e){
      int button=e.getButton();
      int x=e.getX(),y=e.getY();
      if(button==MouseEvent.BUTTON1&&!mousedragging){
        ElementMenuItem i=getItem(x,y);
        if(i!=null){
          setFocusItem(i);
          repaint();}
      }else if(button==MouseEvent.BUTTON3){
        doPopupMenu(x,y);}
      mousedragging=false;
      oldtime=-1;}
    
    public void mouseDragged(MouseEvent e){
      if(oldtime==-1){
        oldtime=System.currentTimeMillis();
        return;}
      newtime=System.currentTimeMillis();
      timediff=newtime-oldtime;
      if(timediff>MOUSEDRAGSAMPLEPERIOD){
        oldtime=newtime;
        int x=e.getX();
        int dx=x-mousesamplex;
        if(Math.abs(dx)>MOUSEDRAG_DX_MIN){
          mousedragging=true;
          modifyScrollXOffset(dx);
          mousesamplex=x;
          repaint();}}}};
  
  /*
   * ################################
   * POPUPMENU
   * ################################
   */
          
  protected abstract void doPopupMenu(int x,int y);
  
  /*
   * ################################
   * ITEMS
   * ################################
   */
  
  protected abstract List<? extends ElementMenuItem> getItems();
  
  protected abstract boolean isFocusItem(ElementMenuItem i);
  
  protected abstract void setFocusItem(ElementMenuItem i);
  
  protected abstract void doDoubleclick(ElementMenuItem i);
  
  private ElementMenuItem getItem(int x,int y){
    iconspan=getIconSpan();
    int col=(x-scrollxoffset-UI.ELEMENTMENU_ARRAYEDGEMARGIN)/(iconspan+UI.ELEMENTMENU_ICONMARGIN);
    int row=(y-UI.ELEMENTMENU_ARRAYEDGEMARGIN)/(iconspan+UI.ELEMENTMENU_ICONMARGIN);
    int iconarrayheight=getIconArrayDimensions()[1];
    int index=col*iconarrayheight+row;
    List<? extends ElementMenuItem> items=getItems();
    int itemcount=items.size();
    if(index<0||index>=itemcount)return null;
    return items.get(index);}
  
  /*
   * ################################
   * ICON ARRAY METRICS 
   * TODO we could optimize this a bit 
   * ################################
   */
  
  private static final int SCROLLXOFFSETMAX=0;//for the obvious reason. picture it.
  private int[] iconarraydimensions=null;
  private int scrollxoffsetmin;
  
  public int[] getIconArrayDimensions(){
    if(iconarraydimensions==null)calculateIconArrayMetrics();
    return iconarraydimensions;}
  
  public void invalidateIconArrayMetrics(){
    iconarraydimensions=null;}
  
  private void calculateIconArrayMetrics(){
    int iconcount=getItems().size();
    //degenerate case
    if(iconcount==0){
      iconarraydimensions=new int[]{0,0};
      scrollxoffsetmin=0;
    }else{
      //icon array dimensions
      int iconarrayheight=rowcount;
      if(iconarrayheight<1)iconarrayheight=1;
      int iconarraywidth=iconcount/iconarrayheight;
      if(iconcount%iconarrayheight!=0)iconarraywidth++;
      if(iconarraywidth<1)iconarraywidth=1;
      iconarraydimensions=new int[]{iconarraywidth,iconarrayheight};
      //min scroll offset
      iconspan=getIconSpan();
      scrollxoffsetmin=iconarraywidth*iconspan;
      scrollxoffsetmin+=(UI.ELEMENTMENU_ARRAYEDGEMARGIN*2);
      scrollxoffsetmin+=((iconarraywidth-1)*UI.ELEMENTMENU_ICONMARGIN);
      scrollxoffsetmin=getWidth()-scrollxoffsetmin;}}
 
  /*
   * ################################
   * SCROLL
   * ################################
   */
  
  private int scrollxoffset=0;
  
  private void modifyScrollXOffset(int delta){
    scrollxoffset+=delta;
    if(scrollxoffset<scrollxoffsetmin)scrollxoffset=scrollxoffsetmin;
    if(scrollxoffset>SCROLLXOFFSETMAX)scrollxoffset=0;}
  
  /*
   * ################################
   * GRAPHICS
   * ################################
   */
  
  public void paint(Graphics g){
    super.paint(g);
    renderItemIcons((Graphics2D)g);
    paintBorder(g);}
  
  private void renderItemIcons(Graphics2D g){
    g.setPaint(UI.ELEMENTMENU_BACKGROUND);
    g.fillRect(0,0,getWidth(),getHeight());
    List<? extends ElementMenuItem> items=getItems();
    if(items==null)return;
    int itemcount=items.size();
    int[] xy;
    ElementMenuItem item;
    for(int itemindex=0;itemindex<itemcount;itemindex++){
      item=items.get(itemindex);
      xy=getIconXY(itemcount,itemindex);
      //render metagon geometry
      g.drawImage(
        item.getGrammarEditorIconImage(getIconSpan())
        ,xy[0],xy[1],null);
      //render border to reveal focus state
      if(isFocusItem(item))
          g.setColor(UI.ELEMENTMENU_ICON_OUTLINEFOCUS);
        else 
          g.setColor(UI.ELEMENTMENU_ICON_OUTLINEDEFAULT);
      int iconspan=getIconSpan();
      g.setStroke(new BasicStroke(4.0f));
      g.drawRect(
          xy[0],
          xy[1],
          iconspan,
          iconspan);}}
  
  /*
   * account for
   * scrollxoffset
   * iconspan (width==height)
   * iconspacing
   * edgepadding
   */
  private int[] getIconXY(int iconcount,int iconindex){
    int[] ad=getIconArrayDimensions();
    //get row and col of icon in array
    int col=iconindex/ad[1];
    int row=iconindex%ad[1];
    //get xy coor of topleft corner point of icon
    int iconspan=getIconSpan();
    int x=col*(iconspan+UI.ELEMENTMENU_ICONMARGIN)+UI.ELEMENTMENU_ARRAYEDGEMARGIN+scrollxoffset;
    int y=row*(iconspan+UI.ELEMENTMENU_ICONMARGIN)+UI.ELEMENTMENU_ARRAYEDGEMARGIN;
    return new int[]{x,y};}
  
  /*
   * ################################
   * ROW COUNT AND ICON SPAN
   * ################################
   */
  
  private int rowcount;
  private int iconspan=-1;
  
  //we calculate it lazily to give the panel a chance to create itself first
  private int getIconSpan(){
    if(iconspan==-1)calculateIconSpan();
    return iconspan;}
  
  /*
   * iconspan=(height-2*windowedgemargin-((rows-1)*iconmargin))/rows
   */
  private void calculateIconSpan(){
    int h=getHeight();
    iconspan=(h-2*UI.ELEMENTMENU_ARRAYEDGEMARGIN-((rowcount-1)*UI.ELEMENTMENU_ICONMARGIN))/rowcount;}
  
  //TODO invalidate on ui dimensions change
  public void invalidateIconSpan(){
    
  }
  
}
