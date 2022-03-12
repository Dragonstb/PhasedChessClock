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
package chessclock.clockpanel;

import java.awt.Font;
import java.awt.Image;
import java.lang.reflect.Field;
import java.util.Optional;

import javax.swing.JLabel;

import additionalTesting.TestUtils;
import chessclock.ChessLabel;
import chessclock.Constants;
import chessclock.Countries;
import chessclock.Country;
import chessclock.MasterFrame;
import localization.EmptyLocalization;
import localization.EnglishLocalization;
import localization.Loc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClockPanelTest {

    private static final MasterFrame FRAME = TestUtils.makeMasterFrame();
    private Field movePanelField = null;
    private Field[] identityFields = null;
    private ClockPanel panel;

    public ClockPanelTest() {
        String[] identityFieldNames = new String[]{"identityPanelLeft", "identityPanelRight"};
        try {
            movePanelField = TestUtils.getFieldAccessible(ClockPanel.class, "movePanel");
            identityFields = new Field[identityFieldNames.length];
            for (int k = 0; k < identityFieldNames.length; k++) {
                identityFields[k] = TestUtils.getFieldAccessible(ClockPanel.class, identityFieldNames[k]);
            }
        } catch (Exception e) {
        }

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
        panel = ClockPanel.makeNew(FRAME, 800, 600).get();
    }

    @AfterEach
    public void tearDown() throws Exception {
        panel = null;
    }

    @Test
    public void testMakeNew() {
        Optional<ClockPanel> opt;

        opt = ClockPanel.makeNew(FRAME, 800, 600);
        assertTrue(opt.isPresent(), "Did not create clock panel besides of proper arguments");

        opt = ClockPanel.makeNew(null, 800, 600);
        assertTrue(opt.isEmpty(), "Still made new besides of null frame");

        opt = ClockPanel.makeNew(FRAME, 0, 600);
        assertTrue(opt.isEmpty(), "Still made new besides of vanishing width");

        opt = ClockPanel.makeNew(FRAME, 800, -100);
        assertTrue(opt.isEmpty(), "Still made new besides of negative height");
    }

    @Test
    public void testRearrange() {
        int width = 1200, height = 800;
        Font font = new JLabel().getFont();
        panel.setFonts(font);
        panel.rearrange(width, height);

        assertEquals(width, panel.getWidth(), "Not the right width");
        assertEquals(height, panel.getHeight(), "Not the right height");
    }

    @Test
    public void testUpdateLocalization() {
        Object movePanel;
        Field moveStringField;
        try {
            movePanel = movePanelField.get(panel);
            moveStringField = TestUtils.getFieldAccessible(movePanel, "moveString");
        } catch (Exception e) {
            fail("Could not reflect on the move panel: " + e.getMessage());
            return;
        }

        Loc loc = new EnglishLocalization();
        panel.updateLocalization(loc);
        String text;
        try {
            text = (String) moveStringField.get(movePanel);
        } catch (Exception e) {
            fail("Could not reflect on the move string 1: " + e.getMessage());
            return;
        }
        assertEquals(loc.retrieveString(Loc.MOVE), text, "Not the right move string 1");

        panel.updateLocalization(null); // should simply return and keep the old loc
        try {
            text = (String) moveStringField.get(movePanel);
        } catch (Exception e) {
            fail("Could not reflect on the move string 2: " + e.getMessage());
            return;
        }
        assertEquals(loc.retrieveString(Loc.MOVE), text, "Not the right move string 2");

        loc = new EmptyLocalization();
        panel.updateLocalization(loc);
        try {
            text = (String) moveStringField.get(movePanel);
        } catch (Exception e) {
            fail("Could not reflect on the move string 3: " + e.getMessage());
            return;
        }
        assertEquals(loc.retrieveString(Loc.MOVE), text, "Not the right move string 3");
    }

    @Test
    public void testSetData() {
        String[] names = new String[identityFields.length];
        for (int k = 0; k < names.length; k++) {
            names[k] = "random name " + k;
        }

        Country country = Countries.EARTH;
        Loc loc = new EnglishLocalization();

        Object identityPanel;
        Field nameLabelField;
        ChessLabel label;

        // proper data
        panel.setDataLeft(names[0], country, loc);
        panel.setDataRight(names[1], country, loc);

        for (int iden = 0; iden < names.length; iden++) {
            try {
                identityPanel = identityFields[iden].get(panel);
                nameLabelField = TestUtils.getFieldAccessible(identityPanel, "nameLabel");
                label = (ChessLabel) nameLabelField.get(identityPanel);
            } catch (Exception e) {
                fail("Cannot reflect on name " + iden + ": " + e.getMessage());
                return;
            }
            assertEquals(names[iden], label.getText(), "Not the right name");
        }

        // invalid data: should not change anything
        int iden = 0;
        panel.setDataLeft(null, country, loc);
        try {
            identityPanel = identityFields[iden].get(panel);
            nameLabelField = TestUtils.getFieldAccessible(identityPanel, "nameLabel");
            label = (ChessLabel) nameLabelField.get(identityPanel);
        } catch (Exception e) {
            fail("Cannot reflect on name " + iden + " when passing invalid name: " + e.getMessage());
            return;
        }
        assertEquals(names[iden], label.getText(), "Not the right name when passing invalid name");

        panel.setDataLeft("Bort", null, loc);
        try {
            identityPanel = identityFields[iden].get(panel);
            nameLabelField = TestUtils.getFieldAccessible(identityPanel, "nameLabel");
            label = (ChessLabel) nameLabelField.get(identityPanel);
        } catch (Exception e) {
            fail("Cannot reflect on name " + iden + " when passing invalid country: " + e.getMessage());
            return;
        }
        assertEquals(names[iden], label.getText(), "Not the right name when passing invalid country");

        panel.setDataLeft("Patricia", country, null);
        try {
            identityPanel = identityFields[iden].get(panel);
            nameLabelField = TestUtils.getFieldAccessible(identityPanel, "nameLabel");
            label = (ChessLabel) nameLabelField.get(identityPanel);
        } catch (Exception e) {
            fail("Cannot reflect on name " + iden + " when passing invald localization: " + e.getMessage());
            return;
        }
        assertEquals(names[iden], label.getText(), "Not the right name when passing invalid localization");
    }

    @Test
    public void testArrangeColorIndicators() {
        Field indicator;
        try {
            indicator = TestUtils.getFieldAccessible(panel, "colorIndicatorLeft");
        } catch (Exception e1) {
            fail("Cannot reflect on colour indicators 0: " + e1.getMessage());
            return;
        }
        Image img;

        boolean whiteIsLeft = true;

        panel.arrangeColorIndicators(!whiteIsLeft);
        try {
            img = (Image) indicator.get(panel);
        } catch (Exception e) {
            fail("Cannot reflect on colour indicators 1: " + e.getMessage());
            return;
        }
        assertEquals(Constants.BLACK_ICON.getImage(), img, "Not the right color indicator");

        panel.arrangeColorIndicators(whiteIsLeft);
        try {
            img = (Image) indicator.get(panel);
        } catch (Exception e) {
            fail("Cannot reflect on colour indicators 2: " + e.getMessage());
            return;
        }
        assertEquals(Constants.WHITE_ICON.getImage(), img, "Not the right color indicator");
    }

    @Test
    public void testSetPausedState() {
        Field leverPanelField;
        JLabel leverPanel;
        try {
            leverPanelField = TestUtils.getFieldAccessible(panel, "leverPanel");
            leverPanel = (JLabel) leverPanelField.get(panel);
        } catch (Exception e1) {
            fail("Cannot reflect on pause strings: " + e1.getMessage());
            return;
        }

        Loc loc = new EnglishLocalization();
        panel.updateLocalization(loc);

        panel.setPausedState(true);
        assertEquals(loc.retrieveString(Loc.PAUSED), leverPanel.getText(), "Not the right pause");

        panel.setPausedState(false);
        assertEquals(Constants.EMPTY_STRING, leverPanel.getText(), "Not the right pause");
    }

    @Test
    public void testSetMoveNumber() {
        Object movePanel;
        Field moveNumberField;
        try {
            movePanel = movePanelField.get(panel);
            moveNumberField = TestUtils.getFieldAccessible(movePanel, "moveNumber");
        } catch (Exception e) {
            fail("Could not reflect on the move number 0: " + e.getMessage());
            return;
        }

        // valid input
        int move = 12345;
        String moveString = String.valueOf(move);
        panel.setMoveNumber(move);
        String text;
        try {
            text = (String) moveNumberField.get(movePanel);
        } catch (Exception e) {
            fail("Could not reflect on the move number 1: " + e.getMessage());
            return;
        }
        assertEquals(moveString, text, "Not the right move number");

        // not an invalid, but a special input as the label has to deal with a zero-length string
        FRAME.setLoc(new EmptyLocalization());
        moveString = "";
        panel.setMoveNumber(-5);
        try {
            text = (String) moveNumberField.get(movePanel);
        } catch (Exception e) {
            fail("Could not reflect on the move number 1: " + e.getMessage());
            return;
        }
        assertEquals(moveString, text, "Not the right move number");

        FRAME.setLoc(new EnglishLocalization());
    }

}
