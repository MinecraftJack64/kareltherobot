package kareltherobot;


// 
// Decompiled by Procyon v0.5.36
// 

 

import java.awt.Color;

public class Robot extends UrRobot implements Directions
{
    public Robot(final int street, final int avenue, final Direction direction, final int beepers) {
        super(street, avenue, direction, beepers);
    }
    
    public Robot(final int street, final int avenue, final Direction direction, final int beepers, final Color badge) {
        super(street, avenue, direction, beepers, badge);
    }
    
    public boolean frontIsClear() {
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
    
    public boolean nextToABeeper() {
        this.sleep();
        this.pause("say if it is nextToABeeper");
        return World.checkBeeper(this.street(), this.avenue());
    }
    
    public boolean nextToARobot() {
        this.sleep();
        this.pause("say if it is nextToARobot");
        return World.checkRobot(this, this.street(), this.avenue());
    }
    
    public boolean facingNorth() {
        this.pause("say if it is facingNorth");
        return this.direction() == Robot.North;
    }
    
    public boolean facingSouth() {
        this.pause("say if it is facingSouth");
        return this.direction() == Robot.South;
    }
    
    public boolean facingEast() {
        this.pause("say if it is facingEast");
        return this.direction() == Robot.East;
    }
    
    public boolean facingWest() {
        this.pause("say if it is facingWest");
        return this.direction() == Robot.West;
    }
    
    public boolean facing(Direction d) {
        this.pause("say if it is facing "+d);
        return this.direction() == d;
    }
    
    public boolean anyBeepersInBeeperBag() {
        this.pause("say if it has anyBeepersInBeeperBag");
        return this.beepers() > 0 || this.beepers() == -1;
    }
}
