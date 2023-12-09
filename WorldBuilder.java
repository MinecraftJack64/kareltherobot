package kareltherobot;

// 
// Decompiled by Procyon v0.5.36
// 


 

import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.GridLayout;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.awt.Label;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.event.WindowAdapter;
import java.awt.Button;
import java.awt.TextField;
import java.awt.Frame;

public class WorldBuilder extends Frame implements Directions, WorldBuilderInterface
{
    private TextField streets;
    private TextField avenues;
    private Button horizontalWall;
    private Button verticalWall;
    private Button beeper;
    private Button robot;
    private Button rotateRobot;
    private Button selectedButton;
    private WindowAdapter closer;
    private Button save;
    private Button open;
    private Button showSpeed;
    private String filename;
    private String directory;
    private boolean isDirty;
    private FileDialog openDialog;
    private FileDialog saveDialog;
    private Color buttonColor;
    private Color selectedColor;
    private Label statusBar;
    private Label whereBar;
    private Label fileBar;
    private Button clearAll;
    private ItemDropper itemDropper;
    private static final RobotWorldWindow view;
    private Font displayFont;
    private BeeperScaler beeperScaler;
    private HorizontalWallScaler horizontalWallScaler;
    private VerticalWallScaler verticalWallScaler;
    private RobotScaler robotScaler;
    private RotateRobotScaler rotateScaler;
    private MouseWatcher mouseWatcher;
    private static final int fromTop = 10;
    static WorldBuilder wb;
    
    static {
        view = World.view();
        WorldBuilder.wb = null;
    }
    
    public void cleanUp() {
        if (this.isDirty) {
            this.saveFile();
        }
    }
    
    public WorldBuilder(final boolean showSpeedControl) {
        super("World Builder");
        this.streets = new TextField(5);
        this.avenues = new TextField(5);
        this.horizontalWall = new Button("Horizontal Wall");
        this.verticalWall = new Button("Vertical Wall");
        this.beeper = new Button("Beeper");
        this.robot = new Button("Robot");
        this.rotateRobot = new Button("Rotate Robot");
        this.selectedButton = null;
        this.closer = null;
        this.save = new Button("Save");
        this.open = new Button("Open");
        this.showSpeed = new Button("Hide Speedcontrol");
        this.filename = "untitled.kwld";
        this.directory = "";
        this.isDirty = false;
        this.openDialog = new FileDialog(this, null, 0);
        this.saveDialog = new FileDialog(this, null, 1);
        this.buttonColor = null;
        this.selectedColor = new Color(192, 255, 192);
        this.statusBar = new Label("");
        this.whereBar = new Label("");
        this.fileBar = new Label(this.filename);
        this.clearAll = new Button("Clear World");
        this.itemDropper = new ItemDropper();
        this.displayFont = new Font("Dialog", 0, 14);
        this.beeperScaler = new BeeperScaler();
        this.robotScaler = new RobotScaler();
        this.rotateScaler = new RotateRobotScaler();
        this.horizontalWallScaler = new HorizontalWallScaler();
        this.verticalWallScaler = new VerticalWallScaler();
        this.mouseWatcher = new MouseWatcher();
        if (!showSpeedControl) {
            this.showSpeed.setLabel("Show Speedcontrol");
        }
        this.setSize(120, 380);
        this.setLocation(560, 30);
        this.addWindowListener(this.closer = new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                WorldBuilder.this.cleanUp();
                System.exit(0);
            }
        });
        WorldBuilder.view.replaceCloser(this.closer);
        final Panel toolPanel = new Panel(new GridLayout(3, 1, 0, 20));
        this.add("Center", toolPanel);
        final Panel spacerPanel = new Panel(new GridLayout(2, 1, 0, 5));
        final Panel sizePanel = new Panel(new GridLayout(2, 2, 0, 5));
        final Label filler1 = new Label();
        filler1.setVisible(false);
        final Label filler2 = new Label();
        filler2.setVisible(false);
        final Panel innerSpacerPanel = new Panel(new GridLayout(2, 1, 0, 5));
        innerSpacerPanel.add(new Label("Controller"));
        innerSpacerPanel.add(this.showSpeed);
        spacerPanel.add(innerSpacerPanel);
        spacerPanel.add(sizePanel);
        toolPanel.add("North", spacerPanel);
        this.streets.setFont(this.displayFont);
        this.avenues.setFont(this.displayFont);
        sizePanel.add(this.streets);
        sizePanel.add(new Label("Streets"));
        sizePanel.add(this.avenues);
        sizePanel.add(new Label("Avenues"));
        final Panel buttonPanel = new Panel(new GridLayout(5, 1, 0, 5));
        final Panel opPanel = new Panel(new GridLayout(5, 1, 0, 5));
        toolPanel.add("Center", buttonPanel);
        this.whereBar.setAlignment(1);
        this.statusBar.setAlignment(1);
        this.whereBar.setFont(this.displayFont);
        this.statusBar.setFont(this.displayFont);
        buttonPanel.add(this.statusBar);
        buttonPanel.add(this.whereBar);
        buttonPanel.add(this.horizontalWall);
        buttonPanel.add(this.verticalWall);
        buttonPanel.add(this.beeper);
        buttonPanel.add(this.robot);
        opPanel.add(this.rotateRobot);
        final Panel savePanel = new Panel(new GridLayout(5, 1, 0, 5));
        toolPanel.add("South", savePanel);
        toolPanel.add("Center", opPanel);
        this.fileBar.setFont(this.displayFont);
        this.showSpeed.addActionListener(new SpeedListener());
        savePanel.add(this.save);
        savePanel.add(this.open);
        savePanel.add(this.fileBar);
        savePanel.add(this.clearAll);
        this.streets.addActionListener(new RowListener());
        this.avenues.addActionListener(new ColumnListener());
        this.horizontalWall.addActionListener(new HorizontalListener());
        this.verticalWall.addActionListener(new VerticalListener());
        this.beeper.addActionListener(new BeeperListener());
        this.robot.addActionListener(new RobotListener());
        this.rotateRobot.addActionListener(new RotateRobotListener());
        this.save.addActionListener(new SaveListener());
        this.open.addActionListener(new OpenListener());
        this.clearAll.addActionListener(new ClearListener());
        this.buttonColor = this.beeper.getBackground();
        WorldBuilder.view.addMouseListener(this.itemDropper);
        this.streets.setText(new StringBuffer().append(World.numberOfStreets()).toString());
        this.avenues.setText(new StringBuffer().append(World.numberOfAvenues()).toString());
        WorldBuilder.view.attachMouseMotionListener(this.mouseWatcher);
        this.pack();
        this.setVisible(true);
        WorldBuilder.view.showControlDialog(showSpeedControl);
        World.registerBuilder(this);
    }
    
    public void updateStreetsAvenues(final int streets, final int avenues) {
        this.streets.setText(new StringBuffer().append(streets).toString());
        this.avenues.setText(new StringBuffer().append(avenues).toString());
    }
    
    private void saveFile() {
        this.saveDialog.setDirectory(this.directory);
        this.saveDialog.setFile(this.filename);
        this.saveDialog.show();
        final String result = this.saveDialog.getFile();
        if (result != null) {
            this.filename = result;
            this.fileBar.setText(result);
            World.saveWorld(this.directory = this.saveDialog.getDirectory(), this.filename);
            this.isDirty = false;
        }
    }
    
    public static void main(final String[] args) {
        World.setVisible(true);
        WorldBuilder.wb = new WorldBuilder(false);
    }
    
    static /* synthetic */ void access$0(final WorldBuilder worldBuilder, final boolean isDirty) {
        worldBuilder.isDirty = isDirty;
    }
    
    static /* synthetic */ void access$9(final WorldBuilder worldBuilder, final Button selectedButton) {
        worldBuilder.selectedButton = selectedButton;
    }
    
    static /* synthetic */ void access$22(final WorldBuilder worldBuilder, final String filename) {
        worldBuilder.filename = filename;
    }
    
    static /* synthetic */ void access$24(final WorldBuilder worldBuilder, final String directory) {
        worldBuilder.directory = directory;
    }
    
    private class ClearListener implements ActionListener
    {
        ClearListener() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            World.reset();
            WorldBuilder.access$0(WorldBuilder.this, true);
            WorldBuilder.this.streets.setText(new StringBuffer().append(World.numberOfStreets()).toString());
            WorldBuilder.this.avenues.setText(new StringBuffer().append(World.numberOfAvenues()).toString());
            WorldBuilder.this.repaint();
            WorldBuilder.view.repaint();
        }
    }
    
    private class SpeedListener implements ActionListener
    {
        SpeedListener() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            if (e.getActionCommand().indexOf("Show") > -1) {
                WorldBuilder.this.showSpeed.setLabel("Hide Speedcontrol");
                WorldBuilder.view.showControlDialog(true);
            }
            else {
                WorldBuilder.this.showSpeed.setLabel("Show Speedcontrol");
                WorldBuilder.view.showControlDialog(false);
            }
        }
    }
    
    private class HorizontalListener implements ActionListener
    {
        HorizontalListener() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            final Button thisButton = (Button)e.getSource();
            if (thisButton != WorldBuilder.this.selectedButton) {
                ((Button)e.getSource()).setBackground(WorldBuilder.this.selectedColor);
                WorldBuilder.this.beeper.setBackground(Color.lightGray);
                WorldBuilder.this.verticalWall.setBackground(Color.lightGray);
                WorldBuilder.this.robot.setBackground(Color.lightGray);
                WorldBuilder.this.rotateRobot.setBackground(Color.lightGray);
                WorldBuilder.access$9(WorldBuilder.this, thisButton);
                WorldBuilder.this.statusBar.setText("Horizontal Wall");
                WorldBuilder.this.mouseWatcher.setScaler(WorldBuilder.this.horizontalWallScaler);
                WorldBuilder.this.itemDropper.setScaler(WorldBuilder.this.horizontalWallScaler);
                WorldBuilder.view.ewCursor();
            }
            else {
                thisButton.setBackground(Color.lightGray);
                WorldBuilder.access$9(WorldBuilder.this, null);
                WorldBuilder.this.statusBar.setText("");
                WorldBuilder.this.whereBar.setText("");
                WorldBuilder.this.mouseWatcher.setScaler(null);
                WorldBuilder.this.itemDropper.setScaler(null);
                WorldBuilder.view.defaultCursor();
            }
        }
    }
    
    private class VerticalListener implements ActionListener
    {
        VerticalListener() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            final Button thisButton = (Button)e.getSource();
            if (thisButton != WorldBuilder.this.selectedButton) {
                ((Button)e.getSource()).setBackground(WorldBuilder.this.selectedColor);
                WorldBuilder.this.beeper.setBackground(Color.lightGray);
                WorldBuilder.this.horizontalWall.setBackground(Color.lightGray);
                WorldBuilder.this.robot.setBackground(Color.lightGray);
                WorldBuilder.this.rotateRobot.setBackground(Color.lightGray);
                WorldBuilder.access$9(WorldBuilder.this, thisButton);
                WorldBuilder.this.statusBar.setText("Vertical Wall");
                WorldBuilder.this.mouseWatcher.setScaler(WorldBuilder.this.verticalWallScaler);
                WorldBuilder.this.itemDropper.setScaler(WorldBuilder.this.verticalWallScaler);
                WorldBuilder.view.nsCursor();
            }
            else {
                thisButton.setBackground(Color.lightGray);
                WorldBuilder.access$9(WorldBuilder.this, null);
                WorldBuilder.this.statusBar.setText("");
                WorldBuilder.this.whereBar.setText("");
                WorldBuilder.this.mouseWatcher.setScaler(null);
                WorldBuilder.this.itemDropper.setScaler(null);
                WorldBuilder.view.defaultCursor();
            }
        }
    }
    
    private class BeeperListener implements ActionListener
    {
        BeeperListener() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            final Button thisButton = (Button)e.getSource();
            if (thisButton != WorldBuilder.this.selectedButton) {
                ((Button)e.getSource()).setBackground(WorldBuilder.this.selectedColor);
                WorldBuilder.this.horizontalWall.setBackground(Color.lightGray);
                WorldBuilder.this.verticalWall.setBackground(Color.lightGray);
                WorldBuilder.this.robot.setBackground(Color.lightGray);
                WorldBuilder.this.rotateRobot.setBackground(Color.lightGray);
                WorldBuilder.access$9(WorldBuilder.this, thisButton);
                WorldBuilder.this.statusBar.setText("Beeper");
                WorldBuilder.this.mouseWatcher.setScaler(WorldBuilder.this.beeperScaler);
                WorldBuilder.this.itemDropper.setScaler(WorldBuilder.this.beeperScaler);
                WorldBuilder.view.beeperCursor();
            }
            else {
                thisButton.setBackground(Color.lightGray);
                WorldBuilder.access$9(WorldBuilder.this, null);
                WorldBuilder.this.statusBar.setText("");
                WorldBuilder.this.whereBar.setText("");
                WorldBuilder.this.mouseWatcher.setScaler(null);
                WorldBuilder.this.itemDropper.setScaler(null);
                WorldBuilder.view.defaultCursor();
            }
        }
    }
    
    private class RobotListener implements ActionListener
    {
        RobotListener() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            final Button thisButton = (Button)e.getSource();
            if (thisButton != WorldBuilder.this.selectedButton) {
                ((Button)e.getSource()).setBackground(WorldBuilder.this.selectedColor);
                WorldBuilder.this.horizontalWall.setBackground(Color.lightGray);
                WorldBuilder.this.verticalWall.setBackground(Color.lightGray);
                WorldBuilder.this.beeper.setBackground(Color.lightGray);
                WorldBuilder.this.rotateRobot.setBackground(Color.lightGray);
                WorldBuilder.access$9(WorldBuilder.this, thisButton);
                WorldBuilder.this.statusBar.setText("Robot");
                WorldBuilder.this.mouseWatcher.setScaler(WorldBuilder.this.robotScaler);
                WorldBuilder.this.itemDropper.setScaler(WorldBuilder.this.robotScaler);
                WorldBuilder.view.robotCursor();
            }
            else {
                thisButton.setBackground(Color.lightGray);
                WorldBuilder.access$9(WorldBuilder.this, null);
                WorldBuilder.this.statusBar.setText("");
                WorldBuilder.this.whereBar.setText("");
                WorldBuilder.this.mouseWatcher.setScaler(null);
                WorldBuilder.this.itemDropper.setScaler(null);
                WorldBuilder.view.defaultCursor();
            }
        }
    }
    
    private class RotateRobotListener implements ActionListener
    {
        RotateRobotListener() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            final Button thisButton = (Button)e.getSource();
            if (thisButton != WorldBuilder.this.selectedButton) {
                ((Button)e.getSource()).setBackground(WorldBuilder.this.selectedColor);
                WorldBuilder.this.horizontalWall.setBackground(Color.lightGray);
                WorldBuilder.this.verticalWall.setBackground(Color.lightGray);
                WorldBuilder.this.beeper.setBackground(Color.lightGray);
                WorldBuilder.this.robot.setBackground(Color.lightGray);
                WorldBuilder.access$9(WorldBuilder.this, thisButton);
                WorldBuilder.this.statusBar.setText("Robot(Turn Left)");
                WorldBuilder.this.mouseWatcher.setScaler(WorldBuilder.this.rotateScaler);
                WorldBuilder.this.itemDropper.setScaler(WorldBuilder.this.rotateScaler);
                WorldBuilder.view.robotRotateCursor();
            }
            else {
                thisButton.setBackground(Color.lightGray);
                WorldBuilder.access$9(WorldBuilder.this, null);
                WorldBuilder.this.statusBar.setText("");
                WorldBuilder.this.whereBar.setText("");
                WorldBuilder.this.mouseWatcher.setScaler(null);
                WorldBuilder.this.itemDropper.setScaler(null);
                WorldBuilder.view.defaultCursor();
            }
        }
    }
    
    private class SaveListener implements ActionListener
    {
        SaveListener() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            WorldBuilder.this.saveFile();
            WorldBuilder.this.repaint();
        }
    }
    
    private class OpenListener implements ActionListener
    {
        OpenListener() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            WorldBuilder.this.openDialog.setDirectory(WorldBuilder.this.directory);
            WorldBuilder.this.openDialog.setFile(WorldBuilder.this.filename);
            WorldBuilder.this.openDialog.show();
            final String result = WorldBuilder.this.openDialog.getFile();
            if (result != null) {
                WorldBuilder.access$22(WorldBuilder.this, result);
                WorldBuilder.this.fileBar.setText(result);
                WorldBuilder.access$24(WorldBuilder.this, WorldBuilder.this.openDialog.getDirectory());
                World.readWorld(WorldBuilder.this.directory, WorldBuilder.this.filename);
                WorldBuilder.this.streets.setText(new StringBuffer().append(World.numberOfStreets()).toString());
                WorldBuilder.this.avenues.setText(new StringBuffer().append(World.numberOfAvenues()).toString());
                WorldBuilder.view.repaint();
                WorldBuilder.access$0(WorldBuilder.this, true);
            }
        }
    }
    
    private class RowListener implements ActionListener
    {
        RowListener() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            try {
                World.setSize(Integer.parseInt(WorldBuilder.this.streets.getText()), World.numberOfAvenues());
                WorldBuilder.view.setVisible(false);
                WorldBuilder.view.repaint();
                WorldBuilder.view.setVisible(true);
                WorldBuilder.access$0(WorldBuilder.this, true);
            }
            catch (NumberFormatException ex) {}
        }
    }
    
    private class ColumnListener implements ActionListener
    {
        ColumnListener() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            try {
                World.setSize(World.numberOfStreets(), Integer.parseInt(WorldBuilder.this.avenues.getText()));
                WorldBuilder.view.setVisible(false);
                WorldBuilder.view.repaint();
                WorldBuilder.view.setVisible(true);
                WorldBuilder.access$0(WorldBuilder.this, true);
            }
            catch (NumberFormatException ex) {}
        }
    }
    
    private class BeeperScaler implements MouseScaler
    {
        private Point where;
        
        BeeperScaler() {
            this.where = new Point();
        }
        
        public void scale(final int rawx, final int rawy, final Point result) {
            int scale = (WorldBuilder.view.bottom() - 10) / World.numberOfStreets();
            if (scale == 0) {
                scale = 1;
            }
            result.x = (rawx - WorldBuilder.view.left() + scale / 2) / scale;
            result.y = (WorldBuilder.view.bottom() - rawy + scale / 2) / scale;
        }
        
        public void dropItem(final MouseEvent evt) {
            this.scale(evt.getX(), evt.getY(), this.where);
            final int street = this.where.y;
            final int avenue = this.where.x;
            if (street < 1 || avenue < 1 || street > World.numberOfStreets() || avenue > World.numberOfAvenues()) {
                return;
            }
            int keyMask = 2;
            if (System.getProperty("os.name").indexOf("Mac") > -1) {
                keyMask = 8;
            }
            final boolean add = (evt.getModifiers() & keyMask) == 0x0;
            if ((evt.getModifiers() & 0x1) != 0x0) {
                World.clearBeepers(street, avenue);
            }
            else {
                World.placeBeepers(street, avenue, add ? 1 : -1);
            }
            WorldBuilder.view.setVisible(true);
        }
    }
    
    private class RobotScaler implements MouseScaler
    {
        private Point where;
        
        RobotScaler() {
            this.where = new Point();
        }
        
        public void scale(final int rawx, final int rawy, final Point result) {
            int scale = (WorldBuilder.view.bottom() - 10) / World.numberOfStreets();
            if (scale == 0) {
                scale = 1;
            }
            result.x = (rawx - WorldBuilder.view.left() + scale / 2) / scale;
            result.y = (WorldBuilder.view.bottom() - rawy + scale / 2) / scale;
        }
        
        public void dropItem(final MouseEvent evt) {
            this.scale(evt.getX(), evt.getY(), this.where);
            final int street = this.where.y;
            final int avenue = this.where.x;
            if (street < 1 || avenue < 1 || street > World.numberOfStreets() || avenue > World.numberOfAvenues()) {
                return;
            }
            int keyMask = 2;
            if (System.getProperty("os.name").indexOf("Mac") > -1) {
                keyMask = 8;
            }
            final boolean add = (evt.getModifiers() & keyMask) == 0x0;
            if (add) {
                new UrRobot(street, avenue, North, 0);
            }
            else {
                for(UrRobot r: World.getRobotsAt(street, avenue)){
                    r.delete();
                }
            }
            WorldBuilder.view.setVisible(true);
        }
    }
    
    private class RotateRobotScaler implements MouseScaler
    {
        private Point where;
        
        RotateRobotScaler() {
            this.where = new Point();
        }
        
        public void scale(final int rawx, final int rawy, final Point result) {
            int scale = (WorldBuilder.view.bottom() - 10) / World.numberOfStreets();
            if (scale == 0) {
                scale = 1;
            }
            result.x = (rawx - WorldBuilder.view.left() + scale / 2) / scale;
            result.y = (WorldBuilder.view.bottom() - rawy + scale / 2) / scale;
        }
        
        public void dropItem(final MouseEvent evt) {
            this.scale(evt.getX(), evt.getY(), this.where);
            final int street = this.where.y;
            final int avenue = this.where.x;
            if (street < 1 || avenue < 1 || street > World.numberOfStreets() || avenue > World.numberOfAvenues()) {
                return;
            }
            int keyMask = 2;
            if (System.getProperty("os.name").indexOf("Mac") > -1) {
                keyMask = 8;
            }
            //final boolean add = (evt.getModifiers() & keyMask) == 0x0;
            for(UrRobot r: World.getRobotsAt(street, avenue)){
                r.turnLeft();
            }
            WorldBuilder.view.setVisible(true);
        }
    }
    
    private class HorizontalWallScaler implements MouseScaler
    {
        private Point where;
        
        HorizontalWallScaler() {
            this.where = new Point();
        }
        
        public void scale(final int rawx, final int rawy, final Point result) {
            int scale = (WorldBuilder.view.bottom() - 10) / World.numberOfStreets();
            if (scale == 0) {
                scale = 1;
            }
            result.x = (rawx - WorldBuilder.view.left() + scale / 2) / scale;
            result.y = (WorldBuilder.view.bottom() - rawy) / scale;
        }
        
        public void dropItem(final MouseEvent evt) {
            this.scale(evt.getX(), evt.getY(), this.where);
            final int street = this.where.y;
            final int avenue = this.where.x;
            if (street < 1 || avenue < 1 || street > World.numberOfStreets() - 1 || avenue > World.numberOfAvenues()) {
                return;
            }
            int keyMask = 2;
            if (System.getProperty("os.name").indexOf("Mac") > -1) {
                keyMask = 8;
            }
            final boolean add = (evt.getModifiers() & keyMask) == 0x0;
            if (add) {
                World.placeEWWall(street, avenue, 1);
            }
            else {
                World.removeEWWall(street, avenue);
            }
            WorldBuilder.view.setVisible(true);
        }
    }
    
    private class VerticalWallScaler implements MouseScaler
    {
        private Point where;
        
        VerticalWallScaler() {
            this.where = new Point();
        }
        
        public void scale(final int rawx, final int rawy, final Point result) {
            int scale = (WorldBuilder.view.bottom() - 10) / World.numberOfStreets();
            if (scale == 0) {
                scale = 1;
            }
            result.x = (rawx - WorldBuilder.view.left()) / scale;
            result.y = (WorldBuilder.view.bottom() - rawy + scale / 2) / scale;
        }
        
        public void dropItem(final MouseEvent evt) {
            this.scale(evt.getX(), evt.getY(), this.where);
            final int street = this.where.y;
            final int avenue = this.where.x;
            if (street < 1 || avenue < 1 || street > World.numberOfStreets() || avenue > World.numberOfAvenues() - 1) {
                return;
            }
            int keyMask = 2;
            if (System.getProperty("os.name").indexOf("Mac") > -1) {
                keyMask = 8;
            }
            final boolean add = (evt.getModifiers() & keyMask) == 0x0;
            if (add) {
                World.placeNSWall(street, avenue, 1);
            }
            else {
                World.removeNSWall(street, avenue);
            }
            WorldBuilder.view.setVisible(true);
        }
    }
    
    private class MouseWatcher extends MouseMotionAdapter
    {
        private MouseScaler scaler;
        private Point where;
        
        MouseWatcher() {
            this.scaler = null;
            this.where = new Point();
        }
        
        public void mouseMoved(final MouseEvent evt) {
            if (this.scaler != null) {
                this.scaler.scale(evt.getX(), evt.getY(), this.where);
                int avenue = this.where.x;
                int street = this.where.y;
                if (avenue < 1) {
                    avenue = 1;
                }
                if (street < 1) {
                    street = 1;
                }
                WorldBuilder.this.whereBar.setText(street + ", " + avenue);
                WorldBuilder.this.repaint();
            }
        }
        
        public void setScaler(final MouseScaler scaler) {
            this.scaler = scaler;
        }
    }
    
    private class ItemDropper extends MouseAdapter
    {
        private MouseScaler scaler;
        
        ItemDropper() {
            this.scaler = null;
        }
        
        public void mouseClicked(final MouseEvent evt) {
            if (this.scaler != null) {
                this.scaler.dropItem(evt);
                WorldBuilder.view.repaint();
                WorldBuilder.access$0(WorldBuilder.this, true);
            }
        }
        
        public void setScaler(final MouseScaler scaler) {
            this.scaler = scaler;
        }
    }
    
    interface MouseScaler
    {
        void scale(final int p0, final int p1, final Point p2);
        
        void dropItem(final MouseEvent p0);
    }
}
