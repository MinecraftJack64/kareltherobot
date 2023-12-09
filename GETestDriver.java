package kareltherobot;


/**
 * Write a description of class test here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class GETestDriver extends Driver
{
    public static void main(String[]args){
        /*GridEntity test = new GridEntity(5,5,North,0);
        GridEntity te2t = new GridEntity(7,1,East,0);*/
        GridEntity t3st = new GridEntity(3,3,West);
        Robot r = new Robot(1,2,North,0);
        r.move();r.move();r.move();
        /*System.out.println("About to call manual scaling");
        //World.view().addListener();
        //World.view().repaint();
        //World.view().scaleAllRobotImages();
        //KarelScript.run(test, "mm'rmm,mmmmmmm");
        Robot special = new Robot(2, 2, North, 0){
            public boolean frontIsClear(){
                return false;
            }
        };
        System.out.println(special.frontIsClear());*/
        t3st.skip();
        for(int i = 1; i <= 9; i++){
            t3st.goTo(i,i);
        }
        t3st.goTo(3,5);
        t3st.face(South);
        t3st.changeState("idle");
        t3st.skip();//Pauses after this
        t3st.startCombo();//Will not pause after
        t3st.goTo(1,1);
        t3st.face(East);
        t3st.stopCombo();//Does not pause after this
        t3st.move(2);//Will pause as normal
        t3st.delete();
    }
    static{
        World.setDelay(50);
    }
}