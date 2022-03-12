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
package additionalTesting;

import java.awt.Window;
import java.util.Arrays;
import java.util.logging.Logger;

/** The unit tests of the {@link chessclock.gamesettings.AddTCPDialog AddTCPDialog} opens such
 * dialogs, that's why we need this thread that can close them automatically.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public class WindowDisposerThread extends Thread {

    private static final Logger LOG = Logger.getLogger(WindowDisposerThread.class.getName());

    private Window window;

    /**
     * @param wh
     */
    public WindowDisposerThread(Window window) {
        this.window = window;
    }

    @Override
    public void run() {
        if (window == null) {
            return;
        }

        boolean ok = true;
        Window[] windows;
        while (ok) {
            try {
                windows = window.getOwnedWindows();
                if (Arrays.stream(windows).anyMatch(Window::isVisible)) {
                    Arrays.stream(windows).filter(Window::isVisible).forEach(Window::dispose);
                    break;
                }
            } catch (java.lang.NullPointerException e1) {
            }

            try {
                Thread.sleep(10);
            } catch (Exception e) {
                ok = false;
            }
        }
    }

}
