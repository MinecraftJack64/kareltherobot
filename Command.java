package kareltherobot;
import java.util.*;

/**
 * Write a description of class Command here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Command
{
    class Cursor{
        int position = 0;
        String seq;
        Cursor(String s){
            seq = s;
        }
        void next(){
            position++;
        }
        char getChar(){
            return seq.charAt(position);
        }
    }
    class Sequence{
        public Sequence(Cursor cursor){
            
        }
    }
    Sequence mainSeq;
    public Command(String seq){
        mainSeq = new Sequence(new Cursor(seq));
    }
    /*
     * Selectors:
     * c - cadet
     * s - squad
     * e - element
     * p - platoon
     * c - company
     * b - battalion
     * r - regiment
     * Action tags:
     * c<thing> - call
     * s<state> - state(default)
     * d<direction> - direction
     * m<distance(default 2)> - move
     * Format examples:
     * sbl+dl sa - Left face
     * @s($s+1)e($e+1) selects cadet one squad and element above
     * @s1e1?sa - Wait for first squad first element to go to attention
     */
}
