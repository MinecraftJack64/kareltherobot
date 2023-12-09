package kareltherobot;
import java.awt.Color;


/**
 * Write a description of class Robot2 here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class UrRobot2 extends GridEntity implements IUrRobot
{
    private int beepers;
    private int state;
    private Color badgeColor;
    private String name = "Robot";
    public UrRobot2(int s, int a, Direction d, int b, Color badgeColor){
        super(s,a,d);
        beepers = b;
        state = 2;
        this.badgeColor = badgeColor;
    }
    public UrRobot2(int s, int a, Direction d, int b){
        this(s,a,d,b,null);
    }
    public void stopCombo(){
        super.stopCombo();
    }
    public void startCombo(){
        super.startCombo();
    }
    int beepers(){
        return beepers;
    }
    final boolean running() {
        return this.state == 2;
    }
    public void move(){
        this.move(1);
    }
    public void turnLeft(){
        this.turn(-1);
    }
    public void skip(){
        super.skip();
    }
    public void putBeeper() {
        if(this.state ==2){
            this.pause("putBeeper");
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
                World.placeBeepers(this.street(), this.avenue(), 1);
                updateState(3);
            }
            this.sleep();
        }
    }
    public void turnOff() {
        this.pause("turnOff");
        if (this.state == 2) {
            System.out.println("Robot " + this.getID() + ": Turning off");
            this.state = 1;
            updateState(4);
        }
    }
    public void pickBeeper() {
        if (this.state == 2) {
            this.pause("pickBeeper");
            boolean crashed = false;
            if (!nextToABeeper()) {
                crashed = this.crash("No beepers to pick");
            }
            if (!crashed) {
                if (this.beepers != -1) {
                    ++this.beepers;
                }
                World.placeBeepers(this.street(), this.avenue(), -1);
            }
            updateState(4);
        }
    }
    private boolean validate() {
        if (this.beepers < -1) {
            return !this.crash("Robot has negative beepers");
        }
        if (this.street() < 1) {
            return !this.crash("Robot tried to move through South boundary wall");
        }
        return this.avenue() >= 1 || !this.crash("Robot tried to move through West boundary wall");
    }
    private boolean crash(final String s) {
        this.state = 0;
        this.showState("Error shutoff: ");
        System.out.println(s);
        return true;
    }
    public String toString() {
        return name+"ID " + this.getID() + " at (street: " + this.street() + ") (avenue: " + this.avenue() + ") (beepers: " + ((this.beepers >= 0) ? new StringBuffer().append(this.beepers).toString() : "infinite") + ") ( direction: " + this.direction().toString() + ((this.state == 2) ? ") on" : ") off");
    }
    final Color badgeColor() {
        return this.badgeColor;
    }
    boolean nextToABeeper() {
        this.sleep();
        this.pause("say if it is nextToABeeper");
        return World.checkBeeper(this.street(), this.avenue());
    }
    boolean frontIsClear() {
        this.pause("say if its frontIsClear");
        switch (this.direction().points()) {
            case 3: {
                if (World.checkEWWall(this.street(), this.avenue())) {
                    return false;
                }
                break;
            }
            case 0: {
                if (World.checkNSWall(this.street(), this.avenue())) {
                    return false;
                }
                break;
            }
            case 1: {
                if (World.checkEWWall(this.street() - 1, this.avenue())) {
                    return false;
                }
                break;
            }
            case 2: {
                if (World.checkNSWall(this.street(), this.avenue() - 1)) {
                    return false;
                }
                break;
            }
        }
        return true;
    }
    boolean anyBeepersInBeeperBag() {
        this.pause("say if it has anyBeepersInBeeperBag");
        return this.beepers() > 0 || this.beepers() == -1;
    }
    boolean facingNorth() {
        this.pause("say if it is facingNorth");
        return this.direction() == Robot.North;
    }
    
    boolean facingSouth() {
        this.pause("say if it is facingSouth");
        return this.direction() == Robot.South;
    }
    
    boolean facingEast() {
        this.pause("say if it is facingEast");
        return this.direction() == Robot.East;
    }
    
    boolean facingWest() {
        this.pause("say if it is facingWest");
        return this.direction() == Robot.West;
    }
    
    boolean facing(Direction d) {
        this.pause("say if it is facing "+d);
        return this.direction() == d;
    }
    
    boolean nextToAnEntity() throws ClassNotFoundException {
        return nextToA(Class.forName("kareltherobot.Entity"));
    }
    boolean nextToASubclass(){
        return nextToA(this.getClass());
    }
    boolean nextToA(Class type){
        this.sleep();
        this.pause("say if it is nextToA"+type.getSimpleName());
        return World.checkEntity(this, type, this.street(), this.avenue());
    }
}
