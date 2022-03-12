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

import java.util.logging.Level;
import java.util.logging.Logger;

/** A clock which gives timing signals to the {@link TimeControl time control}.
 *
 * @author Dragonstb
 * @since 1.0;
 */
final class SignalClock extends Thread {

    private static final Logger LOG = Logger.getLogger(SignalClock.class.getName());

    /** The time control receiving the clock signals. */
    private final TimeControl timeControl;
    /** The old time. */
    private long oldTime;
    /** The new time. */
    private long newTime;
    /** The time difference. */
    private long diff;
    /** Do not continue until told so. */
    private boolean holdOn;

    private static enum State {
        ready,
        running,
        ended
    }

    private State state = State.ready;

    /** Generates as {@code running}.
     *
     * @since 1.0;
     * @param timeControl Time control receiving the clock signals.
     */
    SignalClock(TimeControl timeControl) {
        this.timeControl = timeControl;
        holdOn = true;
    }

    @Override
    public void run() {
        newTime = System.nanoTime();
        while (state == State.running) {
            try {
                if (holdOn) {
                    timeControl.notifyStoppingClock();
                    callToHold(); // wait here
                    newTime = System.nanoTime();
                }
            } catch (InterruptedException e) {
                break;
            }
            oldTime = newTime;
            newTime = System.nanoTime();
            diff = newTime - oldTime;
            timeControl.update(diff);
        }

        LOG.log(Level.INFO, "Signal clock has ended");
    }

    /** Causes the thread to come to an end by removing the {@code running} property.
     *
     * @since 1.0;
     */
    void end() {
        state = State.ended;
    }

    /** Calls {@code wait()} on itself if {@code holdOn} is on.
     *
     * @since 1.0;
     * @throws InterruptedException From {@code wait()}.
     */
    private synchronized void callToHold() throws InterruptedException {
        while (holdOn) {
            wait();
        }
    }

    /** Removes the {@code holdOn} flag and calls {@code notifyAll()}.
     *
     * @since 1.0;
     */
    synchronized void callAwakening() {
        holdOn = false;
        notifyAll();
    }

    /** Sets the {@code holdOn} flag. When set, the thread holds and continues only once the flag
     * becomes unset again.
     *
     * @since 1.0;
     */
    void flagHoldOn() {
        holdOn = true;
    }

    @Override
    public synchronized void start() {
        if (state == State.ready) {
            LOG.log(Level.INFO, "Signal clock has started");
            state = State.running;
            super.start();
        }
    }

}
