package kareltherobot;

// 
// Decompiled by Procyon v0.5.36
// 

 

import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Color;
import java.awt.Button;
import java.awt.TextField;
import java.awt.Frame;

public class MiniBuilder extends Frame implements Directions, WorldBuilderInterface
{
    private TextField streets;
    private TextField avenues;
    private Button horizontalWall;
    private Button verticalWall;
    private Button beeper;
    private Button selectedButton;
    private Button showSpeed;
    private boolean isDirty;
    private Color buttonColor;
    private Color selectedColor;
    private Label statusBar;
    private Label whereBar;
    private Button clearAll;
    private ItemDropper itemDropper;
    private RobotWorldWindow view;
    private Font displayFont;
    private BeeperScaler beeperScaler;
    private HorizontalWallScaler horizontalWallScaler;
    private VerticalWallScaler verticalWallScaler;
    private MouseWatcher mouseWatcher;
    private static final int fromTop = 10;
    static WorldBuilder wb;
    
    static {
        MiniBuilder.wb = null;
    }
    
    public void cleanUp() {
    }
    
    public MiniBuilder(final boolean showSpeedControl) {
        super("World Builder");
        this.streets = new TextField(5);
        this.avenues = new TextField(5);
        this.horizontalWall = new Button("Horizontal Wall");
        this.verticalWall = new Button("Vertical Wall");
        this.beeper = new Button("Beeper");
        this.selectedButton = null;
        this.showSpeed = new Button("Hide Speedcontrol");
        this.isDirty = false;
        this.buttonColor = null;
        this.selectedColor = new Color(192, 255, 192);
        this.statusBar = new Label("");
        this.whereBar = new Label("");
        this.clearAll = new Button("Clear World");
        this.itemDropper = new ItemDropper();
        this.view = World.view();
        this.displayFont = new Font("Dialog", 0, 14);
        this.beeperScaler = new BeeperScaler();
        this.horizontalWallScaler = new HorizontalWallScaler();
        this.verticalWallScaler = new VerticalWallScaler();
        this.mouseWatcher = new MouseWatcher();
        if (!showSpeedControl) {
            this.showSpeed.setLabel("Show Speedcontrol");
        }
        this.setSize(120, 380);
        this.setLocation(560, 30);
        final Panel toolPanel = new Panel(new GridLayout(3, 1, 0, 20));
        this.add("Center", toolPanel);
        this.showSpeed.addActionListener(new SpeedListener());
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
        final Panel savePanel = new Panel(new GridLayout(5, 1, 0, 5));
        toolPanel.add("South", savePanel);
        savePanel.add(this.clearAll);
        this.streets.addActionListener(new RowListener());
        this.avenues.addActionListener(new ColumnListener());
        this.horizontalWall.addActionListener(new HorizontalListener());
        this.verticalWall.addActionListener(new VerticalListener());
        this.beeper.addActionListener(new BeeperListener());
        this.clearAll.addActionListener(new ClearListener());
        this.buttonColor = this.beeper.getBackground();
        this.view.addMouseListener(this.itemDropper);
        this.streets.setText(new StringBuffer().append(World.numberOfStreets()).toString());
        this.avenues.setText(new StringBuffer().append(World.numberOfAvenues()).toString());
        this.view.attachMouseMotionListener(this.mouseWatcher);
        if (!showSpeedControl) {
            this.view.showControlDialog(false);
        }
        this.pack();
        this.setVisible(true);
        World.registerBuilder(this);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                MiniBuilder.this.setVisible(false);
            }
        });
    }
    
    public void updateStreetsAvenues(final int streets, final int avenues) {
        this.streets.setText(new StringBuffer().append(streets).toString());
        this.avenues.setText(new StringBuffer().append(avenues).toString());
    }
    
    public static void main(final String[] args) {
        MiniBuilder.wb = new WorldBuilder(false);
    }
    
    static /* synthetic */ void access$0(final MiniBuilder miniBuilder, final boolean isDirty) {
        miniBuilder.isDirty = isDirty;
    }
    
    static /* synthetic */ void access$8(final MiniBuilder miniBuilder, final Button selectedButton) {
        miniBuilder.selectedButton = selectedButton;
    }
    
    private class ClearListener implements ActionListener
    {
        ClearListener() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            World.reset();
            MiniBuilder.access$0(MiniBuilder.this, true);
            MiniBuilder.this.streets.setText(new StringBuffer().append(World.numberOfStreets()).toString());
            MiniBuilder.this.avenues.setText(new StringBuffer().append(World.numberOfAvenues()).toString());
            MiniBuilder.this.repaint();
            MiniBuilder.this.view.repaint();
        }
    }
    
    private class HorizontalListener implements ActionListener
    {
        HorizontalListener() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            final Button thisButton = (Button)e.getSource();
            if (thisButton != MiniBuilder.this.selectedButton) {
                ((Button)e.getSource()).setBackground(MiniBuilder.this.selectedColor);
                MiniBuilder.this.beeper.setBackground(Color.lightGray);
                MiniBuilder.this.verticalWall.setBackground(Color.lightGray);
                MiniBuilder.access$8(MiniBuilder.this, thisButton);
                MiniBuilder.this.statusBar.setText("Horizontal Wall");
                MiniBuilder.this.mouseWatcher.setScaler(MiniBuilder.this.horizontalWallScaler);
                MiniBuilder.this.itemDropper.setScaler(MiniBuilder.this.horizontalWallScaler);
                MiniBuilder.this.view.ewCursor();
            }
            else {
                thisButton.setBackground(Color.lightGray);
                MiniBuilder.access$8(MiniBuilder.this, null);
                MiniBuilder.this.statusBar.setText("");
                MiniBuilder.this.whereBar.setText("");
                MiniBuilder.this.mouseWatcher.setScaler(null);
                MiniBuilder.this.itemDropper.setScaler(null);
                MiniBuilder.this.view.defaultCursor();
            }
        }
    }
    
    private class VerticalListener implements ActionListener
    {
        VerticalListener() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            final Button thisButton = (Button)e.getSource();
            if (thisButton != MiniBuilder.this.selectedButton) {
                ((Button)e.getSource()).setBackground(MiniBuilder.this.selectedColor);
                MiniBuilder.this.beeper.setBackground(Color.lightGray);
                MiniBuilder.this.horizontalWall.setBackground(Color.lightGray);
                MiniBuilder.access$8(MiniBuilder.this, thisButton);
                MiniBuilder.this.statusBar.setText("Vertical Wall");
                MiniBuilder.this.mouseWatcher.setScaler(MiniBuilder.this.verticalWallScaler);
                MiniBuilder.this.itemDropper.setScaler(MiniBuilder.this.verticalWallScaler);
                MiniBuilder.this.view.nsCursor();
            }
            else {
                thisButton.setBackground(Color.lightGray);
                MiniBuilder.access$8(MiniBuilder.this, null);
                MiniBuilder.this.statusBar.setText("");
                MiniBuilder.this.whereBar.setText("");
                MiniBuilder.this.mouseWatcher.setScaler(null);
                MiniBuilder.this.itemDropper.setScaler(null);
                MiniBuilder.this.view.defaultCursor();
            }
        }
    }
    
    private class BeeperListener implements ActionListener
    {
        BeeperListener() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            final Button thisButton = (Button)e.getSource();
            if (thisButton != MiniBuilder.this.selectedButton) {
                ((Button)e.getSource()).setBackground(MiniBuilder.this.selectedColor);
                MiniBuilder.this.horizontalWall.setBackground(Color.lightGray);
                MiniBuilder.this.verticalWall.setBackground(Color.lightGray);
                MiniBuilder.access$8(MiniBuilder.this, thisButton);
                MiniBuilder.this.statusBar.setText("Beeper");
                MiniBuilder.this.mouseWatcher.setScaler(MiniBuilder.this.beeperScaler);
                MiniBuilder.this.itemDropper.setScaler(MiniBuilder.this.beeperScaler);
                MiniBuilder.this.view.beeperCursor();
            }
            else {
                thisButton.setBackground(Color.lightGray);
                MiniBuilder.access$8(MiniBuilder.this, null);
                MiniBuilder.this.statusBar.setText("");
                MiniBuilder.this.whereBar.setText("");
                MiniBuilder.this.mouseWatcher.setScaler(null);
                MiniBuilder.this.itemDropper.setScaler(null);
                MiniBuilder.this.view.defaultCursor();
            }
        }
    }
    
    private class RowListener implements ActionListener
    {
        RowListener() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            try {
                World.setSize(Integer.parseInt(MiniBuilder.this.streets.getText()), World.numberOfAvenues());
                MiniBuilder.this.view.setVisible(false);
                MiniBuilder.this.view.repaint();
                MiniBuilder.this.view.setVisible(true);
                MiniBuilder.access$0(MiniBuilder.this, true);
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
                World.setSize(World.numberOfStreets(), Integer.parseInt(MiniBuilder.this.avenues.getText()));
                MiniBuilder.this.view.setVisible(false);
                MiniBuilder.this.view.repaint();
                MiniBuilder.this.view.setVisible(true);
                MiniBuilder.access$0(MiniBuilder.this, true);
            }
            catch (NumberFormatException ex) {}
        }
    }
    
    private class SpeedListener implements ActionListener
    {
        SpeedListener() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            if (e.getActionCommand().indexOf("Show") > -1) {
                MiniBuilder.this.showSpeed.setLabel("Hide Speedcontrol");
                MiniBuilder.this.view.showControlDialog(true);
            }
            else {
                MiniBuilder.this.showSpeed.setLabel("Show Speedcontrol");
                MiniBuilder.this.view.showControlDialog(false);
            }
        }
    }
    
    private class BeeperScaler implements MouseScaler
    {
        private Point where;
        
        BeeperScaler() {
            this.where = new Point();
        }
        
        public void scale(final int rawx, final int rawy, final Point result) {
            int scale = (MiniBuilder.this.view.bottom() - 10) / World.numberOfStreets();
            if (scale == 0) {
                scale = 1;
            }
            result.x = (rawx - MiniBuilder.this.view.left() + scale / 2) / scale;
            result.y = (MiniBuilder.this.view.bottom() - rawy + scale / 2) / scale;
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
            MiniBuilder.this.view.setVisible(true);
        }
    }
    
    private class HorizontalWallScaler implements MouseScaler
    {
        private Point where;
        
        HorizontalWallScaler() {
            this.where = new Point();
        }
        
        public void scale(final int rawx, final int rawy, final Point result) {
            int scale = (MiniBuilder.this.view.bottom() - 10) / World.numberOfStreets();
            if (scale == 0) {
                scale = 1;
            }
            result.x = (rawx - MiniBuilder.this.view.left() + scale / 2) / scale;
            result.y = (MiniBuilder.this.view.bottom() - rawy) / scale;
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
            MiniBuilder.this.view.setVisible(true);
        }
    }
    
    private class VerticalWallScaler implements MouseScaler
    {
        private Point where;
        
        VerticalWallScaler() {
            this.where = new Point();
        }
        
        public void scale(final int rawx, final int rawy, final Point result) {
            int scale = (MiniBuilder.this.view.bottom() - 10) / World.numberOfStreets();
            if (scale == 0) {
                scale = 1;
            }
            result.x = (rawx - MiniBuilder.this.view.left()) / scale;
            result.y = (MiniBuilder.this.view.bottom() - rawy + scale / 2) / scale;
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
            MiniBuilder.this.view.setVisible(true);
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
                MiniBuilder.this.whereBar.setText(street + ", " + avenue);
                MiniBuilder.this.repaint();
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
                MiniBuilder.this.view.repaint();
                MiniBuilder.access$0(MiniBuilder.this, true);
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
