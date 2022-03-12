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
package chessclock.gamesettings;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JButton;

import additionalTesting.TestUtils;
import chessclock.MasterFrame;
import localization.EnglishLocalization;
import localization.Loc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import time.TimeControlPhase;

public class TCPPanelTest {

    private static final MasterFrame FRAME = TestUtils.makeMasterFrame();

    private TCPPanel panel;

    private Field fieldEditButton;
    private Method methodEnableTCPSettings;
    private Method methodExecuteAddEntry;
    private Method methodExecuteEditEntry;
    private Field fieldListModel;

    private JButton editButton;
    private DefaultListModel<TCPContainer> listModel;

    private String fail = null;

    public TCPPanelTest() {
        try {
            fieldEditButton = TestUtils.getFieldAccessible(TCPPanel.class, "editCP");
            methodEnableTCPSettings = TestUtils.getDeclaredMethodAccessible(TCPPanel.class, "enableTCPSettings",
                    boolean.class);
            methodExecuteAddEntry = TestUtils.getDeclaredMethodAccessible(TCPPanel.class, "executeAddEntry", int.class,
                    TimeControlPhase.class);
            methodExecuteEditEntry = TestUtils.getDeclaredMethodAccessible(TCPPanel.class, "executeEditEntry",
                    int.class, TimeControlPhase.class);
            fieldListModel = TestUtils.getFieldAccessible(TCPPanel.class, "listModel");
        } catch (Exception e) {
            fail = "failed to access fields: " + e.getClass().getSimpleName() + "(" + e.getMessage() + ")";
        }
    }

    private void readEditButton() throws IllegalArgumentException, IllegalAccessException {
        editButton = (JButton) fieldEditButton.get(panel);
    }

    private void readListModel() throws IllegalArgumentException, IllegalAccessException {
        listModel = (DefaultListModel<TCPContainer>) fieldListModel.get(panel);
    }

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        FRAME.setLoc(new EnglishLocalization());
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
        panel = new TCPPanel(FRAME);
    }

    @AfterEach
    public void tearDown() throws Exception {
        panel = null;
    }

    @Test
    public void testUpdateLocalization() {
        if (fail != null) {
            fail(fail);
            return;
        }

        try {
            readEditButton();
        } catch (Exception e) {
            fail("Could not access edit button.");
            return;
        }

        Loc loc = new EnglishLocalization();
        panel.updateLocalization(loc);
        assertEquals(loc.retrieveString(Loc.EDIT_TCP), editButton.getText(), "Not the right text");
    }

    @Test
    public void testRearrange() {
        panel.rearrange(400, 3);
        int width = 745;
        panel.rearrange(width, 3);

        assertEquals(width, panel.getWidth(), "Not the right width");
    }

    @Test
    public void testEnableTCPSettings() {
        if (fail != null) {
            fail(fail);
            return;
        }

        try {
            readEditButton();
        } catch (Exception e) {
            fail("Could not access edit button.");
            return;
        }

        boolean enable = true;

        panel.enableTCPSettings(enable);
        assertEquals(enable, editButton.isEnabled(), "Not enabled");

        enable = false;
        panel.enableTCPSettings(enable);
        assertEquals(enable, editButton.isEnabled(), "Not disabled");
    }

    @Test
    public void testGetDeepClonedTCPs() {
        if (fail != null) {
            fail(fail);
            return;
        }

        try {
            readListModel();
        } catch (Exception e) {
            fail("Could not access list model.");
            return;
        }

        TimeControlPhase[] tcps = new TimeControlPhase[]{TimeControlPhase.makeNew((short) 40, 7200, 30),
            TimeControlPhase.makeNew((short) 20, 3600, 20),
            TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 1800, 10) // only last one has remainder
    };

        int iniLength = tcps.length;

        listModel.removeAllElements();
        // without TCPs after first remainder
        Arrays.stream(tcps).forEach(tcp -> listModel.addElement(new TCPContainer(tcp, FRAME.getLoc())));

        TimeControlPhase[] copies = panel.getDeepClonedTCPs();

        assertEquals(iniLength, copies.length, "Not the right length 1");
        for (int entry = 0; entry < Math.min(copies.length, tcps.length); entry++) {
            assertTrue(copies[entry].equals(tcps[entry]), "Copy does not equals original 1");
            assertTrue(copies[entry] != tcps[entry], "Copy is the original 1");
        }

        // with TCPs after first remainder, should retrieve an equal array as the first tests
        TimeControlPhase add = TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 12345, 12);
        listModel.addElement(new TCPContainer(add, FRAME.getLoc()));
        add = TimeControlPhase.makeNew((short) 30, 2222, 22);
        listModel.addElement(new TCPContainer(add, FRAME.getLoc()));

        copies = panel.getDeepClonedTCPs();

        assertEquals(iniLength, copies.length, "Not the right length 2");
        for (int entry = 0; entry < Math.min(copies.length, tcps.length); entry++) {
            assertTrue(copies[entry].equals(tcps[entry]), "Copy does not equals original 2");
            assertTrue(copies[entry] != tcps[entry], "Copy is the original 2");
        }

    }

    @Test
    public void testExecuteAddEntry() {
        if (fail != null) {
            fail(fail);
            return;
        }

        try {
            readListModel();
        } catch (Exception e) {
            fail("Could not access list model.");
            return;
        }

        TimeControlPhase[] tcps = new TimeControlPhase[]{TimeControlPhase.makeNew((short) 5, 54321, 42),
            TimeControlPhase.makeNew((short) 50, 6574, 0),
            TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 3600, 60)};

        listModel.removeAllElements();
        listModel.addElement(new TCPContainer(tcps[0], FRAME.getLoc()));
        listModel.addElement(new TCPContainer(tcps[2], FRAME.getLoc()));

        try {
            methodExecuteAddEntry.invoke(panel, 1, tcps[1]);
        } catch (Exception e) {
            fail("Could not invoke executeAddEntry(..)");
            return;
        }

        assertEquals(tcps.length, listModel.size(), "Not the right length");
        for (int entry = 0; entry < Math.min(tcps.length, listModel.size()); entry++) {
            assertEquals(tcps[entry], listModel.get(entry).getTcp(), "Not equal TCP");
        }
    }

    @Test
    public void testExecuteEditEntry() {
        if (fail != null) {
            fail(fail);
            return;
        }

        try {
            readListModel();
        } catch (Exception e) {
            fail("Could not access list model.");
            return;
        }

        TimeControlPhase[] tcps = new TimeControlPhase[]{TimeControlPhase.makeNew((short) 5, 54321, 42),
            TimeControlPhase.makeNew((short) 50, 6574, 0),
            TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 3600, 60)};

        listModel.removeAllElements();
        Arrays.stream(tcps).forEach(tcp -> listModel.addElement(new TCPContainer(tcp, FRAME.getLoc())));

        tcps[1] = TimeControlPhase.makeNew((short) 20, 1000, 10);

        try {
            methodExecuteEditEntry.invoke(panel, 1, tcps[1]);
        } catch (Exception e) {
            fail("Could not invoke executeEditEntry(..)");
            return;
        }

        assertEquals(tcps.length, listModel.size(), "Not the right length");
        for (int entry = 0; entry < Math.min(tcps.length, listModel.size()); entry++) {
            assertEquals(tcps[entry], listModel.get(entry).getTcp(), "Not equal TCP");
        }
    }
}
