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
package chessclock.clockpanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import chessclock.Action;
import chessclock.Constants;
import localization.Loc;

/** A panel on the {@link ClockPanel clock panel} containing some program buttons, very much like a
 * tool bar. Such buttons allow for switching to the settings, pausing the clock.
 *
 * @author Dragonstb
 * @since 1.0;
 */
final class ToolBarPanel extends JPanel implements ActionListener {

    /** Button that switches to the settings. */
    private final InteractiveButton settingsButton;
    /** Button that pauses and resumes a running game. */
    private final InteractiveButton pauseButton;
    /** Width of the gap between two buttons, in pixels. */
    private final int buttonGap = 2;
    /** Height of the "tool bar", in pixels. */
    private final int buttonAreaHeight = 100;
    /** The clock panel that contains this ToolBarPanel. */
    private final ClockPanel clockPanel;

    /** Generates
     *
     * @since 1.0;
     * @param clockPanel The clock panel that contains this ToolBarPanel.
     */
    ToolBarPanel(ClockPanel clockPanel) {
        this.clockPanel = clockPanel;
        setLayout(null);
        setBackground(Color.DARK_GRAY);

        // we haven't got a localization yet, so initialize with any text
        settingsButton = new InteractiveButton("=");
        settingsButton.setIcon(Constants.SETTINGS_ICON);
        settingsButton.setIconTextGap(20);
        settingsButton.setActionCommand(Action.SETTINGS);
        settingsButton.addActionListener(this);
        add(settingsButton);

        pauseButton = new InteractiveButton("||");
        pauseButton.setIcon(Constants.PAUSE_ICON);
        pauseButton.setIconTextGap(20);
        pauseButton.setActionCommand(Action.PAUSE);
        pauseButton.addActionListener(this);
        add(pauseButton);
    }

    /** Updates the localization. Simply returns if {@code null} is passed.
     *
     * @since 1.0;
     * @param loc New localization.
     */
    void updateLocalization(Loc loc) {
        if (loc == null) {
            return;
        }

        settingsButton.setText(loc.retrieveString(Loc.SETTINGS));
        pauseButton.setText(loc.retrieveString(Loc.PAUSE));
    }

    /** Sets the fonts used.
     *
     * @since 1.0
     * @param font Global reference font the font used is derived from.
     */
    void setFonts(Font font) {
        if (font == null) {
            return;
        }

        Font btnFont = font.deriveFont(Font.BOLD, 2 * font.getSize());
        settingsButton.setFont(btnFont);
        pauseButton.setFont(btnFont);
    }

    /** Adopts the new width and rearranges and resizes the GUI elements.
     *
     * @since 1.0;
     * @param width New width, in pixels.
     */
    void rearrange(int width) {
        int useButtonGap = this.buttonGap + width % 2;
        int compW = (width - useButtonGap) / 2;
        int compH = buttonAreaHeight - this.buttonGap;
        settingsButton.setBounds(0, 0, compW, compH);
        pauseButton.setBounds(compW + useButtonGap, 0, compW, compH);

        setSize(width, buttonAreaHeight);
        setPreferredSize(new Dimension(width, buttonAreaHeight));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (clockPanel != null) {
            switch (e.getActionCommand()) {
                case Action.SETTINGS ->
                    clockPanel.goToSettings();
                case Action.PAUSE ->
                    clockPanel.pauseClock();
            }
        }
    }

}
