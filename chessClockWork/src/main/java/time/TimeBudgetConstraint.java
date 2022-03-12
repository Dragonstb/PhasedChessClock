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

/** Constraints for the player's timing.
 * @author Dragonstb
 * @since 1.0;
 */
public final class TimeBudgetConstraint {

    /** List of time control phases. */
    private final TimeControlPhase[] tcps;
    /** Time limit for each move, in nanoseconds. Use a negative value to indicate a missing move time limit. */
    private final long moveTimeLimit;
    /** Threshold time below which a warning is sent to the display that the player is short on time,
     * in nanoseconds. */
    private final long warningThreshold;
    private final boolean cumulativePhases;

    /**
     * @param tcps List of time control phases.
     * @param moveTimeLimit Time limit for each move, in nanoseconds. Use a negative value to indicate a missing move time limit.
     * @param warningThreshold Threshold time below which a warning is sent to the display that the player is short on time,
     * in nanoseconds. Use a negative value to indicate that no warning may be sent.
     * @param cumulativePhases When switching to the next phase, is the time remaining from the last phase added
     * to the time for the new one?
     */
    private TimeBudgetConstraint(TimeControlPhase[] tcps, long moveTimeLimit, long warningThreshold,
            boolean cumulativePhases) {
        this.tcps = tcps;
        this.moveTimeLimit = moveTimeLimit;
        this.warningThreshold = warningThreshold;
        this.cumulativePhases = cumulativePhases;
    }

    /** Generates a new constraint.
     * @since 1.0;
     * @param tcps List of time control phases.
     * @param moveTimeLimit Time limit for each move, in nanoseconds. Use a negative value to indicate a missing move time limit.
     * @param warningThreshold Threshold time below which a warning is sent to the display that the player is short on time,
     * in nanoseconds. Use a negative value to indicate that no warning may be sent.
     * @param cumulativePhases When switching to the next phase, is the time remaining from the last phase added
     * to the time for the new one?
     * @return Optional with the constraint. If {@code tcps} is {@code null} or has no entry, an empty
     * optional is returned.
     */
    public static final Optional<TimeBudgetConstraint> makeNew(TimeControlPhase[] tcps, long moveTimeLimit,
            long warningThreshold, boolean cumulativePhases) {
        if (tcps == null || tcps.length < 1) {
            return Optional.empty();
        }

        TimeBudgetConstraint tbc = new TimeBudgetConstraint(tcps, moveTimeLimit, warningThreshold, cumulativePhases);
        return Optional.ofNullable(tbc);
    }

    /** Returns the list of time control phases.
     *
     * @since 1.0;
     * @return The tcps list of time control phases.
     */
    public final TimeControlPhase[] getTCPs() {
        return tcps;
    }

    /** Returns the time limit for each move. Negative values indicate that there is no such limit.
     *
     * @since 1.0
     * @return The move time limit, in nanoseconds.
     */
    public final long getMoveTimeLimit() {
        return moveTimeLimit;
    }

    /** Returns the time below which the short-on-time warning is turned on.
     *
     * @since 1.0;
     * @return The threshold time, in nanoseconds.
     */
    public final long getWarningThreshold() {
        return warningThreshold;
    }

    /** Returns if phases behave cumulative. If so, the time remaining at the end of a phase is
     * added to the time of the next phase.
     *
     * @since 1.0;
     * @return Phases are cumulative?
     */
    public final boolean isCumulativePhases() {
        return cumulativePhases;
    }

}
