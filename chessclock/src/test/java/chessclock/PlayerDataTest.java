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

import java.util.Optional;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import time.TimeControlPhase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * <b>Note, failure of {@code testMakeNew(..)} automatically invalidates the other test methods, as
 * they depend on {@code makeNew(..)}.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public class PlayerDataTest {

    private static final Logger LOG = Logger.getLogger(PlayerDataTest.class.getName());

    private static final String NAME = "Hubert";
    private static final Country COUNTRY = Countries.EARTH;
    private static final TimeControlPhase PHASE_1 = TimeControlPhase.makeNew((short) 40, 120 * 60, 30);
    private static final TimeControlPhase PHASE_2 = TimeControlPhase.makeNew((short) 15, 30 * 60, 20);
    private static final TimeControlPhase PHASE_3 = TimeControlPhase.makeNew(TimeControlPhase.REMAINDER, 15 * 60, 10);
    private static final TimeControlPhase[] TCPS = new TimeControlPhase[]{PHASE_1, PHASE_2, PHASE_3};
    private static final boolean ADDITIVE = false;
    private static final long MOVE_TIME_LIMIT = -1;
    private static final long WARNING_AT = 150;

    private PlayerData data;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
        data = PlayerData.makeNew(NAME, COUNTRY, TCPS, ADDITIVE, MOVE_TIME_LIMIT, WARNING_AT).get();
    }

    @AfterEach
    public void tearDown() throws Exception {
        data = null;
    }

    @Test
    public void testMakeNew() {
        Optional<PlayerData> opt = PlayerData.makeNew(NAME, null, TCPS, ADDITIVE, MOVE_TIME_LIMIT, WARNING_AT);
        assertTrue(opt.isEmpty(), "No empty Optional following from missing country");

        opt = PlayerData.makeNew(NAME, COUNTRY, null, ADDITIVE, MOVE_TIME_LIMIT, WARNING_AT);
        assertTrue(opt.isEmpty(), "No empty Optional following from missing TCPs");

        opt = PlayerData.makeNew(NAME, COUNTRY, new TimeControlPhase[]{}, ADDITIVE, MOVE_TIME_LIMIT, WARNING_AT);
        assertTrue(opt.isEmpty(), "No empty Optional following from empty list of TCPs");

        opt = PlayerData.makeNew(NAME, COUNTRY, TCPS, ADDITIVE, MOVE_TIME_LIMIT, WARNING_AT);
        assertTrue(opt.isPresent(), "No present PlayerDara following from proper arguments");
    }

    @Test
    public void testGetName() {
        assertEquals(NAME, data.getName(), "Name mismatch");
    }

    @Test
    public void testGetCountry() {
        assertEquals(COUNTRY, data.getCountry(), "Country mismatch");
    }

    @Test
    public void testGetTimeControlPhases() {
        assertArrayEquals(TCPS, data.getTimeControlPhases(), "TCP mismatch");

        for (int tcp = 0; tcp < TCPS.length; tcp++) {
            assertEquals(TCPS[tcp].getMoves(), data.getTimeControlPhases()[tcp].getMoves(),
                    "Moves in phase " + tcp + " do not match");

            assertEquals(TCPS[tcp].getTime(), data.getTimeControlPhases()[tcp].getTime(),
                    "Time in phase " + tcp + " does not match");
            assertEquals(TCPS[tcp].getIncrement(), data.getTimeControlPhases()[tcp].getIncrement(),
                    "Increment in phase " + tcp + " do not match");
        }
    }

    @Test
    public void testArePhasesAdditive() {
        assertEquals(ADDITIVE, data.arePhasesAdditive(), "Phase cumulation mismatch");
    }

    @Test
    public void testGetMoveTimeLimit() {
        assertEquals(MOVE_TIME_LIMIT, data.getMoveTimeLimit(), "Move-time-limit mismatch");
    }

    @Test
    public void testGetWarningThreshold() {
        assertEquals(WARNING_AT, data.getWarningThreshold(), "Warning threshold mismatch");
    }
}
