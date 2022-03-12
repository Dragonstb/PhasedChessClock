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

import localization.Loc;

/** Links a {@link Country Country} with the localized name.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public final class CountryContainer {

    /** The country. */
    private final Country country;
    /** The localized name. This is deduced in the contructor. */
    private final String localizedName;

    /** Generates and deduces the localized name from the given Localization.
     *
     * @since 1.0;
     * @param country The country.
     * @param loc Localization.
     */
    public CountryContainer(Country country, Loc loc) {
        this.country = country;
        localizedName = loc.retrieveString(country.getNameKey());
    }

    /** Returns the country.
     *
     * @since 1.0;
     * @return The country.
     */
    public final Country getCountry() {
        return country;
    }

    /** Returns the localized name.
     *
     * @since 1.0;
     * @return The localized Name.
     */
    public final String getLocalizedName() {
        return localizedName;
    }

    @Override
    public String toString() {
        return localizedName;
    }
}
