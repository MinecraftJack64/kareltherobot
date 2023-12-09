package kareltherobot;

// 
// Decompiled by Procyon v0.5.36
// 

 

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.Button;
import java.awt.LayoutManager;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Frame;

public class RemoteControl extends Frame implements Directions
{
    private static int id;
    private static int delta;
    private UrRobot karel;
    
    static {
        RemoteControl.id = 0;
        RemoteControl.delta = 10;
    }
    
    public RemoteControl(final int street, final int avenue, final Direction direction, final int beepers) {
        this(street, avenue, direction, beepers, null);
    }
    
    public RemoteControl(final int street, final int avenue, final Direction direction) {
        this(street, avenue, direction, -1, null);
    }
    
    public RemoteControl() {
        this(1, 1, RemoteControl.North, -1, null);
    }
    
    public RemoteControl(final int street, final int avenue, final Direction direction, final int beepers, final Color color) {
        super("Robot " + getID() + " Controller");
        this.karel = null;
        if (color != null) {
            this.setBackground(color);
        }
        this.karel = new UrRobot(street, avenue, direction, beepers, color);
        this.setLayout(new GridLayout(3, 2));
        final Button move = new Button("Move");
        move.addActionListener(new Mover());
        this.add(move);
        final Button turn = new Button("Turn Left");
        turn.addActionListener(new LeftTurner());
        this.add(turn);
        final Button pick = new Button("Pick Beeper");
        pick.addActionListener(new Picker());
        this.add(pick);
        final Button put = new Button("Put Beeper");
        put.addActionListener(new Putter());
        this.add(put);
        final Button stop = new Button("Turn Off");
        stop.addActionListener(new Stopper());
        this.add(stop);
        this.addWindowListener(new Hider());
        this.setSize(300, 150);
        this.setLocation(560 + RemoteControl.delta * (RemoteControl.id - 1), 100 + RemoteControl.delta * (RemoteControl.id - 1));
        this.setVisible(true);
    }
    
    private static int getID() {
        return RemoteControl.id++;
    }
    
    private class Hider extends WindowAdapter
    {
        Hider() {
        }
        
        public void windowClosing(final WindowEvent e) {
            RemoteControl.this.setVisible(false);
        }
    }
    
    private class Stopper implements ActionListener
    {
        Stopper() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            RemoteControl.this.karel.turnOff();
        }
    }
    
    private class Putter implements ActionListener
    {
        Putter() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            RemoteControl.this.karel.putBeeper();
        }
    }
    
    private class Picker implements ActionListener
    {
        Picker() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            RemoteControl.this.karel.pickBeeper();
        }
    }
    
    private class Mover implements ActionListener
    {
        Mover() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            RemoteControl.this.karel.move();
        }
    }
    
    private class LeftTurner implements ActionListener
    {
        LeftTurner() {
        }
        
        public void actionPerformed(final ActionEvent e) {
            RemoteControl.this.karel.turnLeft();
        }
    }
}
