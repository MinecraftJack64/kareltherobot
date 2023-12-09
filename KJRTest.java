package kareltherobot;


// 
// Decompiled by Procyon v0.5.36
// 

 

import java.util.Enumeration;
import junit.framework.TestCase;

public class KJRTest extends TestCase implements Directions
{
    public KJRTest(final String name) {
        super(name);
    }
    
    private boolean frontIsClear(final UrRobot robot) {
        final int street = robot.street();
        final int avenue = robot.avenue();
        switch (robot.direction().points()) {
            case 3: {
                if (World.checkEWWall(street, avenue)) {
                    return false;
                }
                break;
            }
            case 0: {
                if (World.checkNSWall(street, avenue)) {
                    return false;
                }
                break;
            }
            case 1: {
                if (World.checkEWWall(street - 1, avenue)) {
                    return false;
                }
                break;
            }
            case 2: {
                if (World.checkNSWall(street, avenue - 1)) {
                    return false;
                }
                break;
            }
        }
        return true;
    }
    private boolean frontIsClear(final UrRobot2 robot) {
        final int street = robot.street();
        final int avenue = robot.avenue();
        switch (robot.direction().points()) {
            case 3: {
                if (World.checkEWWall(street, avenue)) {
                    return false;
                }
                break;
            }
            case 0: {
                if (World.checkNSWall(street, avenue)) {
                    return false;
                }
                break;
            }
            case 1: {
                if (World.checkEWWall(street - 1, avenue)) {
                    return false;
                }
                break;
            }
            case 2: {
                if (World.checkNSWall(street, avenue - 1)) {
                    return false;
                }
                break;
            }
        }
        return true;
    }
    
    public final void assertFacingNorth(final UrRobot robot) {
        assertTrue("Not facing North", robot.direction() == KJRTest.North);
    }
    
    public final void assertNotFacingNorth(final UrRobot robot) {
        assertTrue("Facing North", robot.direction() != KJRTest.North);
    }
    
    public final void assertFacingEast(final UrRobot robot) {
        assertTrue("Not facing East", robot.direction() == KJRTest.East);
    }
    
    public final void assertNotFacingEast(final UrRobot robot) {
        assertTrue("Facing East", robot.direction() != KJRTest.East);
    }
    
    public final void assertFacingSouth(final UrRobot robot) {
        assertTrue("Not facing South", robot.direction() == KJRTest.South);
    }
    
    public final void assertNotFacingSouth(final UrRobot robot) {
        assertTrue("Facing South", robot.direction() != KJRTest.South);
    }
    
    public final void assertFacingWest(final UrRobot robot) {
        assertTrue("Not facing West", robot.direction() == KJRTest.West);
    }
    
    public final void assertNotFacingWest(final UrRobot robot) {
        assertTrue("Facing West", robot.direction() != KJRTest.West);
    }
    
    public final void assertAt(final UrRobot robot, final int street, final int avenue) {
        this.assertOnStreet(robot, street);
        this.assertOnAvenue(robot, avenue);
    }
    
    public final void assertNotAt(final UrRobot robot, final int street, final int avenue) {
        assertTrue("At " + street + " street and " + avenue + " avenue.", robot.street() != street || robot.avenue() != avenue);
    }
    
    public final void assertOnStreet(final UrRobot robot, final int street) {
        assertTrue("Not on " + street + " street.", robot.street() == street);
    }
    
    public final void assertNotOnStreet(final UrRobot robot, final int street) {
        assertTrue("On " + street + " street.", robot.street() != street);
    }
    
    public final void assertOnAvenue(final UrRobot robot, final int avenue) {
        assertTrue("Not on " + avenue + " avenue.", robot.avenue() == avenue);
    }
    
    public final void assertNotOnAvenue(final UrRobot robot, final int avenue) {
        assertTrue("On " + avenue + " avenue.", robot.avenue() != avenue);
    }
    
    public final void assertHasNeighbor(final UrRobot robot) {
        assertTrue("No neighbors present.", World.checkRobot(robot, robot.street(), robot.avenue()));
    }
    
    public final void assertHasNoNeighbor(final UrRobot robot) {
        assertTrue("Neighbors present.", !World.checkRobot(robot, robot.street(), robot.avenue()));
    }
    
    public final void assertNextToABeeper(final UrRobot robot) {
        assertTrue("Not next to a beeper.", World.checkBeeper(robot.street(), robot.avenue()));
    }
    
    public final void assertNotNextToABeeper(final UrRobot robot) {
        assertTrue("Next to a beeper.", !World.checkBeeper(robot.street(), robot.avenue()));
    }
    
    public final void assertBeepersInBeeperBag(final UrRobot robot) {
        assertTrue("No beepers in bag.", robot.beepers() > 0 || robot.beepers() == -1);
    }
    
    public final void assertNoBeepersInBeeperBag(final UrRobot robot) {
        assertTrue("Beepers in bag.", robot.beepers() == 0);
    }
    
    public final void assertFacingNorth(final UrRobot2 robot) {
        assertTrue("Not facing North", robot.direction() == KJRTest.North);
    }
    
    public final void assertNotFacingNorth(final UrRobot2 robot) {
        assertTrue("Facing North", robot.direction() != KJRTest.North);
    }
    
    public final void assertFacingEast(final UrRobot2 robot) {
        assertTrue("Not facing East", robot.direction() == KJRTest.East);
    }
    
    public final void assertNotFacingEast(final UrRobot2 robot) {
        assertTrue("Facing East", robot.direction() != KJRTest.East);
    }
    
    public final void assertFacingSouth(final UrRobot2 robot) {
        assertTrue("Not facing South", robot.direction() == KJRTest.South);
    }
    
    public final void assertNotFacingSouth(final UrRobot2 robot) {
        assertTrue("Facing South", robot.direction() != KJRTest.South);
    }
    
    public final void assertFacingWest(final UrRobot2 robot) {
        assertTrue("Not facing West", robot.direction() == KJRTest.West);
    }
    
    public final void assertNotFacingWest(final UrRobot2 robot) {
        assertTrue("Facing West", robot.direction() != KJRTest.West);
    }
    
    public final void assertAt(final UrRobot2 robot, final int street, final int avenue) {
        this.assertOnStreet(robot, street);
        this.assertOnAvenue(robot, avenue);
    }
    
    public final void assertNotAt(final UrRobot2 robot, final int street, final int avenue) {
        assertTrue("At " + street + " street and " + avenue + " avenue.", robot.street() != street || robot.avenue() != avenue);
    }
    
    public final void assertOnStreet(final UrRobot2 robot, final int street) {
        assertTrue("Not on " + street + " street.", robot.street() == street);
    }
    
    public final void assertNotOnStreet(final UrRobot2 robot, final int street) {
        assertTrue("On " + street + " street.", robot.street() != street);
    }
    
    public final void assertOnAvenue(final UrRobot2 robot, final int avenue) {
        assertTrue("Not on " + avenue + " avenue.", robot.avenue() == avenue);
    }
    
    public final void assertNotOnAvenue(final UrRobot2 robot, final int avenue) {
        assertTrue("On " + avenue + " avenue.", robot.avenue() != avenue);
    }
    
    public final void assertHasNeighbor(final UrRobot2 robot) {
        try{
            assertTrue("No neighbors present.", robot.nextToA(Class.forName("kareltherobot.UrRobot2")));
        }catch(ClassNotFoundException c){
            System.out.println(c);
        }
    }
    
    public final void assertHasNoNeighbor(final UrRobot2 robot) {
        try{
            assertTrue("Neighbors present.", !robot.nextToA(Class.forName("kareltherobot.UrRobot2")));
        }catch(ClassNotFoundException c){
            System.out.println(c);
        }
    }
    
    public final void assertNextToABeeper(final UrRobot2 robot) {
        assertTrue("Not next to a beeper.", World.checkBeeper(robot.street(), robot.avenue()));
    }
    
    public final void assertNotNextToABeeper(final UrRobot2 robot) {
        assertTrue("Next to a beeper.", !World.checkBeeper(robot.street(), robot.avenue()));
    }
    
    public final void assertBeepersInBeeperBag(final UrRobot2 robot) {
        assertTrue("No beepers in bag.", robot.beepers() > 0 || robot.beepers() == -1);
    }
    
    public final void assertNoBeepersInBeeperBag(final UrRobot2 robot) {
        assertTrue("Beepers in bag.", robot.beepers() == 0);
    }
    
    public final void assertFrontIsClear(final UrRobot2 robot) {
        assertTrue("Front is blocked.", robot.frontIsClear());
    }
    
    public final void assertFrontIsBlocked(final UrRobot2 robot) {
        assertTrue("Front is clear.", !robot.frontIsClear());
    }
    
    public final void assertRunning(final UrRobot robot) {
        assertTrue("Not running.", robot.running());
    }
    
    public final void assertNotRunning(final UrRobot robot) {
        assertTrue("Still running.", !robot.running());
    }
    
    public final void assertBeepersInWorld(final int n) {
        final int totalBeepers = this.totalBeepers();
        assertEquals("Wrong number of beepers in world.", n, totalBeepers);
    }
    
    public final void assertBeepersInWorld() {
        final int totalBeepers = this.totalBeepers();
        assertTrue("No beepers in world.", totalBeepers > 0);
    }
    
    private int totalBeepers() {
        int totalBeepers = 0;
        final Enumeration allBeepers = World.beepers();
        while (allBeepers.hasMoreElements()) {
            totalBeepers += ((World.BeeperCell)allBeepers.nextElement()).number();
        }
        return totalBeepers;
    }
    
    private int totalBeepers(final int street, final int avenue) {
        int totalBeepers = 0;
        final Enumeration<World.BeeperCell> allBeepers = World.beepers();
        while (allBeepers.hasMoreElements()) {
            final World.BeeperCell cell = allBeepers.nextElement();
            if (street == cell.street() && avenue == cell.avenue()) {
                totalBeepers = cell.number();
                break;
            }
        }
        return totalBeepers;
    }
    
    public final void assertRobotsInWorld(final int n) {
        assertEquals("Wrong number of robots in world.", n, World.numberOfRobots());
    }
    
    public final void assertRobotsInWorld() {
        assertTrue("No robots in world.", World.numberOfRobots() > 0);
    }
    
    public final void assertBeepersAt(final int street, final int avenue, final int n) {
        assertEquals("Wrong number of beepers on corner.", n, this.totalBeepers(street, avenue));
    }
    
    public final void assertBeepersAt(final int street, final int avenue) {
        assertTrue("No beepers on corner.", this.totalBeepers(street, avenue) > 0);
    }
    
    private final int robotsOnCorner(final int street, final int avenue) {
        int totalRobots = 0;
        final Enumeration<UrRobot> robots = World.robots();
        while (robots.hasMoreElements()) {
            final UrRobot karel = robots.nextElement();
            if (karel.areYouHere(street, avenue)) {
                ++totalRobots;
            }
        }
        return totalRobots;
    }
    
    public final void assertRobotsAt(final int street, final int avenue, final int n) {
        assertEquals("Wrong number of robots on corner.", n, this.robotsOnCorner(street, avenue));
    }
    
    public final void assertRobotsAt(final int street, final int avenue) {
        assertTrue("No robots on corner.", this.robotsOnCorner(street, avenue) > 0);
    }
}
