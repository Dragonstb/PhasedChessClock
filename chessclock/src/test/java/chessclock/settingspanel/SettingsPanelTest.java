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
package chessclock.settingspanel;

import java.awt.Font;
import java.lang.reflect.Field;
import java.util.Optional;

import javax.swing.JButton;

import additionalTesting.TestUtils;
import chessclock.MasterFrame;
import localization.EmptyLocalization;
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

public class SettingsPanelTest {

    private static final MasterFrame FRAME = TestUtils.makeMasterFrame();

    private SettingsPanel panel;

    private Field fieldOkButton;

    private JButton okButton;

    private String fail = null;

    public SettingsPanelTest() {
        try {
            fieldOkButton = TestUtils.getFieldAccessible(SettingsPanel.class, "okButton");
        } catch (Exception e) {
            fail = "failed to access fields: " + e.getClass().getSimpleName() + "(" + e.getMessage() + ")";
        }
    }

    private void readOkButton() throws IllegalArgumentException, IllegalAccessException {
        okButton = (JButton) fieldOkButton.get(panel);
    }

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        FRAME.setLoc(new EmptyLocalization());
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
        panel = SettingsPanel.makeNew(FRAME, 800, 600).orElse(null);
    }

    @AfterEach
    public void tearDown() throws Exception {
        panel = null;
    }

    @Test
    public void testMakeNew() {
        if (panel == null) {
            fail("have failed in creating a panel");
            return;
        }

        Optional<SettingsPanel> opt;
        opt = SettingsPanel.makeNew(null, 800, 600);
        assertTrue(opt.isEmpty(), "Optional present besides of missing frame");

        opt = SettingsPanel.makeNew(FRAME, 0, 600);
        assertTrue(opt.isEmpty(), "Optional present besides of vanishing width");

        opt = SettingsPanel.makeNew(FRAME, 800, -321);
        assertTrue(opt.isEmpty(), "Optional present besides of megative height");

    }

    @Test
    public void testUpdateLocalization() {
        if (fail != null) {
            fail(fail);
            return;
        }

        try {
            readOkButton();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            fail("Could not access okButton");
            return;
        }

        Loc loc = new EnglishLocalization();
        panel.updateLocalization(loc);
        assertEquals(loc.retrieveString(Loc.OK), okButton.getText(), "Not the right text");
    }

    @Test
    public void testSetFonts() {
        if (fail != null) {
            fail(fail);
            return;
        }

        try {
            readOkButton();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            fail("Could not access okButton");
            return;
        }

        Font font = okButton.getFont();
        font = font.deriveFont(Font.PLAIN, 1.5f * font.getSize());
        panel.setFonts(font);
        font = font.deriveFont(Font.BOLD);
        assertEquals(font, okButton.getFont(), "Not the right font");
    }

    @Test
    public void testRearrange() {
        int width = 999, height = 777;
        panel.rearrange(width, height);
        assertEquals(width, panel.getWidth(), "Not the right width");
        assertEquals(height, panel.getHeight(), "Not the right height");
    }

}
