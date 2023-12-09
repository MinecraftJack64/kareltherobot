package kareltherobot;

// 
// Decompiled by Procyon v0.5.36
// 


 

import java.io.Serializable;
import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.awt.Color;
import java.io.BufferedReader;
import java.util.Vector;
import java.util.Observable;

public class UrRobot extends Observable implements Directions, Runnable, IUrRobot
{
    private Vector<BufferedReader> senders;
    private int nextSender;
    private boolean pausing;
    private boolean userLevelPausing;
    BufferedReader sysin;
    private static final int on = 2;
    private static final int off = 1;
    private static final int crashed = 0;
    private int[] loc;
    private int beepers;
    private Color badgeColor;
    private Direction direction;
    private int moves;
    private int state;
    private boolean isVisible;
    private int idNumber;
    private static final int threshhold = 10;
    private static int numberOfRobots;
    private StateObject initialState;
    private boolean isComboMode = false;
    
    static {
        UrRobot.numberOfRobots = 0;
    }
    
    void updateFinalState(int actionCode){
        final StateObject s = new StateObject(actionCode);
        this.setChanged();
        this.notifyObservers(s);
    }
    void updateState(int actionCode){
        updateFinalState(actionCode);
        if(!isComboMode){
            this.sleep();
        } else System.out.println("atemps");
    }
    public void turnLeft() {
        if (this.state == 2) {
            this.pause("turnLeft");
            this.direction = this.direction.rotate(-1);
            updateState(1);
        }
    }
    
    //custom
    public void turnRight() {
        if (this.state == 2) {
            this.pause("turnRight");
            this.direction = this.direction.rotate(6);
            updateState(6);
        }
    }
    
    public void phase() {
        if (this.state == 2) {
            this.pause("phase");
            boolean crashed = false;
            this.normalize();
            switch (this.direction.points()) {
                case 3: {
                    if (World.checkEWWall(this.loc[3], this.loc[0])) {
                        crashed = this.crash("Tried to walk through an East West wall");
                        break;
                    }
                    break;
                }
                case 0: {
                    if (World.checkNSWall(this.loc[3], this.loc[0])) {
                        crashed = this.crash("Tried to walk through a North South wall");
                        break;
                    }
                    break;
                }
                case 1: {
                    if (World.checkEWWall(this.loc[3] - 1, this.loc[0])) {
                        crashed = this.crash("Tried to walk through an East West wall");
                        break;
                    }
                    break;
                }
                case 2: {
                    if (World.checkNSWall(this.loc[3], this.loc[0] - 1)) {
                        crashed = this.crash("Tried to walk through a North South wall");
                        break;
                    }
                    break;
                }
            }
            if (!crashed) {
                final int[] loc = this.loc;
                final int points = this.direction.points();
                ++loc[points];
                ++this.moves;
                if (this.moves > 10) {
                    this.normalize();
                }
            }
            this.validate();
            updateState(0);
        }
    }
    
    public void move() {
        if (this.state == 2) {
            this.pause("move");
            boolean crashed = false;
            this.normalize();
            switch (this.direction.points()) {
                case 3: {
                    if (World.checkEWWall(this.loc[3], this.loc[0])) {
                        crashed = this.crash("Tried to walk through an East West wall");
                        break;
                    }
                    break;
                }
                case 0: {
                    if (World.checkNSWall(this.loc[3], this.loc[0])) {
                        crashed = this.crash("Tried to walk through a North South wall");
                        break;
                    }
                    break;
                }
                case 1: {
                    if (World.checkEWWall(this.loc[3] - 1, this.loc[0])) {
                        crashed = this.crash("Tried to walk through an East West wall");
                        break;
                    }
                    break;
                }
                case 2: {
                    if (World.checkNSWall(this.loc[3], this.loc[0] - 1)) {
                        crashed = this.crash("Tried to walk through a North South wall");
                        break;
                    }
                    break;
                }
            }
            if (!crashed) {
                final int[] loc = this.loc;
                final int points = this.direction.points();
                ++loc[points];
                ++this.moves;
                if (this.moves > 10) {
                    this.normalize();
                }
            }
            this.validate();
            updateState(0);
        }
    }
    
    public void pickBeeper() {
        if (this.state == 2) {
            this.pause("pickBeeper");
            this.normalize();
            boolean crashed = false;
            if (!World.checkBeeper(this.loc[3], this.loc[0])) {
                crashed = this.crash("No beepers to pick");
            }
            if (!crashed) {
                if (this.beepers != -1) {
                    ++this.beepers;
                }
                World.placeBeepers(this.loc[3], this.loc[0], -1);
            }
            updateState(2);
        }
    }
    
    public void putBeeper() {
        if (this.state == 2) {
            this.pause("putBeeper");
            this.normalize();
            if (this.beepers == 0) {
                this.crash("No beepers to put.");
                updateFinalState(3);
                return;
            }
            if (this.beepers != -1) {
                --this.beepers;
            }
            if (!this.validate()) {
                if (this.beepers != -1) {
                    ++this.beepers;
                }
            }
            else {
                World.placeBeepers(this.loc[3], this.loc[0], 1);
                updateFinalState(3);
            }
            this.sleep();
        }
    }
    
    public void skip(){
        if (this.state == 2) {
            this.pause("doze off");
            this.normalize();
            updateState(5);
        }
    }
    
    public void turnOff() {
        this.pause("turnOff");
        if (this.state == 2) {
            System.out.println("Robot " + this.idNumber + ": Turning off");
            this.state = 1;
            updateState(4);
        }
    }
    
    public World world() {
        return World.asObject();
    }
    
    public Enumeration neighbors() {
        final Vector v = new Vector();
        final Enumeration<UrRobot> all = World.robots();
        while (all.hasMoreElements()) {
            final UrRobot r = all.nextElement();
            if (r != this && r.areYouHere(this.street(), this.avenue())) {
                v.addElement(r);
            }
        }
        return v.elements();
    }
    
    public String getNextCommunication() {
        if (this.senders.size() == 0) {
            return null;
        }
        int count = 0;
        while (count < this.senders.size()) {
            if (this.nextSender >= this.senders.size()) {
                this.nextSender = 0;
            }
            final BufferedReader in = this.senders.elementAt(this.nextSender);
            try {
                ++this.nextSender;
                if (in.ready()) {
                    return in.readLine();
                }
            }
            catch (IOException ex) {}
            ++count;
            this.sleep();
        }
        return null;
    }
    
    public String waitForCommunication() {
        if (this.senders.size() == 0) {
            return null;
        }
        while (true) {
            if (this.nextSender >= this.senders.size()) {
                this.nextSender = 0;
            }
            final BufferedReader in = this.senders.elementAt(this.nextSender);
            try {
                ++this.nextSender;
                if (in.ready()) {
                    return in.readLine();
                }
            }
            catch (IOException ex) {}
            this.sleep();
        }
    }
    
    public String waitForNextCommunication() {
        if (this.senders.size() == 0) {
            return null;
        }
        while (true) {
            if (this.nextSender >= this.senders.size()) {
                this.nextSender = 0;
            }
            final BufferedReader in = this.senders.elementAt(this.nextSender);
            try {
                ++this.nextSender;
                return in.readLine();
            }
            catch (IOException ex) {}
        }
    }
    
    public BufferedWriter connectTo(final UrRobot other, final ConnectStrategy strat) throws IOException {
        final PipedOutputStream out = new PipedOutputStream();
        final BufferedWriter result = new BufferedWriter(new OutputStreamWriter(out));
        other.acceptConnectionFrom(this, out, strat);
        return result;
    }
    
    public synchronized void acceptConnectionFrom(final UrRobot sender, final PipedOutputStream s, final ConnectStrategy strat) throws IOException {
        final BufferedReader manager = new BufferedReader(new InputStreamReader(new PipedInputStream(s)));
        if (strat != null) {
            strat.action(sender, this, manager);
        }
        else {
            this.senders.addElement(manager);
        }
    }
    
    public synchronized void acceptConnection(final PipedOutputStream s, final ConnectStrategy strat) throws IOException {
        final BufferedReader manager = new BufferedReader(new InputStreamReader(new PipedInputStream(s)));
        if (strat != null) {
            strat.action(null, this, manager);
        }
        else {
            this.senders.addElement(manager);
        }
    }
    
    public void send(final BufferedWriter other, final String s) throws IOException {
        other.write(String.valueOf(s) + '\n');
        other.flush();
    }
    
    public void run() {
    }
    
    //custom
    protected int getID(){
        return idNumber;
    }
    
    final void pause(final String message) {
        if (this.pausing) {
            System.out.println("RobotID " + this.idNumber + " is about to " + message + ".");
            try {
                this.sysin.readLine();
            }
            catch (IOException ex) {}
        }
    }
    
    public void userPause(final String message) {
        if (this.userLevelPausing) {
            System.out.println("RobotID " + this.idNumber + " is about to " + message + ".");
            try {
                this.sysin.readLine();
            }
            catch (IOException ex) {}
        }
    }
    
    public final void setPause(final boolean pausing) {
        this.pausing = pausing;
    }
    
    public final void setUserPause(final boolean pausing) {
        this.userLevelPausing = pausing;
    }
    
    public UrRobot(final int street, final int avenue, final Direction direction, final int beepers, final Color badgeColor) {
        this.senders = new Vector();
        this.nextSender = 0;
        this.pausing = false;
        this.userLevelPausing = false;
        this.sysin = new BufferedReader(new InputStreamReader(System.in));
        this.loc = new int[4];
        this.badgeColor = null;
        this.moves = 0;
        this.state = 2;
        this.isVisible = true;
        this.loc[3] = street;
        this.loc[1] = 0;
        this.loc[0] = avenue;
        this.loc[2] = 0;
        this.direction = direction;
        this.beepers = beepers;
        this.validate();
        this.idNumber = incrementRobots();
        this.state = 2;
        this.initialState = new StateObject(-1);
        this.badgeColor = badgeColor;
        World.addRobot(this);
        this.sleep();
        this.sleep();
    }
    
    public UrRobot(final int street, final int avenue, final Direction direction, final int beepers) {
        this(street, avenue, direction, beepers, null);
        System.out.println("LAVAHFEIWnfiewujferwfjrwi");
    }
    
    //Custom
    public void delete(){
        World.removeRobot(this);
    }
    public void startCombo(){
        isComboMode = true;
    }
    public void stopCombo(){
        isComboMode = false;
    }
    
    final Color badgeColor() {
        return this.badgeColor;
    }
    
    final void restoreInitialState() {
        this.loc[3] = this.initialState.street;
        this.loc[1] = 0;
        this.loc[0] = this.initialState.avenue;
        this.loc[2] = 0;
        this.direction = this.initialState.direction;
        this.beepers = this.initialState.beepers;
        this.state = 2;
        this.showState("Restoring ");
        this.setChanged();
        this.notifyObservers(this.initialState);
        this.sleep();
    }
    
    public final String toString() {
        this.normalize();
        return "RobotID " + this.idNumber + " at (street: " + this.loc[3] + ") (avenue: " + this.loc[0] + ") (beepers: " + ((this.beepers >= 0) ? new StringBuffer().append(this.beepers).toString() : "infinite") + ") ( direction: " + this.direction.toString() + ((this.state == 2) ? ") on" : ") off");
    }
    
    private String direction(final int d) {
        switch (d) {
            case 0: {
                return "East";
            }
            case 1: {
                return "South";
            }
            case 2: {
                return "West";
            }
            case 3: {
                return "North";
            }
            default: {
                return "ERROR";
            }
        }
    }
    
    protected void sleep() {
        try {
            Thread.sleep(10 * World.delay());
        }
        catch (InterruptedException ex) {}
    }
    
    public final void showState(final String s) {
        this.normalize();
        System.out.println(String.valueOf(s) + this);
    }
    
    final boolean areYouHere(final int street, final int avenue) {
        this.normalize();
        return this.loc[3] == street && this.loc[0] == avenue;
    }
    
    private boolean validate() {
        this.normalize();
        if (this.beepers < -1) {
            return !this.crash("Robot has negative beepers");
        }
        if (this.loc[3] < 1) {
            return !this.crash("Robot tried to move through South boundary wall");
        }
        return this.loc[0] >= 1 || !this.crash("Robot tried to move through West boundary wall");
    }
    
    private boolean crash(final String s) {
        this.state = 0;
        this.showState("Error shutoff: ");
        System.out.println(s);
        return true;
    }
    
    final boolean crashed() {
        return this.state == 0;
    }
    
    private void pauseExit() {
        try {
            this.sysin.readLine();
        }
        catch (IOException ex) {}
        System.exit(0);
    }
    
    private void normalize() {
        this.moves = 0;
        final int[] loc = this.loc;
        final int n = 3;
        loc[n] -= this.loc[1];
        this.loc[1] = 0;
        final int[] loc2 = this.loc;
        final int n2 = 0;
        loc2[n2] -= this.loc[2];
        this.loc[2] = 0;
    }
    
    final int beepers() {
        return this.beepers;
    }
    
    final Direction direction() {
        return this.direction;
    }
    
    final int street() {
        return this.loc[3] - this.loc[1];
    }
    
    final int avenue() {
        return this.loc[0] - this.loc[2];
    }
    
    final boolean running() {
        return this.state == 2;
    }
    
    public final boolean isVisible() {
        return this.isVisible;
    }
    
    public final void setVisible(final boolean visible) {
        if (visible != this.isVisible) {
            this.setChanged();
            this.isVisible = visible;
        }
    }
    
    private static synchronized int incrementRobots() {
        return UrRobot.numberOfRobots++;
    }
    
    final class StateObject implements Serializable
    {
        private int street;
        private int avenue;
        private Direction direction;
        private int beepers;
        private int lastAction;
        
        public StateObject(final int lastAction) {
            this.street = UrRobot.this.loc[3] - UrRobot.this.loc[1];
            this.avenue = UrRobot.this.loc[0] - UrRobot.this.loc[2];
            this.direction = UrRobot.this.direction;
            this.beepers = UrRobot.this.beepers;
            this.lastAction = lastAction;
        }
        
        public int street() {
            return this.street;
        }
        
        public int avenue() {
            return this.avenue;
        }
        
        public Direction direction() {
            return this.direction;
        }
        
        public int beepers() {
            return this.beepers;
        }
        
        public int lastAction() {
            return this.lastAction;
        }
    }
    
    public interface ConnectStrategy
    {
        void action(final UrRobot p0, final UrRobot p1, final BufferedReader p2);
    }
    
    interface Action
    {
        public static final int move = 0;
        public static final int turnLeft = 1;
        public static final int pickBeeper = 2;
        public static final int putBeeper = 3;
        public static final int turnOff = 4;
        public static final int skip = 5;
        public static final int turnRight = 6;
        public static final int initial = -1;
    }
}
