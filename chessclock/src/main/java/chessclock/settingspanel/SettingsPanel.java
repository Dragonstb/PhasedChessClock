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
package chessclock.settingspanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import chessclock.Action;
import chessclock.MasterFrame;
import chessclock.PlayerData;
import chessclock.Util;
import chessclock.about.AboutPanel;
import chessclock.gamesettings.GameSettingsPanel;
import localization.Loc;

/** The panel for the settings. It contains a tabbed field for different options and some buttons
 * for accepting the currently setted options, waiving the setted options and exiting the
 * application.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public class SettingsPanel extends JPanel implements ActionListener {

    /** Spacing between buttons. */
    private static final byte BUTTON_SPACING = 20;

    /** Window frame that holds everything together. */
    private final MasterFrame frame;
    /** Height of the panel with the buttons. The remaining is used by the scroll pane. */
    private final int buttonPanelHeight;
    /** A scroll pane. */
    private final JScrollPane scrollPane;
    /** A tabbed pane for switching between the different setting panels. */
    private final JTabbedPane tabbedPane;
    /** Settings for the actual game. */
    private final GameSettingsPanel gameSettingsPanel;
    /** Panel with the buttons on it. */
    private final JPanel buttonPanel;
    /** Button for accepting the current game settings. */
    private final JButton okButton;
    /** Button for declining the game settings and keeping the old ones instead. */
    private final JButton cancelButton;
    /** Button for exiting the application. */
    private final JButton exitButton;

    /** Panel with infos about the application. */
    private final AboutPanel aboutPanel;

    /**
     * @since 1.0;
     * @param width
     * @param height
     * @param frame
     */
    private SettingsPanel(int width, int height, MasterFrame frame) {
        super(null);
        setVisible(false);
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        this.frame = frame;
        setBackground(Color.black);
        buttonPanelHeight = 100;

        // tabbed pane
        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
        // tabbedPane.setBackground(Color.pink);

        gameSettingsPanel = GameSettingsPanel.makeNew(frame).get();
        tabbedPane.add("1", gameSettingsPanel);

        aboutPanel = AboutPanel.makeNew();
        tabbedPane.add("2", aboutPanel);

        scrollPane = new JScrollPane(tabbedPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBackground(Color.black);
        add(scrollPane);

        // buttons
        buttonPanel = new JPanel(null);
        buttonPanel.setBackground(Color.black);

        okButton = new JButton();
        okButton.setActionCommand(Action.OK);
        okButton.addActionListener(this);
        buttonPanel.add(okButton);

        cancelButton = new JButton();
        cancelButton.setActionCommand(Action.CANCEL);
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);

        exitButton = new JButton();
        exitButton.setActionCommand(Action.EXIT);
        exitButton.addActionListener(this);
        buttonPanel.add(exitButton);

        add(buttonPanel);
    }

    /**
     * @since 1.0;
     * @param frame Surrounding frame.
     * @param width Width in pixels.
     * @param height Height in pixels.
     * @return Optional with the panel. or an empty optional if any argument is non-helpful.
     */
    public static Optional<SettingsPanel> makeNew(MasterFrame frame, int width, int height) {
        if (frame == null || width < 1 || height < 1) {
            return Optional.empty();
        }

        SettingsPanel panel = new SettingsPanel(width, height, frame);
        return Optional.ofNullable(panel);
    }

    /** Updates the localization.
     *
     * @since 1.0;
     * @param loc New localization.
     */
    public void updateLocalization(Loc loc) {
        if (loc == null) {
            return;
        }

        tabbedPane.setTitleAt(0, loc.retrieveString(Loc.GAME_SETTINGS));
        tabbedPane.setTitleAt(1, loc.retrieveString(Loc.ABOUT));

        gameSettingsPanel.updateLocalization(loc);
        aboutPanel.updateLocalization(loc);

        okButton.setText(loc.retrieveString(Loc.OK));
        cancelButton.setText(loc.retrieveString(Loc.CANCEL));
        exitButton.setText(loc.retrieveString(Loc.EXIT));
    }

    /** Updates the fonts used.
     *
     * @since 1.0;
     * @param font Reference font all fonts are derived from.
     */
    public void setFonts(Font font) {
        if (font == null) {
            return;
        }

        Font bold = Util.deriveBold(font);
        tabbedPane.setFont(bold);

        gameSettingsPanel.setFonts(font);
        aboutPanel.setFonts(font);

        okButton.setFont(bold);
        cancelButton.setFont(bold);
        exitButton.setFont(bold);
    }

    /** Resizes and rearranges the panel and all child GUI elements, adopting the new size.
     *
     * @since 1.0;
     * @param width New width, in pixels.
     * @param height New height, in pixels.
     */
    public void rearrange(int width, int height) {
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        scrollPane.setBounds(0, 0, width, height - buttonPanelHeight);

        int compW = width / 5;
        int compH = buttonPanelHeight - 2 * BUTTON_SPACING;
        int compY = (buttonPanelHeight - compH) / 2;
        okButton.setBounds(BUTTON_SPACING, compY, compW, compH);
        cancelButton.setBounds(compW + 2 * BUTTON_SPACING, compY, compW, compH);
        exitButton.setBounds(width - compW - BUTTON_SPACING, compY, compW, compH);
        buttonPanel.setBounds(0, height - buttonPanelHeight, width, buttonPanelHeight);

        int tabHeight = tabbedPane.getHeight();
        int tabWidth = scrollPane.getViewport().getWidth();
        tabbedPane.setSize(tabWidth, tabHeight);

        gameSettingsPanel.rearrange(tabWidth - 5);
    }

    /**
     * @since 1.0;
     * @return <i>Right</i> player's data. Empty optional if fetching the data failed.
     */
    public Optional<PlayerData> getPlayerDataRight() {
        return gameSettingsPanel.getPlayerDataRight();
    }

    /**
     * @since 1.0;
     * @return <i>Left</i> player's data. Empty optional if fetching the data failed.
     */
    public Optional<PlayerData> getPlayerDataLeft() {
        return gameSettingsPanel.getPlayerDataLeft();
    }

    /**
     * @since 1.0
     * @return {@code True} if and only if the player on the left plays with the white pieces.
     */
    public boolean isLeftWhite() {
        return gameSettingsPanel.isLeftIsWhite();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case Action.CANCEL ->
                frame.goToClockPanel();
            case Action.OK -> {
                Optional<PlayerData> playerDataLeft = gameSettingsPanel.getPlayerDataLeft();
                Optional<PlayerData> playerDataRight = gameSettingsPanel.getPlayerDataRight();
                if (playerDataLeft.isPresent() && playerDataRight.isPresent()) {
                    boolean isLeftWhite = gameSettingsPanel.isLeftIsWhite();
                    frame.acceptSettings(playerDataLeft.get(), playerDataRight.get(), isLeftWhite);
                } else {
                    String message = frame.getLoc().retrieveString(Loc.PLAYER_DATA_ERROR);
                    String title = frame.getLoc().retrieveString(Loc.EXCEPTION_TITLE);
                    JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
                }
            }
            case Action.EXIT ->
                frame.exitApplication();
        }
    }

}
