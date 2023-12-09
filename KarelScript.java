package kareltherobot;


public class KarelScript
{//KarelScript By Jack Liu 2023 v 0.5
    public static void pletter(String bot, char lt){
        switch(lt){
            case 'l':
                System.out.println(bot+".turnLeft();");
                break;
            case 't':
                System.out.println(bot+".turnLeft();\n"+bot+".turnLeft();");
                break;
            case 'r':
                System.out.println(bot+".turnLeft();\n"+bot+".turnLeft();\n"+bot+".turnLeft();");
                break;
            case 'm':
                System.out.println(bot+".move();");
                break;
            case 'p':
                System.out.println(bot+".putBeeper();");
                break;
            case 'u':
                System.out.println(bot+".pickBeeper();");
                break;
            case 'x':
                System.out.println(bot+".turnOff();");
                break;
            case ' ':
                System.out.println(bot+".skip();");
                break;
        }
    }
    public static void runletter(IUrRobot bot, char lt){
        switch(lt){
            case 'l':
                bot.turnLeft();
                break;
            case 't':
                bot.turnLeft();
                bot.turnLeft();
                break;
            case 'r':
                bot.turnLeft();
                bot.turnLeft();
                bot.turnLeft();
                break;
            case 'm':
                bot.move();
                break;
            case 'p':
                bot.putBeeper();
                break;
            case 'u':
                bot.pickBeeper();
                break;
            case '\'':
                bot.startCombo();
                break;
            case ',':
                bot.stopCombo();
                break;
            case 'x':
                bot.turnOff();
                break;
            case ' ':
                bot.skip();
                break;
        }
    }
    public static int parse(IUrRobot bot, String command, int spos, int times, boolean printmode){
        int layer = 0;
        int endpos = 0;
        for(int q = 0; q < times; q++){
            int startpos = spos;
            while(startpos<command.length()){
                char tc = command.charAt(startpos);
                if(Character.isDigit(tc)){
                    String tnums = "";
                    do{
                        tnums+=tc;
                        startpos++;
                        tc = command.charAt(startpos);
                    }while(Character.isDigit(tc));
                    int tnum = Integer.parseInt(tnums);
                    if(Character.isLetter(tc)){
                        for(int x = 0; x < tnum; x++){
                            if(printmode)
                                pletter("karel",tc);
                            else
                                runletter(bot, tc);
                        }
                        startpos++;
                    }else if(tc=='('){
                        startpos = parse(bot, command, startpos+1, tnum, printmode);
                    }
                }else if(tc==')'){
                    endpos = startpos;
                    break;
                }else{
                    if(printmode)
                        pletter("karel",tc);
                    else
                        runletter(bot, tc);
                    startpos++;
                }
            }
            endpos = startpos;
        }
        return endpos+1;
    }
    public static void run(IUrRobot bot, String command){
        //String[] cmds = command.split("|");
        //for(String cmd:cmds){
            String cmd = command;
            parse(bot,cmd,0, 1, false);
        //}
    }
    public static void print(IUrRobot bot, String command){
        //String[] cmds = command.split("|");
        //for(String cmd:cmds){
            String cmd = command;
            parse(bot,cmd,0, 1, true);
        //}
    }
}