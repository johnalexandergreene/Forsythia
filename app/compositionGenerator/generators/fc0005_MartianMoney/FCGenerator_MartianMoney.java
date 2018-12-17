package org.fleen.forsythia.app.compositionGenerator.generators.fc0005_MartianMoney;


/*
 * a single forsythia composition
 */
public class FCGenerator_MartianMoney{
//  
//  /*
//   * ################################
//   * CONSTRUCTOR
//   * ################################
//   */
//  
//  public FCGenerator_MartianMoney(){
//    initUI();}
//  
//  /*
//   * ################################
//   * ################################
//   * ################################
//   * PARAMS
//   * ################################
//   * ################################
//   * ################################
//   */
//  
//  Color[] palette=Palette.P_TOY_STORY_ADJUSTED2;
//  
////  String grammar_file_path="/home/john/Desktop/stripegrammar/s003.grammar";
////  String grammar_file_path="/home/john/Desktop/ge/nuther003.grammar";
////  String grammar_file_path="/home/john/Desktop/ge/aa004.grammar";
////  String grammar_file_path="/home/john/Desktop/grammars/s008.grammar";
////  String grammar_file_path="/home/john/Desktop/grammars/hexmandala001.grammar";
//  String grammar_file_path="/home/john/Desktop/grammars/s011martianmoney.grammar";
//  
//  ForsythiaCompositionGen composer=new Composer002_SplitBoil_DoubleRootEntropy();
////  Composer composer=new Composer001_SplitBoil();
////  static final double DETAIL_LIMIT=0.025;
//  
//  //coarse, for the 4x6 prints
//  static final double DETAIL_LIMIT=0.09;
//  
//  //for 20x30 print
////  static final double DETAIL_LIMIT=0.03;
//  
//  
////  Renderer renderer=new Renderer_Rasterizer005_TestRDSystem();
////  Renderer renderer=new Renderer_002_ArbitrarySubPalettes();
////  Renderer renderer=new Renderer_001();
////  Renderer renderer=new Renderer_Rasterizer004_ALittleSyntheticStroke();
////  Renderer2 renderer=new R_ZCell();
////  Renderer2 renderer=new R_ZCell_DarkStrokes();
//  
//  String exportdirpath="/home/john/Desktop/newstuff";
//  
//  Renderer renderer=new R_SimpleStrokes();
//  
//  ColorMap colormap;
//  
//  /*
//   * ++++++++++++++++++++++++++++++++
//   * GRAMMAR
//   * ++++++++++++++++++++++++++++++++
//   */
//    
//  private ForsythiaGrammar grammar=null;
//  
//  public ForsythiaGrammar getGrammar(){
//    if(grammar==null)
//      initGrammar();
//    return grammar;}
//
//  private void initGrammar(){
//    grammar=null;
//    try{
//      File f=new File(grammar_file_path);
//      grammar=importGrammarFromFile(f);
//    }catch(Exception x){
//      System.out.println("exception in grammar import");
//      x.printStackTrace();}}
//    
//  private ForsythiaGrammar importGrammarFromFile(File file){
//    FileInputStream fis;
//    ObjectInputStream ois;
//    ForsythiaGrammar g=null;
//    try{
//      fis=new FileInputStream(file);
//      ois=new ObjectInputStream(fis);
//      g=(ForsythiaGrammar)ois.readObject();
//      ois.close();
//    }catch(Exception x){}
//    return g;}
//
//  /*
//   * ################################
//   * ################################
//   * ################################
//   * UI
//   * ################################
//   * ################################
//   * ################################
//   */
//  
//  private static final String TITLE="Fleen Bread 0.3";
//  public UI ui;
//  
//  private void initUI(){
//    EventQueue.invokeLater(new Runnable(){
//      public void run(){
//        try{
//          ui=new UI(FCGenerator_MartianMoney.this);
//          ui.setDefaultWindowBounds();
//          ui.setVisible(true);
//          ui.setTitle(TITLE);
//          ui.txtinterval.setText(String.valueOf(CREATION_INTERVAL_DEFAULT));
//         }catch(Exception e){
//           e.printStackTrace();}}});}
//  
//  /*
//   * ################################
//   * ################################
//   * ################################
//   * PRODUCTION
//   * ################################
//   * ################################
//   * ################################
//   */
//  
//  ForsythiaComposition composition;
//  BufferedImage image=null;
//  
//  /*
//   * ++++++++++++++++++++++++++++++++
//   * CREATION MODE
//   * continuous or intermittant
//   * ++++++++++++++++++++++++++++++++
//   */
//  
//  static final boolean MODE_CONTINUOUS=false,MODE_INTERMITTANT=true; 
//  boolean creationmode=MODE_INTERMITTANT;
//  
//  void toggleCreationMode(){
//    if(creating)startStopCreation();
//    creationmode=!creationmode;
//    if(creationmode==MODE_CONTINUOUS){
//      ui.lblmode.setText("CON");
//    }else{
//      ui.lblmode.setText("INT");}}
//  
//  /*
//   * ++++++++++++++++++++++++++++++++
//   * START-STOP CREATION
//   * ++++++++++++++++++++++++++++++++
//   */
//  
//  private boolean creating=false;
//  
//  void startStopCreation(){
//    if(creationmode==MODE_INTERMITTANT){
//      if(!creating){
//        ui.lblstartstop.setText("||");
//        doIntermittantCreation();
//        ui.lblstartstop.setText(">>");}
//    }else{
//      if(creating){
//        stopContinuousCreation();
//        ui.lblstartstop.setText(">>");
//        creating=false;
//      }else{
//        startContinuousCreation();
//        ui.lblstartstop.setText("||");
//        creating=true;}}}
//  
//  /*
//   * ++++++++++++++++++++++++++++++++
//   * CREATION INTERVAL
//   * minimum interval between images in continuous creation mode
//   * ++++++++++++++++++++++++++++++++
//   */
//  
//  private static final long CREATION_INTERVAL_DEFAULT=1000;
//  
//  private long creationinterval=CREATION_INTERVAL_DEFAULT;
//  
//  void setCreationInterval(JTextField t){
//    try{
//      long a=Long.valueOf(t.getText());
//      creationinterval=a;
//    }catch(Exception x){
//      if(t.getText().equals(""))creationinterval=0;
//      t.setText(String.valueOf(creationinterval));}}
//  
//  /*
//   * ++++++++++++++++++++++++++++++++
//   * INTERMITTANT CREATION
//   * ++++++++++++++++++++++++++++++++
//   */
//  
//  private void doIntermittantCreation(){
//    composition=composer.compose(getGrammar(),DETAIL_LIMIT);
//    colormap=new CM_SymmetricChaos(composition,Palette.P_TOY_STORY_ADJUSTED2);
//    image=renderer.createImage(ui.panimage.getWidth(),ui.panimage.getHeight(),composition,colormap);
//    ui.panimage.repaint();
//    //maybe export
//    if(isExportModeAuto())
//      export();}
//  
//  /*
//   * ++++++++++++++++++++++++++++++++
//   * CONTINUOUS CREATION
//   * ++++++++++++++++++++++++++++++++
//   */
//  
//  private boolean stopcontinuouscreation;
//  
//  private void startContinuousCreation(){
//    stopcontinuouscreation=false;
//    new Thread(){
//      public void run(){
//        long 
//          starttime,
//          elapsedtime,
//          pausetime;
//        while(!stopcontinuouscreation){
//          starttime=System.currentTimeMillis();
//          //compose and render
//          composition=composer.compose(grammar,DETAIL_LIMIT);
//          colormap=new CM_SymmetricChaos(composition,Palette.P_TOY_STORY_ADJUSTED2);
//          image=renderer.createImage(ui.panimage.getWidth(),ui.panimage.getHeight(),composition,colormap);
//          //pause if necessary
//          elapsedtime=System.currentTimeMillis()-starttime;
//          pausetime=creationinterval-elapsedtime;
//          try{
//            if(pausetime>0)Thread.sleep(pausetime,0);
//          }catch(Exception x){x.printStackTrace();}
//          //paint
//          ui.panimage.repaint();
//          //maybe export
//          if(isExportModeAuto())
//            export();}}
//    }.start();}
//  
//  private void stopContinuousCreation(){
//    stopcontinuouscreation=true;}
//  
//  /*
//   * ################################
//   * ################################
//   * ################################
//   * EXPORT
//   * render the current composition using the current composer and renderer
//   * use the image size specified in the ui as a guide for that
//   * ################################
//   * ################################
//   * ################################
//   */
//  
//  private static final int 
//    EXPORTMODE_MANUAL=0,
//    EXPORTMODE_AUTO=1;
//  
//  private static final int 
//    EXPORT_DEFAULT_WIDTH=1000,
//    EXPORT_DEFAULT_HEIGHT=1000;
//  
//  private int getExportMode(){
//    if(ui.chkautoexport.isSelected())
//      return EXPORTMODE_AUTO;
//    else
//      return EXPORTMODE_MANUAL;}
//  
//  boolean isExportModeManual(){
//    return getExportMode()==EXPORTMODE_MANUAL;}
//  
//  boolean isExportModeAuto(){
//    return getExportMode()==EXPORTMODE_AUTO;}
//  
//  void export(){
//    File exportdir=getExportDir();
//    //
//    int w=EXPORT_DEFAULT_WIDTH,h=EXPORT_DEFAULT_HEIGHT;
//    try{
//      String a=ui.txtexportsize.getText();
//      String[] b=a.split("x");
//      w=Integer.valueOf(b[0]);
//      h=Integer.valueOf(b[1]);
//    }catch(Exception x){
//      x.printStackTrace();}
//    //
//    export(exportdir,w,h);}
//  
//  private File getExportDir(){
//    File exportdir=null;
//    try{
//      exportdir=new File(exportdirpath);
//    }catch(Exception x){}
//    return exportdir;}
//  
//  RasterExporter rasterexporter=new RasterExporter();
//  
//  private void export(File exportdir,int w,int h){
//    System.out.println(">>>EXPORT<<<");
//    if(colormap==null)
//      colormap=new CM_SymmetricChaos(composition,Palette.P_TOY_STORY_ADJUSTED2);
//    BufferedImage exportimage=renderer.createImage(w,h,composition,colormap);
//    rasterexporter.setExportDir(exportdir);
//    rasterexporter.export(exportimage);}
//  
  /*
   * ++++++++++++++++++++++++++++++++
   * MARTIAN MONEY
   * 4x6 print
   * 1800wx1200h
   * composition fills 1727x1200
   * which leaves 63x1200 on the end
   * which we fill with a nice random hexadecimal string
   * font is something mono and techno
   * 2 nonmatching colors from our palette comprise the textcolor and background color
   * ++++++++++++++++++++++++++++++++
   */
//  private void export(File exportdir,int w,int h){
//    System.out.println(">>>EXPORT MARTIAN MONEY<<<");
//    BufferedImage exportimage=getMartianMoneyImage();
//    rasterexporter.setExportDir(exportdir);
//    rasterexporter.export(exportimage);}
  
//  private BufferedImage getMartianMoneyImage(){
//    if(colormap==null)
//      colormap=new CM_SymmetricChaos(composition,Palette.P_TOY_STORY_ADJUSTED2);
//    BufferedImage c=renderer.createImage(1728,1200,composition,colormap);
//    //
//    BufferedImage m=new BufferedImage(1800,1200,BufferedImage.TYPE_INT_RGB);
//    Graphics2D g=m.createGraphics();
//    g.setRenderingHints(Renderer_Abstract.RENDERING_HINTS);
//    g.drawImage(c,null,null);
//    //
//    Color[] c3=get3Colors();
//    //
//    BufferedImage s=getCharacterStrip(c3);
//    g.drawImage(s,1727,0,null);
//    //
//    drawMars(g,c3[2]);
//    //
//    return m;}
//  
//  private void drawMars(Graphics2D g,Color c){
//    int x=1736,y=22;
//    g.setStroke(createStroke(8f));
//    g.setPaint(c);
//    g.drawOval(x,y,30,30);
//    Path2D.Double a=new Path2D.Double();
//    a.moveTo(x+27,y+27);
//    a.lineTo(x+45,y+45);
//    a.lineTo(x+45,y+25);
//    a.moveTo(x+45,y+45);
//    a.lineTo(x+25,y+45);
//    g.draw(a);}
//  
//  private Stroke createStroke(float w){
//    Stroke stroke=new BasicStroke(w,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,null,0);
//    return stroke;}
//  
//  private Color[] get3Colors(){
//    Random r=new Random();
//    List<Color> c=new ArrayList<Color>();
//    c.addAll(Arrays.asList(Palette.P_TOY_STORY_ADJUSTED2));
//    Color[] a=new Color[3];
//    a[0]=c.remove(r.nextInt(c.size()));
//    a[1]=c.remove(r.nextInt(c.size()));
//    a[2]=c.remove(r.nextInt(c.size()));
//    return a;}
//  
//  private BufferedImage getCharacterStrip(Color[] c3){
//    BufferedImage s=new BufferedImage(73,1200,BufferedImage.TYPE_INT_RGB);
//    Graphics2D g=s.createGraphics();
//    g.setRenderingHints(Renderer_Abstract.RENDERING_HINTS);
//    g.setPaint(c3[0]);
//    g.fillRect(0,0,73,1200);
//    //
//    g.setTransform(AffineTransform.getQuadrantRotateInstance(1));
//    //
//    g.setPaint(c3[1]);
//    g.setFont(getMMFont());
//    g.drawString(getMMString(),88,-10);
//    
//    return s;
//  }
//  
//  static final String CHAR="0123456789ABCDEF";
//  
//  private String getMMString(){
//    Random r=new Random();
//    StringBuffer a=new StringBuffer();
//    for(int i=0;i<31;i++)
//      a.append(CHAR.charAt(r.nextInt(CHAR.length())));
//    return a.toString();}
//  
//  private Font getMMFont(){
//    try{
//      GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
//      ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,new File("/home/john/.fonts/ShareTechMono-Regular.ttf")));
//    }catch(Exception x){
//      x.printStackTrace();}
//    Font f=new Font("Share Tech Mono",Font.PLAIN,65);
//    return f;}
//  
//  /*
//   * ################################
//   * MAIN
//   * ################################
//   */
//  
////  public static final void main(String[] a){
////    new FCGenerator_MartianMoney();}
//  
}
