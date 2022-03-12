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

import java.awt.Font;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import chessclock.clockpanel.ClockPanel;
import chessclock.settingspanel.SettingsPanel;
import localization.EnglishLocalization;
import localization.Loc;
import time.TimeControl;

/** The thread that does all the launching work.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public class LaunchThread extends Thread {

    private static final Logger LOG = Logger.getLogger(LaunchThread.class.getName());

    /** The window frame that hosts all the GUI. */
    private ChessClockFrame frame;
    /** A shutdown thread in case something goes wrong. */
    private LaunchShutdownThread shutdownThread;

    /** Generates
     *
     * @since 1.0;
     * @param frame The frame that hosts the GUI.
     */
    LaunchThread(ChessClockFrame frame) {
        LOG.setLevel(Level.ALL);
        shutdownThread = new LaunchShutdownThread(this);
        Runtime.getRuntime().addShutdownHook(shutdownThread);
        this.frame = frame;
    }

    @Override
    public void run() {
        try {
            frame.setLoc(generateInitialLocalization());
            frame.addKeyListener(frame);
            frame.addWindowListener(frame);

            // setting up the panels
            int panelWidth = frame.getContentPane().getWidth();
            int panelHeight = frame.getContentPane().getHeight();
            Optional<ClockPanel> optClockPanel = ClockPanel.makeNew(frame, panelWidth, panelHeight);
            if (optClockPanel.isEmpty()) {
                throw new java.lang.Exception();
            }

            ClockPanel clockPanel = optClockPanel.get();
            clockPanel.setLocation(0, 0);
            frame.addClockPanel(clockPanel);

            Optional<SettingsPanel> optSettingsPanel = SettingsPanel.makeNew(frame, panelWidth, panelHeight);
            if (optSettingsPanel.isEmpty()) {
                throw new java.lang.Exception();
            }

            SettingsPanel settingsPanel = optSettingsPanel.get();
            settingsPanel.setLocation(0, 0);
            frame.addSettingsPanel(settingsPanel);

            // setup the time control
            Optional<TimeControl> optTimeControl = TimeControl.makeNew(clockPanel);
            if (optTimeControl.isEmpty()) {
                throw new java.lang.Exception();
            }

            TimeControl timeControl = optTimeControl.get();
            clockPanel.setTimeControl(timeControl);
            frame.setTimeControl(timeControl);

            // setting the fonts
            Font font = loadReferenceFont();
            frame.setReferenceFont(font);

            // localize strings in UI
            frame.updateLocalization(frame.getLoc());

            // arrange UI, and yes, called twice 'cos in settingsPanel.rearrange() "tabWidth" is -3 in first call
            frame.rearrange();
            frame.rearrange();

            // go, go, go
            timeControl.startClock();
            frame.iniDone();

            // clean up
            Runtime.getRuntime().removeShutdownHook(shutdownThread);
            frame = null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Problem while loading application, shutting down: " + e.getMessage());
            Util.showMessage(frame, Loc.LAUNCH_ERROR, JOptionPane.ERROR_MESSAGE);
            frame.dispose();
        }
    }

    /** Creates an initial localization to start with.
     *
     * @since 1.0;
     * @return
     */
    private Loc generateInitialLocalization() {
        return new EnglishLocalization();
    }

    /** Creates an initial standard font used for the GUI elements.
     *
     * @since 1.0;
     * @return A standard font.
     */
    private Font loadReferenceFont() {
        return new JPanel().getFont();
    }

    /** Starts the shutdown thread that interrupts the launch thread.
     *
     * @since 1.0;
     */
    private class LaunchShutdownThread extends Thread {

        private LaunchThread launchThread;

        private LaunchShutdownThread(LaunchThread thread) {
            launchThread = thread;
        }

        @Override
        public void run() {
            launchThread.interrupt();
        }

    }
}
