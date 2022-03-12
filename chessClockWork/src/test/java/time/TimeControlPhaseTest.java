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


import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TimeControlPhaseTest {

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void testMakeNewTimeControlPhase() {
        TimeControlPhase tcp = TimeControlPhase.makeNew((short) 40, 3600, 30), copy;
        Optional<TimeControlPhase> opt = TimeControlPhase.makeNew(null);

        assertTrue(opt.isEmpty(), "Optional present besides invalid parameter");

        opt = TimeControlPhase.makeNew(tcp);
        assertTrue(opt.isPresent(), "No optional besides proper parameters");
        if (opt.isEmpty()) {
            return; // going on would just cause null pointers
        }
        copy = opt.get();
        assertEquals(tcp.getMoves(), copy.getMoves(), "Not the right numbers of moves");
        assertEquals(tcp.getTime(), copy.getTime(), "Not the right number of seconds");
        assertEquals(tcp.getIncrement(), copy.getIncrement(), "Not the right number of increment");
        assertTrue(tcp != copy, "Copy is actually the same");
    }

    @Test
    public void testEqualsObject() {
        TimeControlPhase tcp = TimeControlPhase.makeNew((short) 40, 3600, 30);
        TimeControlPhase copy = TimeControlPhase.makeNew(tcp).get();
        TimeControlPhase other = TimeControlPhase.makeNew((short) 20, 1800, 60);

        assertTrue(tcp.equals(tcp), "Does not equals itself");
        assertFalse(tcp.equals(null), "Does equals null");
        assertFalse(tcp.equals(new String("Hello")), "Does equals another class");
        assertTrue(tcp.equals(copy), "Does not equals a TCP with same values");
        assertFalse(tcp.equals(other), "Does equals a TCP with different values");
    }
}
