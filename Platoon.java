package kareltherobot;

import java.util.*;
/**
 * Write a description of class Platoon here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Platoon extends Formation
{
    String id;
    ArrayList<Squad> squads = new ArrayList<Squad>();
    Cadet guide;
    public Platoon(Cadet pc, Cadet guide){
        this.guide = guide;
        this.cmdr = pc;
    }
    public Platoon(String id, Cadet pc, Cadet guide){
        this(pc, guide);
        this.id = id;
    }
    public void call(Command cmd){
        for(Squad s: squads){
            s.call(cmd);
        }
        guide.call(cmd);
    }
    public Squad getFirstShortestSquad(){
        Squad shortest = squads.get(0);
        for(Squad s:squads){
            if(s.length()<shortest.length()){
                shortest = s;
            }
        }
        return shortest;
    }
    public void fallIn(Cadet c){
        Squad squad = getFirstShortestSquad();
        squad.fallIn(c);
    }
    /*
     * Variables
     * s - current squad
     * S - number of squads
     * e - current element
     * E - number of elements
     * ED - elemental distance(ex. 1 by default, 0 after close march)
     * SD - Squad distance(ex. 1 by default, 2 after open ranks)
     * D - platoon direction
     * O - platoon orientation
     * Selectors
     * s<number> - get squad by number
     * s<number>e<number> - get element by numbers
     * e<number> - get element of current squad by number
     */
}
