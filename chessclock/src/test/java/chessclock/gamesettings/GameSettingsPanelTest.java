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
import java.util.Optional;

import additionalTesting.TestUtils;
import chessclock.ChessCheckBox;
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

public class GameSettingsPanelTest {

    private static final MasterFrame FRAME = TestUtils.makeMasterFrame();
    private GameSettingsPanel panel;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        FRAME.setLoc(new EnglishLocalization());
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
        panel = GameSettingsPanel.makeNew(FRAME).get();
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void testMakeNew() {
        Optional<GameSettingsPanel> opt;

        opt = GameSettingsPanel.makeNew(FRAME);
        assertTrue(opt.isPresent(), "No optional besides proper input");

        opt = GameSettingsPanel.makeNew(null);
        assertTrue(opt.isEmpty(), "Optional present besides stupid arguments");
    }

    @Test
    public void testUpdateLocalization() {
        ChessCheckBox checkBox;
        try {
            Field field = TestUtils.getFieldAccessible(panel, "sameTCPs");
            checkBox = (ChessCheckBox) field.get(panel);
        } catch (Exception e) {
            fail("Cannot access checkTCPs");
            return;
        }

        panel.updateLocalization(new EmptyLocalization());

        Loc loc = new EnglishLocalization();
        panel.updateLocalization(loc);
        assertEquals(loc.retrieveString(Loc.SAME_BUDGET), checkBox.getText(), "Localization error 1");

        panel.updateLocalization(null);
        assertEquals(loc.retrieveString(Loc.SAME_BUDGET), checkBox.getText(), "Localization error 2");

    }
}
