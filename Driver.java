package kareltherobot;



/**
 * Write a description of class Driver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Driver implements Directions
{
    public RobotTask[] agents;
    static
    {
        World.reset();
        World.setDelay(25);
        World.setVisible(true);        
    }
}
