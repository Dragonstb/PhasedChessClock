/*
 * PhasedChessClock - clockwork (short "the phased clockwork" in the following)
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

 * Linking the phased clockwork statically or dynamically with other modules
 * is making a combined work based on the phased clockwork. Thus, the terms
 * and conditions of the GNU General Public License cover the whole combination.

 * As a special exception, the copyright holders of the phased clockwork give
 * you permission to combine the phased clockwork with free software programs
 * or libraries that are released under the GNU LGPL or any other licence and
 * with independent modules that communicate with the phased clockwork solely
 * through the public interface of the phased clockwork. You may copy and
 * distribute such a system following the terms of the GNU GPL for the phased
 * clockwork and the licenses of the other code concerned, provided that you
 * include the source code of that other code when and as the GNU GPL requires
 * distribution of source code and provided that you do not modify the public
 * interface of the phased clockwork.

 * Note that people who make modified versions of the phased clockwork are not
 * obligated to grant this special exception for their modified versions; it is
 * their choice whether to do so. The GNU General Public License gives
 * permission to release a modified version without this exception; this
 * exception also makes it possible to release a modified version which carries
 * forward this exception. If you modify the public interface of the phased
 * clockwork, this exception does not apply to your modified version of the
 * phased clockwork, and you must remove this exception when you distribute your
 * modified version.

 * This exception is an additional permission under section 7 of the GNU General Public License, version 3 (“GPLv3”)
 */
package time;


import java.lang.reflect.Field;


import additionalTesting.MockClockDisplay;
import additionalTesting.MockDisplay;
import additionalTesting.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TimeControlTest {

    private TimeControl timeControl;
    private MockClockDisplay display;

    private Field currentStateField;
    private Field switchActivePlayerField;
    private Field timeHasRanOutField;
    private Field currentPTCField;
    private Field timeControlLeftField;
    private Field timeControlRightField;
    private Field halfMoveField;
    private Field leftAtTurnField;

    private TimeControl.State currentState;
    private boolean switchActivePlayer;
    private boolean timeHasRanOut;
    private PlayerTimeControl currentPTC;
    private PlayerTimeControl timeControlLeft;
    private PlayerTimeControl timeControlRight;
    private int halfMove;
    private boolean leftAtTurn;

    private String fail = null;

    public TimeControlTest() {
        Class<?> cls = TimeControl.class;
        try {
            currentStateField = TestUtils.getFieldAccessible(cls, "currentState");
            switchActivePlayerField = TestUtils.getFieldAccessible(cls, "switchActivePlayer");
            timeHasRanOutField = TestUtils.getFieldAccessible(cls, "timeHasRanOut");
            currentPTCField = TestUtils.getFieldAccessible(cls, "currentPTC");
            timeControlLeftField = TestUtils.getFieldAccessible(cls, "timeControlLeft");
            timeControlRightField = TestUtils.getFieldAccessible(cls, "timeControlRight");
            halfMoveField = TestUtils.getFieldAccessible(cls, "halfMove");
            leftAtTurnField = TestUtils.getFieldAccessible(cls, "leftAtTurn");
        } catch (Exception e) {
            fail = "failed to access fields: " + e.getClass().getSimpleName() + "(" + e.getMessage() + ")";
        }
    }

    private void currentStateRead() throws IllegalArgumentException, IllegalAccessException {
        currentState = (TimeControl.State) currentStateField.get(timeControl);
    }

    private void switchActivePlayerRead() throws IllegalArgumentException, IllegalAccessException {
        switchActivePlayer = switchActivePlayerField.getBoolean(timeControl);
    }

    private void timeHasRanOutRead() throws IllegalArgumentException, IllegalAccessException {
        timeHasRanOut = timeHasRanOutField.getBoolean(timeControl);
    }

    private void currentPTCRead() throws IllegalArgumentException, IllegalAccessException {
        currentPTC = (PlayerTimeControl) currentPTCField.get(timeControl);
    }

    private void timeControlLeftRead() throws IllegalArgumentException, IllegalAccessException {
        timeControlLeft = (PlayerTimeControl) timeControlLeftField.get(timeControl);
    }

    private void timeControlRightRead() throws IllegalArgumentException, IllegalAccessException {
        timeControlRight = (PlayerTimeControl) timeControlRightField.get(timeControl);
    }

    private void halfMoveRead() throws IllegalArgumentException, IllegalAccessException {
        halfMove = halfMoveField.getInt(timeControl);
    }

    private void leftAtTurnRead() throws IllegalArgumentException, IllegalAccessException {
        leftAtTurn = leftAtTurnField.getBoolean(timeControl);
    }

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
        display = new MockClockDisplay();
        timeControl = TimeControl.makeNew(display).get();
        timeControl.startClock();
    }

    @AfterEach
    public void tearDown() throws Exception {
        timeControl.endSignalClock();
        timeControl = null;
        display = null;
    }

    @Test
    public void testNotifyMoveDone1() {
        if (fail != null) {
            fail(fail);
            return;
        }

        try {
            currentStateField.set(timeControl, TimeControl.State.running);
        } catch (Exception e1) {
            fail("Could not set field currentState");
            return;
        }

        boolean wannaSwitch = false;
        try {
            switchActivePlayerField.set(timeControl, wannaSwitch);
        } catch (Exception e) {
            fail("Could not set field switchActivePlayer");
            return;
        }

        int moveNo = 987;
        display.setMoveNumber(moveNo);
        timeControl.notifyMoveDone();

        try {
            currentStateRead();
            switchActivePlayerRead();
        } catch (Exception e) {
            fail("Could not read fields");
        }

        assertEquals(!wannaSwitch, switchActivePlayer, "Did not change switch flag");
        assertEquals(TimeControl.State.running, currentState, "State is not running anymore");
        assertEquals(moveNo, display.getMoveNumber(), "Not the right move number");
    }

    @Test
    public void testNotifyMoveDone2() {
        if (fail != null) {
            fail(fail);
            return;
        }

        try {
            currentStateField.set(timeControl, TimeControl.State.noneRunning);
        } catch (Exception e1) {
            fail("Could not set field currentState");
            return;
        }

        boolean wannaSwitch = false;
        try {
            switchActivePlayerField.set(timeControl, wannaSwitch);
        } catch (Exception e) {
            fail("Could not set field switchActivePlayer");
            return;
        }

        display.setMoveNumber(100); // should be set to unity by the call of notifyMoveDone()
        timeControl.notifyMoveDone();

        try {
            currentStateRead();
            switchActivePlayerRead();
        } catch (Exception e) {
            fail("Could not read fields");
        }

        assertEquals(wannaSwitch, switchActivePlayer, "Changed switch flag");
        assertEquals(TimeControl.State.running, currentState, "State is not running");
        assertEquals(1, display.getMoveNumber(), "Not the right move number");
    }

    @Test
    public void testNotifyMoveDone3() {
        if (fail != null) {
            fail(fail);
            return;
        }

        try {
            currentStateField.set(timeControl, TimeControl.State.paused);
        } catch (Exception e1) {
            fail("Could not set field currentState");
            return;
        }

        boolean wannaSwitch = false;
        try {
            switchActivePlayerField.set(timeControl, wannaSwitch);
        } catch (Exception e) {
            fail("Could not set field switchActivePlayer");
            return;
        }

        int moveNo = 543;
        display.setMoveNumber(moveNo);
        timeControl.notifyMoveDone();

        try {
            currentStateRead();
            switchActivePlayerRead();
        } catch (Exception e) {
            fail("Could not read fields");
        }

        assertEquals(wannaSwitch, switchActivePlayer, "Changed switch flag");
        assertEquals(TimeControl.State.paused, currentState, "State is not paused anymore");
        assertEquals(moveNo, display.getMoveNumber(), "Not the right move number");
    }

    @Test
    public void testNotifyMoveDone4() {
        if (fail != null) {
            fail(fail);
            return;
        }

//        try {
//            currentStateField.set(timeControl, TimeControl.State.firstMove);
//        } catch (Exception e1) {
//            fail("Could not set field currentState");
//            return;
//        }

        TimeControlPhase tcp1 = TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 100, 10);
        TimeControlPhase tcp2 = TimeControlPhase.makeNew(tcp1).get();
        long moveTimeLimit = -1;
        boolean additive = false;
        long warningThreshold = 600;

        TimeBudgetConstraint tbc1 = TimeBudgetConstraint
                .makeNew(new TimeControlPhase[]{tcp1}, moveTimeLimit, warningThreshold, additive).get();
        TimeBudgetConstraint tbc2 = TimeBudgetConstraint
                .makeNew(new TimeControlPhase[]{tcp2}, moveTimeLimit, warningThreshold, additive).get();

        boolean leftIsWhite = true;
        boolean timeRunsInFirstMove = false;
        timeControl.setupNewGameTiming(tbc1, tbc2, leftIsWhite, timeRunsInFirstMove);

        boolean wannaSwitch = false;
        try {
            switchActivePlayerField.set(timeControl, wannaSwitch);
        } catch (Exception e) {
            fail("Could not set field switchActivePlayer");
            return;
        }

        display.setMoveNumber(100); // should be set to unity by the call of notifyMoveDone()

        // first half move
        timeControl.notifyMoveDone();

        try {
            currentStateRead();
            switchActivePlayerRead();
        } catch (Exception e) {
            fail("Could not read fields");
        }

        assertEquals(wannaSwitch, switchActivePlayer, "Changed switch flag");
        assertEquals(TimeControl.State.firstMove, currentState, "Left first move too early");
        assertEquals(1, display.getMoveNumber(), "Not the right move number");

        // second half move
        timeControl.notifyMoveDone();

        try {
            currentStateRead();
            switchActivePlayerRead();
        } catch (Exception e) {
            fail("Could not read fields");
        }

        assertEquals(wannaSwitch, switchActivePlayer, "Changed switch flag");
        assertEquals(TimeControl.State.running, currentState, "Not in running state");
        assertEquals(2, display.getMoveNumber(), "Not the right move number");
    }

    @Test
    public void testNotifyPauseChangeRequest1() {
        if (fail != null) {
            fail(fail);
            return;
        }

        try {
            currentStateField.set(timeControl, TimeControl.State.running);
        } catch (Exception e1) {
            fail("Could not set field currentState");
            return;
        }

        display.setPausedState(false);
        timeControl.notifyPauseChangeRequest();

        try {
            currentStateRead();
        } catch (Exception e) {
            fail("Could not read fields");
        }

        assertEquals(TimeControl.State.paused, currentState, "State is not paused");
        assertEquals(true, display.isPaused(), "Not the correct pause state on clock display");
    }

    @Test
    public void testNotifyPauseChangeRequest2() {
        if (fail != null) {
            fail(fail);
            return;
        }

        try {
            currentStateField.set(timeControl, TimeControl.State.paused);
        } catch (Exception e1) {
            fail("Could not set field currentState");
            return;
        }

        display.setPausedState(true);
        timeControl.notifyPauseChangeRequest();

        try {
            currentStateRead();
        } catch (Exception e) {
            fail("Could not read fields");
        }

        assertEquals(TimeControl.State.running, currentState, "State is not running");
        assertEquals(false, display.isPaused(), "Not the correct pause state on clock display");
    }

    @Test
    public void testNotifyPauseChangeRequest3() {
        if (fail != null) {
            fail(fail);
            return;
        }

        try {
            currentStateField.set(timeControl, TimeControl.State.noneRunning);
        } catch (Exception e1) {
            fail("Could not set field currentState");
            return;
        }

        display.setPausedState(true);
        timeControl.notifyPauseChangeRequest();

        try {
            currentStateRead();
        } catch (Exception e) {
            fail("Could not read fields");
        }

        assertEquals(TimeControl.State.noneRunning, currentState, "State is not nonRunning");
        assertEquals(true, display.isPaused(), "Not the correct pause state on clock display");
    }

    /** Update when nothing happens. The clock keeps running for the same player.
     *
     * @since 1.0;
     */
    @Test
    public void testUpdate() {
        if (fail != null) {
            fail(fail);
            return;
        }

        int halfMove = 3;
        boolean leftIsWhite = true;
        TimeControlPhase tpc1 = TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 300, 10);
        TimeControlPhase tpc2 = TimeControlPhase.makeNew(tpc1).get();
        long moveTimeLimit = -1;
        boolean additive = false;
        long warningThreshold = 600;

        TimeBudgetConstraint tbc1 = TimeBudgetConstraint
                .makeNew(new TimeControlPhase[]{tpc1}, moveTimeLimit, warningThreshold, additive).get();
        TimeBudgetConstraint tbc2 = TimeBudgetConstraint
                .makeNew(new TimeControlPhase[]{tpc2}, moveTimeLimit, warningThreshold, additive).get();

        timeControl.setupNewGameTiming(tbc1, tbc2, leftIsWhite);
        try {
            currentStateField.set(timeControl, TimeControl.State.running);
            switchActivePlayerField.set(timeControl, false);
            halfMoveField.set(timeControl, halfMove);
        } catch (Exception e1) {
            fail("Could not set fields");
            return;
        }

        long dt = 1 * Constants.NANOSEC_PER_SEC;
        timeControl.update(dt);

        try {
            timeHasRanOutRead();
            currentPTCRead();
            timeControlLeftRead();
            timeControlRightRead();
            halfMoveRead();
            switchActivePlayerRead();
            leftAtTurnRead();

        } catch (Exception e) {
            fail("Could not read fields");
        }

        assertFalse(timeHasRanOut, "Time is flagged as out");
        assertEquals(timeControlLeft, currentPTC, "Not the right time control is active");
        assertEquals(halfMove, this.halfMove, "Not the right number of half moves");
        assertFalse(switchActivePlayer, "Switch-active-player flag set");
        assertEquals(leftIsWhite, leftAtTurn, "Left-at-turn is wrong");
    }

    /** Update when a change of player occurs.
     *
     * @since 1.0;
     */
    @Test
    public void testUpdate2() {
        if (fail != null) {
            fail(fail);
            return;
        }

        int halfMove = 3;
        boolean leftIsWhite = true;
        TimeControlPhase tpc1 = TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 300, 10);
        TimeControlPhase tpc2 = TimeControlPhase.makeNew(tpc1).get();
        long moveTimeLimit = -1;
        boolean additive = false;
        long warningThreshold = 600;

        TimeBudgetConstraint tbc1 = TimeBudgetConstraint
                .makeNew(new TimeControlPhase[]{tpc1}, moveTimeLimit, warningThreshold, additive).get();
        TimeBudgetConstraint tbc2 = TimeBudgetConstraint
                .makeNew(new TimeControlPhase[]{tpc2}, moveTimeLimit, warningThreshold, additive).get();

        timeControl.setupNewGameTiming(tbc1, tbc2, leftIsWhite);
        try {
            currentStateField.set(timeControl, TimeControl.State.running);
            switchActivePlayerField.set(timeControl, true); // !!!!! TRUE! that's the point
            halfMoveField.set(timeControl, halfMove);
        } catch (Exception e1) {
            fail("Could not set fields");
            return;
        }

        long dt = 1 * Constants.NANOSEC_PER_SEC;
        timeControl.update(dt);

        try {
            timeHasRanOutRead();
            currentPTCRead();
            timeControlLeftRead();
            timeControlRightRead();
            halfMoveRead();
            switchActivePlayerRead();
            leftAtTurnRead();

        } catch (Exception e) {
            fail("Could not read fields");
        }

        assertFalse(timeHasRanOut, "Time is flagged as out");
        assertEquals(timeControlRight, currentPTC, "Not the right time control is active");
        assertEquals(halfMove + 1, this.halfMove, "Not the right number of half moves");
        assertFalse(switchActivePlayer, "Switch-active-player flag set");
        assertFalse(leftAtTurn, "Left-at-turn is wrong");
    }

    /** Update when the time runs out
     *
     * @since 1.0;
     */
    @Test
    public void testUpdate3() {
        if (fail != null) {
            fail(fail);
            return;
        }

        int halfMove = 3;
        boolean leftIsWhite = true;
        TimeControlPhase tpc1 = TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 1, 10);
        TimeControlPhase tpc2 = TimeControlPhase.makeNew(tpc1).get();
        long moveTimeLimit = -1;
        boolean additive = false;
        long warningThreshold = 600;

        TimeBudgetConstraint tbc1 = TimeBudgetConstraint
                .makeNew(new TimeControlPhase[]{tpc1}, moveTimeLimit, warningThreshold, additive).get();
        TimeBudgetConstraint tbc2 = TimeBudgetConstraint
                .makeNew(new TimeControlPhase[]{tpc2}, moveTimeLimit, warningThreshold, additive).get();

        timeControl.setupNewGameTiming(tbc1, tbc2, leftIsWhite);
        try {
            currentStateField.set(timeControl, TimeControl.State.running);
            switchActivePlayerField.set(timeControl, false);
            halfMoveField.set(timeControl, halfMove);
        } catch (Exception e1) {
            fail("Could not set fields");
            return;
        }

        long dt = 2 * Constants.NANOSEC_PER_SEC;
        timeControl.update(dt);

        try {
            timeHasRanOutRead();
            currentPTCRead();
            timeControlLeftRead();
            timeControlRightRead();
            halfMoveRead();
            switchActivePlayerRead();
            leftAtTurnRead();

        } catch (Exception e) {
            fail("Could not read fields");
        }

        assertTrue(timeHasRanOut, "Time is not flagged as out");
        assertEquals(timeControlLeft, currentPTC, "Not the right time control is active");
        assertEquals(halfMove, this.halfMove, "Not the right number of half moves");
        assertFalse(switchActivePlayer, "Switch-active-player flag set");
        assertEquals(leftIsWhite, leftAtTurn, "Left-at-turn is wrong");
    }

    /** Update in first move when the time is not running here.
     *
     * @since 1.0;
     */
    @Test
    public void testUpdate4() {
        if (fail != null) {
            fail(fail);
            return;
        }

        boolean leftIsWhite = true;
        TimeControlPhase tcp1 = TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 100, 10);
        TimeControlPhase tcp2 = TimeControlPhase.makeNew(tcp1).get();
        long moveTimeLimit = -1;
        boolean additive = false;
        long warningThreshold = 600;

        TimeBudgetConstraint tbc1 = TimeBudgetConstraint
                .makeNew(new TimeControlPhase[]{tcp1}, moveTimeLimit, warningThreshold, additive).get();
        TimeBudgetConstraint tbc2 = TimeBudgetConstraint
                .makeNew(new TimeControlPhase[]{tcp2}, moveTimeLimit, warningThreshold, additive).get();

        MockDisplay playerDisplayLeft = new MockDisplay();
        timeControl.setPlayerTimeDisplay(0, playerDisplayLeft);

        // don't need the method with additional boolean here, as this method we use sets this boolean to false
        timeControl.setupNewGameTiming(tbc1, tbc2, leftIsWhite);
        try {
            currentStateField.set(timeControl, TimeControl.State.firstMove);
            switchActivePlayerField.set(timeControl, false);
        } catch (Exception e1) {
            fail("Could not set fields");
            return;
        }

        long dt = 100; // nanosecs

        // first half move
        timeControl.update(dt);

        try {
            timeHasRanOutRead();
            currentPTCRead();
            timeControlLeftRead();
            timeControlRightRead();
            halfMoveRead();
            switchActivePlayerRead();
            leftAtTurnRead();

        } catch (Exception e) {
            fail("Could not read fields");
        }

        assertEquals(tcp1.getTime() * Constants.NANOSEC_PER_SEC, playerDisplayLeft.getClockTime(),
                "Wrong number of nanoseconds on the clock");
        assertFalse(timeHasRanOut, "Time is flagged as out");
        assertEquals(timeControlLeft, currentPTC, "Not the right time control is active");
        assertEquals(halfMove, this.halfMove, "Not the right number of half moves");
        assertFalse(switchActivePlayer, "Switch-active-player flag set");
        assertEquals(leftIsWhite, leftAtTurn, "Left-at-turn is wrong");
    }

    /** Update in first move when the time <i>is</i> running here.
     *
     * @since 1.0;
     */
    @Test
    public void testUpdate5() {
        if (fail != null) {
            fail(fail);
            return;
        }

        boolean leftIsWhite = true;
        TimeControlPhase tcp1 = TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 100, 10);
        TimeControlPhase tcp2 = TimeControlPhase.makeNew(tcp1).get();
        long moveTimeLimit = -1;
        boolean additive = false;
        long warningThreshold = 600;

        TimeBudgetConstraint tbc1 = TimeBudgetConstraint
                .makeNew(new TimeControlPhase[]{tcp1}, moveTimeLimit, warningThreshold, additive).get();
        TimeBudgetConstraint tbc2 = TimeBudgetConstraint
                .makeNew(new TimeControlPhase[]{tcp2}, moveTimeLimit, warningThreshold, additive).get();

        MockDisplay playerDisplayLeft = new MockDisplay();
        timeControl.setPlayerTimeDisplay(0, playerDisplayLeft);

        // don't need the method with additional boolean here, as this method we use sets this boolean to false
        timeControl.setupNewGameTiming(tbc1, tbc2, leftIsWhite);
        try {
            currentStateField.set(timeControl, TimeControl.State.running);
            switchActivePlayerField.set(timeControl, false);
        } catch (Exception e1) {
            fail("Could not set fields");
            return;
        }

        long dt = 100; // nanosecs
        long expect = tcp1.getTime() * Constants.NANOSEC_PER_SEC - dt;

        // first half move
        timeControl.update(dt);

        try {
            timeHasRanOutRead();
            currentPTCRead();
            timeControlLeftRead();
            timeControlRightRead();
            halfMoveRead();
            switchActivePlayerRead();
            leftAtTurnRead();

        } catch (Exception e) {
            fail("Could not read fields");
        }

        assertEquals(expect, playerDisplayLeft.getClockTime(),
                "Wrong number of nanoseconds on the clock");
        assertFalse(timeHasRanOut, "Time is flagged as out");
        assertEquals(timeControlLeft, currentPTC, "Not the right time control is active");
        assertEquals(halfMove, this.halfMove, "Not the right number of half moves");
        assertFalse(switchActivePlayer, "Switch-active-player flag set");
        assertEquals(leftIsWhite, leftAtTurn, "Left-at-turn is wrong");
    }

    @Test
    public void testNotifyStoppingClock() {
        timeControl.notifyStoppingClock();
        assertTrue(display.isStopped(), "Has not been notified");
    }

    /** Test when the time <i>is</i> running in first move.
     * @since 1.0
     */
    @Test
    public void testSetupNewGameTiming1() {
        boolean leftIsWhite = true;
        TimeControlPhase tcp1 = TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 100, 10);
        TimeControlPhase tcp2 = TimeControlPhase.makeNew(tcp1).get();
        long moveTimeLimit = -1;
        boolean additive = false;
        long warningThreshold = 600;

        TimeBudgetConstraint tbc1 = TimeBudgetConstraint
                .makeNew(new TimeControlPhase[]{tcp1}, moveTimeLimit, warningThreshold, additive).get();
        TimeBudgetConstraint tbc2 = TimeBudgetConstraint
                .makeNew(new TimeControlPhase[]{tcp2}, moveTimeLimit, warningThreshold, additive).get();

        boolean timeRunsInFirstMove = true; // difference to test 2
        timeControl.setupNewGameTiming(tbc1, tbc2, leftIsWhite, timeRunsInFirstMove);

        try {
            timeHasRanOutRead();
            currentPTCRead();
            timeControlLeftRead();
            timeControlRightRead();
            halfMoveRead();
            switchActivePlayerRead();
            leftAtTurnRead();
            currentStateRead();
        } catch (Exception e) {
            fail("Could not read fields");
        }

        assertEquals(TimeControl.State.noneRunning, currentState, "Not in state noneRunning");
        assertFalse(timeHasRanOut, "Time is flagged as out");
        assertEquals(timeControlLeft, currentPTC, "Not the right time control is active");
        assertEquals(2, this.halfMove, "Not the right number of half moves");
        assertFalse(switchActivePlayer, "Switch-active-player flag set");
        assertEquals(leftIsWhite, leftAtTurn, "Left-at-turn is wrong");
    }

    /** Test when the time <i>is not</i> running in first move.
     * @since 1.0
     */
    @Test
    public void testSetupNewGameTiming2() {
        boolean leftIsWhite = true;
        TimeControlPhase tcp1 = TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 100, 10);
        TimeControlPhase tcp2 = TimeControlPhase.makeNew(tcp1).get();
        long moveTimeLimit = -1;
        boolean additive = false;
        long warningThreshold = 600;

        TimeBudgetConstraint tbc1 = TimeBudgetConstraint
                .makeNew(new TimeControlPhase[]{tcp1}, moveTimeLimit, warningThreshold, additive).get();
        TimeBudgetConstraint tbc2 = TimeBudgetConstraint
                .makeNew(new TimeControlPhase[]{tcp2}, moveTimeLimit, warningThreshold, additive).get();

        boolean timeRunsInFirstMove = false; // difference to test 1
        timeControl.setupNewGameTiming(tbc1, tbc2, leftIsWhite, timeRunsInFirstMove);

        try {
            timeHasRanOutRead();
            currentPTCRead();
            timeControlLeftRead();
            timeControlRightRead();
            halfMoveRead();
            switchActivePlayerRead();
            leftAtTurnRead();
            currentStateRead();
        } catch (Exception e) {
            fail("Could not read fields");
        }

        assertEquals(TimeControl.State.firstMove, currentState, "Wrong state");
        assertFalse(timeHasRanOut, "Time is flagged as out");
        assertEquals(timeControlLeft, currentPTC, "Not the right time control is active");
        assertEquals(2, this.halfMove, "Not the right number of half moves");
        assertFalse(switchActivePlayer, "Switch-active-player flag set");
        assertEquals(leftIsWhite, leftAtTurn, "Left-at-turn is wrong");
    }

}
