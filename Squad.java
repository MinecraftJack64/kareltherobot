package kareltherobot;

import java.util.*;
/**
 * Write a description of class Squad here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Squad extends Formation
{
    ArrayList<Cadet> elements = new ArrayList<Cadet>();

    public void call(Command cmd){
        for(Cadet c: elements){
            c.call(cmd);
        }
    }
    public int length(){
        return elements.size();
    }
    public void fallIn(Cadet c){
        elements.add(c);
        if(elements.size()==1){
            cmdr = elements.get(0);
            cmdr.own(this);
        }
    }
    public void fallOut(int element){
        if(element>elements.size()){
            return;
        }
        if(element==1){
            elements.get(0).disown();
        }
        elements.remove(element-1);
        if(element==1&&elements.size()>0){
            elements.get(0).own(this);
        }
    }
}
