package kareltherobot;
import java.awt.Color;


/**
 * Write a description of class Robot2 here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Robot2 extends UrRobot2
{
    public Robot2(int s, int a, Direction d, int b, Color badgeColor){
        super(s,a,d,b,badgeColor);
    }
    public Robot2(int s, int a, Direction d, int b){
        this(s,a,d,b,null);
    }
    public boolean nextToABeeper(){
        return super.nextToABeeper();
    }
    public boolean anyBeepersInBeeperBag(){
        return super.anyBeepersInBeeperBag();
    }
    public boolean frontIsClear(){
        return super.frontIsClear();
    }
    public boolean facingNorth(){
        return super.facingNorth();
    }
    public boolean facingEast(){
        return super.facingEast();
    }
    public boolean facingSouth(){
        return super.facingSouth();
    }
    public boolean facingWest(){
        return super.facingWest();
    }
    //custom
    public boolean facing(Direction d){
        return super.facing(d);
    }
    public boolean nextToARobot(){
        try{
            return nextToA(Class.forName("kareltherobot.UrRobot2"));
        }catch(Exception e){System.out.println(e);return false;}
    }
}
