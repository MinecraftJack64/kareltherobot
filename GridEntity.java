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

public class GridEntity extends Observable implements Directions, Runnable
{
    private Vector<BufferedReader> senders;//Current other entities sending messages to this one, deprecated
    private int nextSender;//Deprecated
    private boolean pausing;//allows a robot to pause(wait for an input before continuing) | pause()
    private boolean userLevelPausing;//Appears to be unused | userPause()
    BufferedReader sysin;//System.in for inputs
    private static final int on = 2;//Unused
    private static final int off = 1;//Unused
    private static final int crashed = 0;//Unused
    private int[] loc;
    //private int beepers;//Number of beepers the entity has *-1 means it has infinite beepers
    //private Color badgeColor;//color to display for the badge
    private Direction direction;//Current direction
    private String sprite = "idle";
    private boolean isComboMode = false;
    private int moves;//Increased whenever the grid entity changes location *will call normalize and reset to 0 when >10
    //private int state;//Current robot status(0 is crashed, 1 is off, and 2 is on)
    private boolean isVisible;//If the robot should be displayed | setVisible() | robotworldwindow
    private int idNumber;//The unique id of the robot
    private static final int threshhold = 10;//Unused
    private static int numberOfEntities;//The number of gridentitys currently in operation | incrementRobots()
    private StateObject initialState;//The initial state(-1)
    private StateObject lastState;
    private static final Entities type = new Entities();
    protected static Painter painter;
    private String name = "GridEntity";
    
    //Set the number of entities to 0
    static {
        GridEntity.numberOfEntities = 0;
        painter = new Painter(new String[]{"idle ca karel[d].png"});
        System.out.println("Painter is loaded");
    }
    
    //rotate the entity counterclockwise
    void goTo(int s, int a) {
        this.pause("goTo");
        this.loc[0] = a;
        this.loc[1] = s;
        updateState(0);
    }
    void transform(int s, int a){
        this.goTo(this.loc[1]+s, this.loc[0]+a);
    }
    void move(int i){
        this.goTo(this.loc[1]+this.direction.getStreetUnit()*i, this.loc[0]+this.direction.getAvenueUnit()*i);
    }
    
    void face(Direction d){
        this.pause("face");
        this.direction = Direction.select(d.points());
        updateState(1);
    }
    void turn(int amount){
        this.face(this.direction.rotate(amount));
    }
    
    void changeState(String d){
        this.pause("restate");
        this.sprite = d;
        updateState(2);
    }
    
    void skip(){
        this.pause("doze off");
        updateState(3);
    }
    
    void updateFinalState(int actionCode){
        final StateObject s = new StateObject(actionCode);
        this.setChanged();
        this.notifyObservers(s);
        lastState = s;
    }
    void updateState(int actionCode){
        updateFinalState(actionCode);
        if(!isComboMode){
            this.sleep();
            System.out.println("isleep");
        } else System.out.println("atemps");
    }
    
    void startCombo(){
        isComboMode = true;
    }
    void stopCombo(){
        isComboMode = false;
    }
    
    public World world() {
        return World.asObject();
    }
    
    public Enumeration neighbors() {
        final Vector v = new Vector();
        final Enumeration<GridEntity> all = World.entities();
        while (all.hasMoreElements()) {
            final GridEntity r = all.nextElement();
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
    
    public BufferedWriter connectTo(final GridEntity other, final ConnectStrategy strat) throws IOException {
        final PipedOutputStream out = new PipedOutputStream();
        final BufferedWriter result = new BufferedWriter(new OutputStreamWriter(out));
        other.acceptConnectionFrom(this, out, strat);
        return result;
    }
    
    public synchronized void acceptConnectionFrom(final GridEntity sender, final PipedOutputStream s, final ConnectStrategy strat) throws IOException {
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
            System.out.println(name+"ID " + this.idNumber + " is about to " + message + ".");
            try {
                this.sysin.readLine();
            }
            catch (IOException ex) {}
        }
        System.out.println("Entity "+idNumber+" just ran "+message+".");
    }
    
    public void userPause(final String message) {
        if (this.userLevelPausing) {
            System.out.println(name+"ID " + this.idNumber + " is about to " + message + ".");
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
    
    public GridEntity(final int street, final int avenue, final Direction direction) {
        //this.senders.toString();
        this.senders = new Vector();
        this.nextSender = 0;
        this.pausing = false;
        this.userLevelPausing = false;
        this.sysin = new BufferedReader(new InputStreamReader(System.in));
        this.loc = new int[4];
        //this.badgeColor = null;
        this.moves = 0;
        //this.state = 2;
        this.isVisible = true;
        this.loc[1] = street;
        this.loc[0] = avenue;
        this.direction = direction;
        //this.beepers = beepers;
        this.idNumber = incrementEntities();
        //this.state = 2;
        this.initialState = new StateObject(-1);
        //this.badgeColor = badgeColor;
        World.addEntity(this);
        this.sleep();
        this.sleep();
    }
    
    //Custom
    void delete(){
        World.removeEntity(this);
    }
    
    public Image getImage(){
        //System.out.println("Image requested");
        return painter.getScaledImage(this.sprite, this.direction(), 0);
    }
    
    /*final Color badgeColor() {
        return this.badgeColor;
    }*/
    
    final void restoreInitialState() {
        this.loc[1] = this.initialState.street;
        this.loc[0] = this.initialState.avenue;
        this.direction = this.initialState.direction;
        this.showState("Restoring ");
        this.setChanged();
        this.notifyObservers(this.initialState);
        this.sleep();
    }
    
    public String toString() {
        //this.normalize();
        return name+"ID " + this.idNumber + " at (street: " + this.loc[1] + ") (avenue: " + this.loc[0] + ") ( direction: " + this.direction.toString() + ")";
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
        //this.normalize();
        System.out.println(String.valueOf(s) + this);
    }
    
    final boolean areYouHere(final int street, final int avenue) {
        //this.normalize();
        return this.loc[1] == street && this.loc[0] == avenue;
    }
    
    private boolean crash(final String s) {
        //this.state = 0;
        this.showState("Error shutoff: ");
        System.out.println(s);
        return true;
    }
    
    private void pauseExit() {
        try {
            this.sysin.readLine();
        }
        catch (IOException ex) {}
        System.exit(0);
    }
    
    final Direction direction() {
        return this.direction;
    }
    
    final int street() {
        return this.loc[1];
    }
    
    final int avenue() {
        return this.loc[0];
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
    
    private static synchronized int incrementEntities() {
        return GridEntity.numberOfEntities++;
    }
    
    final class StateObject implements Serializable
    {
        private int street;
        private int avenue;
        private Direction direction;
        private int lastAction;
        private String state;
        private StateObject lastState;
        //Actions 0 - movement, 1 - direction, 2 - state
        public StateObject(final int lastAction) {
            this.street = GridEntity.this.loc[1];
            this.avenue = GridEntity.this.loc[0];
            this.direction = GridEntity.this.direction;
            this.lastAction = lastAction;
            this.state = GridEntity.this.sprite;
            if(lastAction == 0){
                lastState = GridEntity.this.lastState;
            }
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
        
        public String state(){
            return this.state;
        }
        
        public int lastAction() {
            return this.lastAction;
        }
        public StateObject lastState(){
            return lastState;
        }
    }
    
    public interface ConnectStrategy
    {
        void action(final GridEntity p0, final GridEntity p1, final BufferedReader p2);
    }
    
    interface Action
    {
        public static final int move = 0;
    }
}
