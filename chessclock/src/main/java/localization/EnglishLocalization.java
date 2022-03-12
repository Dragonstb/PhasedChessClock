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
package localization;

import time.Constants;
import time.TimeControlPhase;

/** An English (uk) localization.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public final class EnglishLocalization extends Loc {

    public EnglishLocalization() {
        super();
        setLanguageCode("en-en");
        super.loadDefaultCountryDescriptions();
        super.loadDefaultDisplayStrings();
        super.setupListOfCountries();
    }

    @Override
    public String getTimeControlPhaseString(TimeControlPhase tcp) {
        int moves = tcp.getMoves();
        int min = tcp.getTime() / Constants.SEC_PER_MIN;
        int sec = tcp.getTime() % Constants.SEC_PER_MIN;

        StringBuilder buf = new StringBuilder();

        if (moves > 1) {
            buf.append(moves).append(" moves: ");
        } else if (moves != TimeControlPhase.REMAINDER) {
            buf.append(moves).append(" move: ");
        } else {
            buf.append("remainder: ");
        }

        buf.append(min).append(" min, ");

        if (sec > 9) {
            buf.append(sec);
        } else {
            buf.append("0").append(sec);
        }

        buf.append(" sec + ").append(tcp.getIncrement()).append(" sec/move");

        return buf.toString();
    }

}
