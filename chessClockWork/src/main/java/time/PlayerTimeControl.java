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

/** Time control for a single player.
 *
 * @author Dragonstb
 * @since 1.0;
 */
final class PlayerTimeControl {

    private static final byte ENDLESS_PHASE = -1;

    private static final PlayerTimeDisplay NULL_DISPLAY = new PlayerTimeDisplay() {

        @Override
        public void useWarningColor(boolean use) {
        }

        @Override
        public void updateDisplay() {
        }

        @Override
        public void setupForGame(boolean withMoveTimeLimit) {
        }

        @Override
        public void setOutOfTime() {
        }

        @Override
        public void setMoveTime(long time) {
        }

        @Override
        public void setClockTime(long time) {
        }

        @Override
        public void setActive(boolean active) {
        }

    };

    /** The player's time control phases. */
    private TimeControlPhase[] tcps;
    /** Time left in his/her current phase, in nanoseconds. */
    private long timeRemaining;
    /** Time limit for a move. */
    private long moveTime;
    /** Remaining time for this move. */
    private long moveTimeRemaining;
    /** Does the player has limited time for a move? */
    private boolean hasMoveTimeLimit;
    /** Current increment per move, in nanoseconds. */
    private long increment;
    /** Index (of arrays {@code tcps}) of time control phase the player is currently in. */
    private int currentPhase;
    /** First move in the player's next time control phase. */
    private int nextPhaseAfterMove;
    /** Are time phases additive for the player? */
    private boolean additive;
    /** The player time display that receives the data to be displayed. */
    private PlayerTimeDisplay display = NULL_DISPLAY;
    /** Warning threshold for the player, in nanoseconds. */
    private long warningThreshold;
    /** Use short-on-time warning. */
    private boolean useWarningColor;
    /** Current move. */
    private int move;

    PlayerTimeControl() {
    }

    /** Sets the receiver of the data.
     *
     * @since 1.0;
     * @param display Receives data from the clock.
     */
    void setDisplay(PlayerTimeDisplay display) {
        this.display = (display != null) ? display : NULL_DISPLAY;
    }

    /** Resets and sets the constraints.
     *
     * @since 1.0;
     * @param tbc New constraint.
     */
    void setupGameTiming(TimeBudgetConstraint tbc) {
        clearForNewGame(tbc);
        nextTCP();

        display.setupForGame(hasMoveTimeLimit);
        updateTimePanelAndRepaint();
    }

    /** Sets all values as stated in the constraint.
     *
     * @since 1.0;
     * @param tbc New constraint.
     */
    private void clearForNewGame(TimeBudgetConstraint tbc) {
        timeRemaining = 0;
        tcps = tbc.getTCPs();
        moveTime = tbc.getMoveTimeLimit();
        hasMoveTimeLimit = moveTime > 0;
        additive = tbc.isCumulativePhases();
        warningThreshold = tbc.getWarningThreshold();
        nextPhaseAfterMove = 0;
        useWarningColor = false;
        move = 1;
        currentPhase = -1;
    }

    /** Sents the timing data to the display.
     *
     * @since 1.0;
     */
    private void updateTimePanel() {
        display.setClockTime(timeRemaining);

        if (hasMoveTimeLimit) {
            display.setMoveTime(moveTimeRemaining);
        }
    }

    /** Sents the data to the display and tells the display that it may repaint itself right no.
     *
     * @since 1.0;
     */
    void updateTimePanelAndRepaint() {
        updateTimePanel();
        display.updateDisplay();
    }

    /** Switches to the next {@link TimeControlPhase time control phase} for the <i>left</i> player.
     * Increases the phase counter. Sets the remaining time and adds {@code timeRemaining} to it if
     * phases are additive. Sets the half move counter for the next phase change. If no further
     * phase change is supposed to occur, the next move value is set to -1. Resets the time
     * remaining for a single move.
     *
     * @since 1.0;
     */
    private void nextTCP() {
        if (currentPhase >= tcps.length - 1) {
            return;
        }

        ++currentPhase;
        if (!additive) {
            // destroy all remaining seconds
            timeRemaining = 0;
        }

        timeRemaining += tcps[currentPhase].getTime() * Constants.NANOSEC_PER_SEC;
        increment = tcps[currentPhase].getIncrement() * Constants.NANOSEC_PER_SEC;

        if (tcps.length > currentPhase + 1 && tcps[currentPhase].getMoves() != TimeControlPhase.REMAINDER) // set move after which the next change of a TCP occurs for this player
        {
            nextPhaseAfterMove = tcps[currentPhase].getMoves();
        } else {
            nextPhaseAfterMove = ENDLESS_PHASE;
        }
        moveTimeRemaining = moveTime;
    }

    /** Tells the display that is has either to display the active state or the inactive one and
     * causes it to repaint itself.
     * @since 1.0;
     * @param active Active state?
     */
    void setActiveAndRepaint(boolean active) {
        display.setActive(active);
        display.updateDisplay();
    }

    /** Progresses the clock by the given amount of time.
     *
     * @since 1.0;
     * @param dt Step size, in nanoseconds.
     * @return Is time up?
     */
    boolean update(long dt) {
        boolean timeHasRunOut = false;

        timeRemaining -= dt;
        moveTimeRemaining -= dt;
        if (timeRemaining <= 0) {
            timeHasRunOut = true;
            timeRemaining = 0;
            display.setOutOfTime();
        } else if (hasMoveTimeLimit && moveTimeRemaining <= 0) {
            moveTimeRemaining = 0;
            timeHasRunOut = true;
            display.setOutOfTime();
        } else if (timeRemaining < warningThreshold) // check for change for usage of warning colour, notify time panel if so
        {
            if (timeRemaining / Constants.NANOSEC_PER_SEC % 2 == 1) {
                // first half of a second => warning colour
                if (!useWarningColor) {
                    applyWarningColor(true);
                }
            } else if (useWarningColor) {
                applyWarningColor(false);
            }
        }

        return timeHasRunOut;
    }

    /** Ends the current move by adding the increment and telling the display.
     * @since 1.0;
     */
    void endMoveAndRepaint() {
        timeRemaining += increment;
        // hmmm... long overflow, I would rather find a pot of gold at the end of the rainbow, but you never know ;)
        if (timeRemaining < 0) {
            timeRemaining = Long.MAX_VALUE;
        }
        moveTimeRemaining = moveTime;
        applyWarningColor(false);
        if (nextPhaseAfterMove != ENDLESS_PHASE) {
            if (move < nextPhaseAfterMove) // count moves within the current phase
            {
                move++;
            } else {
                // go to next phase and reset move counter
                nextTCP();
                move = 1;
            }
        }
        updateTimePanel();
        setActiveAndRepaint(false);
    }

    /** Tells the display that it shall either turn the warning on or off.
     * @since 1.0;
     * @param apply Turn on warning colour?
     */
    private void applyWarningColor(boolean apply) {
        useWarningColor = apply;
        display.useWarningColor(useWarningColor);
    }

}
