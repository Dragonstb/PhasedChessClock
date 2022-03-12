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

import localization.CountryCodes;
import localization.CountryNames;

/** List of {@link Country countries}.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public interface Countries {

    /** <b>Earth</b>. */
    public static final Country EARTH = new Country(
            CountryNames.EARTH_NAME,
            CountryCodes.EARTH_CODE,
            "flagEarth.png");

    /** The bughunting Republic of <b>Debug Country A</b>. */
    public static final Country DEBUG_A = new Country(
            CountryNames.DEBUG_A_NAME,
            CountryCodes.DEBUG_A_CODE,
            "flagEarth.png"
    );
}
