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

import localization.EnglishLocalization;
import localization.Loc;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CountryContainerTest {

    private CountryContainer container;
    private static final Loc LOC = new EnglishLocalization();
    private static final Country COUNTRY = Countries.EARTH;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
        container = new CountryContainer(COUNTRY, LOC);
    }

    @AfterEach
    public void tearDown() throws Exception {
        container = null;
    }

    @Test
    public void testGetCountry() {
        assertEquals(COUNTRY, container.getCountry(), "Wrong country");
    }

    @Test
    public void testGetLocalizedName() {
        assertEquals(LOC.retrieveString(COUNTRY.getNameKey()), container.getLocalizedName(), "Not the right name");
    }

    @Test
    public void testToString() {
        assertEquals(LOC.retrieveString(COUNTRY.getNameKey()), container.toString(), "Not the expected String");
    }
}
