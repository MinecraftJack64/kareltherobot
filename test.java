package kareltherobot;


/**
 * Write a description of class test here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class test extends Driver
{
    public static void main(String[]args){
        UrRobot test = new UrRobot(1,1,North,0);
        KarelScript.run(test, "mm'rmm,mmmmmmm");
    }
}
