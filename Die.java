// 
// Decompiled by Procyon v0.5.36
// 
package kareltherobot;
 

import java.util.Random;

public class Die
{
    private int value;
    private Random r;
    
    public Die(final int faces) {
        this.r = new Random();
        this.value = faces;
    }
    
    public int roll() {
        return Math.abs(this.r.nextInt()) % this.value + 1;
    }
    
    public int faces() {
        return this.value;
    }
}
