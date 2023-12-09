package kareltherobot;

import java.awt.*;
import java.util.*;


class Cycle{
    Image[] frames;
    Image[] sprites;
    String prefix = "";
    public Cycle(String dat, int op){
        load(new String[]{dat}, op);
    }
    public void load(String[] urls, int op){
        MediaTracker track = new MediaTracker(World.view());
        sprites = new Image[urls.length];
        frames = new Image[urls.length];
        for(int i = 0; i < urls.length; i++){
            String curl = urls[i];
            if(curl.contains("[d]")){
                curl = curl.replace("[d]", ""+Directions.directionchars[op-1]);
                System.out.println(curl+" "+Directions.directionchars[op-1]);
            }
            System.out.println("Loading image "+prefix+curl);
            sprites[i] = World.view().getImage(prefix+curl, op);
            frames[i] = sprites[i];
        }
        for(int i = 0; i < sprites.length; i++){
            track.addImage(sprites[i], i);
        }
        try {
            track.waitForAll();
        }
        catch (InterruptedException ex15) {}
        RobotWorldWindow.subscribeScaler(sprites, frames);
    }
    public Image getScaledImageAt(int frame){
        return frames[frame];
    }
    public String toString(){
        String r = "[";
        for(Image i: frames){
            r+=i+",";
        }
        return r+"]";
    }
}
class State implements Directions{
    Cycle[] cycles = new Cycle[4];
    char mode;
    boolean auto;
    Directions.Direction lastd;
    public State(String[] dat){
        mode = dat[1].charAt(0);
        if(dat[1].length()>1){
            auto = dat[1].contains("a");
        }
        switch(mode){
            case 'c'://cardinal
                if(auto){//Rotate images automatically
                    for(int x = 0; x < 4; x++){//From east to north
                        cycles[x] = new Cycle(dat[2], x+1);
                    }
                }
                else{//Custom rotation scheme
                }
            break;
            case 'n'://No rotate
                cycles[0] = new Cycle(dat[0], 0);
            break;
            case 'b'://bidirectional
            break;
            default:
            break;
        }
    }
    public Cycle getCycle(Direction d){
        switch(mode){
            case 'c'://cardinal
                return cycles[d.points()];
            case 'n'://No rotate
                return cycles[0];
            case 'b'://bidirectional
                return cycles[d.points()<2?(d.points()):(d.points()-2)];
            default:
                return cycles[0];
        }
    }
    public String toString(){
        String r = mode+"(";
        for(Cycle c: cycles){
            r+=c+", ";
        }
        return r+")";
    }
}
/**
 * Write a description of class Painter here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Painter implements Directions
{
    HashMap<String, State> states = new HashMap<String, State>();
    public void load(String[] data){
        for(String state: data){
            String dat[] = state.split(" ");
            states.put(dat[0], new State(dat));
        }
        //Default example: new String[]{"idle ca karele.png"};
        //Advanced example: new String[]{"idle b man[0-1].png 4", "walk c man-walk[0-3].png 1", "attack c man-attack[0-5].png 1"};
    }
    public String toString(){
        String r = "";
        for(Map.Entry<String,State> state : states.entrySet()){
            r+=state.getKey()+": "+state.getValue()+", ";
        }
        return r;
    }
    public Image getScaledImage(String state, Direction d, int frame){
        return states.get(state).getCycle(d).getScaledImageAt(frame);
    }
    public Painter(String[] data){
        System.out.println("Beginning the loading of Painter");
        load(data);
    }
    public static void main(String[]args){
        Painter painter = new Painter(new String[]{"idle ca karele.png"});
        System.out.println(painter);
        System.out.println(painter.getScaledImage("idle", Directions.West, 0));
    }
}

/*
 *Robot
 *karel ca 1
 *karelOff ca 1
 *endl
 *Boar
 *walk b 4
 *attack b 2
 *sleep b 2
 *endl
 */
