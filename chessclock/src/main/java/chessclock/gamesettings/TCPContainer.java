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
package chessclock.gamesettings;

import localization.Loc;
import time.TimeControlPhase;

/** A container that store a {@link TimeControlPhase time control phase} (TCP) as well as the
 * localized String that describes the TCP and is displayed in a list on the {@link TCPPanel}.
 *
 * @author Dragonstb
 * @since 1.0;
 */
final class TCPContainer {

    /** The time control phase. */
    private final TimeControlPhase tcp;
    /** The description of the time control phase. */
    private String description;

    /** Generates.
     *
     * @since 1.0;
     */
    TCPContainer(TimeControlPhase tcp, Loc loc) {
        this.tcp = tcp;
        if (loc != null) {
            description = loc.getTimeControlPhaseString(tcp);
        } else {
            description = "TCP: " + tcp.getMoves() + ": " + tcp.getTime() + " + " + tcp.getIncrement();
        }
    }

    /** Updates the localization.
     *
     * @since 1.0;
     */
    void updateLocalization(Loc loc) {
        description = loc.getTimeControlPhaseString(tcp);
    }

    /** Returns the time control phase.
     *
     * @since 1.0;
     * @return The time control phase stored.
     */
    TimeControlPhase getTcp() {
        return tcp;
    }

    /** Returns the description of the time control phase.
     *
     * @since 1.0;
     * @return The description of the time control phase.
     */
    String getDescription() {
        return description;
    }

    /** Returns the description of the time control phase. Simply invokes {@code getDescription()};
     *
     * @since 1.0;
     * @return The description of the time control phase.
     * @see TCPContainer#getDescription();
     */
    @Override
    public String toString() {
        return getDescription();
    }

}
