package kareltherobot;



/**
 * Write a description of class God here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class AStar
{
    public static boolean NClear(int s, int a){
        return !World.checkEWWall(s, a);
    }
    public static boolean EClear(int s, int a){
        return !World.checkNSWall(s, a);
    }
    public static boolean SClear(int s, int a){
        return !World.checkEWWall(s-1, a);
    }
    public static boolean WClear(int s, int a){
        return !World.checkNSWall(s, a-1);
    }
    public String getpathto(){return "";}
}
