package kareltherobot;


// 
// Decompiled by Procyon v0.5.36
// 

 

import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.util.Enumeration;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.LayoutManager;
import java.awt.GridLayout;
import java.awt.event.AdjustmentListener;
import java.awt.Scrollbar;
import java.awt.Panel;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.Canvas;
import java.awt.Rectangle;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.Point;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.MediaTracker;
import java.awt.Component;
import java.awt.ScrollPane;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.event.WindowListener;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Frame;
import java.util.ArrayList;

public class RobotWorldWindow extends Frame
{
    private static int scaleInset;
    private static int oldScale;
    private static Toolkit toolkit;
    private static boolean isMac;
    private static Image karelImageNOnBase;
    private static Image karelImageSOnBase;
    private static Image karelImageEOnBase;
    private static Image karelImageWOnBase;
    private static Image karelImageNOffBase;
    private static Image karelImageSOffBase;
    private static Image karelImageEOffBase;
    private static Image karelImageWOffBase;
    private static Image nsCursorImage;
    private static Image ewCursorImage;
    private static Image beeperCursorImage;
    private static Image robotCursorImage;
    private static Image robotRotateImage;
    private static Image nsDeleteImage;
    private static Image ewDeleteImage;
    private static Image beeperDeleteImage;
    private static Image robotDeleteImage;
    private static Cursor defaultCursor;
    private static Cursor nsCursor;
    private static Cursor ewCursor;
    private static Cursor beeperCursor;
    private static Cursor robotCursor;
    private static Cursor nsDelete;
    private static Cursor ewDelete;
    private static Cursor beeperDelete;
    private static Cursor robotDelete;
    private static Cursor robotRotateCursor;
    private WindowListener closer;
    private static Image[] robotsOn;
    private static Image[] robotsOff;
    private Label whereBar;
    private MouseWatcher mouser;
    private static int worldHeight;
    private static int worldWidth;
    private static Dimension lastSize;
    private static final int fromTop = 10;
    private static final int leftEdge = 15;
    private static final int delta = 5;
    private static Color streetColor;
    private static Color wallColor;
    private static Color beeperColor;
    private static Color backgroundColor;
    private static int bottomEdge;
    private static final int wallSize = 4;
    private ViewCanvas worldView;
    private int delay;
    private ControlThread controlThread;
    private Font displayFont;
    private static ArrayList<Image[]> toScale;
    private static ArrayList<Image[]> scaleResults;
    static /* synthetic */ Class class$0;
    
    static {
        RobotWorldWindow.scaleInset = 0;
        RobotWorldWindow.oldScale = 0;
        RobotWorldWindow.toolkit = null;
        RobotWorldWindow.isMac = false;
        RobotWorldWindow.robotsOn = new Image[4];
        RobotWorldWindow.robotsOff = new Image[4];
        RobotWorldWindow.toScale = new ArrayList<Image[]>();
        RobotWorldWindow.scaleResults = new ArrayList<Image[]>();
        RobotWorldWindow.worldHeight = 0;
        RobotWorldWindow.worldWidth = RobotWorldWindow.worldHeight + 80;
        RobotWorldWindow.lastSize = new Dimension(0, 0);
        RobotWorldWindow.streetColor = Color.red.darker();
        RobotWorldWindow.wallColor = Color.black;
        RobotWorldWindow.beeperColor = Color.black;
        RobotWorldWindow.backgroundColor = Color.white;
        RobotWorldWindow.bottomEdge = RobotWorldWindow.worldHeight - 15 - 5;
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(RobotWorldWindow.worldWidth + 10, RobotWorldWindow.worldHeight + 20);
    }
    
    public Dimension innerDimension() {
        final Dimension size;
        final Dimension base = size = this.getSize();
        size.height -= 20;
        final Dimension dimension = base;
        dimension.width -= 10;
        return base;
    }
    
    public RobotWorldWindow() {
        this(Toolkit.getDefaultToolkit().getScreenSize().height - 20);
    }
    
    public RobotWorldWindow(final int height) {
        super("Karel J. Robot");
        this.closer = new CloseListener();
        this.whereBar = new Label("");
        this.mouser = new MouseWatcher();
        this.worldView = null;
        this.delay = 100;
        this.controlThread = new ControlThread();
        this.displayFont = new Font("Dialog", 0, 14);
        RobotWorldWindow.worldHeight = height;
        RobotWorldWindow.worldWidth = height + 80;
        this.setSize(RobotWorldWindow.worldWidth + 10, RobotWorldWindow.worldHeight + 20);
        this.setLocation(5, 10);
        final ScrollPane scroller = new ScrollPane();
        scroller.add(this.worldView = new ViewCanvas());
        this.add("Center", scroller);
        this.addWindowListener(this.closer);
        this.setBackground(Color.white);
        this.repaint();
        this.controlThread.start();
        RobotWorldWindow.toolkit = this.getToolkit();
        final String suffix = ".png";
        RobotWorldWindow.karelImageNOnBase = this.getImage("kareln" + suffix);
        RobotWorldWindow.karelImageSOnBase = this.getImage("karels" + suffix);
        RobotWorldWindow.karelImageEOnBase = this.getImage("karele" + suffix);
        RobotWorldWindow.karelImageWOnBase = this.getImage("karelw" + suffix);
        MediaTracker track = new MediaTracker(this);
        track.addImage(RobotWorldWindow.karelImageNOnBase, 0);
        track.addImage(RobotWorldWindow.karelImageSOnBase, 1);
        track.addImage(RobotWorldWindow.karelImageEOnBase, 2);
        track.addImage(RobotWorldWindow.karelImageWOnBase, 3);
        try {
            track.waitForAll();
        }
        catch (InterruptedException ex15) {}
        RobotWorldWindow.karelImageNOffBase = this.getImage("karelnOff" + suffix);
        RobotWorldWindow.karelImageSOffBase = this.getImage("karelsOff" + suffix);
        RobotWorldWindow.karelImageEOffBase = this.getImage("kareleOff" + suffix);
        RobotWorldWindow.karelImageWOffBase = this.getImage("karelwOff" + suffix);
        track.addImage(RobotWorldWindow.karelImageNOffBase, 0);
        track.addImage(RobotWorldWindow.karelImageSOffBase, 1);
        track.addImage(RobotWorldWindow.karelImageEOffBase, 2);
        track.addImage(RobotWorldWindow.karelImageWOffBase, 3);
        try {
            track.waitForAll();
        }
        catch (InterruptedException ex16) {}
        System.out.println("Reached main benchmark");
        //GridEntity.painter = new Painter(new String[]{"idle ca karele.png"}, this);
        /*GridEntity g = new GridEntity(1,1,Directions.East,0);
        g.delete();*/
        System.out.println("Past main benchmark "+GridEntity.painter);
        //this.scaleAllRobotImages();
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(final ComponentEvent evt) {
                RobotWorldWindow.this.scaleAllRobotImages();
                RobotWorldWindow.this.repaint();
            }
        });
        RobotWorldWindow.defaultCursor = this.getCursor();
        RobotWorldWindow.nsCursor = RobotWorldWindow.defaultCursor;
        RobotWorldWindow.ewCursor = RobotWorldWindow.defaultCursor;
        RobotWorldWindow.beeperCursor = RobotWorldWindow.defaultCursor;
        RobotWorldWindow.robotCursor = RobotWorldWindow.defaultCursor;
        RobotWorldWindow.robotRotateCursor = RobotWorldWindow.defaultCursor;
        RobotWorldWindow.nsDelete = RobotWorldWindow.defaultCursor;
        RobotWorldWindow.ewDelete = RobotWorldWindow.defaultCursor;
        RobotWorldWindow.beeperDelete = RobotWorldWindow.defaultCursor;
        RobotWorldWindow.robotDelete = RobotWorldWindow.defaultCursor;
        try {
            RobotWorldWindow.nsCursorImage = this.getImage("nscursor.gif");
            RobotWorldWindow.ewCursorImage = this.getImage("ewcursor.gif");
            RobotWorldWindow.beeperCursorImage = this.getImage("beepercursor.gif");
            RobotWorldWindow.nsDeleteImage = this.getImage("nsdeletecursor.gif");
            RobotWorldWindow.ewDeleteImage = this.getImage("ewdeletecursor.gif");
            RobotWorldWindow.beeperDeleteImage = this.getImage("beeperdeletecursor.gif");
            RobotWorldWindow.robotCursorImage  = RobotWorldWindow.karelImageNOnBase;
            RobotWorldWindow.robotDeleteImage  = RobotWorldWindow.karelImageNOffBase;
            RobotWorldWindow.robotRotateImage  = RobotWorldWindow.karelImageWOnBase;
            track = new MediaTracker(this);
            track.addImage(RobotWorldWindow.nsCursorImage, 0);
            track.addImage(RobotWorldWindow.ewCursorImage, 1);
            track.addImage(RobotWorldWindow.beeperCursorImage, 2);
            track.addImage(RobotWorldWindow.robotCursorImage, 6);
            track.addImage(RobotWorldWindow.nsDeleteImage, 3);
            track.addImage(RobotWorldWindow.ewDeleteImage, 4);
            track.addImage(RobotWorldWindow.beeperDeleteImage, 5);
            track.addImage(RobotWorldWindow.robotDeleteImage, 7);
            track.addImage(RobotWorldWindow.robotRotateImage, 8);
            try {
                track.waitForAll();
            }
            catch (InterruptedException ex17) {}
            RobotWorldWindow.ewCursor = RobotWorldWindow.toolkit.createCustomCursor(RobotWorldWindow.ewCursorImage, new Point(8, 8), "ewWall");
            RobotWorldWindow.nsCursor = RobotWorldWindow.toolkit.createCustomCursor(RobotWorldWindow.nsCursorImage, new Point(8, 8), "nsWall");
            RobotWorldWindow.beeperCursor = RobotWorldWindow.toolkit.createCustomCursor(RobotWorldWindow.beeperCursorImage, new Point(8, 8), "beeper");
            RobotWorldWindow.robotCursor = RobotWorldWindow.toolkit.createCustomCursor(RobotWorldWindow.robotCursorImage, new Point(8, 8), "robot");
            RobotWorldWindow.ewDelete = RobotWorldWindow.toolkit.createCustomCursor(RobotWorldWindow.ewDeleteImage, new Point(8, 8), "ewDelete");
            RobotWorldWindow.nsDelete = RobotWorldWindow.toolkit.createCustomCursor(RobotWorldWindow.nsDeleteImage, new Point(8, 8), "nsDelete");
            RobotWorldWindow.beeperDelete = RobotWorldWindow.toolkit.createCustomCursor(RobotWorldWindow.beeperDeleteImage, new Point(8, 8), "beeperDelete");
            RobotWorldWindow.robotDelete = RobotWorldWindow.toolkit.createCustomCursor(RobotWorldWindow.robotDeleteImage, new Point(8, 8), "robotDelete");
            RobotWorldWindow.robotRotateCursor = RobotWorldWindow.toolkit.createCustomCursor(RobotWorldWindow.robotRotateImage, new Point(8, 8), "robotRotate");
        }
        catch (Throwable e1) {
            System.out.println("Failed to create the custom cursors.");
        }
        this.setVisible(false);
    }
    
    public final void showControlDialog(final boolean show) {
        this.controlThread.showDialog(show);
    }
    
    public synchronized void scaleAllRobotImages() {
        int scaleVal = getresizescale();
        //System.out.println("big scale scaleVal: "+scaleVal);
        if(scaleVal==0){
            return;
        }
        RobotWorldWindow.robotsOn[3] = RobotWorldWindow.karelImageNOnBase.getScaledInstance(scaleVal, -1, 4);
        RobotWorldWindow.robotsOn[1] = RobotWorldWindow.karelImageSOnBase.getScaledInstance(scaleVal, -1, 4);
        RobotWorldWindow.robotsOn[0] = RobotWorldWindow.karelImageEOnBase.getScaledInstance(scaleVal, -1, 4);
        RobotWorldWindow.robotsOn[2] = RobotWorldWindow.karelImageWOnBase.getScaledInstance(scaleVal, -1, 4);
        RobotWorldWindow.robotsOff[3] = RobotWorldWindow.karelImageNOffBase.getScaledInstance(scaleVal, -1, 4);
        RobotWorldWindow.robotsOff[1] = RobotWorldWindow.karelImageSOffBase.getScaledInstance(scaleVal, -1, 4);
        RobotWorldWindow.robotsOff[0] = RobotWorldWindow.karelImageEOffBase.getScaledInstance(scaleVal, -1, 4);
        RobotWorldWindow.robotsOff[2] = RobotWorldWindow.karelImageWOffBase.getScaledInstance(scaleVal, -1, 4);
        final MediaTracker track = new MediaTracker(this);
        for(int i = 0; i < toScale.size(); i++){
            scaleSubscribedCycle(i);
        }
        for (int i = 0; i < 4; ++i) {
            track.addImage(RobotWorldWindow.robotsOn[i], 2 * i);
            track.addImage(RobotWorldWindow.robotsOff[i], 2 * i + 1);
        }
        try {
            track.waitForAll();
        }
        catch (InterruptedException ex) {}
        RobotWorldWindow.oldScale = scaleVal;
        this.repaint();
        System.out.println("Scaling");
    }
    
    //custom
    public int scaleSubscribedCycle(int i){
        int scaleVal = getresizescale();
        System.out.println("cycle scale scaleVal: "+scaleVal);
        if(scaleVal==0){
            return 0;
        }
        final MediaTracker track = new MediaTracker(this);
        Image[] b = toScale.get(i);
        Image[] a = scaleResults.get(i);
        for(int j = 0; j < b.length; j++){
            a[j] = b[j].getScaledInstance(scaleVal, -1, 4);
            track.addImage(a[j], j);
        }
        try{
            track.waitForAll();
        }catch(InterruptedException ex){}
        return scaleVal;
    }
    public int getresizescale(){
        int scale = (this.bottom() - 10) / World.numberOfStreets();
        if (scale == 0) {
            scale = 1;
        }
        int scaleVal = scale * 7 / 9;
        if (scaleVal < 10) {
            scaleVal = 10;
        }
        if (RobotWorldWindow.oldScale > 0 && 4 * RobotWorldWindow.oldScale < 5 * scaleVal && 5 * scaleVal < 6 * RobotWorldWindow.oldScale) {
            return 0;
        }
        RobotWorldWindow.scaleInset = scaleVal;
        return scaleVal;
    }
    public static void subscribeScaler(Image[] pre, Image[] post){
        toScale.add(pre);
        scaleResults.add(post);
        System.out.println("Subscription called");
        /*scaleSubscribedCycle(toScale.size()-1);
        this.scaleAllRobotImages();
        this.repaint();*/
    }
    public void unsubscribeScaler(Image[] pre, Image[] post){
        toScale.remove(pre);
        scaleResults.remove(post);
    }
    public Image getImage(String url){
        final Toolkit toolkit = RobotWorldWindow.toolkit;
        Class class$0;
        if ((class$0 = RobotWorldWindow.class$0) == null) {
            try {
                class$0 = (RobotWorldWindow.class$0 = Class.forName("kareltherobot.RobotWorldWindow"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        return toolkit.getImage(class$0.getResource(url));
    }
    public Image rotateTo(int op, Image j){//0=east, 3=north
        return j;
    }
    public Image mirror(Image j){return null;}
    public Image getImage(String url, int op){
        Image j = getImage(url);
        if(op==-1){
            return mirror(j);
        }else if(op>0){
            System.out.println(j);
            return rotateTo(op-1, j);
        }else{
            return j;
        }
    }
    
    public void addMouseListener(final MouseListener m) {
        this.worldView.addMouseListener(m);
    }
    
    public void attachMouseMotionListener(final MouseMotionListener m) {
        this.worldView.addMouseMotionListener(m);
    }
    
    void defaultCursor() {
        this.toFront();
        this.worldView.setCursor(RobotWorldWindow.defaultCursor);
        this.worldView.requestFocus();
    }
    
    void ewCursor() {
        this.toFront();
        this.worldView.setCursor(RobotWorldWindow.ewCursor);
        this.worldView.setVisible(true);
        this.worldView.requestFocus();
    }
    
    void nsCursor() {
        this.toFront();
        this.worldView.setCursor(RobotWorldWindow.nsCursor);
        this.worldView.requestFocus();
    }
    
    void beeperCursor() {
        this.toFront();
        this.worldView.setCursor(RobotWorldWindow.beeperCursor);
        this.worldView.requestFocus();
    }
    
    void robotCursor() {
        this.toFront();
        this.worldView.setCursor(RobotWorldWindow.robotCursor);
        this.worldView.requestFocus();
    }
    
    void robotRotateCursor() {
        this.toFront();
        this.worldView.setCursor(RobotWorldWindow.robotRotateCursor);
        this.worldView.requestFocus();
    }
    
    public final void setMac(final boolean mac) {
        RobotWorldWindow.isMac = mac;
    }
    
    public void enableStop() {
        this.controlThread.stop.setEnabled(true);
    }
    
    public static int scaleInset() {
        return RobotWorldWindow.scaleInset;
    }
    
    public Image scale(final Image aRobot, final int scaleVal, final int hint) {
        return aRobot.getScaledInstance(scaleVal, -1, hint);
    }
    
    void replaceCloser(final WindowListener other) {
        this.removeWindowListener(this.closer);
        this.addWindowListener(this.closer = other);
    }
    
    public void resetControl() {
        this.controlThread.reset();
    }
    
    public void reset() {
        this.controlThread.stop.setLabel("Resume");
        World.stop();
        this.worldView.repaint();
    }
    
    private Rectangle getClip(final UrRobot.StateObject so) {
        final Rectangle r = new Rectangle();
        final int street = so.street();
        final int avenue = so.avenue();
        final int adjust = scaleInset() / 2;
        final int logicalBottomEdge = this.logicalBottomEdge();
        final int scale = this.getScale();
        r.x = 15 + (avenue - 1) * scale - adjust;
        r.y = logicalBottomEdge - (street + 1) * scale - adjust;
        int cells = 2;
        if (so.lastAction() == 0) {
            cells = 3;
        }
        r.height = cells * scale;
        r.width = cells * scale;
        return r;
    }
    
    private Rectangle getClip(final GridEntity.StateObject so) {
        final Rectangle r = new Rectangle();
        final int street = so.street();
        final int avenue = so.avenue();
        final int adjust = scaleInset() / 2;
        final int logicalBottomEdge = this.logicalBottomEdge();
        final int scale = this.getScale();
        r.x = 15 + (avenue - 1) * scale - adjust;
        r.y = logicalBottomEdge - (street + 1) * scale - adjust;
        int cells = 10;
        if (so.lastAction() == 0) {
            cells = 10;
        }
        r.height = cells * scale;
        r.width = cells * scale;
        return r;
    }
    
    public synchronized void prepareToDraw(final UrRobot.StateObject so) {
        final Rectangle r = this.getClip(so);
        //System.out.println(r.x+" "+r.y+" "+r.width+" "+r.height);
        this.worldView.repaint(r.x, r.y, r.width, r.height);
    }
    
    public synchronized void prepareToDraw(final GridEntity.StateObject so) {
        Rectangle r;
        if(so.lastState()!=null){
            r = this.getClip(so.lastState());
            this.worldView.repaint(r.x, r.y, r.width, r.height);
        }
        r = this.getClip(so);
        //System.out.println(r.x+" "+r.y+" "+r.width+" "+r.height);
        this.worldView.repaint(r.x, r.y, r.width, r.height);
    }
    
    public synchronized void repaint() {
        this.worldView.repaint();
    }
    
    public int getScale() {
        final int worldHeight = this.innerDimension().height;
        int streets = World.numberOfStreets();
        if (streets <= 0) {
            streets = 10;
        }
        int result = (worldHeight - 15 - 10) / streets;
        if (result <= 0) {
            result = 1;
        }
        return result;
    }
    
    private int logicalBottomEdge() {
        return this.innerDimension().height - 15 - 10;
    }
    
    public int bottom() {
        return this.logicalBottomEdge() + 5;
    }
    
    public int left() {
        return 15;
    }
    
    private int wallSize() {
        int result = 4;
        final int streets = World.numberOfStreets();
        if (streets > 20) {
            result /= 2;
        }
        if (streets > 60) {
            result /= 2;
        }
        if (streets == 0) {
            result = 1;
        }
        return result;
    }
    
    static final void setStreetColor(final Color c) {
        if (c != null) {
            RobotWorldWindow.streetColor = c;
        }
    }
    
    static final void setWallColor(final Color c) {
        if (c != null) {
            RobotWorldWindow.wallColor = c;
        }
    }
    
    static final void setBeeperColor(final Color c) {
        if (c != null) {
            RobotWorldWindow.beeperColor = c;
        }
    }
    
    final void setBackgroundColor(final Color c) {
        this.worldView.setBackground(c);
        RobotWorldWindow.backgroundColor = c;
    }
    
    Canvas worldCanvas() {
        return this.worldView;
    }
    
    static /* synthetic */ void access$1(final RobotWorldWindow robotWorldWindow, final int delay) {
        robotWorldWindow.delay = delay;
    }
    
    static /* synthetic */ void access$13(final Dimension lastSize) {
        RobotWorldWindow.lastSize = lastSize;
    }
    
    private class CornerScaler implements WorldBuilder.MouseScaler
    {
        CornerScaler() {
        }
        
        public void scale(final int rawx, final int rawy, final Point result) {
            final RobotWorldWindow view = World.view();
            int scale = (view.bottom() - 10) / World.numberOfStreets();
            if (scale == 0) {
                scale = 1;
            }
            result.x = (rawx - view.left() + scale / 2) / scale;
            result.y = (view.bottom() - rawy + scale / 2) / scale;
        }
        
        public void dropItem(final MouseEvent evt) {
        }
    }
    
    private class MouseWatcher extends MouseMotionAdapter
    {
        private WorldBuilder.MouseScaler scaler;
        private Point where;
        
        MouseWatcher() {
            this.scaler = new CornerScaler();
            this.where = new Point();
        }
        
        public void mouseMoved(final MouseEvent evt) {
            this.scaler.scale(evt.getX(), evt.getY(), this.where);
            int avenue = this.where.x;
            int street = this.where.y;
            if (avenue < 1) {
                avenue = 1;
            }
            if (street < 1) {
                street = 1;
            }
            RobotWorldWindow.this.whereBar.setText(street + ", " + avenue);
            RobotWorldWindow.this.whereBar.repaint();
        }
    }
    
    
    
    class ViewCanvas extends Canvas implements Directions
    {
        private int x1;
        private int y1;
        private int x2;
        private int y2;
        private int oldStreet;
        private int oldAvenue;
        private int oldFacing;
        private boolean moved;
        private boolean turned;
        private boolean picked;
        private Image offScreen;
        private Graphics osg;
        private boolean resized;
        private int scale;
        
        public ViewCanvas() {
            this.x1 = 0;
            this.y1 = 0;
            this.x2 = 0;
            this.y2 = 0;
            this.oldStreet = 0;
            this.oldAvenue = 0;
            this.oldFacing = 3;
            this.moved = false;
            this.turned = false;
            this.picked = false;
            this.offScreen = null;
            this.osg = null;
            this.resized = false;
            this.addMouseMotionListener(RobotWorldWindow.this.mouser);
            this.addKeyListener(new ControlKeyListener());
        }
        
        public boolean isDoubleBuffered() {
            return true;
        }
        
        public Dimension getPreferredSize() {
            final Dimension world = RobotWorldWindow.this.innerDimension();
            int width = world.width;
            final int height = world.height - 30;
            final int bigWidth = width = RobotWorldWindow.this.getScale() * (World.numberOfAvenues() + 1);
            return new Dimension(width, height);
        }
        
        public synchronized void paint(final Graphics g) {
            //System.out.println("PAINT");
            int streets = World.numberOfStreets();
            int avenues = World.numberOfAvenues();
            final int wallSize = RobotWorldWindow.this.wallSize();
            RobotWorldWindow.access$13(RobotWorldWindow.this.innerDimension());
            if (streets <= 0) {
                streets = 10;
            }
            if (avenues <= 0) {
                avenues = 10;
            }
            final int bottomEdge = RobotWorldWindow.this.logicalBottomEdge();
            this.scale = RobotWorldWindow.this.getScale();
            this.setBackground(RobotWorldWindow.backgroundColor);
            g.setColor(RobotWorldWindow.streetColor);
            for (int i = 1; i <= streets; ++i) {
                g.drawLine(15 + this.scale / 2, bottomEdge - i * this.scale, 15 + avenues * this.scale, bottomEdge - i * this.scale);
            }
            for (int i = 1; i <= avenues; ++i) {
                g.drawLine(15 + i * this.scale, bottomEdge - this.scale / 2, 15 + i * this.scale, bottomEdge - streets * this.scale);
            }
            g.setColor(RobotWorldWindow.wallColor);
            g.fillRect(15 - wallSize + this.scale / 2, bottomEdge - streets * this.scale, wallSize, streets * this.scale - this.scale / 2 + wallSize);
            g.fillRect(15 + this.scale / 2, bottomEdge - this.scale / 2, avenues * this.scale - this.scale / 2, wallSize);
            g.setColor(Color.black);
            if (streets <= 50) {
                int i = 0;
                int x = 0;
                final int y = bottomEdge - this.scale / 2 + 15;
                for (i = 1; i <= avenues; ++i) {
                    g.drawString(String.valueOf(i), 15 + i * this.scale - 5, y);
                }
                x = 15 - wallSize + this.scale / 2 - 15;
                for (i = 1; i <= streets; ++i) {
                    g.drawString(String.valueOf(i), x, bottomEdge - i * this.scale + 5);
                }
            }
            g.setColor(RobotWorldWindow.wallColor);
            Enumeration<World.IntPair> e = World.ewWalls();
            while (e.hasMoreElements()) {
                final World.IntPair p = e.nextElement();
                this.drawHWall(p.street(), p.avenue(), g);
            }
            e = World.nsWalls();
            while (e.hasMoreElements()) {
                final World.IntPair p = e.nextElement();
                this.drawVWall(p.street(), p.avenue(), g);
            }
            Enumeration<World.BeeperCell> a = World.beepers();
            g.setColor(RobotWorldWindow.beeperColor);
            while (a.hasMoreElements()) {
                final World.BeeperCell p2 = a.nextElement();
                this.drawBeeper(p2.street(), p2.avenue(), p2.number(), g, g.getColor());
            }
            Enumeration<UrRobot> v = World.robots();
            while (v.hasMoreElements()) {
                final UrRobot p3 = v.nextElement();
                this.drawRobot(p3, g);
            }
            Enumeration<GridEntity> s = World.entities();
            while (s.hasMoreElements()) {
                final GridEntity d = s.nextElement();
                this.drawEntity(d, g);
            }
        }
        
        public final synchronized void update(final Graphics g) {
            super.update(g);
            this.resized = !RobotWorldWindow.this.innerDimension().equals(RobotWorldWindow.lastSize);
            final int worldHeight = RobotWorldWindow.this.innerDimension().height;
            final int worldWidth = 15 + (World.numberOfAvenues() + 1) * RobotWorldWindow.this.getScale();
            if (this.resized) {
                this.offScreen = null;
                this.resized = false;
            }
            if (this.resized || this.offScreen == null) {
                this.offScreen = this.createImage(worldWidth, worldHeight);
                this.resized = false;
            }
            if (this.offScreen != null) {
                if (this.osg != null) {
                    this.osg.dispose();
                }
                this.osg = this.offScreen.getGraphics();
                if (RobotWorldWindow.isMac) {
                    this.osg.setClip(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1);
                    if (this.moved || this.turned) {
                        this.drawOldRobot(this.oldStreet, this.oldAvenue, this.oldFacing, true, this.osg, this.getBackground());
                    }
                    if (this.picked) {
                        this.drawBeeper(this.oldStreet, this.oldAvenue, 1, this.osg, this.getBackground());
                    }
                }
                this.paint(this.osg);
                if (RobotWorldWindow.isMac) {
                    g.drawImage(this.offScreen, this.x1, this.y1, this.x2, this.y2, this.x1, this.y1, this.x2, this.y2, null);
                    this.moved = false;
                    this.turned = false;
                    this.picked = false;
                }
                else {
                    g.drawImage(this.offScreen, 0, 0, null);
                    this.offScreen.flush();
                    this.offScreen = null;
                }
            }
        }
        
        private void drawHWall(final int s, final int a, final Graphics g) {
            g.fillRect(15 + a * this.scale - this.scale / 2, RobotWorldWindow.this.logicalBottomEdge() - s * this.scale - this.scale / 2, this.scale, RobotWorldWindow.this.wallSize());
        }
        
        private void drawVWall(final int s, final int a, final Graphics g) {
            g.fillRect(15 + a * this.scale + this.scale / 2, RobotWorldWindow.this.logicalBottomEdge() - s * this.scale - this.scale / 2, RobotWorldWindow.this.wallSize(), this.scale + RobotWorldWindow.this.wallSize());
        }
        
        private synchronized void drawBeeper(final int s, final int a, final int howMany, final Graphics g, final Color c) {
            final Color oldColor = g.getColor();
            g.setColor(c);
            g.fillOval(15 + a * this.scale - this.scale / 4, RobotWorldWindow.this.logicalBottomEdge() - s * this.scale - this.scale / 4, this.scale / 2, this.scale / 2);
            g.setColor(Color.white);
            final String name = (howMany > 0) ? new StringBuffer().append(howMany).toString() : "oo";
            g.drawString(name, 15 + a * this.scale - this.scale / 8, RobotWorldWindow.this.logicalBottomEdge() - s * this.scale + this.scale / 8);
            g.setColor(oldColor);
        }
        
        private synchronized Point locationToPixels(final int street, final int avenue) {
            final int scale = RobotWorldWindow.this.getScale();
            final int x = 15 + scale * avenue;
            final int y = RobotWorldWindow.this.logicalBottomEdge() - scale * street;
            return new Point(x, y);
        }
        
        private synchronized Rectangle robotCenter(final UrRobot karel) {
            final int s = karel.street();
            final int a = karel.avenue();
            final Point center = this.locationToPixels(s, a);
            int size = RobotWorldWindow.this.getScale() / 12;
            if (size < 2) {
                size = 2;
            }
            if (karel.crashed()) {
                ++size;
            }
            return new Rectangle(center.x - size, center.y - size, 2 * size, 2 * size);
        }
        
        private synchronized void drawRobot(final UrRobot karel, final Graphics g) {
            if (!karel.isVisible()) {
                return;
            }
            final int s = karel.street();
            final int a = karel.avenue();
            Image[] robots = RobotWorldWindow.robotsOn;
            if (!karel.running()) {
                robots = RobotWorldWindow.robotsOff;
            }
            final Image karelImage = robots[karel.direction().points()];
            final int adjust = RobotWorldWindow.scaleInset() / 2;
            g.drawImage(karelImage, 15 + a * this.scale - adjust, RobotWorldWindow.this.logicalBottomEdge() - s * this.scale - adjust, this);
            if (karel.badgeColor() != null) {
                g.setColor(karel.badgeColor());
                final Rectangle center = this.robotCenter(karel);
                g.fillRect(center.x, center.y, center.width, center.height);
                g.setColor(Color.black);
                if (karel.badgeColor().equals(Color.black)) {
                    g.setColor(Color.red);
                }
                g.drawRect(center.x, center.y, center.width, center.height);
                if (karel.crashed()) {
                    this.drawCrashed(center, g);
                }
            }
            else if (karel.crashed()) {
                g.setColor(Color.white);
                final Rectangle center = this.robotCenter(karel);
                g.drawRect(center.x, center.y, center.width, center.height);
                this.drawCrashed(center, g);
            }
        }
        
        //custom
        private synchronized void drawEntity(final GridEntity karel, final Graphics g) {
            if (!karel.isVisible()) {
                return;
            }
            final int s = karel.street();
            final int a = karel.avenue();
            Image robotest = RobotWorldWindow.robotsOn[karel.direction().points()];
            final Image karelImage = karel.getImage();
            final int adjust = RobotWorldWindow.scaleInset() / 2;
            //System.out.println("About to draw "+karelImage);
            g.drawImage(karelImage, 15 + a * this.scale - adjust, RobotWorldWindow.this.logicalBottomEdge() - s * this.scale - adjust, this);
            /*if (karel.badgeColor() != null) {
                g.setColor(karel.badgeColor());
                final Rectangle center = this.robotCenter(karel);
                g.fillRect(center.x, center.y, center.width, center.height);
                g.setColor(Color.black);
                if (karel.badgeColor().equals(Color.black)) {
                    g.setColor(Color.red);
                }
                g.drawRect(center.x, center.y, center.width, center.height);
                if (karel.crashed()) {
                    this.drawCrashed(center, g);
                }
            }
            else if (karel.crashed()) {
                g.setColor(Color.white);
                final Rectangle center = this.robotCenter(karel);
                g.drawRect(center.x, center.y, center.width, center.height);
                this.drawCrashed(center, g);
            }*/
        }
        
        private void drawCrashed(final Rectangle center, final Graphics g) {
            g.drawLine(center.x, center.y, center.x + center.width, center.y + center.width);
            g.drawLine(center.x, center.y + center.width, center.x + center.width, center.y);
        }
        
        private synchronized void drawOldRobot(final int s, final int a, final int facing, final boolean running, final Graphics g, final Color c) {
            Image[] robots = RobotWorldWindow.robotsOn;
            if (!running) {
                robots = RobotWorldWindow.robotsOff;
            }
            final Image karelImage = robots[facing];
            g.drawImage(karelImage, 15 + a * this.scale - 12, RobotWorldWindow.this.logicalBottomEdge() - s * this.scale - 12, null);
        }
        
        class ControlKeyListener implements KeyListener
        {
            public void keyTyped(final KeyEvent e) {
            }
            
            public void keyPressed(final KeyEvent e) {
                final Cursor cursor = ViewCanvas.this.getCursor();
                final boolean shift = e.isShiftDown();
                final boolean control = e.isControlDown();
                final boolean shiftOrControl = shift || control;
                final boolean alt = e.isAltDown();
                if (!(shiftOrControl)) {
                    return;
                }
                if (control && cursor.equals(RobotWorldWindow.ewCursor)) {
                    ViewCanvas.this.setCursor(RobotWorldWindow.ewDelete);
                }
                else if (control && cursor.equals(RobotWorldWindow.nsCursor)) {
                    ViewCanvas.this.setCursor(RobotWorldWindow.nsDelete);
                }
                else if (shiftOrControl && cursor.equals(RobotWorldWindow.beeperCursor)) {
                    ViewCanvas.this.setCursor(RobotWorldWindow.beeperDelete);
                }
                else if (control && cursor.equals(RobotWorldWindow.robotCursor)) {
                    ViewCanvas.this.setCursor(RobotWorldWindow.robotDelete);
                }
            }
            
            public void keyReleased(final KeyEvent e) {
                final Cursor cursor = ViewCanvas.this.getCursor();
                final boolean shift = e.isShiftDown();
                final boolean control = e.isControlDown();
                final boolean shiftOrControl = shift || control;
                final boolean alt = e.isAltDown();
                if (shiftOrControl) {
                    return;
                }
                if (!control && cursor.equals(RobotWorldWindow.ewDelete)) {
                    ViewCanvas.this.setCursor(RobotWorldWindow.ewCursor);
                }
                else if (!control && cursor.equals(RobotWorldWindow.nsDelete)) {
                    ViewCanvas.this.setCursor(RobotWorldWindow.nsCursor);
                }
                else if (!shiftOrControl && cursor.equals(RobotWorldWindow.beeperDelete)) {
                    ViewCanvas.this.setCursor(RobotWorldWindow.beeperCursor);
                }
                else if (!control && cursor.equals(RobotWorldWindow.robotDelete)) {
                    ViewCanvas.this.setCursor(RobotWorldWindow.robotCursor);
                }
            }
        }
    }
    
    private class CloseListener extends WindowAdapter
    {
        CloseListener() {
        }
        
        public void windowClosing(final WindowEvent e) {
            e.getWindow().setVisible(false);
            RobotWorldWindow.this.controlThread.doStop();
            e.getWindow().dispose();
            World.killBuilder();
            World.stop();
            System.exit(0);
        }
    }
    
    class ControlThread extends Thread
    {
        private Dialog frame;
        private Button stop;
        private Panel controls;
        private Scrollbar speed;
        private boolean started;
        
        public ControlThread() {
            this.frame = new Dialog(new Frame("empty"), "Control");
            this.stop = new Button("Resume");
            this.controls = new Panel();
            this.speed = new Scrollbar(0);
            this.started = false;
            this.stop.setEnabled(false);
            this.frame.setResizable(false);
        }
        
        public void run() {
            final SpeedListener speedListener = new SpeedListener();
            this.speed.addAdjustmentListener(speedListener);
            this.frame.setLocation(450, 400);
            this.frame.setSize(210, 90);
            this.controls.setSize(200, 80);
            this.controls.setLayout(new GridLayout(2, 2));
            this.frame.add(this.controls);
            this.speed.setValues(0, 0, 0, 101);
            this.speed.setSize(300, this.speed.getSize().height);
            this.speed.setValue(100 - World.delay());
            this.stop.addActionListener(new StopListener());
            RobotWorldWindow.this.whereBar.setBackground(Color.white);
            this.controls.add(this.stop);
            RobotWorldWindow.this.whereBar.setFont(RobotWorldWindow.this.displayFont);
            this.controls.add(RobotWorldWindow.this.whereBar);
            final Label speedLabel = new Label("Set Speed", 2);
            speedLabel.setFont(RobotWorldWindow.this.displayFont);
            this.controls.add(speedLabel);
            final Panel spacer = new Panel();
            spacer.setLayout(new BorderLayout());
            spacer.add(new Panel(), "North");
            spacer.add(this.speed);
            spacer.add(new Panel(), "South");
            this.controls.add(spacer);
            this.frame.addWindowListener(new DialogHider());
            this.frame.setVisible(false);
        }
        
        public final void showDialog(final boolean show) {
            this.controls.setVisible(show);
            this.controls.repaint();
            this.frame.setVisible(show);
        }
        
        public final void doStop() {
            this.frame.setVisible(false);
            this.stop();
        }
        
        public void reset() {
            this.started = false;
        }
        
        void access$3(final ControlThread controlThread, final boolean started) {
            controlThread.started = started;
        }
        
        private class DialogHider extends WindowAdapter
        {
            DialogHider() {
            }
            
            public void windowClosing(final WindowEvent e) {
                ControlThread.this.showDialog(false);
            }
        }
        
        private class SpeedListener implements AdjustmentListener
        {
            SpeedListener() {
            }
            
            public void adjustmentValueChanged(final AdjustmentEvent e) {
                RobotWorldWindow.access$1(RobotWorldWindow.this, ControlThread.this.speed.getValue());
                World.setDelay(100 - RobotWorldWindow.this.delay);
                RobotWorldWindow.this.worldView.repaint();
            }
        }
        
        private class StopListener implements ActionListener
        {
            StopListener() {
            }
            
            public void actionPerformed(final ActionEvent e) {
                if (e.getActionCommand().equals("Stop")) {
                    ControlThread.this.stop.setLabel("Resume");
                    World.stop();
                }
                else {
                    ControlThread.this.stop.setLabel("Stop");
                    if (ControlThread.this.started) {
                        World.resume();
                    }
                    else {
                        access$3(ControlThread.this, true);
                        World.startThreads();
                    }
                }
            }
        }
    }
}
