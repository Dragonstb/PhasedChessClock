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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import additionalTesting.TestUtils;
import additionalTesting.WindowDisposerThread;
import chessclock.Constants;
import chessclock.LimitedTextField;
import chessclock.MasterFrame;
import localization.EnglishLocalization;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import time.TimeControlPhase;

public class AddTCPDialogTest {

    private static final MasterFrame FRAME = TestUtils.makeMasterFrame();
    private static final Font FONT = new JLabel().getFont();

    private Field fieldMinutesTextField;
    private Field fieldSecondsTextField;
    private Field fieldIncrementTextField;
    private Field fieldMovesTextField;
    private Field fieldNumMoves;
    private Field fieldNumMinutes;
    private Field fieldNumSeconds;
    private Field fieldNumIncrement;
    private Field fieldRemainderCheckBox;
    private Field fieldTCP;

    private LimitedTextField minutesTextField;
    private LimitedTextField secondsTextField;
    private LimitedTextField incrementTextField;
    private LimitedTextField movesTextField;
    private short numMoves;
    private int numSeconds;
    private int numMinutes;
    private int numIncrement;
    private JCheckBox remainderCheckBox;
    private TimeControlPhase tcp;

    private boolean invalid;
    private boolean forgetIt;
    private String masterFail;
    private String subFail;

    private AddTCPDialog dialog;
    private Constructor<?> constructor;

    private WindowDisposerThread disposer;

    public AddTCPDialogTest() {
        try {
            constructor = TestUtils.getConstructorAccessible(AddTCPDialog.class, MasterFrame.class, Font.class);
            fieldMovesTextField = TestUtils.getFieldAccessible(AddTCPDialog.class, "movesTextField");
            fieldMinutesTextField = TestUtils.getFieldAccessible(AddTCPDialog.class, "minutesTextField");
            fieldSecondsTextField = TestUtils.getFieldAccessible(AddTCPDialog.class, "secondsTextField");
            fieldIncrementTextField = TestUtils.getFieldAccessible(AddTCPDialog.class, "incrementTextField");
            fieldNumMoves = TestUtils.getFieldAccessible(AddTCPDialog.class, "numMoves");
            fieldNumMinutes = TestUtils.getFieldAccessible(AddTCPDialog.class, "numMinutes");
            fieldNumSeconds = TestUtils.getFieldAccessible(AddTCPDialog.class, "numSeconds");
            fieldNumIncrement = TestUtils.getFieldAccessible(AddTCPDialog.class, "numIncrement");
            fieldRemainderCheckBox = TestUtils.getFieldAccessible(AddTCPDialog.class, "remainderCheckBox");
            fieldTCP = TestUtils.getFieldAccessible(AddTCPDialog.class, "tcp");
            forgetIt = false;
        } catch (Exception e) {
            forgetIt = true;
            masterFail = e.getMessage();
        }
    }

    private void readMoves() throws IllegalArgumentException, IllegalAccessException {
        numMoves = fieldNumMoves.getShort(dialog);
    }

    private void readMinutes() throws IllegalArgumentException, IllegalAccessException {
        numMinutes = fieldNumMinutes.getInt(dialog);
    }

    private void readSeconds() throws IllegalArgumentException, IllegalAccessException {
        numSeconds = fieldNumSeconds.getInt(dialog);
    }

    private void readIncrement() throws IllegalArgumentException, IllegalAccessException {
        numIncrement = fieldNumIncrement.getInt(dialog);
    }

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        FRAME.setLoc(new EnglishLocalization());
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
        FRAME.dispose();
    }

    @BeforeEach
    public void setUp() throws Exception {
        subFail = "";
        try {
            disposer = new WindowDisposerThread(FRAME);
            disposer.start();
            dialog = (AddTCPDialog) constructor.newInstance(FRAME, FONT);
            movesTextField = (LimitedTextField) fieldMovesTextField.get(dialog);
            minutesTextField = (LimitedTextField) fieldMinutesTextField.get(dialog);
            secondsTextField = (LimitedTextField) fieldSecondsTextField.get(dialog);
            incrementTextField = (LimitedTextField) fieldIncrementTextField.get(dialog);
            readMoves();
            readMinutes();
            readSeconds();
            readIncrement();
            remainderCheckBox = (JCheckBox) fieldRemainderCheckBox.get(dialog);
            tcp = (TimeControlPhase) fieldTCP.get(dialog);
            invalid = false;
        } catch (Exception e) {
            invalid = true;
            subFail = e.getMessage();
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        disposer = null;
        dialog = null;
    }

    @Test
    public void testGetTCP() {
        if (forgetIt) {
            fail("forget it: could not gain access: " + masterFail);
            return;
        }
        if (invalid) {
            fail("Was not able to create an instance: " + subFail);
            return;
        }

        String name = "getTCP";
        Optional<TimeControlPhase> opt;

        try {
            fieldTCP.set(dialog, null);
            opt = dialog.getTCP();
        } catch (Exception e) {
            fail("Could not invoke method \"" + name + "\" when passing null");
            return;
        }
        assertTrue(opt.isEmpty(), "Optional present besides passing null");

        try {
            fieldTCP.set(dialog, TimeControlPhase.makeNew((short) 20, 12345, 10));
            opt = dialog.getTCP();
        } catch (Exception e) {
            fail("Could not invoke method \"" + name + "\" when passing null");
            return;
        }
        assertTrue(opt.isPresent(), "Optional empty besides proper value");

    }

    @Test
    public void testParseMoves() {
        if (forgetIt) {
            fail("forget it: could not gain access: " + masterFail);
            return;
        }
        if (invalid) {
            fail("Was not able to create an instance: " + subFail);
            return;
        }

        String methodName = "parseMoves";
        Method method;
        try {
            method = TestUtils.getDeclaredMethodAccessible(AddTCPDialog.class, methodName, (Class<?>[]) null);
        } catch (Exception e) {
            fail("Could not access method \"" + methodName + "\"");
            return;
        }

        short value = 12; // positive, that's important here

        // a proper value
        movesTextField.setText(String.valueOf(value));
        try {
            method.invoke(dialog, (Object[]) null);
            readMoves();
        } catch (Exception e) {
            fail("Cannot do a proper value");
        }
        assertEquals(value, numMoves, "Not the right number of moves");

        // a negative value keeps the old one
        movesTextField.setText("-5");
        try {
            method.invoke(dialog, (Object[]) null);
            readMoves();
        } catch (Exception e) {
            fail("Cannot do a negative value");
        }
        assertEquals(value, numMoves, "Not the right number of moves");

        // likewise, a not-integer-parsable keeps the old one
        movesTextField.setText("hello world");
        try {
            method.invoke(dialog, (Object[]) null);
            readMoves();
        } catch (Exception e) {
            fail("Cannot do a non-value value");
        }
        assertEquals(value, numMoves, "Not the right number of moves");

        // positive integers too large for a short are capped
        movesTextField.setText("99999"); // the text field for moves can take 5 characters
        try {
            method.invoke(dialog, (Object[]) null);
            readMoves();
        } catch (Exception e) {
            fail("Cannot do a large value");
        }
        assertEquals(Short.MAX_VALUE, numMoves, "Not the right number of moves");

    }

    @Test
    public void testParseMinutes() {
        if (forgetIt) {
            fail("forget it: could not gain access: " + masterFail);
            return;
        }
        if (invalid) {
            fail("Was not able to create an instance: " + subFail);
            return;
        }

        String message = "Not the right number of minutes when passing ";
        String methodName = "parseMinutes";
        Method method;
        try {
            method = TestUtils.getDeclaredMethodAccessible(dialog, methodName, (Class<?>[]) null);
        } catch (Exception e) {
            fail("Could not access method \"" + methodName + "\"");
            return;
        }

        LimitedTextField textField = minutesTextField;
        int value = 12; // positive, but not exceeding Constants.MINUTES_SOFT_LIMIT, that's important here

        // a proper value
        textField.setText(String.valueOf(value));
        try {
            method.invoke(dialog, (Object[]) null);
            readMinutes();
        } catch (Exception e) {
            fail("Cannot do a proper value");
        }
        assertEquals(value, numMinutes, message + "a proper value");

        // a negative value keeps the old one
        textField.setText("-5");
        try {
            method.invoke(dialog, (Object[]) null);
            readMinutes();
        } catch (Exception e) {
            fail("Cannot do a negative value");
        }
        assertEquals(value, numMinutes, message + "a negative value");

        // likewise, a not-integer-parsable keeps the old one
        textField.setText("hello universe");
        try {
            method.invoke(dialog, (Object[]) null);
            readMinutes();
        } catch (Exception e) {
            fail("Cannot do a non-value value");
        }
        assertEquals(value, numMinutes, message + "a non-value");

        // positive integers too large for a short are capped
        textField.setText(String.valueOf(999)); // the text field for minutes can take three characters
        try {
            method.invoke(dialog, (Object[]) null);
            readMinutes();
        } catch (Exception e) {
            fail("Cannot do a large value");
        }
        assertEquals(Constants.MINUTES_SOFT_LIMIT, numMinutes, message + "a large value");

    }

    @Test
    public void testParseSeconds() {
        if (forgetIt) {
            fail("forget it: could not gain access: " + masterFail);
            return;
        }
        if (invalid) {
            fail("Was not able to create an instance: " + subFail);
            return;
        }

        String message = "Not the right number of seconds when passing ";
        String methodName = "parseSeconds";
        Method method;
        try {
            method = TestUtils.getDeclaredMethodAccessible(dialog, methodName, (Class<?>[]) null);
        } catch (Exception e) {
            fail("Could not access method \"" + methodName + "\"");
            return;
        }

        LimitedTextField textField = secondsTextField;
        int value = 12; // non-negative, but not larger than 59, that's important here

        // a proper value
        textField.setText(String.valueOf(value));
        try {
            method.invoke(dialog, (Object[]) null);
            readSeconds();
        } catch (Exception e) {
            fail("Cannot do a proper value");
        }
        assertEquals(value, numSeconds, message + "a proper value");

        // a negative value keeps the old one
        textField.setText("-5");
        try {
            method.invoke(dialog, (Object[]) null);
            readSeconds();
        } catch (Exception e) {
            fail("Cannot do a negative value");
        }
        assertEquals(value, numSeconds, message + "a negative value");

        // likewise, a not-integer-parsable keeps the old one
        textField.setText("hello space");
        try {
            method.invoke(dialog, (Object[]) null);
            readSeconds();
        } catch (Exception e) {
            fail("Cannot do a non-value value");
        }
        assertEquals(value, numSeconds, message + "a non-value");

        // positive integers too large are waived, too
        textField.setText(String.valueOf(99)); // the text field for seconds can take two characters
        try {
            method.invoke(dialog, (Object[]) null);
            readSeconds();
        } catch (Exception e) {
            fail("Cannot do a large value");
        }
        assertEquals(value, numSeconds, message + "a large value");
    }

    @Test
    public void testParseIncrement() {
        if (forgetIt) {
            fail("forget it: could not gain access: " + masterFail);
            return;
        }
        if (invalid) {
            fail("Was not able to create an instance: " + subFail);
            return;
        }

        String message = "Not the right number of increment when passing ";
        String methodName = "parseIncrement";
        Method method;
        try {
            method = TestUtils.getDeclaredMethodAccessible(dialog, methodName, (Class<?>[]) null);
        } catch (Exception e) {
            fail("Could not access method \"" + methodName + "\"");
            return;
        }

        LimitedTextField textField = incrementTextField;
        int value = 12; // non-negative, that's important here

        // a proper value
        textField.setText(String.valueOf(value));
        try {
            method.invoke(dialog, (Object[]) null);
            readIncrement();
        } catch (Exception e) {
            fail("Cannot do a proper value");
        }
        assertEquals(value, numIncrement, message + "a proper value");

        // a negative value keeps the old one
        textField.setText("-8");
        try {
            method.invoke(dialog, (Object[]) null);
            readIncrement();
        } catch (Exception e) {
            fail("Cannot do a negative value");
        }
        assertEquals(value, numIncrement, message + "a negative value");

        // likewise, a not-integer-parsable keeps the old one
        textField.setText("hello programmer");
        try {
            method.invoke(dialog, (Object[]) null);
            readIncrement();
        } catch (Exception e) {
            fail("Cannot do a non-value value");
        }
        assertEquals(value, numIncrement, message + "a non-value");

    }
}
