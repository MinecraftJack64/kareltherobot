package kareltherobot;


/**
 * Write a description of class Console here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Console
{
    public static void log(String stuff){
        if(World.allowedToPrint){
            System.out.print(stuff);
        }
    }
    public static void logln(String stuff){
        if(World.allowedToPrint){
            System.out.println(stuff);
        }
    }
}
