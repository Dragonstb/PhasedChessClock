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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chessclock.Constants;
import chessclock.MasterFrame;
import chessclock.clockpanel.ClockPanel;
import chessclock.settingspanel.SettingsPanel;
import localization.EnglishLocalization;
import localization.Loc;

/** For DIY (debug it yourself).
 * @author Dragonstb
 * @since 1.0;
 */
public class TestPanel {

    public static Font getRandomFont(int size) {
        String[] fonts = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Random rand = new Random();
        String chosenFont = fonts[rand.nextInt(fonts.length)];
        Font font = new Font(chosenFont, Font.PLAIN, 12);
        System.out.println("Chosen: " + font.getName() + " " + font.getSize2D());
        return font;
    }

    public static void main(String[] args) {
        System.out.println("Class path: " + System.getProperty("java.class.path"));
        switch (6) {
            case 0 ->
                testSettingsPanel();
            case 1 ->
                testToolBarPanel();
            case 2 ->
                testClockPanel();
            case 3 ->
                testIdentityPanel();
            case 4 ->
                testTCPPanel();
            case 5 ->
                testPlayerDataPanel();
            case 6 ->
                testAboutPanel();
        }

    }

    /**
     * @since 1.0;
     */
    private static void testSettingsPanel() {
        System.out.println("loaded: " + Constants.BLACK_ICON.getImageLoadStatus());
        int width = 400, height = 300;
        Loc loc = new EnglishLocalization();
        MasterFrame frame = TestUtils.makeMasterFrame();
        frame.setLoc(loc);
        frame.getContentPane().setBackground(new Color(50, 0, 0));

        Font font = getRandomFont(12);

        Optional<SettingsPanel> optSP = SettingsPanel.makeNew(frame, width, height);
        if (optSP.isEmpty()) {
            System.out.println("ERROR!");
            System.out.println("Couldn\'t initialize settings panel");
            System.exit(2);
            return;
        }
        SettingsPanel panel = optSP.get();

        panel.setPreferredSize(new Dimension(width, height));
        System.out.println("size 2: " + panel.getWidth() + ", " + panel.getHeight());

        // frame.add(panel);
        frame.addSettingsPanel(panel);

        System.out.println("size 3: " + panel.getWidth() + ", " + panel.getHeight());
        // closing via the window listener operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);

        frame.setLocation(20, 20);
        // frame.validate();
        frame.pack();
        System.out.println("size 4: " + panel.getWidth() + ", " + panel.getHeight());
        panel.updateLocalization(loc);
        panel.setFonts(font);
        panel.rearrange(width, height);
        panel.setVisible(true);
        // panel.rearrange();
        frame.pack();
        System.out.println("size 5: " + panel.getWidth() + ", " + panel.getHeight());
        frame.setVisible(true);
    }

    private static void testToolBarPanel() {
        int width = 400, height = 300;
        Loc loc = new EnglishLocalization();
        MasterFrame frame = TestUtils.makeMasterFrame();
        frame.setLoc(loc);
        frame.getContentPane().setBackground(new Color(50, 0, 0));

        try {
            Class<?> toolBarPanelClass = TestUtils.getAccessibleClass("chessclock.clockpanel.ToolBarPanel");
            Constructor<?> TBPCon = TestUtils.getConstructorAccessible(toolBarPanelClass, ClockPanel.class);
            Object panel = TBPCon.newInstance((ClockPanel) null);
            Method updateLocalization = TestUtils.getDeclaredMethodAccessible(panel, "updateLocalization", Loc.class);
            Method rearrange = TestUtils.getDeclaredMethodAccessible(panel, "rearrange", int.class);

            JPanel jpanel = (JPanel) panel;

            updateLocalization.invoke(panel, loc);
            frame.add(jpanel);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setLocation(20, 20);

            System.out.println("size 4: " + jpanel.getWidth() + ", " + jpanel.getHeight()); // should be (0, 0)
            rearrange.invoke(panel, width);
            jpanel.setVisible(true);

            frame.validate();
            frame.pack();
            System.out.println("size 5: " + jpanel.getWidth() + ", " + jpanel.getHeight());
            frame.setVisible(true);
        } catch (Exception e) {
            System.out.println("OUCH!");
            e.printStackTrace();
        }
    }

    private static void testClockPanel() {

        JLabel label = new JLabel("Hello :)");
        Font font = label.getFont();

        int width = 400, height = 300;
        Loc loc = new EnglishLocalization();
        MasterFrame frame = TestUtils.makeMasterFrame();
        frame.setLoc(loc);
        frame.getContentPane().setBackground(new Color(50, 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocation(20, 20);

        Optional<ClockPanel> optPanel = ClockPanel.makeNew(frame, width, height);
        if (optPanel.isEmpty()) {
            System.out.println("ARGH!");
            System.exit(3);
            return;
        }
        ClockPanel panel = optPanel.get();
        panel.updateLocalization(loc);
        panel.setFonts(font);

        frame.add(panel);
        frame.addClockPanel(panel);
        panel.setVisible(true);

        panel.rearrange(width, height);

        frame.validate();
        frame.pack();
        System.out.println("size 5: " + panel.getWidth() + ", " + panel.getHeight());
        frame.setVisible(true);
    }

    private static void testIdentityPanel() {

        int width = 500, height = 300;
        Loc loc = new EnglishLocalization();
        MasterFrame frame = TestUtils.makeMasterFrame();
        frame.setLoc(loc);
        frame.getContentPane().setBackground(new Color(50, 0, 0));

        Font referenceFont = new JLabel().getFont();
        boolean left = true;

        try {
            Class<?> cls = TestUtils.getAccessibleClass("chessclock.clockpanel.IdentityPanel");
            Constructor<?> con = TestUtils.getConstructorAccessible(cls, boolean.class);
            Object panel = con.newInstance(left);
            Method setFonts = TestUtils.getDeclaredMethodAccessible(panel, "setFonts", Font.class);
            Method updateLocalization = TestUtils.getDeclaredMethodAccessible(panel, "updateLocalization", Loc.class);
            Method rearrange = TestUtils.getDeclaredMethodAccessible(panel, "rearrange", int.class);

            JPanel jpanel = (JPanel) panel;

            // panel.setFonts(referenceFont);
            // panel.updateLocalization(loc);
            setFonts.invoke(panel, referenceFont);
            updateLocalization.invoke(panel, loc);
            jpanel.setPreferredSize(new Dimension(width, height));
            frame.add(jpanel);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setLocation(20, 20);

            // panel.rearrange(width);
            rearrange.invoke(panel, width);
            jpanel.setVisible(true);

            frame.validate();
            frame.pack();
            System.out.println("size 5: " + jpanel.getWidth() + ", " + jpanel.getHeight());
            frame.setVisible(true);
        } catch (Exception e) {
            System.out.println("OUUU");
            e.printStackTrace();
            System.exit(5);
        }
    }

    private static void testTCPPanel() {

        try {
            int width = 600, height = 300;
            Loc loc = new EnglishLocalization();
            MasterFrame frame = TestUtils.makeMasterFrame();
            frame.setLoc(loc);
            frame.getContentPane().setBackground(new Color(50, 0, 0));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setLocation(20, 20);

            int smallSpacing = 3;

            Class<?> cls = TestUtils.getAccessibleClass("chessclock.gamesettings.TCPPanel");
            Constructor<?> con = TestUtils.getConstructorAccessible(cls, MasterFrame.class);
            Object panel = con.newInstance(frame);
            Method setFonts = TestUtils.getDeclaredMethodAccessible(panel, "setFonts", Font.class);
            Method updateLocalization = TestUtils.getDeclaredMethodAccessible(panel, "updateLocalization", Loc.class);
            Method rearrange = TestUtils.getDeclaredMethodAccessible(panel, "rearrange", int.class, int.class);

            JPanel jpanel = (JPanel) panel;

            frame.add(jpanel);

            Font font = new JLabel().getFont();

            setFonts.invoke(panel, font);
            updateLocalization.invoke(panel, loc);
            rearrange.invoke(panel, width, smallSpacing);
            jpanel.setVisible(true);
            frame.pack();
            System.out.println("size 5: " + jpanel.getWidth() + ", " + jpanel.getHeight());
            frame.setVisible(true);
        } catch (Exception e) {
            System.out.println("OH NOOOOO!");
            e.printStackTrace();
            System.exit(6);
        }
    }

    private static void testPlayerDataPanel() {
        try {
            int width = 600, height = 300;
            Loc loc = new EnglishLocalization();
            MasterFrame frame = TestUtils.makeMasterFrame();
            frame.setLoc(loc);
            frame.getContentPane().setBackground(new Color(50, 0, 0));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setLocation(20, 20);

            Class<?> cls = TestUtils.getAccessibleClass("chessclock.gamesettings.PlayerDataPanel");
            Constructor<?> con = TestUtils.getConstructorAccessible(cls, MasterFrame.class);
            Object panel = con.newInstance(frame);
            Method setFonts = TestUtils.getDeclaredMethodAccessible(panel, "setFonts", Font.class);
            Method updateLocalization = TestUtils.getDeclaredMethodAccessible(panel, "updateLocalization", Loc.class);
            Method rearrange = TestUtils.getDeclaredMethodAccessible(panel, "rearrange", int.class);

            JPanel jpanel = (JPanel) panel;

            frame.add(jpanel);

            Font font = new JLabel().getFont();

            setFonts.invoke(panel, font);
            updateLocalization.invoke(panel, loc);
            rearrange.invoke(panel, width);

            jpanel.setVisible(true);
            frame.pack();
            System.out.println("size 5: " + jpanel.getWidth() + ", " + jpanel.getHeight());
            frame.setVisible(true);
        } catch (Exception e) {
            System.out.println("OWWW");
            e.printStackTrace();
            System.exit(7);
        }
    }

    private static void testAboutPanel() {
        try {
            int width = 600, height = 300;
            Loc loc = new EnglishLocalization();
            MasterFrame frame = TestUtils.makeMasterFrame();
            frame.setLoc(loc);
            frame.getContentPane().setBackground(new Color(50, 0, 0));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setLocation(20, 20);

            Class<?> cls = TestUtils.getAccessibleClass("chessclock.about.AboutPanel");
            Constructor<?> con = TestUtils.getConstructorAccessible(cls, (Class<?>[]) null);
            Object panel = con.newInstance((Object[]) null);
            Method setFonts = TestUtils.getDeclaredMethodAccessible(panel, "setFonts", Font.class);
            Method updateLocalization = TestUtils.getDeclaredMethodAccessible(panel, "updateLocalization", Loc.class);

            JPanel jpanel = (JPanel) panel;

            frame.add(jpanel);

            Font font = new JLabel().getFont();

            setFonts.invoke(panel, font);
            updateLocalization.invoke(panel, loc);
            jpanel.setVisible(true);
            frame.pack();
            System.out.println("size 5: " + jpanel.getWidth() + ", " + jpanel.getHeight());
            frame.setVisible(true);
        } catch (Exception e) {
            System.out.println("AAAAAAAAAAAHHHHHHH!!!!!!!!!!");
            e.printStackTrace();
            System.exit(8);
        }
    }
}
