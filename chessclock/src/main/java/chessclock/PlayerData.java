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

import chessclock.gamesettings.GameSettingsPanel;
import time.TimeControlPhase;

/** A complete list of a player's data required for a game. Essentially, that's as you can adjust
 * in the {@link GameSettingsPanel game settings panel}.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public final class PlayerData {

    /** Player's name. */
    private final String name;
    /** Player's country. */
    private final Country country;
    /** List of time control phases. */
    private final TimeControlPhase[] timeControlPhases;
    /** Are time control phases additive? Or are they destructive? */
    private final boolean phasesAdditive;
    /** Time limit per move in nanoseconds. */
    private final long moveTimeLimit;
    /** Threshold for short-on-time-warning in nanoseconds. */
    private final long warningThreshold;

    /** Generates.
     *
     * @since 1.0;
     * @param name Player's name.
     * @param country Player's country.
     * @param timeControlPhases List of time control phases.
     * @param phasesAdditive Are time control phases additive ({@code true})? Or are they
     * destructive ({@code false})?
     * @param moveTimeLimit Time limit per move in nanoseconds.
     * @param warningThreshold Threshold for the visual short-on-time-warning in nanoseconds.
     */
    private PlayerData(String name, Country country, TimeControlPhase[] timeControlPhases, boolean phasesAdditive,
            long moveTimeLimit, long warningThreshold) {
        this.name = name;
        this.country = country;
        this.timeControlPhases = timeControlPhases;
        this.phasesAdditive = phasesAdditive;
        this.moveTimeLimit = moveTimeLimit;
        this.warningThreshold = warningThreshold;
    }

    /** Creates a new set of player data.
     *
     * @since 1.0;
     * @param name Player's name.
     * @param country Player's country.
     * @param timeControlPhases List of time control phases.
     * @param phasesAdditive Are time control phases additive ({@code true})? Or are they
     * destructive ({@code false})?
     * @param moveTimeLimit Time limit per move in nanoseconds.
     * @param warningThreshold Threshold for the visual short-on-time-warning in nanoseconds.
     * @return The player data or an empty optional in case you pass useless arguments.
     */
    public static Optional<PlayerData> makeNew(String name, Country country, TimeControlPhase[] timeControlPhases,
            boolean phasesAdditive, long moveTimeLimit, long warningThreshold) {
        if (country == null) {
            return Optional.empty();
        }
        if (timeControlPhases == null || timeControlPhases.length == 0) {
            return Optional.empty();
        }

        String useName = name != null ? name : Constants.EMPTY_STRING;
        PlayerData data = new PlayerData(useName, country, timeControlPhases, phasesAdditive, moveTimeLimit,
                warningThreshold);
        return Optional.ofNullable(data);
    }

    /** Returns the player's name.
     *
     * @since 1.0;
     * @return The player's name.
     */
    public final String getName() {
        return name;
    }

    /** Returns the country.
     *
     * @since 1.0;
     * @return The country.
     */
    public final Country getCountry() {
        return country;
    }

    /** Returns the time control phases.
     *
     * @since 1.0;
     * @return The time control phases.
     */
    public final TimeControlPhase[] getTimeControlPhases() {
        return timeControlPhases;
    }

    /** Are time control phases additive? Or are they destructive?
     *
     * @since 1.0;
     * @return Are time control phases additive? Or are they destructive?
     */
    public final boolean arePhasesAdditive() {
        return phasesAdditive;
    }

    /** Returns the time limit per move in nanoseconds.
     *
     * @since 1.0;
     * @return The time limit per move in nanoseconds.
     */
    public final long getMoveTimeLimit() {
        return moveTimeLimit;
    }

    /** Returns the threshold for the visual short-on-time-warning, in nanoseconds.
     *
     * @since 1.0;
     * @return The threshold for the visual short-on-time-warning, in nanoseconds.
     */
    public final long getWarningThreshold() {
        return warningThreshold;
    }

}
