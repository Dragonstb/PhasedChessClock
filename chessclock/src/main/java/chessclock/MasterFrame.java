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

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

import chessclock.clockpanel.ClockPanel;
import chessclock.settingspanel.SettingsPanel;
import localization.Loc;

/**
 * @author Dragonstb
 * @since 1.0;
 */
public abstract class MasterFrame extends JFrame implements ComponentListener {

    private static final int MIN_WIDTH = 700;
    private static final int MIN_HEIGHT = 500;

    private Loc loc;
    private SettingsPanel settingsPanel;
    private ClockPanel clockPanel;

    /** Generates.
     *
     */
    public MasterFrame() {
        this("Phased Chess Clock");
    }

    MasterFrame(String title) {
        super(title);
        getContentPane().addComponentListener(this);
    }

    /** Sets the settings panel, replacing the existing one.
     *
     * @since 1.0;
     * @param panel Setting panel used from now on.
     */
    public final void addSettingsPanel(SettingsPanel panel) {
        if (settingsPanel != null) {
            remove(settingsPanel);
        }
        settingsPanel = panel;
        if (panel != null) {
            add(panel);
        }
    }

    /** Sets the clock panel, replacing the existing one.
     *
     * @since 1.0;
     * @param panel Clock panel used from now on.
     */
    public final void addClockPanel(ClockPanel panel) {
        if (clockPanel != null) {
            remove(clockPanel);
        }
        clockPanel = panel;
        if (panel != null) {
            add(panel);
        }
    }

    /** rearranges all GUI elements to fit the size of the content pane. Note that the GUI keeps a
     * minimum size if you make the window too small.
     *
     * @since 1.0;
     */
    public final void rearrange() {
        int w = getContentPane().getWidth();
        int h = getContentPane().getHeight();
        w = Math.max(w, MIN_WIDTH);
        h = Math.max(h, MIN_HEIGHT);
        if (settingsPanel != null) {
            settingsPanel.rearrange(w, h);
        }
        if (clockPanel != null) {
            clockPanel.rearrange(w, h);
        }
    }

    /** Sets the localization.
     *
     * @since 1.0;
     * @param loc The localization used from now on.
     */
    public final void setLoc(Loc loc) {
        if (loc == null) {
            return;
        }

        this.loc = loc;
        setTitle(loc.retrieveString(Loc.FRAME_TITLE));
    }

    /** Returns the localization.
     *
     * @since 1.0;
     * @return The localization.
     */
    public final Loc getLoc() {
        return loc;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        rearrange();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    public abstract void goToSettingsPanel();

    public abstract void goToClockPanel();

    public abstract void acceptSettings(PlayerData dataLeft, PlayerData dataRight, boolean isLeftWhite, boolean timeRunning);

    public abstract void exitApplication();

    /** Returns the settings panel.
     *
     * @since 1.0;
     * @return The settingsPanel
     */
    final SettingsPanel getSettingsPanel() {
        return settingsPanel;
    }

    /** Returns the clock panel.
     *
     * @since 1.0;
     * @return The clockPanel
     */
    final ClockPanel getClockPanel() {
        return clockPanel;
    }

}
