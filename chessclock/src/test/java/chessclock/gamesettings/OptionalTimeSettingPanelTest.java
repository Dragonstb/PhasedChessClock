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
import java.lang.reflect.Method;

import additionalTesting.TestUtils;
import chessclock.ChessCheckBox;
import chessclock.ChessLabel;
import chessclock.LimitedTextField;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import time.Constants;

public class OptionalTimeSettingPanelTest {

    private OptionalTimeSettingPanel panel;

    private Field fieldLabel;
    private Field fieldCheckBox;
    private Field fieldTextField;
    private Field fieldSeconds;

    private ChessCheckBox checkBox;
    private ChessLabel label;
    private LimitedTextField textField;
    private int seconds;

    String masterFail = null;
    String subFail = null;

    public OptionalTimeSettingPanelTest() {
        try {
            fieldLabel = TestUtils.getFieldAccessible(OptionalTimeSettingPanel.class, "label");
            fieldCheckBox = TestUtils.getFieldAccessible(OptionalTimeSettingPanel.class, "checkBox");
            fieldTextField = TestUtils.getFieldAccessible(OptionalTimeSettingPanel.class, "textField");
            fieldSeconds = TestUtils.getFieldAccessible(OptionalTimeSettingPanel.class, "seconds");
        } catch (Exception e) {
            masterFail = "Could not access fields: " + e.getMessage();
        }
    }

    private void readSeconds() throws IllegalArgumentException, IllegalAccessException {
        seconds = fieldSeconds.getInt(panel);
    }

    private void readLabel() throws IllegalArgumentException, IllegalAccessException {
        label = (ChessLabel) fieldLabel.get(panel);
    }

    private void readTextField() throws IllegalArgumentException, IllegalAccessException {
        textField = (LimitedTextField) fieldTextField.get(panel);
    }

    private void readCheckBox() throws IllegalArgumentException, IllegalAccessException {
        checkBox = (ChessCheckBox) fieldCheckBox.get(panel);
    }

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
        panel = new OptionalTimeSettingPanel();
    }

    @AfterEach
    public void tearDown() throws Exception {
        panel = null;
    }

    @Test
    public void testUpdateLocalization() {
        if (masterFail != null) {
            fail(masterFail);
            return;
        }

        try {
            readLabel();
        } catch (Exception e) {
            fail("Could not access label");
            return;
        }

        String text = "zyxw";
        panel.updateLocalization(text);
        assertEquals(text, label.getText(), "Not the right text");

        text = "this is fine";
        panel.updateLocalization(text);
        assertEquals(text, label.getText(), "Not the right text");

        panel.updateLocalization(null);
        assertEquals(null, label.getText(), "Not the right text");
    }

    @Test
    public void testSetFonts() {
        if (masterFail != null) {
            fail(masterFail);
            return;
        }

        try {
            readTextField();
        } catch (Exception e) {
            fail("Could not access text field");
            return;
        }

        Font font = TestUtils.getRandomFont();
        panel.setFonts(font);
        assertEquals(font, textField.getFont(), "Not the right fonts");

    }

    @Test
    public void testRearrange() {
        panel.rearrange(200, 25, 3);
        panel.rearrange(400, 25, 3);
        assertEquals(400, panel.getWidth(), "Not the right width");
    }

    @Test
    public void testParseSeconds() {
        if (masterFail != null) {
            fail(masterFail);
            return;
        }

        try {
            readTextField();
        } catch (Exception e) {
            fail("Could not access text field");
            return;
        }

        String methodName = "parseSeconds";
        Method method;
        try {
            method = TestUtils.getDeclaredMethodAccessible(OptionalTimeSettingPanel.class, methodName);
        } catch (Exception e) {
            fail("Could not access method \"" + methodName + "\"");
            return;
        }

        int value = 25;
        String desc = "a proper value";
        textField.setText(String.valueOf(value));
        try {
            method.invoke(panel);
            readSeconds();
        } catch (Exception e) {
            fail("Cannot do " + desc);
        }
        assertEquals(value, seconds, "Not the right number of seconds when passing " + desc);

        desc = "a non-positive value"; // change should be rejected
        textField.setText(String.valueOf(-18));
        try {
            method.invoke(panel);
            readSeconds();
        } catch (Exception e) {
            fail("Cannot do " + desc);
        }
        assertEquals(value, seconds, "Not the right number of seconds when passing " + desc);

        desc = "a non-value"; // shouldn't change anything either
        textField.setText("this is correct");
        try {
            method.invoke(panel);
            readSeconds();
        } catch (Exception e) {
            fail("Cannot do " + desc);
        }
        assertEquals(value, seconds, "Not the right number of seconds when passing " + desc);

    }

    @Test
    public void testGetEffectiveNanoseconds() {
        if (masterFail != null) {
            fail(masterFail);
            return;
        }

        int value = 30;

        try {
            fieldSeconds.setInt(panel, value);
        } catch (Exception e) {
            fail("Could not access seconds");
            return;
        }

        try {
            readCheckBox();
        } catch (Exception e) {
            fail("Could not access check box");
            return;
        }

        checkBox.setSelected(true);
        assertEquals(value * Constants.NANOSEC_PER_SEC, panel.getEffectiveNanoseconds(),
                "Nanoseconds not correct for checked box");

        checkBox.setSelected(false);
        assertEquals(-1, panel.getEffectiveNanoseconds(), "Nanoseconds incorrect for un-checked box");
    }

    @Test
    public void testEnableOTS() {
        if (masterFail != null) {
            fail(masterFail);
            return;
        }

        String methodName = "updateTextFieldEnabling";
        Method method;
        try {
            method = TestUtils.getDeclaredMethodAccessible(OptionalTimeSettingPanel.class, methodName);
        } catch (Exception e) {
            fail("Could not access method \"" + methodName + "\"");
            return;
        }

        try {
            readCheckBox();
        } catch (Exception e) {
            fail("Could not access check box");
            return;
        }

        try {
            readTextField();
        } catch (Exception e) {
            fail("Could not access text field");
            return;
        }

        checkBox.setSelected(true);
        checkBox.setEnabled(true);
        try {
            method.invoke(panel);
        } catch (Exception e) {
            fail("Cannot do method");
        }
        assertTrue(textField.isEnabled(), "Text field should be editable");

        checkBox.setSelected(false);
        checkBox.setEnabled(true);
        try {
            method.invoke(panel);
        } catch (Exception e) {
            fail("Cannot do method");
        }
        assertFalse(textField.isEnabled(), "Text field should be non-editable");

        checkBox.setSelected(true);
        checkBox.setEnabled(false);
        try {
            method.invoke(panel);
        } catch (Exception e) {
            fail("Cannot do method");
        }
        assertFalse(textField.isEnabled(), "Text field should be non-editable");

        checkBox.setSelected(false);
        checkBox.setEnabled(false);
        try {
            method.invoke(panel);
        } catch (Exception e) {
            fail("Cannot do method");
        }
        assertFalse(textField.isEnabled(), "Text field should be non-editable");

    }
}
