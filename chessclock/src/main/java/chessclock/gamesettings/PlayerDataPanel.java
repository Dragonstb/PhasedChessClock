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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import chessclock.Country;
import chessclock.CountryContainer;
import chessclock.LimitedTextField;
import chessclock.MasterFrame;
import chessclock.Util;
import localization.Loc;
import time.TimeControlPhase;

/** A panel on which the settings of a single player, like name or
 * {@link TimeControlPhase time control phases}, can be adjusted. For each player, exactly one
 * instance of this class appears on the {@link GameSettingsPanel game settings panel}.
 *
 * @author Dragonstb
 * @since 1.0;
 */
final class PlayerDataPanel extends JPanel {

    /** Text field for entering the player's name. */
    private final JTextField name;

    /** Combo box for selecting the player's country. */
    private final JComboBox<CountryContainer> country;

    /** The panel with the components for setting up the
     * {@link TimeControlPhase time control phases}. */
    private final TCPPanel tcpPanel;
    /** Radio button for additive behaviour of the time control phases. */
    private final JRadioButton additiveButton;
    /** Radio button for destructive behaviour of the time control phases. */
    private final JRadioButton destructiveButton;
    /** Group of radio buttons that control the left player's time control phase behaviour. */
    private final ButtonGroup phaseBehavior;

    /** Optional additional time limit per move. */
    private final OptionalTimeSettingPanel moveTimeLimitPanel;
    /** Additional option for a short-on-time warning. */
    private final OptionalTimeSettingPanel warningPanel;

    private final int border = 0;
    private final int lineHeight = 20;
    private final int smallSpacing = 3;
    private final int largeSpacing = 20;

    /** Generates
     *
     * @since 1.0;
     * @param frame Window frame that holds everzthing together.
     */
    PlayerDataPanel(MasterFrame frame) {
        super();
        Loc loc = frame.getLoc();
        setOpaque(false);

        setLayout(null);

        // name and home country
        name = new LimitedTextField(loc.retrieveString(Loc.INITIAL_NAME), 50);
        add(name);

        country = new JComboBox<>();
        add(country);

        // time control phases
        tcpPanel = new TCPPanel(frame);
        add(tcpPanel);

        // cumulative TCP options
        additiveButton = new JRadioButton("+", true);
        additiveButton.setOpaque(false);
        additiveButton.setForeground(Color.white);
        add(additiveButton);

        destructiveButton = new JRadioButton("-", !additiveButton.isSelected());
        destructiveButton.setOpaque(false);
        destructiveButton.setForeground(Color.white);
        add(destructiveButton);

        phaseBehavior = new ButtonGroup();
        phaseBehavior.add(additiveButton);
        phaseBehavior.add(destructiveButton);

        // optional time settings
        moveTimeLimitPanel = new OptionalTimeSettingPanel();
        add(moveTimeLimitPanel);
        warningPanel = new OptionalTimeSettingPanel();
        add(warningPanel);
    }

    /** Updates the localization. Simpy returns if {@code null} is passed.
     *
     * @since 1.0;
     * @param loc New localization.
     */
    void updateLocalization(Loc loc) {
        if (loc == null) {
            return;
        }

        // countries
        country.removeAllItems();
        Arrays.stream(loc.getCountries()).forEach(country::addItem);

        // time control phases
        tcpPanel.updateLocalization(loc);

        // further
        additiveButton.setText(loc.retrieveString(Loc.ADDITIVE_TCP));
        destructiveButton.setText(loc.retrieveString(Loc.DESTRUCTIVE_TCP));
        moveTimeLimitPanel.updateLocalization(loc.retrieveString(Loc.MOVE_TIME_LIMIT_SEC));
        warningPanel.updateLocalization(loc.retrieveString(Loc.TIME_WARNING_SEC));
    }

    /** Updates the fonts used.
     *
     * @since 1.0;
     * @param font Reference font all fonts are derived from.
     */
    void setFonts(Font font) {
        Font bold = Util.deriveBold(font);
        name.setFont(font);
        country.setFont(bold);
        tcpPanel.setFonts(font);
        additiveButton.setFont(bold);
        destructiveButton.setFont(bold);
        moveTimeLimitPanel.setFonts(font);
        warningPanel.setFonts(font);
    }

    /** Resizes and rearranges the GUI elements according to the given width.
     *
     * @since 1.0;
     * @param width New width, in pixels.
     * @return Height the panel has after all that rearrangement, in pixels.
     */
    int rearrange(int width) {

        int subPanelWidth = width - 2 * border;
        int y = border;

        // name and country
        name.setBounds(border, y, subPanelWidth, lineHeight);
        y += smallSpacing + lineHeight;
        country.setBounds(border, y, subPanelWidth, lineHeight);
        y += largeSpacing + lineHeight;

        // time control phases
        int tcpPanelHeight = tcpPanel.rearrange(subPanelWidth, smallSpacing);
        tcpPanel.setLocation(border, y);
        y += tcpPanelHeight;

        // cumulative time budgets
        y += largeSpacing;
        additiveButton.setBounds(border, y, subPanelWidth, lineHeight);
        y += smallSpacing + lineHeight;
        destructiveButton.setBounds(border, y, subPanelWidth, lineHeight);

        // additional options
        y += lineHeight + largeSpacing;
        moveTimeLimitPanel.rearrange(subPanelWidth, lineHeight, smallSpacing);
        moveTimeLimitPanel.setLocation(border, y);

        y += lineHeight + smallSpacing;
        warningPanel.rearrange(subPanelWidth, lineHeight, smallSpacing);
        warningPanel.setLocation(border, y);

        // new height
        y += border + lineHeight;
        setSize(width, y);
        setPreferredSize(new Dimension(width, y));
        return y;
    }

    /** gets a deep copy of the time control phases currently configured.
     *
     * @since 1.0;
     * @return Time control phases.
     */
    TimeControlPhase[] getDeepClonedTCPs() {
        return tcpPanel.getDeepClonedTCPs();
    }

    boolean arePhasesAdditive() {
        return additiveButton.isSelected();
    }

    /**
     * @since 1.0;
     * @return Move time limit, in nanoseconds. Is -1 if the checkbox for having a move time limit
     * is <i>not</i> set.
     */
    long getMoveTimeLimit() {
        return moveTimeLimitPanel.getEffectiveNanoseconds();
    }

    /**
     * @since 1.0;
     * @return Time on the clock below which the time display starts blinking, in nanoseconds. Is -1
     * if the check box for a short-on-time warning is <i>not</i> set.
     */
    long getWarningThreshold() {
        return warningPanel.getEffectiveNanoseconds();
    }

    /** gets the country currently selected in the drop down menu.
     *
     * @since 1.0;
     * @return Selected country.
     */
    Country getSelectedCountry() {
        return country.getItemAt(country.getSelectedIndex()).getCountry();
    }

    /**
     * @since 1.0;
     * @return Text in the text field for the name.
     */
    String getPlayerName() {
        return name.getText();
    }

    /** Enables or disables the editing of the time control phases.
     *
     * @since 1.0;
     * @param enable Enable?
     */
    void enableTCPSettings(boolean enable) {
        tcpPanel.enableTCPSettings(enable);
    }

    /** Enables or disables editing the time limit for a single move.
     *
     * @since 1.0;
     * @param enable Enable?
     */
    void enableMoveTimeLimitSetting(boolean enable) {
        moveTimeLimitPanel.enableOTS(enable);
    }

}
