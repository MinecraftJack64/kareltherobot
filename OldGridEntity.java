package kareltherobot;

// 
// Decompiled by Procyon v0.5.36
// 

 
import java.awt.*;
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
import java.io.BufferedReader;
import java.util.Vector;
import java.util.Observable;

public class OldGridEntity extends Observable implements Directions, Runnable
{
    private Vector<BufferedReader> senders;//Current other entities sending messages to this one, deprecated
    private int nextSender;//Deprecated
    private boolean pausing;//allows a robot to pause(wait for an input before continuing) | pause()
    private boolean userLevelPausing;//Appears to be unused | userPause()
    BufferedReader sysin;//System.in for inputs
    private static final int on = 2;//Unused
    private static final int off = 1;//Unused
    private static final int crashed = 0;//Unused
    private int[] loc;//A 4 int array for the total movements(0 - number of east movements, 1 - number of south movements, 2 - number of west movements, 3 - number of north movements) *The normalize method removes large numbers, like 4 North and 3 south will become 1 north and 0 south | normalize()
    private int beepers;//Number of beepers the entity has *-1 means it has infinite beepers
    private Color badgeColor;//color to display for the badge
    private Direction direction;//Current direction
    private int moves;//Increased whenever the grid entity changes location *will call normalize and reset to 0 when >10
    private int state;//Current robot status(0 is crashed, 1 is off, and 2 is on)
    private boolean isVisible;//If the robot should be displayed | setVisible() | robotworldwindow
    private int idNumber;//The unique id of the robot
    private static final int threshhold = 10;//Unused
    private static int numberOfEntities;//The number of gridentitys currently in operation | incrementRobots()
    private StateObject initialState;//The initial state(-1)
    private static final Entities type = new Entities();
    protected static Painter painter;
    
    //Set the number of entities to 0
    static {
        OldGridEntity.numberOfEntities = 0;
        //painter = new Painter(type);
    }
    
    //rotate the entity counterclockwise
    public void turnLeft() {
        if (this.state == 2) {
            this.pause("turnLeft");
            this.direction = this.direction.rotate(-1);
            final StateObject s = new StateObject(1);
            this.setChanged();
            this.notifyObservers(s);
            this.sleep();
        }
    }
    
    //custom: rotate the entity clockwise
    public void turnRight() {
        if (this.state == 2) {
            this.pause("turnRight");
            this.direction = this.direction.rotate(1);
            final StateObject s = new StateObject(6);
            this.setChanged();
            this.notifyObservers(s);
            this.sleep();
        }
    }
    
    //custom: move the entity 1 unit in its current direction ignoring walls
    public void phase() {
        if (this.state == 2) {
            this.pause("phase");
            boolean crashed = false;
            this.normalize();
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
            final StateObject s = new StateObject(0);
            this.setChanged();
            this.notifyObservers(s);
            this.sleep();
        }
    }
    
    //move the entity one unit forward, crash if there is a wall in front
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
            final StateObject s = new StateObject(0);
            this.setChanged();
            this.notifyObservers(s);
            this.sleep();
        }
    }
    
    //Remove one beeper from the world and increase current beepers by 1, crashes if no beeper in the current location
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
            final StateObject s = new StateObject(2);
            this.setChanged();
            this.notifyObservers(s);
            this.sleep();
        }
    }
    
    //
    public void putBeeper() {
        if (this.state == 2) {
            this.pause("putBeeper");
            this.normalize();
            if (this.beepers == 0) {
                this.crash("No beepers to put.");
                final StateObject s = new StateObject(3);
                this.setChanged();
                this.notifyObservers(s);
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
                final StateObject s = new StateObject(3);
                this.setChanged();
                this.notifyObservers(s);
            }
            this.sleep();
        }
    }
    
    public void skip(){
        if (this.state == 2) {
            this.pause("doze off");
            this.normalize();
            final StateObject s = new StateObject(5);
            this.setChanged();
            this.notifyObservers(s);
            this.sleep();
        }
    }
    
    public void turnOff() {
        this.pause("turnOff");
        if (this.state == 2) {
            System.out.println("Robot " + this.idNumber + ": Turning off");
            this.state = 1;
            final StateObject s = new StateObject(4);
            this.setChanged();
            this.notifyObservers(s);
            this.sleep();
        }
    }
    
    public World world() {
        return World.asObject();
    }
    
    public Enumeration neighbors() {
        final Vector v = new Vector();
        final Enumeration<OldGridEntity> all = World.robots();
        while (all.hasMoreElements()) {
            final OldGridEntity r = all.nextElement();
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
    
    public BufferedWriter connectTo(final OldGridEntity other, final ConnectStrategy strat) throws IOException {
        final PipedOutputStream out = new PipedOutputStream();
        final BufferedWriter result = new BufferedWriter(new OutputStreamWriter(out));
        other.acceptConnectionFrom(this, out, strat);
        return result;
    }
    
    public synchronized void acceptConnectionFrom(final OldGridEntity sender, final PipedOutputStream s, final ConnectStrategy strat) throws IOException {
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
    
    public OldGridEntity(final int street, final int avenue, final Direction direction, final int beepers, final Color badgeColor) {
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
        //World.addEntity(this);
        this.sleep();
        this.sleep();
    }
    
    public OldGridEntity(final int street, final int avenue, final Direction direction, final int beepers) {
        this(street, avenue, direction, beepers, null);
        System.out.println("LAVAHFEIWnfiewujferwfjrwi");
    }
    
    //Custom
    public void delete(){
        //World.removeEntity(this);
    }
    
    public Image getImage(){
        return painter.getScaledImage("idle", this.direction(), 0);
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
        return OldGridEntity.numberOfEntities++;
    }
    
    final class StateObject implements Serializable
    {
        private int street;
        private int avenue;
        private Direction direction;
        private int beepers;
        private int lastAction;
        
        public StateObject(final int lastAction) {
            this.street = OldGridEntity.this.loc[3] - OldGridEntity.this.loc[1];
            this.avenue = OldGridEntity.this.loc[0] - OldGridEntity.this.loc[2];
            this.direction = OldGridEntity.this.direction;
            this.beepers = OldGridEntity.this.beepers;
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
        void action(final OldGridEntity p0, final OldGridEntity p1, final BufferedReader p2);
    }
    
    interface Action
    {
        public static final int move = 0;
        public static final int turnLeft = 1;
        public static final int pickBeeper = 2;
        public static final int putBeeper = 3;
        public static final int turnOff = 4;
        public static final int turnRight = 6;
        public static final int skip = 5;
        public static final int initial = -1;
    }
}
