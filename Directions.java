package kareltherobot;

// 
// Decompiled by Procyon v0.5.36
// 

 

import java.util.Hashtable;

public interface Directions
{
    public static final int infinity = -1;
    public static final Direction North = Direction.North;
    public static final Direction East = Direction.East;
    public static final Direction South = Direction.South;
    public static final Direction West = Direction.West;
    public static final char[] directionchars = {'e', 's', 'w', 'n'};
    
    public static class Direction
    {
        static final int NorthVal = 3;
        static final int WestVal = 2;
        static final int SouthVal = 1;
        static final int EastVal = 0;
        private int where;
        private static Hashtable<Integer, Direction> repository;
        private static final Direction North;
        private static final Direction West;
        private static final Direction South;
        private static final Direction East;
        public static final int s[] = {0, -1, 0, 1};
        public static final int a[] = {1, 0, -1, 0};
        
        static {
            Direction.repository = new Hashtable(4);
            North = new Direction(3);
            West = new Direction(2);
            South = new Direction(1);
            East = new Direction(0);
        }
        
        private Direction(final int which) {
            this.where = 0;
            this.where = which;
            Direction.repository.put(new Integer(which), this);
        }
        
        Direction rotate(final int rotateBy) {
            int newDirection = (this.where + rotateBy) % 4;
            if (newDirection < 0) {
                newDirection += 4;
            }
            return Direction.repository.get(new Integer(newDirection));
        }
        
        int points() {
            return this.where;
        }
        
        public String toString() {
            String result = "Error";
            switch (this.where) {
                case 3: {
                    result = "North";
                    break;
                }
                case 2: {
                    result = "West";
                    break;
                }
                case 1: {
                    result = "South";
                    break;
                }
                case 0: {
                    result = "East";
                    break;
                }
            }
            return result;
        }
        
        static Direction select(final int which) {
            return Direction.repository.get(new Integer(which % 4));
        }
        
        int getStreetUnit(){
            return s[this.points()];
        }
        int getAvenueUnit(){
            return a[this.points()];
        }
    }
}
