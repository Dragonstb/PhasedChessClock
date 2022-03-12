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

import javax.swing.ImageIcon;

/** The data of a country is an individual ID code, a key string to its name in the currently used
 * localisation object, a further key to the county key code in the localisation, and a string with
 * the name of the flag.<br>
 * The ID code is used for array access, thus these values must be positive.
 * <b>As with 0.1, the ID is not used.</b>
 *
 * @author Dragonstb
 * @since 1.0;
 */
public final class Country {

    /** Localisation key to the name of the country. */
    private final String nameKey;
    /** Localisation key to the name of the country code. */
    private final String codeKey;
    /** Name of image file with flag, including file suffix. */
    private final String flagFile;
    /** Image icon with the flag. */
    private final ImageIcon flag;

    /** Generates.
     *
     * @since 1.0;
     * @param nameKey Localization key to the name of the country.
     * @param codeKey Localization key to the name of the country code.
     * @param flagFile Name of image file with flag, including file suffix.
     */
    public Country(String nameKey, String codeKey, String flagFile) {
        this.nameKey = nameKey;
        this.codeKey = codeKey;
        this.flagFile = flagFile;

        flag = Util.getImageIcon(Constants.FLAG_DIR + flagFile);
    }

    /** Returns the localisation key to the name of the country.
     *
     * @since 1.0;
     * @return The localisation key to the name of the country.
     */
    public final String getNameKey() {
        return nameKey;
    }

    /** Returns the localisation key to the name of the country code.
     *
     * @since 1.0;
     * @return The localisation key to the name of the country code.
     */
    public final String getCodeKey() {
        return codeKey;
    }

    /** Returns the name of image file with flag, including file suffix.
     *
     * @since 1.0;
     * @return The name of image file with flag, including file suffix.
     */
    public final String getFlagFile() {
        return flagFile;
    }

    /** Returns the image icon displaying the flag.
     *
     * @since 1.0;
     * @return The image icon displaying the flag.
     */
    public final ImageIcon getFlag() {
        return flag;
    }

}
