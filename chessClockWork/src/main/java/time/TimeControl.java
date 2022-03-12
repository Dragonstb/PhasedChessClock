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

/** The time control which is the heart of this chess clock. To be fair, this class leaves all the
 * details of accounting to the {@link PlayerTimeControl PlayerTimeCOntrol}. But this class manages everything
 *
 * @author Dragonstb
 * @since 1.0;
 */
public final class TimeControl {

    /** The value {@code halfMove} starts with. In the end, {@code halfMove/2}, which is an integer
     * division, must always be the number current (full) move, beginning at 1.
     */
    private static final byte INI_HALF_MOVE = 2;

    private static final ClockDisplay NULL_CLOCK = new ClockDisplay() {

        @Override
        public void setPausedState(boolean pause) {
        }

        @Override
        public void setMoveNumber(int moveNo) {
        }

        @Override
        public void notifyStoppingClock() {
        }
    };

    static enum State {
        /** No game is running. */
        noneRunning,
        /** A game is running currently. */
        running,
        /** A running game is currently paused. */
        paused
    }

    /** Current state. */
    private State currentState = State.noneRunning;
    /** Is it the left player's turn at the moment? */
    private boolean leftAtTurn = true;
    /** Is the active playe to be switched? */
    private boolean switchActivePlayer = false;
    /** Time has ran out? */
    private boolean timeHasRanOut = false;
    /** Counts the current half move. It starts at {@code INI_HALF_MOVE} and is increased by one
     * whenever a turn is done. */
    private int halfMove = INI_HALF_MOVE;
    /** The actual clock that provides the timing signal. */
    private final SignalClock signalClock;
    /** The time control for the left player. */
    private final PlayerTimeControl timeControlLeft;
    /** The time control for the right player. */
    private final PlayerTimeControl timeControlRight;
    /** The currently active player time control. */
    private PlayerTimeControl currentPTC;
    /** The clock display that is responsible for displaying the times. */
    private final ClockDisplay clockDisplay;

    /** Generates.
     *
     * @since 1.0;
     * @param clockPanel The clock display that is responsible for displaying the times.
     */
    private TimeControl(ClockDisplay clockDisplay) {
        this.clockDisplay = clockDisplay != null ? clockDisplay : NULL_CLOCK;

        timeControlLeft = new PlayerTimeControl();
        timeControlRight = new PlayerTimeControl();
        currentPTC = timeControlLeft;

        signalClock = new SignalClock(this);
    }

    /** Creates a new instance.
     * @since 1.0;
     * @param clockDisplay The clock display that is responsible for displaying the times.
     * @return An optional with the new instance.
     */
    public static Optional<TimeControl> makeNew(ClockDisplay clockDisplay) {
        TimeControl control = new TimeControl(clockDisplay);
        return Optional.ofNullable(control);
    }

    /** Sets the recipient of a player's timing data.
     * @since 1.0;
     * @param playerNo Index of player. The left player is 0, the right one is 1. Does nothign for
     * other numbers.
     * @param display The recipient for the data.
     */
    public void setPlayerTimeDisplay(int playerNo, PlayerTimeDisplay display) {
        switch (playerNo) {
            case 0 ->
                timeControlLeft.setDisplay(display);
            case 1 ->
                timeControlRight.setDisplay(display);
        }
    }

    /** Sets all data for a new game and tells all displays what they need to know now.
     *
     * @since 1.0;
     * @param dataLeft Left player's data.
     * @param dataRight Right player's data.
     * @param leftIsWhite Does the left player plays with the white pieces?
     */
    public void setupNewGameTiming(TimeBudgetConstraint dataLeft, TimeBudgetConstraint dataRight, boolean leftIsWhite) {
        if (dataLeft == null || dataRight == null) {
            return;
        }

        switchActivePlayer = timeHasRanOut = false;
        halfMove = INI_HALF_MOVE;
        clockDisplay.setPausedState(false);

        currentState = State.noneRunning;
        leftAtTurn = leftIsWhite;
        signalClock.flagHoldOn();

        timeControlLeft.setupGameTiming(dataLeft);
        timeControlRight.setupGameTiming(dataRight);
        currentPTC = leftIsWhite ? timeControlLeft : timeControlRight;

        clockDisplay.setMoveNumber(-1);
    }

    /** Called when a move is done. Invoking this method is the digital equivalent of pressing the
     * lever on a physical chess clock. It causes that the time is taken from the opponents budget
     * from now on.
     *
     * @since 1.0;
     */
    public void notifyMoveDone() {
        if (currentState == State.running) {
            switchActivePlayer = !switchActivePlayer;
        } else if (currentState == State.noneRunning) {
            // start game
            currentPTC.setActiveAndRepaint(true);
            clockDisplay.setMoveNumber(halfMove / 2);
            signalClock.callAwakening();
            currentState = State.running;
        }
    }

    /** Called when the pause status changes. Does nothing if the current state is
     * {@code noneRunning}.
     *
     * @since 1.0;
     */
    public void notifyPauseChangeRequest() {
        if (currentState == State.running) {
            currentState = State.paused;
            signalClock.flagHoldOn();
            clockDisplay.setPausedState(true);
        } else if (currentState == State.paused) {
            currentState = State.running;
            clockDisplay.setPausedState(false);
            signalClock.callAwakening();
        }
    }

    /** The timing method. It removes {@code dt} nanoseconds from the time budget of the current
     * player. Also, it changes the currently active player.
     *
     * @since 1.0;
     * @param dt Time to be taken away, in nanoseconds.
     */
    void update(long dt) {
        if (currentState == State.running) {
            timeHasRanOut = currentPTC.update(dt);
            if (timeHasRanOut) {
                signalClock.flagHoldOn();
            }

            if (!switchActivePlayer || timeHasRanOut) {
                currentPTC.updateTimePanelAndRepaint();
            } else {
                switchActivePlayer = false;
                leftAtTurn = !leftAtTurn;

                currentPTC.endMoveAndRepaint();
                currentPTC = leftAtTurn ? timeControlLeft : timeControlRight;
                currentPTC.setActiveAndRepaint(true);

                halfMove++;
                clockDisplay.setMoveNumber(halfMove / 2);
            }
        }
    }

    /** Notify the clock display that the clock has been stopped.
     * @since 1.0;
     */
    void notifyStoppingClock() {
        clockDisplay.notifyStoppingClock();
    }

    /** Signals the clock that it shall come to an end. Note that this end is forever. So call this
     * method only when you shut down and exit from the application.
     * @since 1.0;
     */
    public void endSignalClock() {
        signalClock.end();
    }

    /** Signals the clock that it shall wake up.
     * @since 1.0;
     */
    public void wakeSignalClockUp() {
        signalClock.callAwakening();
    }

    /** Signals the clock that is shall start. Needs to be called <i>exactly</i> once in the beginning
     * of the application.
     * @since 1.0;
     */
    public void startClock() {
        signalClock.start();
    }

}
