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

import java.awt.Font;
import java.lang.reflect.Field;

import javax.swing.JComboBox;
import javax.swing.JRadioButton;

import additionalTesting.TestUtils;
import chessclock.Countries;
import chessclock.CountryContainer;
import chessclock.LimitedTextField;
import chessclock.MasterFrame;
import localization.EmptyLocalization;
import localization.EnglishLocalization;
import localization.Loc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerDataPanelTest {

    private static final MasterFrame FRAME = TestUtils.makeMasterFrame();

    private PlayerDataPanel panel;

    private Field fieldAdditiveButton;
    private Field fieldName;
    private Field fieldCountry;

    private JRadioButton additiveButton;
    private LimitedTextField name;
    private JComboBox<CountryContainer> country;

    private String fail = null;

    public PlayerDataPanelTest() {
        try {
            fieldAdditiveButton = TestUtils.getFieldAccessible(PlayerDataPanel.class, "additiveButton");
            fieldName = TestUtils.getFieldAccessible(PlayerDataPanel.class, "name");
            fieldCountry = TestUtils.getFieldAccessible(PlayerDataPanel.class, "country");
        } catch (Exception e) {
            fail = "Could not access fields: " + e.getMessage();
        }
    }

    private void readAdditiveButton() throws IllegalArgumentException, IllegalAccessException {
        additiveButton = (JRadioButton) fieldAdditiveButton.get(panel);
    }

    private void readName() throws IllegalArgumentException, IllegalAccessException {
        name = (LimitedTextField) fieldName.get(panel);
    }

    private void readCountry() throws IllegalArgumentException, IllegalAccessException {
        country = (JComboBox<CountryContainer>) fieldCountry.get(panel);
    }

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        FRAME.setLoc(new EmptyLocalization());
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
        FRAME.dispose();
    }

    @BeforeEach
    public void setUp() throws Exception {
        panel = new PlayerDataPanel(FRAME);
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
            readAdditiveButton();
        } catch (Exception e) {
            fail("Could not access additive button: " + e.getMessage());
            return;
        }

        Loc loc = new EnglishLocalization();
        panel.updateLocalization(loc);
        assertEquals(loc.retrieveString(Loc.ADDITIVE_TCP), additiveButton.getText(), "Not the right text");

    }

    @Test
    public void testSetFonts() {
        if (fail != null) {
            fail(fail);
            return;
        }

        try {
            readName();
        } catch (Exception e) {
            fail("Could not access name: " + e.getMessage());
            return;
        }

        Font font = TestUtils.getRandomFont();
        panel.setFonts(font);
        assertEquals(font, name.getFont(), "Not the right font");
    }

    @Test
    public void testRearrange() {
        panel.rearrange(200);
        panel.rearrange(400);
        assertEquals(400, panel.getWidth(), "Not the right width");
    }

    @Test
    public void testGetSelectedCountry() {
        if (fail != null) {
            fail(fail);
            return;
        }

        try {
            readCountry();
        } catch (Exception e) {
            fail("Could not access country selection: " + e.getMessage());
            return;
        }

        Loc loc = new EnglishLocalization();
        panel.updateLocalization(loc);

        country.setSelectedIndex(0);
        assertEquals(Countries.EARTH, panel.getSelectedCountry(), "That is not the Earth");

        country.setSelectedIndex(1);
        assertEquals(Countries.DEBUG_A, panel.getSelectedCountry(), "That is not Debug County A");
    }

}
