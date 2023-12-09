package kareltherobot;
import java.util.*;

/**
 * Write a description of class DrillPrescence here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class DrillPrescence
{
    static ArrayList<Cadet> cadets = new ArrayList<Cadet>();
    public static void addCadet(Cadet c){
        cadets.add(c);
    }
    public static void callCommand(){}
    public static Cadet findGuide(){
        return null;
    }
    public static ArrayList<Cadet> getCadets(String selectors){
        ArrayList<Cadet> cs = new ArrayList<Cadet>();
        String[] temp = selectors.split(";");
        for(Cadet c: cadets){
            cs.add(c);
        }
        return cs;
    }
}
