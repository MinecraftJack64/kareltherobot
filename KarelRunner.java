package kareltherobot;
// 
// Decompiled by Procyon v0.5.36
// 

 

import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class KarelRunner
{
    private static BufferedReader in;
    private static boolean clearing;
    private static String filename;
    
    static {
        KarelRunner.in = new BufferedReader(new InputStreamReader(System.in));
        KarelRunner.clearing = true;
        KarelRunner.filename = "";
    }
    
    public static void main(final String[] args) {
        String className = null;
        String worldOption = "";
        if (args.length < 1) {
            className = getName();
        }
        else {
            className = args[0];
            for (int i = 1; i < args.length; ++i) {
                if ((args[i].charAt(0) == '/' || args[i].charAt(0) == '-') && args[i].length() > 1) {
                    switch (Character.toUpperCase(args[i].charAt(1))) {
                        case 'B': {
                            worldOption = "b";
                            break;
                        }
                        case 'W': {
                            if (args.length > i + 1) {
                                KarelRunner.filename = args[i + 1];
                                ++i;
                                break;
                            }
                            System.out.println("No world filename");
                            System.exit(1);
                            break;
                        }
                        default: {
                            System.out.println("Invalid option " + args[i] + ": Only BW allowed\n");
                            System.exit(1);
                            break;
                        }
                    }
                }
            }
        }
        try {
            if (worldOption.equalsIgnoreCase("b")) {
                final WorldBuilder worldBuilder = new WorldBuilder(true);
            }
            final Class robotClass = Class.forName(className);
            final RobotTask robotInstance = (RobotTask)robotClass.newInstance();
            for (String answer = prompt(); ok(answer); answer = prompt()) {
                if (KarelRunner.clearing) {
                    World.reset();
                }
                if (KarelRunner.filename != null && KarelRunner.filename != "") {
                    World.readWorld(KarelRunner.filename);
                }
                World.setVisible(true);
                robotInstance.task();
            }
            System.exit(0);
        }
        catch (ClassCastException classcast) {
            System.out.println("Your class does not implement RobotTester.");
            System.exit(3);
        }
        catch (ClassNotFoundException noClass) {
            System.out.println("No such class.");
            System.exit(2);
        }
        catch (IllegalAccessException illegalClass) {
            System.out.println("Can't access that class.");
            System.exit(4);
        }
        catch (InstantiationException illegalClass2) {
            System.out.println("Can't instantiate that class.");
            System.exit(5);
        }
        catch (Throwable other) {
            System.out.println("Unexplained Error.");
            System.exit(6);
        }
    }
    
    private static String getName() {
        String result = null;
        System.out.print("Name of class to test: ");
        try {
            result = KarelRunner.in.readLine();
        }
        catch (IOException e) {
            System.out.println("No such class");
            System.exit(1);
        }
        return result;
    }
    
    private static String getWorldBuilderOption() {
        String result = "x";
        System.out.print("Would you like the world builder? Y/n: ");
        try {
            result = KarelRunner.in.readLine();
            if (ok(result)) {
                return "b";
            }
        }
        catch (IOException e) {
            System.out.println("Error");
            System.exit(1);
        }
        return result;
    }
    
    private static boolean ok(final String answer) {
        if (answer.length() > 0 && (answer.charAt(0) == 'n' || answer.charAt(0) == 'N')) {
            return false;
        }
        KarelRunner.clearing = (answer.length() > 0 && (answer.charAt(0) == 'c' || answer.charAt(0) == 'C'));
        final boolean worldfile = answer.length() > 0 && (answer.charAt(0) == 'w' || answer.charAt(0) == 'W');
        if (worldfile) {
            KarelRunner.filename = answer.substring(1).trim();
        }
        return true;
    }
    
    private static String prompt() {
        System.out.print("Run robot task? Y/n/c/w ");
        String result = "no";
        try {
            result = KarelRunner.in.readLine();
            if (result == null) {}
        }
        catch (Exception ex) {}
        return result.trim();
    }
}
