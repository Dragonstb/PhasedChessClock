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
package additionalTesting;

import time.PlayerTimeDisplay;

/** An implementation of the interface {@link PlayerTimeDisplay PlayerTimeDisplay} with the fields
 * that are modified by the interface methods being public.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public class MockDisplay implements PlayerTimeDisplay {

    public static enum HasMoveTimeLimit {
        notSet, yes, no
    }

    long clockTime = 0;
    long moveTime = 0;
    boolean active = false;
    boolean useWarningColor;
    boolean withMoveTimeLimit;
    HasMoveTimeLimit hasMoveTimeLimit = HasMoveTimeLimit.notSet;
    boolean outOfTime = false;
    boolean displayUpToDate = false;

    @Override
    public void setClockTime(long time) {
        clockTime = time;

    }

    @Override
    public void setMoveTime(long time) {
        moveTime = time;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void useWarningColor(boolean use) {
        useWarningColor = use;
    }

    @Override
    public void setupForGame(boolean withMoveTimeLimit) {
        hasMoveTimeLimit = withMoveTimeLimit ? HasMoveTimeLimit.yes : HasMoveTimeLimit.no;
    }

    @Override
    public void setOutOfTime() {
        outOfTime = true;
    }

    @Override
    public void updateDisplay() {
        displayUpToDate = true;
    }

    public final long getClockTime() {
        return clockTime;
    }

    public final long getMoveTime() {
        return moveTime;
    }

    public final boolean isActive() {
        return active;
    }

    public final boolean isUseWarningColor() {
        return useWarningColor;
    }

    public final boolean isWithMoveTimeLimit() {
        return withMoveTimeLimit;
    }

    public final HasMoveTimeLimit getHasMoveTimeLimit() {
        return hasMoveTimeLimit;
    }

    public final boolean isOutOfTime() {
        return outOfTime;
    }

    public final boolean isDisplayUpToDate() {
        return displayUpToDate;
    }

    public final void setDisplayUpToDate(boolean displayUpToDate) {
        this.displayUpToDate = displayUpToDate;
    }

}
