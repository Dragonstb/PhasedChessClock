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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @since 1.0;
 * @author Dragonstb
 */
public class LimitedTextFieldTest {

    private static final int LIMIT = 20;
    private LimitedTextField textField;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
        textField = new LimitedTextField("", 10, LIMIT);
    }

    @AfterEach
    public void tearDown() throws Exception {
        textField = null;
    }

    @Test
    public void testSetText() {
        String text;
        StringBuffer buf;

        text = "abcdefg"; // text.length < LIMIT, that's important here
        textField.setText(text);
        assertEquals(text, textField.getText(), "Not the right text");

        buf = new StringBuffer();
        for (int k = 0; k < 2 * LIMIT; k++) {
            buf.append('a');
        }
        text = buf.toString();
        textField.setText(text);
        assertEquals(text.substring(0, LIMIT), textField.getText(), "text not truncated 1");

        buf = new StringBuffer();
        for (int k = 0; k < 2000 + LIMIT; k++) {
            buf.append('a');
        }
        text = buf.toString();
        textField.setText(text);
        assertEquals(text.substring(0, LIMIT), textField.getText(), "text not truncated 2");

    }
}
