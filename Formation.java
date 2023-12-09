package kareltherobot;


/**
 * Write a description of interface Formation here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class Formation implements Group, Directions
{
    Direction orientation, direction;
    Point origin;
    Cadet cmdr;
}
