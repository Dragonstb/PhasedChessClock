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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.stream.Collectors;

import chessclock.Countries;
import chessclock.Country;
import chessclock.CountryContainer;
import time.Constants;
import time.TimeControlPhase;

/** An abstract localisation.<br>
 * The constant fields of this class are used as keys for the data base.<br>
 *
 * @author Dragonstb
 * @since 1.0;
 */
public abstract class Loc {

    /** The data set collecting the strings encoded by keys. */
    private final TreeMap<String, String> set = new TreeMap<>();
    /** Language code. */
    private String languageCode = "12345";
    /** List of available countries, together with their localized names. */
    private CountryContainer[] countries;

    Loc() {
    }

    /** Loads English texts for the labels that may appear on the screen.
     *
     * @since 1.0;
     */
    void loadDefaultDisplayStrings() {
        addString("ChessClock " + chessclock.Constants.VERSION, FRAME_TITLE);
        addString("ChessClock Exception", EXCEPTION_TITLE);
        addString("Enter name", INITIAL_NAME);
        addString("Launching has failed.", LAUNCH_ERROR);
        addString(
                "Could not process player data." + System.getProperty("line.separator")
                + "Please try again. If this does"
                + " not work, clear and redo the lists of time control phases, and try again.",
                PLAYER_DATA_ERROR);

        // clock panel
        addString("move", MOVE);
        addString("Settings", SETTINGS);
        addString("Pause", PAUSE);
        addString("Continue", CONTINUE);
        addString("Paused", PAUSED);
        addString("--", NOT_RUNNING_MOVE);

        // settings panel in general
        addString("Game settings", GAME_SETTINGS);
        addString("About", ABOUT);
        addString("OK", OK);
        addString("Cancel", CANCEL);
        addString("Exit", EXIT);

        // game settings panel
        addString("Same phases of time control", SAME_BUDGET);
        addString("Same time limits per move", SAME_MOVE_TIME_LIMIT);

        // TCP panel
        addString("Add above", ADD_TCP_ABOVE);
        addString("Add below", ADD_TCP_BELOW);
        addString("Up", TCP_UP);
        addString("Down", TCP_DOWN);
        addString("Remove", REMOVE_TCP);
        addString("Edit", EDIT_TCP);

        // player data panel
        addString("Remaining time is added to new phase", ADDITIVE_TCP);
        addString("Remaining time is waived after phase", DESTRUCTIVE_TCP);
        addString("Time limit for each move, in seconds:", MOVE_TIME_LIMIT_SEC);
        addString("Short-on-time warning, seconds before time\'s up:", TIME_WARNING_SEC);

        // add time control phase dialog
        addString("Add", ADD_TCP);
        addString("Moves:", MOVES);
        addString("Minutes:", MINUTES);
        addString("Seconds:", SECONDS);
        addString("For the remainder of the game?", REMAINDER);
        addString("Increment in sec/move:", INCREMENT);
    }

    /** Loads English names and coudes of the countries.
     *
     * @since 1.0;
     */
    void loadDefaultCountryDescriptions() {

        addString("Debug Country A", CountryNames.DEBUG_A_NAME);
        addString("DBA", CountryCodes.DEBUG_A_CODE);

        addString("Earth", CountryNames.EARTH_NAME);
        addString("TER", CountryCodes.EARTH_CODE);

    }

    /** Sets the list of countries up and sorts all countries alphabetically, respecting the
     * localization. The earth is added to the beginning of the list in any case.
     *
     * @since 1.0;
     */
    void setupListOfCountries() {
        Country[] countryArray = new Country[]{
            Countries.DEBUG_A
        };

        // create list of countries including localized names
        var unsortedCountries = new ArrayList<CountryContainer>();
        Arrays.stream(countryArray).forEach(country -> unsortedCountries.add(new CountryContainer(country, Loc.this)));
        // from this, derive list sorted by localized name
        Comparator<CountryContainer> comp = (cc1, cc2) -> cc1.getLocalizedName().compareTo(cc2.getLocalizedName());
        var sortedCountries = unsortedCountries.stream().sorted(comp).collect(Collectors.toList());

        // add some special countries out of alphabetical order
        sortedCountries.add(0, new CountryContainer(Countries.EARTH, this));

        countries = new CountryContainer[sortedCountries.size()];
        sortedCountries.toArray(countries);
    }

    /** Adds a string to the localisation data base.
     *
     * @since 1.0;
     * @param string String to be stored.
     * @param key Key under which the string becomes stored.
     */
    public final void addString(String string, String key) {
        set.put(key, string);
    }

    /** Retrieves a string from the localisation data base. If the string cannot be found, the key
     * embraced by pointy crackets is returned instead.
     *
     * @since 1.0;
     * @param key Key for retrieving the string.
     * @return The string stored or {@code "<key>"} if no string has been found.
     */
    public final String retrieveString(String key) {
        String string = set.get(key);
        return string != null ? string : "<" + key + ">";
    }

    /** Returns the language code. This code is in the
     * format of {@code ac-cd}. Here, {@code ab} is the language and {@code cd} refers to a local
     * variant, like french french and belgian french.
     *
     * @since 1.0;
     * @return Language code.
     */
    public final String getLanguageCode() {
        return languageCode;
    }

    /** Sets the language code as specified in interface {@link LangKeys}. This code is in the
     * format of {@code ac-cd}. Here, {@code ab} is the language and {@code cd} refers to a local
     * variant, like french french and belgian french.
     *
     * @since 1.0;
     * @param languageCode Language code.
     */
    final void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    /** Returns the list of countries.
     *
     * @since 1.0;
     * @return The countries.
     */
    public final CountryContainer[] getCountries() {
        return countries;
    }

    /** Returns a default string representation of the
     * {@link time.TimeControlPhase time control phase}
     *
     * @since 1.0;
     * @param tcp The time control phase to be represented.
     * @return A string representation of the time control phase.
     */
    public String getTimeControlPhaseString(TimeControlPhase tcp) {
        String string;
        int moves = tcp.getMoves();
        int min = tcp.getTime() / Constants.SEC_PER_MIN;
        int sec = tcp.getTime() % Constants.SEC_PER_MIN;

        if (moves > 1) {
            string = moves + " moves - ";
        } else if (moves != TimeControlPhase.REMAINDER) {
            string = moves + " move - ";
        } else {
            string = "remainder - ";
        }

        string += min + ":";

        if (sec > 9) {
            string += String.valueOf(sec);
        } else {
            string += "0" + sec;
        }

        string += " + " + tcp.getIncrement() + " sec/move";

        return string;
    }

    /** Returns string representations displaying the time remaining given in the number of
     * milliseconds, ceiled to the next full deci-second.<br>
     * The abstract {@link Loc} class provides a default implementation. The return is in the in the
     * format {@code min:sec} at index 0, with the seconds being two digits always. At index 1, a
     * string in the format {@code .tens}, showing the tens of a second, can be found.<br>
     * Subclasses can overwrite this method if they want a different format.
     *
     * @since 1.0;
     * @param time Time to be represented in milliseconds.
     * @return String representations of the time. First minutes and seconds, second tens of a
     * second.
     */
    public String[] getTimeStrings(long time) {
        long tens = time / Constants.NANOSEC_PER_TENS_OF_SEC;
        if (time % Constants.NANOSEC_PER_TENS_OF_SEC != 0) {
            tens++;// ceiling
        }
        long sec = tens / Constants.DECISEC_PER_SEC;
        long min = sec / Constants.SEC_PER_MIN;
        sec %= Constants.SEC_PER_MIN;
        tens %= Constants.DECISEC_PER_SEC;

        String minSecString, tensString;
        if (sec > 9) {
            minSecString = min + ":" + sec;
        } else {
            minSecString = min + ":0" + sec;
        }
        tensString = "." + tens;

        return new String[]{minSecString, tensString};
    }

    // +++++++ some unique keys +++++++
    // ======= misc ==============
    /** The title of the window frame. */
    public static final String FRAME_TITLE = "frame_title";
    /** The title of the frames of exception messages. */
    public static final String EXCEPTION_TITLE = "exception_title";
    /** An initial name for the name field on the
     * {@link chessclock.gamesettings.PlayerDataPanel PlayerDataPanel}. */
    public static final String INITIAL_NAME = "initial_name";
    /** Key for the text displayed in error window poping up when the launch fails. */
    public static final String LAUNCH_ERROR = "launch_error";
    public static final String PLAYER_DATA_ERROR = "player_data_error";

    // ======= clock panel =======
    /** The word displayed over the number of the current move. */
    public static final String MOVE = "move";
    /** The label on the settings button. */
    public static final String SETTINGS = "settings";
    /** The label on the pause button when the game is running. */
    public static final String PAUSE = "pause";
    /** The label on the pause button when the game is paused. */
    public static final String CONTINUE = "continue";
    /** The paused text appearing when the game is paused. */
    public static final String PAUSED = "paused";
    /** The text displayed as move number when there is actually <i>no competition ongoing</i>
     * (i.e. while waiting for white to do the first move). */
    public static final String NOT_RUNNING_MOVE = "not_running_move";

    // ======= settings panel =======
    /** Title of game settings tab. */
    public static final String GAME_SETTINGS = "game_settings";
    /** Label of the OK button. */
    public static final String OK = "ok";
    /** Label of the cancel button. */
    public static final String CANCEL = "cancel";
    /** Label of the exit button. */
    public static final String EXIT = "exit";

    /** Title of about tab. */
    public static final String ABOUT = "about";

    /** Check box: same time contingent for all competitors. */
    public static final String SAME_BUDGET = "same_budget";
    /** Check box: same increment per move for all competitors. */
    public static final String SAME_INCREMENT = "same_increment";
    /** Check box: same time limit per move for all competitors. */
    public static final String SAME_MOVE_TIME_LIMIT = "same_move_time_limit";
    /** label: player left. */
    public static final String PLAYER_LEFT = "player_left";
    /** label: player right. */
    public static final String PLAYER_RIGHT = "player_right";
    /** Button: Add time control phase above. */
    public static final String ADD_TCP_ABOVE = "add_tcp_above";
    /** Button: add time control phase below. */
    public static final String ADD_TCP_BELOW = "add_tcp_below";
    /** Button: shift time control phase up. */
    public static final String TCP_UP = "tcp_up";
    /** Button: shift time control phase down. */
    public static final String TCP_DOWN = "tcp_down";
    /** Button: remove time control phase. */
    public static final String REMOVE_TCP = "remove_tcp";
    /** Button: edit time control phase. */
    public static final String EDIT_TCP = "edit_tcp";
    /** Radio Button: additive behaviour of time control phases. */
    public static final String ADDITIVE_TCP = "additive_tcp";
    /** Radio Button: destroying behaviour of time control phases. */
    public static final String DESTRUCTIVE_TCP = "destructive_tcp";
    /** Game settings - Check box: Enable or disable short-on-time-warning. */
    public static final String TIME_WARNING_SEC = "time_warning_sec";
    /** Game settings - Check box: Enable or disable move time limit. */
    public static final String MOVE_TIME_LIMIT_SEC = "move_time_limit_sec";

    // ======= Add Time Control Phase dialog =======
    /** Dialog: Add time control phase. */
    public static final String ADD_TCP = "add_tcp";
    /** Dialog: Moves. */
    public static final String MOVES = "moves";
    /** Dialog: Minutes. */
    public static final String MINUTES = "minutes";
    /** Dialog: Seconds. */
    public static final String SECONDS = "seconds";
    /** Dialog: Remainder? */
    public static final String REMAINDER = "remainder";
    /** Dialog: Increment? */
    public static final String INCREMENT = "increment";

}
