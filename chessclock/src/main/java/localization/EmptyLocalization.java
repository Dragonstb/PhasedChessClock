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

import chessclock.Constants;
import time.TimeControlPhase;

/** An empty localization where nearly every string is the empty one (""). Can serve as a template
 * for other localizations.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public class EmptyLocalization extends Loc {

    public EmptyLocalization() {
        super();
        setLanguageCode("empty");

        // ===== displayed strings =====
        addString("ChessClock " + Constants.VERSION, FRAME_TITLE);
        addString("", EXCEPTION_TITLE);
        addString("", INITIAL_NAME);
        addString("", LAUNCH_ERROR);
        addString("", PLAYER_DATA_ERROR);

        // clock panel
        addString("", MOVE);
        addString("", SETTINGS);
        addString("", PAUSE);
        addString("", CONTINUE);
        addString("", PAUSED);
        addString("", NOT_RUNNING_MOVE);

        // settings panel in general
        addString("", GAME_SETTINGS);
        addString("", ABOUT);
        addString("", OK);
        addString("", CANCEL);
        addString("", EXIT);

        // game settings panel
        addString("", SAME_BUDGET);
        addString("", SAME_MOVE_TIME_LIMIT);
        addString("", TIME_RUNS_IN_FIRST_MOVE);

        // TCP panel
        addString("", ADD_TCP_ABOVE);
        addString("", ADD_TCP_BELOW);
        addString("", TCP_UP);
        addString("", TCP_DOWN);
        addString("", REMOVE_TCP);
        addString("", EDIT_TCP);

        // player data panel
        addString("", ADDITIVE_TCP);
        addString("", DESTRUCTIVE_TCP);
        addString("", MOVE_TIME_LIMIT_SEC);
        addString("", TIME_WARNING_SEC);

        // add time control phase dialog
        addString("", ADD_TCP);
        addString("", MOVES);
        addString("", MINUTES);
        addString("", SECONDS);
        addString("", REMAINDER);
        addString("", INCREMENT);

        // ===== add country codes and names that differ from the English ones =====
        addString("", CountryNames.DEBUG_A_NAME);
        addString("", CountryCodes.DEBUG_A_CODE);

        addString("", CountryNames.EARTH_NAME);
        addString("", CountryCodes.EARTH_CODE);

        // sorts countries by localized name, so set the country names first
        super.setupListOfCountries();
    }

    @Override
    public String getTimeControlPhaseString(TimeControlPhase tcp) {
        long milliSec = tcp.getTime() % 1000;
        long sec = tcp.getTime() / 1000;
        long min = sec / 60;
        sec = sec % 60;
        String string;
        if (tcp.getMoves() != TimeControlPhase.REMAINDER) {
            string = tcp.getMoves() + " moves - ";
        } else {
            string = "remainder - ";
        }
        string += min + ":";
        if (sec < 10) {
            string += "0" + sec;
        } else {
            string += sec;
        }
        if (milliSec != 0) {
            string += "." + milliSec;
        }
        return string;
    }

}
