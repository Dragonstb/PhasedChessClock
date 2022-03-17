/*
 * PhasedChessClock - GUI
 * Copyright (C) 2022 Dragonstb
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package additionalTesting;

import java.awt.Font;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

import chessclock.MasterFrame;
import chessclock.PlayerData;

/**
 * @author Dragonstb
 * @since 1.0;
 */
public class TestUtils {

    /* Creates a master frame where some actions are replaced by console output.
     */
    public static MasterFrame makeMasterFrame() {
        MasterFrame frame = new MasterFrame() {

            @Override
            public void goToSettingsPanel() {
                System.out.println("go to settings panel");
            }

            @Override
            public void goToClockPanel() {
                System.out.println("go to clock panel");
            }

            @Override
            public void acceptSettings(PlayerData dataLeft, PlayerData dataRight, boolean isLeftWhite, boolean timeRunning) {
                System.out.println("accepting settings");
            }

            @Override
            public void exitApplication() {
                System.out.println("exiting");
            }

        };

        return frame;
    }

    public static Method getDeclaredMethodAccessible(Object obj, String methodName, Class<?>... parms)
            throws NoSuchMethodException, SecurityException {
        return getDeclaredMethodAccessible(obj.getClass(), methodName, parms);
    }

    public static Method getDeclaredMethodAccessible(Class<?> cls, String methodName)
            throws NoSuchMethodException, SecurityException {
        return getDeclaredMethodAccessible(cls, methodName, (Class<?>[]) null);
    }

    public static Method getDeclaredMethodAccessible(Class<?> cls, String methodName, Class<?>... parms)
            throws NoSuchMethodException, SecurityException {
        Method method = cls.getDeclaredMethod(methodName, parms);
        method.setAccessible(true);
        return method;
    }

    public static Method getMethodAccessible(Object obj, String methodName, Class<?>... parms)
            throws NoSuchMethodException, SecurityException {
        Method method = obj.getClass().getMethod(methodName, parms);
        method.setAccessible(true);
        return method;
    }

    public static Field getFieldAccessible(Object obj, String fieldName)
            throws NoSuchFieldException, SecurityException {
        // Field field = obj.getClass().getDeclaredField(fieldName);
        // field.setAccessible(true);
        // return field;
        return getFieldAccessible(obj.getClass(), fieldName);
    }

    public static Field getFieldAccessible(Class<?> cls, String fieldName)
            throws NoSuchFieldException, SecurityException {
        Field field = cls.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public static Constructor<?> getConstructorAccessible(Class<?> cls, Class<?>... parms)
            throws NoSuchMethodException, SecurityException {
        Constructor<?> constructor = cls.getDeclaredConstructor(parms);
        constructor.setAccessible(true);
        return constructor;
    }

    public static Class<?> getAccessibleClass(String name) throws ClassNotFoundException {
        Class<?> cls = Class.forName(name);
        return cls;
    }

    public static Font getRandomFont() {
        String[] fonts = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Random rand = new Random();
        String chosenFont = fonts[rand.nextInt(fonts.length)];
        int size = 10 + rand.nextInt(20);
        Font font = new Font(chosenFont, Font.PLAIN, size);
        return font;
    }

}
