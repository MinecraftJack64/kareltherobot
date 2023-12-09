package kareltherobot;


// 
// Decompiled by Procyon v0.5.36
// 

 

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.lang.reflect.Constructor;
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.awt.Button;
import java.lang.reflect.Method;
import java.awt.LayoutManager;
import java.awt.GridLayout;
import java.util.Vector;
import java.awt.Component;
import java.awt.Label;
import java.awt.event.WindowListener;
import java.awt.Color;
import java.awt.Frame;

public class AutoRemote extends Frame implements Directions
{
    private static int id;
    private static int delta;
    private UrRobot karel;
    Class[] argTypes;
    static /* synthetic */ Class class$1;
    static /* synthetic */ Class class$2;
    static /* synthetic */ Class class$3;
    static /* synthetic */ Class class$4;
    static /* synthetic */ Class class$5;
    
    static {
        AutoRemote.id = 0;
        AutoRemote.delta = 10;
    }
    
    public AutoRemote() {
        this("kareltherobot.UrRobot", 1, 1, AutoRemote.North, 0, null);
    }
    
    public AutoRemote(final String className) {
        this(className, 1, 1, AutoRemote.North, 0, null);
    }
    
    public AutoRemote(final String className, final int street, final int avenue, final Direction direction, final int beepers) {
        this(className, street, avenue, direction, beepers, null);
    }
    
    public AutoRemote(final String className, final int street, final int avenue, final Direction direction, final int beepers, final Color color) {
        super(String.valueOf(className) + " " + getID() + " control");
        this.karel = null;
        final Class[] argTypes = { Integer.TYPE, Integer.TYPE, null, null, null };
        final int n = 2;
        Class class$1;
        if ((class$1 = AutoRemote.class$1) == null) {
            try {
                class$1 = (AutoRemote.class$1 = Class.forName("kareltherobot.Directions$Direction"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        argTypes[n] = class$1;
        argTypes[3] = Integer.TYPE;
        final int n2 = 4;
        Class class$2;
        if ((class$2 = AutoRemote.class$2) == null) {
            try {
                class$2 = (AutoRemote.class$2 = Class.forName("java.awt.Color"));
            }
            catch (ClassNotFoundException ex2) {
                throw new NoClassDefFoundError(ex2.getMessage());
            }
        }
        argTypes[n2] = class$2;
        this.argTypes = argTypes;
        try {
            this.addWindowListener(new Hider());
            this.setSize(340, 150);
            this.setLocation(560 + AutoRemote.delta * (AutoRemote.id - 1), 100 + AutoRemote.delta * (AutoRemote.id - 1));
            if (color != null) {
                this.setBackground(color);
            }
            final Class robotClass = Class.forName(className);
            Class class$3;
            if ((class$3 = AutoRemote.class$3) == null) {
                try {
                    class$3 = (AutoRemote.class$3 = Class.forName("kareltherobot.UrRobot"));
                }
                catch (ClassNotFoundException ex3) {
                    throw new NoClassDefFoundError(ex3.getMessage());
                }
            }
            if (!class$3.isAssignableFrom(robotClass)) {
                this.add(new Label(String.valueOf(className) + " is not a robot class."));
                return;
            }
            final Constructor ctor = robotClass.getDeclaredConstructor((Class[])this.argTypes);
            final Object[] values = { new Integer(street), new Integer(avenue), direction, new Integer(beepers), color };
            this.karel = (UrRobot)ctor.newInstance(values);
            final Method[] allMethods = robotClass.getMethods();
            final Vector usefulMethods = new Vector();
            for (int i = 0; i < allMethods.length; ++i) {
                if (isValid(allMethods[i])) {
                    usefulMethods.addElement(allMethods[i]);
                }
            }
            final int numMethods = usefulMethods.size();
            this.setSize(340, 30 * numMethods);
            this.setLayout(new GridLayout(numMethods, 2));
            final Enumeration<Method> enum1 = usefulMethods.elements();
            while (enum1.hasMoreElements()) {
                final Method aMethod = enum1.nextElement();
                final Button aButton = new Button(aMethod.getName());
                this.add(aButton);
                if (isProc(aMethod)) {
                    aButton.addActionListener(new ProcListener(aMethod));
                    this.add(new Label("void"));
                }
                else {
                    final TextField aField = new TextField(20);
                    aButton.addActionListener(new FuncListener(aMethod, aField));
                    this.add(aField);
                }
            }
            this.setVisible(true);
        }
        catch (Exception e) {
            this.add(new Label(String.valueOf(className) + " does not seem to be the name of a robot class." + e.toString()));
            this.setVisible(true);
        }
    }
    
    private static boolean isValid(final Method m) {
        final Class<?> declaringClass;
        final Class declaring = declaringClass = m.getDeclaringClass();
        Class class$4;
        if ((class$4 = AutoRemote.class$4) == null) {
            try {
                class$4 = (AutoRemote.class$4 = Class.forName("java.lang.Object"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        if (declaringClass.equals(class$4)) {
            return false;
        }
        final Class clazz = declaring;
        Class class$5;
        if ((class$5 = AutoRemote.class$5) == null) {
            try {
                class$5 = (AutoRemote.class$5 = Class.forName("java.util.Observable"));
            }
            catch (ClassNotFoundException ex2) {
                throw new NoClassDefFoundError(ex2.getMessage());
            }
        }
        if (clazz.equals(class$5)) {
            return false;
        }
        if (Modifier.isStatic(m.getModifiers())) {
            return false;
        }
        final Class clazz2 = declaring;
        Class class$6;
        if ((class$6 = AutoRemote.class$3) == null) {
            try {
                class$6 = (AutoRemote.class$3 = Class.forName("kareltherobot.UrRobot"));
            }
            catch (ClassNotFoundException ex3) {
                throw new NoClassDefFoundError(ex3.getMessage());
            }
        }
        if (clazz2.equals(class$6)) {
            final String name = m.getName();
            if (!name.equals("move") && !name.equals("turnLeft") && !name.equals("turnOff") && !name.equals("putBeeper") && !name.equals("pickBeeper") && !name.equals("toString")) {
                return false;
            }
        }
        return m.getParameterTypes().length == 0;
    }
    
    private static boolean isProc(final Method m) {
        return m.getReturnType().equals(Void.TYPE);
    }
    
    private static int getID() {
        return AutoRemote.id++;
    }
    
    private class Hider extends WindowAdapter
    {
        Hider() {
        }
        
        public void windowClosing(final WindowEvent e) {
            AutoRemote.this.setVisible(false);
        }
    }
    
    private class ProcListener implements ActionListener
    {
        Method method;
        
        public ProcListener(final Method method) {
            this.method = null;
            this.method = method;
        }
        
        public void actionPerformed(final ActionEvent e) {
            try {
                this.method.invoke(AutoRemote.this.karel, (Object[])null);
            }
            catch (Exception ex) {}
        }
    }
    
    private class FuncListener implements ActionListener
    {
        Method method;
        TextField field;
        
        public FuncListener(final Method method, final TextField field) {
            this.method = null;
            this.field = null;
            this.field = field;
            this.method = method;
        }
        
        public void actionPerformed(final ActionEvent e) {
            try {
                final Object result = this.method.invoke(AutoRemote.this.karel, (Object[])null);
                this.field.setText(result.toString());
            }
            catch (Exception ex) {}
        }
    }
}
