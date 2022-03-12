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
package chessclock;

import java.util.Optional;

import additionalTesting.TestUtils;
import chessclock.clockpanel.ClockPanel;
import chessclock.settingspanel.SettingsPanel;
import localization.EnglishLocalization;
import localization.Loc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Dragonstb
 * @since 1.0;
 */
public class MasterFrameTest {

    private MasterFrame frame;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
        frame = TestUtils.makeMasterFrame();
    }

    @AfterEach
    public void tearDown() throws Exception {
        frame.dispose();
        frame = null;
    }

    @Test
    public void testAddAndGetSettingsPanel() {
        frame.setLoc(new EnglishLocalization());
        Optional<SettingsPanel> optPanelA = SettingsPanel.makeNew(frame, 800, 600);
        Optional<SettingsPanel> optPanelB = SettingsPanel.makeNew(frame, 800, 600);
        if (optPanelA.isEmpty() || optPanelA.isEmpty()) {
            fail("Could not create SettingsPanel for tests");
            return;
        }

        SettingsPanel panelA = optPanelA.get();
        SettingsPanel panelB = optPanelB.get();

        assertNull(frame.getSettingsPanel(), "Did not expect a settings panel here");

        frame.addSettingsPanel(panelA);
        assertEquals(panelA, frame.getSettingsPanel(), "Did not found panel A");

        frame.addSettingsPanel(panelB);
        assertEquals(panelB, frame.getSettingsPanel(), "Did not found panel B");

        int numChildren = frame.getContentPane().getComponentCount();
        assertEquals(1, numChildren, "found " + numChildren + " components");
    }

    @Test
    public void testAddAndGetClockPanel() {
        Optional<ClockPanel> optPanelA = ClockPanel.makeNew(frame, 800, 600);
        Optional<ClockPanel> optPanelB = ClockPanel.makeNew(frame, 800, 600);
        if (optPanelA.isEmpty() || optPanelA.isEmpty()) {
            fail("Could not create SettingsPanel for tests");
            return;
        }

        ClockPanel panelA = optPanelA.get();
        ClockPanel panelB = optPanelB.get();

        assertNull(frame.getClockPanel(), "Did not expect a settings panel here");

        frame.addClockPanel(panelA);
        assertEquals(panelA, frame.getClockPanel(), "Did not found panel A");

        frame.addClockPanel(panelB);
        assertEquals(panelB, frame.getClockPanel(), "Did not found panel B");

        int numChildren = frame.getContentPane().getComponentCount();
        assertEquals(1, numChildren, "found " + numChildren + " components");
    }

    /**
     * This method tests both {@code getLoc()} and {@code setLoc(Loc)}.
     *
     * @since 1.0;
     */
    @Test
    public void testSetAndSetLoc() {
        Loc loc = new EnglishLocalization();

        assertNull(frame.getLoc(), "Did not expect a Loc here");

        frame.setLoc(loc);

        assertEquals(loc, frame.getLoc(), "Loc not set properly, or not retrieved properly");
        assertEquals(loc.retrieveString(Loc.FRAME_TITLE), frame.getTitle(), "Frame title not correct");

    }

}
