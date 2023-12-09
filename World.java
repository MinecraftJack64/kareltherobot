package kareltherobot;


// 
// Decompiled by Procyon v0.5.36
// 

 

import java.util.Observable;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.util.StringTokenizer;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.util.Enumeration;
import java.util.Observer;
import java.awt.Canvas;
import java.util.Vector;
import java.util.Hashtable;
import java.util.ArrayList;

public class World implements Directions
{
    public static final World asObject;
    private static Hashtable<IntPair, Integer> beepers;
    private static Hashtable<IntPair, Integer> ewWalls;
    private static Hashtable<IntPair, Integer> nsWalls;
    private static final Integer one;
    private static Vector<UrRobot> robots;
    private static Vector<GridEntity> entities;
    private static RobotObserver myObserver;
    private static EntityObserver entityObserver;
    private static int delay;
    private static int streets;
    private static int avenues;
    private static ThreadGroup threads;
    private static int threadGroupNumber;
    private static boolean tracing;
    private static Hashtable checkpointBeepers;
    private static RobotWorldWindow view;
    private static WorldBuilderInterface worldBuilder;
    private static Vector<Thread> threadVector;
    private static boolean isVisible;
    public static boolean allowedToPrint = true;
    
    static {
        asObject = new World();
        World.beepers = new Hashtable();
        World.ewWalls = new Hashtable();
        World.nsWalls = new Hashtable();
        one = new Integer(1);
        World.robots = new Vector();
        World.entities = new Vector();
        World.myObserver = new RobotObserver();
        World.entityObserver = new EntityObserver();
        World.delay = 100;
        World.streets = 10;
        World.avenues = 10;
        World.threads = new ThreadGroup("RobotThread0");
        World.threadGroupNumber = 0;
        World.tracing = true;
        World.checkpointBeepers = null;
        World.view = null;
        World.worldBuilder = null;
        World.threadVector = new Vector();
        World.isVisible = false;
    }
    
    private World() {
    }
    
    public static final World asObject() {
        return World.asObject;
    }
    
    static final int numberOfStreets() {
        return World.streets;
    }
    
    static final int numberOfAvenues() {
        return World.avenues;
    }
    
    static final void setOS9Mac(final boolean mac) {
        if (World.view == null) {
            World.view = new RobotWorldWindow();
        }
        World.view.setMac(mac);
    }
    
    public static synchronized void placeBeepers(final int Street, final int Avenue, final int howMany) {
        final IntPair where = new IntPair(Street, Avenue);
        final Integer p = World.beepers.get(where);
        if (p == null) {
            if (howMany > 0 || howMany == -1) {
                World.beepers.put(where, new Integer(howMany));
            }
        }
        else {
            if (p == -1) {
                return;
            }
            final int newval = p + howMany;
            World.beepers.remove(where);
            if (newval > 0 || newval == -1) {
                World.beepers.put(where, new Integer(newval));
            }
        }
    }
    
    public static synchronized void clearBeepers(final int Street, final int Avenue) {
        final IntPair where = new IntPair(Street, Avenue);
        World.beepers.remove(where);
    }
    
    static synchronized boolean decreaseBeeperIfPossible(final int Street, final int Avenue) {
        boolean result = false;
        if (checkBeeper(Street, Avenue)) {
            result = true;
            placeBeepers(Street, Avenue, -1);
        }
        return result;
    }
    
    static synchronized boolean checkBeeper(final int Street, final int Avenue) {
        final IntPair p = new IntPair(Street, Avenue);
        return World.beepers.get(p) != null;
    }
    
    //custom
    static synchronized int countBeepersAt(final int Street, final int Avenue) {
        final IntPair p = new IntPair(Street, Avenue);
        return World.beepers.get(p);
    }
    
    public static void smallView(final int height) {
        World.view = new RobotWorldWindow(height);
    }
    
    static RobotWorldWindow view() {
        if (World.view == null) {
            World.view = new RobotWorldWindow();
        }
        return World.view;
    }
    
    public static Canvas worldCanvas() {
        if (World.view == null) {
            World.view = new RobotWorldWindow();
        }
        return World.view.worldCanvas();
    }
    
    static void addRobot(final UrRobot k) {
        World.robots.addElement(k);
        System.out.println("Addes");
        k.addObserver(World.myObserver);
        if (World.tracing) {
            System.out.println(k);
        }
        if (World.view != null) {
            World.view.repaint();
        }
    }
    
    static void addEntity(final GridEntity k) {
        World.entities.addElement(k);
        System.out.println("Added Entity "+k);
        k.addObserver(World.entityObserver);
        if (World.tracing) {
            System.out.println(k);
        }
        if (World.view != null) {
            World.view.repaint();
        }
    }
    
    static void removeRobot(final UrRobot k) {
        System.out.println("Removes");
        k.deleteObserver(World.myObserver);
        World.robots.remove(k);
        if (World.view != null) {
            World.view.repaint();
        }
    }
    
    static void removeEntity(final GridEntity k) {
        System.out.println("Removed Entity "+k+" from World");
        k.deleteObserver(World.entityObserver);
        World.entities.remove(k);
        if (World.view != null) {
            World.view.repaint();
        }
    }
    
    static synchronized boolean checkRobot(final UrRobot k, final int street, final int avenue) {
        final Enumeration e = World.robots.elements();
        while (e.hasMoreElements()) {
            final UrRobot o = (UrRobot)e.nextElement();
            if (o != k && o.areYouHere(street, avenue)) {
                return true;
            }
        }
        return false;
    }
    
    static synchronized boolean checkEntity(final GridEntity k, Class type, final int street, final int avenue) {
        final Enumeration<GridEntity> e = World.entities.elements();
        while (e.hasMoreElements()) {
            final GridEntity o = e.nextElement();
            if (o != k && o.areYouHere(street, avenue)&&o.getClass().equals(type)) {
                return true;
            }
        }
        return false;
    }
    
    //begin custom
    static synchronized ArrayList<UrRobot> getRobotsAt(final UrRobot k, final int street, final int avenue) {
        final Enumeration e = World.robots.elements();
        ArrayList<UrRobot> l = new ArrayList<UrRobot>();
        while (e.hasMoreElements()) {
            final UrRobot o = (UrRobot)e.nextElement();
            if (o != k && o.areYouHere(street, avenue)) {
                l.add(o);
            }
        }
        return l;
    }
    
    static synchronized ArrayList<GridEntity> getEntitiesAt(final GridEntity k, final int street, final int avenue) {
        final Enumeration<GridEntity> e = World.entities.elements();
        ArrayList<GridEntity> l = new ArrayList<GridEntity>();
        while (e.hasMoreElements()) {
            final GridEntity o = e.nextElement();
            if (o != k && o.areYouHere(street, avenue)) {
                l.add(o);
            }
        }
        return l;
    }
    
    static synchronized ArrayList<UrRobot> getRobotsAt(final int street, final int avenue) {
        return getRobotsAt(null, street, avenue);
    }
    
    static synchronized ArrayList<GridEntity> getEntitiesAt(final int street, final int avenue) {
        return getEntitiesAt(null, street, avenue);
    }
    
    static synchronized UrRobot getRobotByID(final int id) {
        final Enumeration e = World.robots.elements();
        while (e.hasMoreElements()) {
            final UrRobot o = (UrRobot)e.nextElement();
            if (o.getID()==id) {
                return o;
            }
        }
        return null;
    }
    
    static synchronized GridEntity getEntityByID(final int id) {
        final Enumeration<GridEntity> e = World.entities.elements();
        while (e.hasMoreElements()) {
            final GridEntity o = e.nextElement();
            if (o.getID()==id) {
                return o;
            }
        }
        return null;
    }
    //end custom
    
    static Enumeration ewWalls() {
        return World.ewWalls.keys();
    }
    
    static Enumeration nsWalls() {
        return World.nsWalls.keys();
    }
    
    static Enumeration robots() {
        return World.robots.elements();
    }
    
    static Enumeration entities() {
        return World.entities.elements();
    }
    
    static int numberOfRobots() {
        return World.robots.size();
    }
    
    static int numberOfEntities() {
        return World.entities.size();
    }
    
    static Enumeration beepers() {
        return new BeeperEnum();
    }
    
    static final boolean checkEWWall(final int NorthOfStreet, final int atAvenue) {
        if (NorthOfStreet == 0) {
            return true;
        }
        final IntPair p = new IntPair(NorthOfStreet, atAvenue);
        return World.ewWalls.get(p) != null;
    }
    
    static final boolean checkNSWall(final int atStreet, final int EastOfAvenue) {
        if (EastOfAvenue == 0) {
            return true;
        }
        final IntPair p = new IntPair(atStreet, EastOfAvenue);
        return World.nsWalls.get(p) != null;
    }
    
    public static final void placeEWWall(final int NorthOfStreet, final int atAvenue, final int lengthTowardEast) {
        for (int i = 0; i < lengthTowardEast; ++i) {
            World.ewWalls.put(new IntPair(NorthOfStreet, atAvenue + i), World.one);
        }
    }
    
    public static final void placeNSWall(final int atStreet, final int EastOfAvenue, final int lengthTowardNorth) {
        for (int i = 0; i < lengthTowardNorth; ++i) {
            World.nsWalls.put(new IntPair(atStreet + i, EastOfAvenue), World.one);
        }
    }
    
    public static final void removeEWWall(final int NorthOfStreet, final int atAvenue) {
        final IntPair where = new IntPair(NorthOfStreet, atAvenue);
        World.ewWalls.remove(where);
    }
    
    public static final void removeNSWall(final int atStreet, final int EastOfAvenue) {
        final IntPair where = new IntPair(atStreet, EastOfAvenue);
        World.nsWalls.remove(where);
    }
    
    public static final void saveWorld(final String filename) {
        saveWorld(null, filename);
    }
    
    public static final void saveXMLWorld(final String filename) {
        saveXMLWorld(null, filename);
    }
    
    public static final String asText(final String sep) {
        String result = "KarelWorld" + sep;
        result = String.valueOf(result) + "streets " + numberOfStreets() + sep;
        result = String.valueOf(result) + "avenues " + numberOfAvenues() + sep;
        Enumeration e = World.beepers.keys();
        while (e.hasMoreElements()) {
            final IntPair p = (IntPair)e.nextElement();
            result = String.valueOf(result) + "beepers " + p.s + " " + p.a + " " + World.beepers.get(p) + sep;
        }
        e = World.ewWalls.keys();
        while (e.hasMoreElements()) {
            final IntPair p = (IntPair)e.nextElement();
            result = String.valueOf(result) + "eastwestwalls " + p.s + " " + p.a + " " + p.a + sep;
        }
        e = World.nsWalls.keys();
        while (e.hasMoreElements()) {
            final IntPair p = (IntPair)e.nextElement();
            result = String.valueOf(result) + "northsouthwalls " + p.a + " " + p.s + " " + p.s + sep;
        }
        return result;
    }
    
    public static final void saveWorld(final String directoryPath, final String filename) {
        try {
            final BufferedWriter w = new BufferedWriter(new FileWriter(new File(directoryPath, filename)));
            w.write("KarelWorld");
            w.newLine();
            w.write("streets " + numberOfStreets());
            w.newLine();
            w.write("avenues " + numberOfAvenues());
            w.newLine();
            Enumeration<IntPair> e = World.beepers.keys();
            while (e.hasMoreElements()) {
                final IntPair p = e.nextElement();
                w.write("beepers " + p.s + " " + p.a + " " + World.beepers.get(p));
                w.newLine();
            }
            e = World.ewWalls.keys();
            while (e.hasMoreElements()) {
                final IntPair p = e.nextElement();
                w.write("eastwestwalls " + p.s + " " + p.a + " " + p.a);
                w.newLine();
            }
            e = World.nsWalls.keys();
            while (e.hasMoreElements()) {
                final IntPair p = e.nextElement();
                w.write("northsouthwalls " + p.a + " " + p.s + " " + p.s);
                w.newLine();
            }
            Enumeration<UrRobot> r = World.robots();
            while (r.hasMoreElements()) {
                final UrRobot b = r.nextElement();
                w.write("urrobot " + b.street() + " " + b.avenue() + " " + b.direction().points());
                w.newLine();
            }
            //Handle Grid Entities
            w.close();
        }
        catch (IOException e2) {
            System.out.println("Can't save world.");
        }
    }
    
    public static void saveXMLWorld(final String directoryPath, final String filename) {
        try {
            final BufferedWriter w = new BufferedWriter(new FileWriter(new File(directoryPath, filename)));
            w.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            w.newLine();
            w.write("<karelworld ");
            w.write("streets=\"" + numberOfStreets() + "\" ");
            w.write("avenues=\"" + numberOfAvenues() + "\" >");
            w.newLine();
            w.write("\t<beepers>");
            w.newLine();
            Enumeration<IntPair> e = World.beepers.keys();
            while (e.hasMoreElements()) {
                final IntPair p = e.nextElement();
                w.write("\t\t<where street=\"" + p.s + "\" avenue=\"" + p.a + "\" howmany=\"" + World.beepers.get(p) + "\" />");
                w.newLine();
            }
            w.write("\t</beepers>");
            w.newLine();
            w.write("\t<eastwestwalls>");
            w.newLine();
            e = World.ewWalls.keys();
            while (e.hasMoreElements()) {
                final IntPair p = e.nextElement();
                w.write("\t\t<where northof=\"" + p.s + "\" firstavenue=\"" + p.a + "\" lastavenue=\"" + p.a + "\" />");
                w.newLine();
            }
            w.write("\t</eastwestwalls>");
            w.newLine();
            w.write("\t<northsouthwalls>");
            w.newLine();
            e = World.nsWalls.keys();
            while (e.hasMoreElements()) {
                final IntPair p = e.nextElement();
                w.write("\t\t<where eastof=\"" + p.a + "\" firststreet=\"" + p.s + "\" laststreet=\"" + p.s + "\" />");
                w.newLine();
            }
            w.write("\t</northsouthwalls>");
            w.newLine();
            w.write("</karelworld>");
            w.newLine();
            w.close();
        }
        catch (IOException e2) {
            System.out.println("Can't save world.");
        }
    }
    
    public static final void showBeepers() {
        final Enumeration<IntPair> e = World.beepers.keys();
        while (e.hasMoreElements()) {
            final IntPair p = e.nextElement();
            final int b = World.beepers.get(p);
            System.out.println(String.valueOf((b >= 0) ? new StringBuffer().append(b).toString() : "infinite number of") + " beepers at " + p.s + " street and " + p.a + " avenue");
        }
    }
    
    public static final void showWorld() {
        System.out.println();
        showBeepers();
        Enumeration<IntPair> e = World.ewWalls.keys();
        while (e.hasMoreElements()) {
            final IntPair p = e.nextElement();
            System.out.println("east west wall above " + p.s + " street crossing " + p.a + " avenue");
        }
        e = World.nsWalls.keys();
        while (e.hasMoreElements()) {
            final IntPair p = e.nextElement();
            System.out.println("north south wall east of " + p.a + " avenue crossing " + p.s + " street");
        }
        System.out.println();
    }
    
    public static final void readWorld(final String filename) {
        readWorld(null, filename);
    }
    
    public static final void readWorld(final String directoryPath, final String filename) {
        try {
            final FileInputStream f = new FileInputStream(new File(directoryPath, filename));
            final InputStreamReader r = new InputStreamReader(f);
            final int size = f.available();
            final char[] buf = new char[size];
            r.read(buf, 0, size);
            final String commands = new String(buf);
            getWorld(commands);
            r.close();
        }
        catch (IOException e) {
            System.out.println("Can't read world.");
        }
    }
    
    public static void getWorld(final String commands) {
        if (commands == null) {
            return;
        }
        final StringTokenizer t = new StringTokenizer(commands);
        while (t.hasMoreTokens()) {
            try {
                final String token = t.nextToken();
                if (token.equalsIgnoreCase("streets")) {
                    setSize(Integer.parseInt(t.nextToken()), numberOfAvenues());
                }
                else if (token.equalsIgnoreCase("avenues")) {
                    setSize(numberOfStreets(), Integer.parseInt(t.nextToken()));
                }
                else if (token.equalsIgnoreCase("beepers")) {
                    final int s = Integer.parseInt(t.nextToken());
                    final int a = Integer.parseInt(t.nextToken());
                    final int n = Integer.parseInt(t.nextToken());
                    placeBeepers(s, a, n);
                }
                else if (token.equalsIgnoreCase("urrobot")) {
                    final int s = Integer.parseInt(t.nextToken());
                    final int a = Integer.parseInt(t.nextToken());
                    final Direction d = Direction.select(Integer.parseInt(t.nextToken()));
                    new UrRobot(s, a, d, 0);
                }
                else if (token.equalsIgnoreCase("eastwestwalls")) {
                    final int s = Integer.parseInt(t.nextToken());
                    final int a = Integer.parseInt(t.nextToken());
                    final int n = Integer.parseInt(t.nextToken());
                    placeEWWall(s, a, n - a + 1);
                }
                else {
                    if (!token.equalsIgnoreCase("northsouthwalls")) {
                        continue;
                    }
                    final int a2 = Integer.parseInt(t.nextToken());
                    final int s2 = Integer.parseInt(t.nextToken());
                    final int n = Integer.parseInt(t.nextToken());
                    placeNSWall(s2, a2, n - s2 + 1);
                }
            }
            catch (Exception ex) {}
        }
        if (World.view != null) {
            World.view.repaint();
        }
    }
    
    public static final void resetRobots() {
        final Enumeration<UrRobot> e = World.robots.elements();
        while (e.hasMoreElements()) {
            e.nextElement().deleteObserver(World.myObserver);
        }
        World.robots.setSize(0);
        World.threads.stop();
        if (World.view != null) {
            World.view.resetControl();
        }
        World.threads = new ThreadGroup("RobotThread" + ++World.threadGroupNumber);
        World.threadVector.setSize(0);
    }
    
    public static final void resetEntities() {
        final Enumeration<GridEntity> e = World.entities.elements();
        while (e.hasMoreElements()) {
            e.nextElement().deleteObserver(World.myObserver);
        }
        World.entities.setSize(0);
        World.threads.stop();
        if (World.view != null) {
            World.view.resetControl();
        }
        World.threads = new ThreadGroup("RobotThread" + ++World.threadGroupNumber);
        World.threadVector.setSize(0);
    }
    
    public static final void reset() {
        World.beepers.clear();
        World.ewWalls.clear();
        World.nsWalls.clear();
        World.streets = 10;
        World.avenues = 10;
        final Enumeration<UrRobot> e = World.robots.elements();
        while (e.hasMoreElements()) {
            e.nextElement().deleteObserver(World.myObserver);
        }
        World.robots.setSize(0);
        final Enumeration<GridEntity> r = World.entities.elements();
        while (r.hasMoreElements()) {
            r.nextElement().deleteObserver(World.myObserver);
        }
        World.entities.setSize(0);
        World.threads.stop();
        if (World.view != null) {
            World.view.resetControl();
        }
        World.threads = new ThreadGroup("RobotThread" + ++World.threadGroupNumber);
        World.threadVector.setSize(0);
    }
    
    public static final void setDelay(int d) {
        if (d < 0) {
            d = 0;
        }
        World.delay = d;
    }
    
    public static final int delay() {
        return World.delay;
    }
    
    public static final void resume() {
        World.threads.resume();
    }
    
    public static final void stop() {
        World.threads.suspend();
    }
    
    public static final void setSize(final int numberOfStreets, final int numberOfAvenues) {
        World.streets = ((numberOfStreets > 0) ? numberOfStreets : 10);
        World.avenues = ((numberOfAvenues > 0) ? numberOfAvenues : 10);
        if (World.worldBuilder != null) {
            World.worldBuilder.updateStreetsAvenues(World.streets, World.avenues);
        }
        if (World.view != null) {
            World.view.scaleAllRobotImages();
            World.view.repaint();
        }
    }
    
    public static final void setTrace(final boolean t) {
        World.tracing = t;
    }
    
    public static final void setupThread(final Runnable r) {
        if (World.view == null) {
            World.view = new RobotWorldWindow();
        }
        World.view.enableStop();
        final Thread t = new Thread(World.threads, r);
        World.threadVector.addElement(t);
    }
    
    static void killBuilder() {
        if (World.worldBuilder != null) {
            World.worldBuilder.cleanUp();
        }
    }
    
    static void registerBuilder(final WorldBuilderInterface wb) {
        World.worldBuilder = wb;
    }
    
    public static final void showSpeedControl(final boolean show) {
        if (World.view == null) {
            World.view = new RobotWorldWindow();
        }
        World.view.showControlDialog(show);
    }
    
    public static final void setVisible(final boolean show) {
        if (show && World.view == null) {
            System.out.println("New worldview");
            World.view = new RobotWorldWindow();
        }
        if (World.view != null) {
            World.view.setVisible(show);
        }
        World.isVisible = show;
    }
    
    public static final void setVisible(final boolean show, final int width, final int height) {
        setVisible(false);
        World.view.setSize(width, height);
        setVisible(show);
    }
    
    public static final void setVisible() {
        setVisible(World.isVisible = !World.isVisible);
    }
    
    public static final void replaceCloser(WindowListener w) {
        if (w == null) {
            w = new WindowAdapter() {};
        }
        if (World.view != null) {
            World.view.replaceCloser(w);
        }
    }
    
    public static final void setStreetColor(final Color color) {
        RobotWorldWindow.setStreetColor(color);
    }
    
    public static final void setNeutroniumColor(final Color color) {
        RobotWorldWindow.setWallColor(color);
    }
    
    public static final void setBeeperColor(final Color color) {
        RobotWorldWindow.setBeeperColor(color);
    }
    
    public static final void setWorldColor(final Color color) {
        if (World.view != null) {
            World.view.setBackgroundColor(color);
        }
    }
    
    public static final void repaint() {
        if (World.view != null) {
            World.view.repaint();
        }
    }
    
    static final void startThreads() {
        final Enumeration<Thread> e = World.threadVector.elements();
        while (e.hasMoreElements()) {
            e.nextElement().start();
        }
    }
    
    public static final void makeView() {
        World.view = new RobotWorldWindow();
    }
    
    static class BeeperEnum implements Enumeration
    {
        private Enumeration<IntPair> k;
        private Enumeration<Integer> v;
        
        BeeperEnum() {
            this.k = World.beepers.keys();
            this.v = World.beepers.elements();
        }
        
        public boolean hasMoreElements() {
            return this.k.hasMoreElements();
        }
        
        public Object nextElement() {
            final IntPair p = this.k.nextElement();
            final Integer val = this.v.nextElement();
            return new BeeperCell(p.s, p.a, val);
        }
    }
    
    static class BeeperCell
    {
        private int street;
        private int avenue;
        private int number;
        
        public BeeperCell(final int s, final int a, final int n) {
            this.street = s;
            this.avenue = a;
            this.number = n;
        }
        
        public int street() {
            return this.street;
        }
        
        public int avenue() {
            return this.avenue;
        }
        
        public int number() {
            return this.number;
        }
    }
    
    static class IntPair
    {
        private int s;
        private int a;
        
        IntPair(final int s, final int a) {
            this.s = s;
            this.a = a;
        }
        
        public boolean equals(final Object o) {
            return o instanceof IntPair && (this.s == ((IntPair)o).s && this.a == ((IntPair)o).a);
        }
        
        public String toString() {
            return "<" + this.s + "," + this.a + ">";
        }
        
        public int hashCode() {
            return this.a + 1001 * this.s;
        }
        
        public int street() {
            return this.s;
        }
        
        public int avenue() {
            return this.a;
        }
    }
    
    static class RobotObserver implements Observer
    {
        public synchronized void update(final Observable o, final Object s) {
            if (World.tracing) {
                System.out.println(o);
            }
            if (World.view != null) {
                //System.out.println("Drwn");
                World.view.prepareToDraw((UrRobot.StateObject)s);
            }
        }
    }
    
    static class EntityObserver implements Observer
    {
        public synchronized void update(final Observable o, final Object s) {
            if (World.tracing) {
                System.out.println(o);
            }
            if (World.view != null) {
                System.out.println("Drwn");
                World.view.prepareToDraw((GridEntity.StateObject)s);
            }
        }
    }
}
