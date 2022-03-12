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
package chessclock.about;

import java.awt.Font;
import java.lang.reflect.Field;

import javax.swing.JEditorPane;

import additionalTesting.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AboutPanelTest {

    private AboutPanel panel;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
        panel = AboutPanel.makeNew();
    }

    @AfterEach
    public void tearDown() throws Exception {
        panel = null;
    }

    @Test
    public void testSetFonts() {
        JEditorPane about;
        try {
            Field aboutField = TestUtils.getFieldAccessible(panel, "about");
            about = (JEditorPane) aboutField.get(panel);
        } catch (Exception e) {
            fail("Failed to access AboutPanel.about");
            return;
        }

        Font font = TestUtils.getRandomFont();

        panel.setFonts(font);
        assertEquals(font, about.getFont(), "Not the expected font 1");

        font = font.deriveFont(1.2f * font.getSize()); // another font for sure
        panel.setFonts(font);
        assertEquals(font, about.getFont(), "Not the expected font 2");

        panel.setFonts(null);
        assertEquals(font, about.getFont(), "Not the expected font 3");
    }
}
