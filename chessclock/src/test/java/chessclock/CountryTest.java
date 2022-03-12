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

public class CountryTest {

    private Country country;
    private static final String NAMEKEY = "wulpplulp";
    private static final String COUNTRYKEY = "xyz123";
    private static final String ICONFILE = "pause.png";

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
        country = new Country(NAMEKEY, COUNTRYKEY, ICONFILE);
    }

    @AfterEach
    public void tearDown() throws Exception {
        country = null;
    }

    @Test
    public void testGetNameKey() {
        assertEquals(NAMEKEY, country.getNameKey(), "Not the expected name key");
    }

    @Test
    public void testGetCodeKey() {
        assertEquals(COUNTRYKEY, country.getCodeKey(), "Not the expected code key");
    }

    @Test
    public void testGetFlagFile() {
        assertEquals(ICONFILE, country.getFlagFile(), "Not the expected flag file");
    }

}
