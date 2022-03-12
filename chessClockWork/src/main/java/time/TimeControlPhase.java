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

/** The time control phase determines the amount of time a player has for a certain amount of
 * moves.<br>
 * In case the time budget is not limited to a certain amount of moves, the constant {@code INF} can
 * be set for the move number.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public final class TimeControlPhase {

    /** The number of moves that represent "for the rest of the game". */
    public static final short REMAINDER = -111;

    /** The number of moves in this phase. */
    private final short moves;
    /** The time for this phase of the game, in seconds. */
    private final int time;
    /** The time added to the budget after each turn made, in seconds. */
    private final int increment;

    /** Generates.
     *
     * @since 1.0;
     * @param moves The number of moves in this phase. Non-positive values indicate a phase that lasts
     * for the rest of the game.
     * @param time The time for this phase of the game, in seconds. Becomes unity if non-positive.
     * @param increment The time added after each turn made, in seconds. Set to 0 for negative values.
     */
    private TimeControlPhase(short moves, int time, int increment) {
        this.moves = moves > 0 ? moves : REMAINDER;
        this.time = time > 0 ? time : 1;
        this.increment = Math.max(increment, 0);
    }

    /** Creates a new instance.
     * @since 1.0;
     * @param moves The number of moves in this phase. Non-positive values indicate a phase that lasts
     * for the rest of the game.
     * @param time The time for this phase of the game, in seconds. Becomes unity if non-positive.
     * @param increment The time added after each turn made, in seconds. Set to 0 for negative values.
     * @return The new instance.
     */
    public static TimeControlPhase makeNew(short moves, int time, int increment) {
        return new TimeControlPhase(moves, time, increment);
    }

    /** Creates a deep copy of the given phase.
     * @since 1.0;
     * @param tcp Original we take a copy from.
     * @return Optional with the copy. If {@code null} is passed, an empty optional is returned.
     */
    public static Optional<TimeControlPhase> makeNew(TimeControlPhase tcp) {
        if (tcp == null) {
            return Optional.empty();
        }

        TimeControlPhase newtcp = new TimeControlPhase(tcp.getMoves(), tcp.getTime(), tcp.getIncrement());
        return Optional.ofNullable(newtcp);
    }

    /** Returns the number of moves in this phase.
     *
     * @since 1.0;
     * @return The number of moves in this phase.
     */
    public final short getMoves() {
        return moves;
    }

    /** Returns the time for this phase of the game, in seconds.
     *
     * @since 1.0;
     * @return The time for this phase of the game, in seconds.
     */
    public final int getTime() {
        return time;
    }

    /** Returns the increment for this phase of the game.
     *
     * @since 1.0;
     * @return The increment for this phase of the game, in seconds.
     */
    public final int getIncrement() {
        return increment;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + increment;
        result = prime * result + moves;
        result = prime * result + time;
        return result;
    }

    /** Checks of {@code this} and {@code other} represent time control phases of equal conditions.
     *
     * @since 1.0;
     * @param obj The object that might or might not be equal to {@code this}.
     * @return {@code True} if and only if {@code obj} is a non-null instance of
     * {@code TimeControlPhase} and all {@code moves}, {@code time}, and {@code increment} have the
     * same value in both {@code this} and {@code obj}.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof TimeControlPhase)) {
            return false;
        }

        TimeControlPhase other = (TimeControlPhase) obj;
        return moves == other.moves
                && time == other.time
                && increment == other.increment;
    }

}
