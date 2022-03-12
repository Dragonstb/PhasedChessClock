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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @since 1.0;
 * @author Dragonstb
 */
public class ChessLabelTest {

    private ChessLabel label;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
        label = new ChessLabel("a text");
    }

    @AfterEach
    public void tearDown() throws Exception {
        label = null;
    }

    @Test
    public void testHTMLInjectionHardness() {
        String text = "Bryan Bishop";
        label.setText(text);
        assertEquals(text, label.getText(), "Not the right text");

        String innerText = "suddenly slant";
        text = "<html><i>" + innerText + "</i></html>";
        label.setText(text);
        assertNotEquals(innerText, label.getText(), "Malicious: HTML injection successful");
        assertEquals(text, label.getText(), "Not the right text");
    }
}
