package kareltherobot;
import java.util.ArrayList;

/**
 * Write a description of class Cadet here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Cadet extends Robot
{
    Group group;
    String state;
    public Cadet(int s, int a, Direction d, int b){
        super(s,a,d,b);
        DrillPrescence.addCadet(this);
    }
    protected void call(Command cmd){
    }
    public void formPlatoon(){
        Cadet guide = DrillPrescence.findGuide();
        ArrayList<Cadet> cadets = DrillPrescence.getCadets("");
        guide.call(new Command(""));
        
        group = new Platoon(this, guide);
    }
    public void own(Group group){
        this.group = group;
    }
    public void disown(){
        this.group = null;
    }
}
