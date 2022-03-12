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
import java.lang.reflect.Method;


import additionalTesting.MockDisplay;
import additionalTesting.MockDisplay.HasMoveTimeLimit;
import additionalTesting.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTimeControlTest {

    private PlayerTimeControl ptc;
    private MockDisplay mockDisplay;

    private Field fieldTimeRemaining;
    private Field fieldIncrement;
    private Field fieldMoveTimeRemaining;
    private Field fieldNextPhaseAfterMove;
    private Field fieldCurrentPhase;
    private Field fieldMoveTime;
    private Field fieldHasMoveTimeLimit;
    private Field fieldAdditive;
    private Field fieldWarningThreshold;
    private Field fieldUseWarningColor;
    private Field fieldMove;
    private Field fieldTCPs;
    private Method methodNextTCP;
    private Method methodClearForNewMatch;

    private long timeRemaining;
    private long increment;
    private long moveTimeRemaining;
    private int nextPhaseAfterMove;
    private int currentPhase;
    private long moveTime;
    private boolean hasMoveTimeLimit;
    private boolean additive;
    private long warningThreshold;
    private boolean useWarningColor;
    private int move;
    private TimeControlPhase[] tcps;

    private String fail = null;

    public PlayerTimeControlTest() {
        try {
            fieldTimeRemaining = TestUtils.getFieldAccessible(PlayerTimeControl.class, "timeRemaining");
            fieldIncrement = TestUtils.getFieldAccessible(PlayerTimeControl.class, "increment");
            fieldMoveTimeRemaining = TestUtils.getFieldAccessible(PlayerTimeControl.class, "moveTimeRemaining");
            fieldNextPhaseAfterMove = TestUtils.getFieldAccessible(PlayerTimeControl.class, "nextPhaseAfterMove");
            fieldCurrentPhase = TestUtils.getFieldAccessible(PlayerTimeControl.class, "currentPhase");
            fieldMoveTime = TestUtils.getFieldAccessible(PlayerTimeControl.class, "moveTime");
            fieldHasMoveTimeLimit = TestUtils.getFieldAccessible(PlayerTimeControl.class, "hasMoveTimeLimit");
            fieldAdditive = TestUtils.getFieldAccessible(PlayerTimeControl.class, "additive");
            fieldWarningThreshold = TestUtils.getFieldAccessible(PlayerTimeControl.class, "warningThreshold");
            fieldUseWarningColor = TestUtils.getFieldAccessible(PlayerTimeControl.class, "useWarningColor");
            fieldMove = TestUtils.getFieldAccessible(PlayerTimeControl.class, "move");
            fieldTCPs = TestUtils.getFieldAccessible(PlayerTimeControl.class, "tcps");

            methodNextTCP = TestUtils.getDeclaredMethodAccessible(PlayerTimeControl.class, "nextTCP");
            methodClearForNewMatch = TestUtils.getDeclaredMethodAccessible(PlayerTimeControl.class, "clearForNewGame",
                    TimeBudgetConstraint.class);
        } catch (Exception e) {
            fail = "failed to access fields: " + e.getClass().getSimpleName() + "(" + e.getMessage() + ")";
        }
    }

    private void readTimeRemaining() throws IllegalArgumentException, IllegalAccessException {
        timeRemaining = fieldTimeRemaining.getLong(ptc);
    }

    private void readIncrement() throws IllegalArgumentException, IllegalAccessException {
        increment = fieldIncrement.getLong(ptc);
    }

    private void readMoveTimeRemaining() throws IllegalArgumentException, IllegalAccessException {
        moveTimeRemaining = fieldMoveTimeRemaining.getLong(ptc);
    }

    private void readNextPhaseAfterMove() throws IllegalArgumentException, IllegalAccessException {
        nextPhaseAfterMove = fieldNextPhaseAfterMove.getInt(ptc);
    }

    private void readCurrentPhase() throws IllegalArgumentException, IllegalAccessException {
        currentPhase = fieldCurrentPhase.getInt(ptc);
    }

    private void readMoveTime() throws IllegalArgumentException, IllegalAccessException {
        moveTime = fieldMoveTime.getLong(ptc);
    }

    private void readHasMoveTimeLimit() throws IllegalArgumentException, IllegalAccessException {
        hasMoveTimeLimit = fieldHasMoveTimeLimit.getBoolean(ptc);
    }

    private void readAdditive() throws IllegalArgumentException, IllegalAccessException {
        additive = fieldAdditive.getBoolean(ptc);
    }

    private void readUseWarningColor() throws IllegalArgumentException, IllegalAccessException {
        useWarningColor = fieldUseWarningColor.getBoolean(ptc);
    }

    private void readWarningThreshold() throws IllegalArgumentException, IllegalAccessException {
        warningThreshold = fieldWarningThreshold.getLong(ptc);
    }

    private void readMove() throws IllegalArgumentException, IllegalAccessException {
        move = fieldMove.getInt(ptc);
    }

    private void readTCPs() throws IllegalArgumentException, IllegalAccessException {
        tcps = (TimeControlPhase[]) fieldTCPs.get(ptc);
    }

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
        mockDisplay = new MockDisplay();
        ptc = new PlayerTimeControl();
        ptc.setDisplay(mockDisplay);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mockDisplay = null;
        ptc = null;
    }

    /**
     * also tests {@code updateTimePanelAndRepaint()}.
     *
     * @since 1.0;
     */
    @Test
    public void testSetupMatchTiming() {
        // first check
        TimeControlPhase[] tcps = new TimeControlPhase[]{TimeControlPhase.makeNew((short) 40, 7200, 30),
            TimeControlPhase.makeNew((short) 20, 3600, 20),
            TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 1800, 10)};
        long moveTimeLimit = -1 * Constants.NANOSEC_PER_SEC;
        long warningThreshold = 2002;
        boolean cumulativePhases = false;

        TimeBudgetConstraint tbc = TimeBudgetConstraint.makeNew(tcps, moveTimeLimit, warningThreshold, cumulativePhases)
                .get();

        ptc.setupGameTiming(tbc);

        assertEquals(HasMoveTimeLimit.no, mockDisplay.getHasMoveTimeLimit(),
                "Presence of move time limit not correctly announced");
        assertEquals(tcps[0].getTime() * Constants.NANOSEC_PER_SEC, mockDisplay.getClockTime(),
                "Not the right clock time");
        assertEquals(0, mockDisplay.getMoveTime(), "Not the right move time");
        assertTrue(mockDisplay.isDisplayUpToDate(), "Display has not been updated");

        // second check
        tcps = new TimeControlPhase[]{TimeControlPhase.makeNew((short) 10, 987, 10),
            TimeControlPhase.makeNew((short) 33, 123, 60), TimeControlPhase.makeNew((short) 8, 22222, 22),
            TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 1500, 0)};
        moveTimeLimit = 180 * Constants.NANOSEC_PER_SEC;
        warningThreshold = 500;
        cumulativePhases = true;

        tbc = TimeBudgetConstraint.makeNew(tcps, moveTimeLimit, warningThreshold, cumulativePhases).get();

        ptc.setupGameTiming(tbc);

        assertEquals(HasMoveTimeLimit.yes, mockDisplay.getHasMoveTimeLimit(),
                "Presence of move time limit not correctly announced");
        assertEquals(tcps[0].getTime() * Constants.NANOSEC_PER_SEC, mockDisplay.getClockTime(),
                "Not the right clock time");
        assertEquals(moveTimeLimit, mockDisplay.getMoveTime(),
                "Not the right move time");
        assertTrue(mockDisplay.isDisplayUpToDate(), "Display has not been updated");
    }

    @Test
    public void testClearForNewMatch() {
        if (fail != null) {
            fail(fail);
            return;
        }

        // first check
        TimeControlPhase[] tcps = new TimeControlPhase[]{TimeControlPhase.makeNew((short) 40, 7200, 30),
            TimeControlPhase.makeNew((short) 20, 3600, 20),
            TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 1800, 10)};
        long moveTimeLimit = -1;
        long warningThreshold = 2002;
        boolean cumulativePhases = false;

        TimeBudgetConstraint tbc = TimeBudgetConstraint.makeNew(tcps, moveTimeLimit, warningThreshold, cumulativePhases)
                .get();

        try {
            methodClearForNewMatch.invoke(ptc, tbc);
        } catch (Exception e) {
            fail("Failed to invoke CleaForNewMatch(..)");
            return;
        }

        try {
            readTimeRemaining();
            readNextPhaseAfterMove();
            readMoveTime();
            readHasMoveTimeLimit();
            readAdditive();
            readWarningThreshold();
            readUseWarningColor();
            readMove();
            readTCPs();
            readCurrentPhase();
        } catch (Exception e) {
            fail("Failed to access fields - " + e.getMessage());
            return;
        }

        assertEquals(0, timeRemaining, "Not the right time remaining");
        assertArrayEquals(tcps, this.tcps, "Not the right TCPs");
        assertEquals(moveTimeLimit, moveTime, "Not the right move time");
        assertEquals(moveTimeLimit > 0, hasMoveTimeLimit, "Not the right move time limit flag");
        assertEquals(cumulativePhases, additive, "Not the right cumulation flag");
        assertEquals(warningThreshold, this.warningThreshold, "Not the right warning threshold");
        assertEquals(0, nextPhaseAfterMove, "Not the right phase-change move");
        assertFalse(useWarningColor, "Not the right warning flag");
        assertEquals(1, move, "Not the right move");
        assertEquals(-1, currentPhase, "Not the right phase");

        // second check
        moveTimeLimit = 360;
        warningThreshold = 10000;
        cumulativePhases = true;

        tbc = TimeBudgetConstraint.makeNew(tcps, moveTimeLimit, warningThreshold, cumulativePhases).get();

        try {
            methodClearForNewMatch.invoke(ptc, tbc);
        } catch (Exception e) {
            fail("Failed to invoke CleaForNewMatch(..) 2");
            return;
        }

        try {
            readTimeRemaining();
            readNextPhaseAfterMove();
            readMoveTime();
            readHasMoveTimeLimit();
            readAdditive();
            readWarningThreshold();
            readUseWarningColor();
            readMove();
            readTCPs();
        } catch (Exception e) {
            fail("Failed to access fields 2");
            return;
        }

        assertEquals(0, timeRemaining, "Not the right time remaining");
        assertArrayEquals(tcps, this.tcps, "Not the right TCPs");
        assertEquals(moveTimeLimit, moveTime, "Not the right move time");
        assertEquals(moveTimeLimit > 0, hasMoveTimeLimit, "Not the right move time limit flag");
        assertEquals(cumulativePhases, additive, "Not the right cumulation flag");
        assertEquals(warningThreshold, this.warningThreshold, "Not the right warning threshold");
        assertEquals(0, nextPhaseAfterMove, "Not the right phase-change move");
        assertFalse(useWarningColor, "Not the right warning flag");
        assertEquals(1, move, "Not the right move");
        assertEquals(-1, currentPhase, "Not the right phase");

    }

    /** Tests when there is a next, limited TCP, with non-cumulative TCPs.
     *
     * @since 1.0;
     */
    @Test
    public void testNextTCP1() {
        if (fail != null) {
            fail(fail);
            return;
        }

        TimeControlPhase[] tcps = new TimeControlPhase[]{TimeControlPhase.makeNew((short) 40, 7200, 30),
            TimeControlPhase.makeNew((short) 20, 3600, 20),
            TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 1800, 10)};
        long moveTimeLimit = 360 * Constants.NANOSEC_PER_SEC;
        long warningThreshold = 2002;
        boolean cumulativePhases = false;

        TimeBudgetConstraint tbc = TimeBudgetConstraint.makeNew(tcps, moveTimeLimit, warningThreshold, cumulativePhases)
                .get();

        ptc.setupGameTiming(tbc);

        try {
            methodNextTCP.invoke(ptc);
        } catch (Exception e) {
            fail("Failed to invoke nextTCP()");
            return;
        }

        try {
            readTimeRemaining();
            readIncrement();
            readMoveTimeRemaining();
            readNextPhaseAfterMove();
            readCurrentPhase();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            fail("Failed to read field");
            return;
        }

        TimeControlPhase current = tbc.getTCPs()[1];
        assertEquals(current.getTime() * Constants.NANOSEC_PER_SEC, timeRemaining, "Not the right clock time");
        assertEquals(tbc.getMoveTimeLimit(), moveTimeRemaining, "Not the right move time");
        assertEquals(current.getMoves(), nextPhaseAfterMove, "Not the right phase-change move-number");
        assertEquals(current.getIncrement() * Constants.NANOSEC_PER_SEC, increment,
                "Not the right increment");
        assertEquals(1, currentPhase, "Not the right phase");
    }

    /**
     * Tests when there is a next, but unlimited TCP, with non-cumulative TCPs.
     *
     * @since 1.0;
     */
    @Test
    public void testNextTCP2() {
        if (fail != null) {
            fail(fail);
            return;
        }

        TimeControlPhase[] tcps = new TimeControlPhase[]{TimeControlPhase.makeNew((short) 40, 7200, 30),
            TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 1800, 10)};
        long moveTimeLimit = -29 * Constants.NANOSEC_PER_SEC;
        long warningThreshold = 2002;
        boolean cumulativePhases = false;

        TimeBudgetConstraint tbc = TimeBudgetConstraint.makeNew(tcps, moveTimeLimit, warningThreshold, cumulativePhases)
                .get();

        ptc.setupGameTiming(tbc);

        try {
            methodNextTCP.invoke(ptc);
        } catch (Exception e) {
            fail("Failed to invoke nextTCP()");
            return;
        }

        try {
            readTimeRemaining();
            readIncrement();
            readMoveTimeRemaining();
            readNextPhaseAfterMove();
            readCurrentPhase();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            fail("Failed to read field");
            return;
        }

        TimeControlPhase current = tbc.getTCPs()[1];
        TimeControlPhase before = tbc.getTCPs()[0];
        assertEquals(current.getTime() * Constants.NANOSEC_PER_SEC, timeRemaining, "Not the right clock time");
        assertEquals(tbc.getMoveTimeLimit(), moveTimeRemaining, "Not the right move time");
        assertEquals(-1, nextPhaseAfterMove, "Not the right phase-change move-number");
        assertEquals(current.getIncrement() * Constants.NANOSEC_PER_SEC, increment, "Not the right increment");
        assertEquals(1, currentPhase, "Not the right phase");
    }

    /** Tests when there is a next, limited, but last TCP, with non-cumulative TCPs.
     *
     * @since 1.0;
     */
    @Test
    public void testNextTCP3() {
        if (fail != null) {
            fail(fail);
            return;
        }

        TimeControlPhase[] tcps = new TimeControlPhase[]{TimeControlPhase.makeNew((short) 40, 7200, 30),
            TimeControlPhase.makeNew((short) 20, 1800, 10)};
        long moveTimeLimit = -29 * Constants.NANOSEC_PER_SEC;
        long warningThreshold = 2002;
        boolean cumulativePhases = false;

        TimeBudgetConstraint tbc = TimeBudgetConstraint.makeNew(tcps, moveTimeLimit, warningThreshold, cumulativePhases)
                .get();

        ptc.setupGameTiming(tbc);

        try {
            methodNextTCP.invoke(ptc);
        } catch (Exception e) {
            fail("Failed to invoke nextTCP()");
            return;
        }

        try {
            readTimeRemaining();
            readIncrement();
            readMoveTimeRemaining();
            readNextPhaseAfterMove();
            readCurrentPhase();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            fail("Failed to read field");
            return;
        }

        TimeControlPhase current = tbc.getTCPs()[1];
        TimeControlPhase before = tbc.getTCPs()[0];
        assertEquals(current.getTime() * Constants.NANOSEC_PER_SEC, timeRemaining,
                "Not the right clock time");
        assertEquals(tbc.getMoveTimeLimit(), moveTimeRemaining,
                "Not the right move time");
        assertEquals(-1, nextPhaseAfterMove,
                "Not the right phase-change move-number");
        assertEquals(current.getIncrement() * Constants.NANOSEC_PER_SEC, increment,
                "Not the right increment");
        assertEquals(1, currentPhase, "Not the right phase");
    }

    /** Tests when there is a next, limited TCP, with cumulative TCPs.
     *
     * @since 1.0;
     */
    @Test
    public void testNextTCP4() {
        if (fail != null) {
            fail(fail);
            return;
        }

        TimeControlPhase[] tcps = new TimeControlPhase[]{TimeControlPhase.makeNew((short) 40, 7200, 30),
            TimeControlPhase.makeNew((short) 20, 1800, 10),
            TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 1800, 10)};
        long moveTimeLimit = -29 * Constants.NANOSEC_PER_SEC;
        long warningThreshold = 2002;
        boolean cumulativePhases = true;

        TimeBudgetConstraint tbc = TimeBudgetConstraint.makeNew(tcps, moveTimeLimit, warningThreshold, cumulativePhases)
                .get();

        ptc.setupGameTiming(tbc);

        try {
            methodNextTCP.invoke(ptc);
        } catch (Exception e) {
            fail("Failed to invoke nextTCP()");
            return;
        }

        try {
            readTimeRemaining();
            readIncrement();
            readMoveTimeRemaining();
            readNextPhaseAfterMove();
            readCurrentPhase();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            fail("Failed to read field");
            return;
        }

        TimeControlPhase current = tbc.getTCPs()[1];
        TimeControlPhase before = tbc.getTCPs()[0];
        assertEquals((before.getTime() + current.getTime()) * Constants.NANOSEC_PER_SEC, timeRemaining,
                "Not the right clock time");
        assertEquals(tbc.getMoveTimeLimit(), moveTimeRemaining, "Not the right move time");
        assertEquals(current.getMoves(), nextPhaseAfterMove,
                "Not the right phase-change move-number");
        assertEquals(current.getIncrement() * Constants.NANOSEC_PER_SEC, increment,
                "Not the right increment");
        assertEquals(1, currentPhase, "Not the right phase");
    }

    @Test
    public void testSetActiveAndRepaint() {
        boolean active;
        active = true;
        ptc.setActiveAndRepaint(active);
        assertEquals(active, mockDisplay.isActive(), "Flag Active not set propertly");
        assertTrue(mockDisplay.isDisplayUpToDate(), "Display has not been updated");

        mockDisplay.setDisplayUpToDate(false);

        active = false;
        ptc.setActiveAndRepaint(active);
        assertEquals(active, mockDisplay.isActive(), "Flag Active not set propertly");
        assertTrue(mockDisplay.isDisplayUpToDate(), "Display has not been updated");
    }

    /** Test for a case when running out of clock-time
     *
     * @since 1.0;
     */
    @Test
    public void testUpdate1() {
        if (fail != null) {
            fail(fail);
            return;
        }

        int dt = 2; // in seconds
        TimeControlPhase[] tcps = new TimeControlPhase[]{
            TimeControlPhase.makeNew((short) 40, dt / 2, 30), // time set here is positive,
            // but less than dt
            TimeControlPhase.makeNew((short) 20, 3600, 20),
            TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 1800, 10)};
        long moveTimeLimit = 360 * Constants.NANOSEC_PER_SEC;
        long warningThreshold = 2002;
        boolean cumulativePhases = false;

        TimeBudgetConstraint tbc = TimeBudgetConstraint.makeNew(tcps, moveTimeLimit, warningThreshold, cumulativePhases)
                .get();
        ptc.setupGameTiming(tbc);

        dt *= Constants.NANOSEC_PER_SEC;
        boolean outOfTime = ptc.update(dt);

        assertTrue(outOfTime, "Not out of time");
        assertTrue(mockDisplay.isOutOfTime(), "Diplay not set out of time");

        try {
            readTimeRemaining();
        } catch (Exception e) {
            fail("Failed to access timeRemaining.");
            return;
        }
        assertEquals(0, timeRemaining, "Time remaining not set to zero");

        try {
            readMoveTimeRemaining();
        } catch (Exception e) {
            fail("Failed to access moveTimeRemaining.");
            return;
        }
        assertEquals(moveTimeLimit - dt, moveTimeRemaining, "Move-time remaining not correctly updated");
    }

    /** Test for a case when running out of move-time
     *
     * @since 1.0;
     */
    @Test
    public void testUpdate2() {
        if (fail != null) {
            fail(fail);
            return;
        }

        int dt = 2; // in seconds
        TimeControlPhase[] tcps = new TimeControlPhase[]{TimeControlPhase.makeNew((short) 40, 7200, 30),
            TimeControlPhase.makeNew((short) 20, 3600, 20),
            TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 1800, 10)};
        long moveTimeLimit = dt / 2; // time set here is positive, but less than dt
        long warningThreshold = 2002;
        boolean cumulativePhases = false;

        TimeBudgetConstraint tbc = TimeBudgetConstraint.makeNew(tcps, moveTimeLimit, warningThreshold, cumulativePhases)
                .get();
        ptc.setupGameTiming(tbc);

        boolean outOfTime = ptc.update(dt * Constants.NANOSEC_PER_SEC);

        assertTrue(outOfTime, "Not out of time");
        assertTrue(mockDisplay.isOutOfTime(), "Diplay not set out of time");

        try {
            readMoveTimeRemaining();
        } catch (Exception e) {
            fail("Failed to access moveTimeRemaining.");
            return;
        }
        assertEquals(0, moveTimeRemaining, "Move-time remaining not set to zero");

        try {
            readTimeRemaining();
        } catch (Exception e) {
            fail("Failed to access timeRemaining.");
            return;
        }
        assertEquals((tcps[0].getTime() - dt) * Constants.NANOSEC_PER_SEC, timeRemaining,
                "Clock-time remaining not correctly updated");
    }

    /** Test for a case when everything is fine.
     *
     * @since 1.0;
     */
    @Test
    public void testUpdate3() {
        if (fail != null) {
            fail(fail);
            return;
        }

        int dt = 1; // in seconds
        TimeControlPhase[] tcps = new TimeControlPhase[]{TimeControlPhase.makeNew((short) 40, 7200, 30),
            TimeControlPhase.makeNew((short) 20, 3600, 20),
            TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 1800, 10)};
        long moveTimeLimit = 360 * Constants.NANOSEC_PER_SEC;
        long warningThreshold = 2002;
        boolean cumulativePhases = false;

        TimeBudgetConstraint tbc = TimeBudgetConstraint.makeNew(tcps, moveTimeLimit, warningThreshold, cumulativePhases)
                .get();
        ptc.setupGameTiming(tbc);

        dt *= Constants.NANOSEC_PER_SEC;
        boolean outOfTime = ptc.update(dt);

        assertFalse(outOfTime, "Out of time while plenty of that left");
        assertFalse(mockDisplay.isOutOfTime(), "Diplay set out of time while plenty of that left");

        try {
            readMoveTimeRemaining();
        } catch (Exception e) {
            fail("Failed to access moveTimeRemaining.");
            return;
        }
        assertEquals(moveTimeLimit - dt, moveTimeRemaining, "Move-time remaining not correctly updated");

        try {
            readTimeRemaining();
        } catch (Exception e) {
            fail("Failed to access timeRemaining.");
            return;
        }
        assertEquals(tcps[0].getTime() * Constants.NANOSEC_PER_SEC - dt, timeRemaining,
                "Clock-time remaining not correctly updated");
    }

    public void testEndMoveAndRepaint() {
        fail("Not yet implemented");
    }

}
