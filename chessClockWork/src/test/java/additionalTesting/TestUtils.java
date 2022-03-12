/*
 * PhasedChessClock - clockwork (short "the phased clockwork" in the following)
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

 * Linking the phased clockwork statically or dynamically with other modules
 * is making a combined work based on the phased clockwork. Thus, the terms
 * and conditions of the GNU General Public License cover the whole combination.

 * As a special exception, the copyright holders of the phased clockwork give
 * you permission to combine the phased clockwork with free software programs
 * or libraries that are released under the GNU LGPL or any other licence and
 * with independent modules that communicate with the phased clockwork solely
 * through the public interface of the phased clockwork. You may copy and
 * distribute such a system following the terms of the GNU GPL for the phased
 * clockwork and the licenses of the other code concerned, provided that you
 * include the source code of that other code when and as the GNU GPL requires
 * distribution of source code and provided that you do not modify the public
 * interface of the phased clockwork.

 * Note that people who make modified versions of the phased clockwork are not
 * obligated to grant this special exception for their modified versions; it is
 * their choice whether to do so. The GNU General Public License gives
 * permission to release a modified version without this exception; this
 * exception also makes it possible to release a modified version which carries
 * forward this exception. If you modify the public interface of the phased
 * clockwork, this exception does not apply to your modified version of the
 * phased clockwork, and you must remove this exception when you distribute your
 * modified version.

 * This exception is an additional permission under section 7 of the GNU General Public License, version 3 (“GPLv3”)
 */
package additionalTesting;

import java.awt.Font;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

/**
 * @author Dragonstb
 * @since 1.0;
 */
public class TestUtils {

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
