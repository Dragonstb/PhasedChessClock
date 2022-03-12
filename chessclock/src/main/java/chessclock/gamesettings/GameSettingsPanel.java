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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JPanel;

import chessclock.ChessCheckBox;
import chessclock.Constants;
import chessclock.Country;
import chessclock.MasterFrame;
import chessclock.PlayerData;
import chessclock.Util;
import chessclock.settingspanel.SettingsPanel;
import localization.Loc;
import time.TimeControlPhase;

/** The panel for the settings for the game time control. Player names, sides and {@link TimeControlPhase time control
 * phases} are adjusted here. This panel makes up one tab of the tabbed pane on the
 * {@link SettingsPanel}.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public final class GameSettingsPanel extends JPanel implements ActionListener {

    /** Pixels between the elements and the edges. */
    private static final byte EDGE = 10;
    /** Pixels between the general settings and the piece colour settings. */
    private static final byte GEN_PIECE_GAP = 50;
    /** Pixels between the piece colour settings and the naming text fields. */
    private static final byte PIECE_NAME_GAP = 30;

    /** Pixels between the elements of the players. */
    private final short playerGap;

    /** Is the player on the left playing with the white pieces? */
    private boolean leftIsWhite;

    /** Check box: Same time budget for all? */
    private final ChessCheckBox sameTCPs;
    /** Check box: Same time limit for a move? */
    private final ChessCheckBox sameMoveTimeLimit;

    /** Exchanges the sides of the players. */
    private final JButton exchangePlayersButton;

    /** Colour indicator for <i>left</i> player. */
    private Image colorIndicatorLeft;
    /** Colour indicator for <i>right</i> player. */
    private Image colorIndicatorRight;
    /** X location of <i>left</i> colour indicator. */
    private int colorIndicatorLeftX;
    /** X location of <i>right</i> colour indicator. */
    private int colorIndicatorRightX;
    /** Y location of colour indicators. */
    private int colorIndicatorY;
    /** Button for exchanging the colours. */
    private final JButton exchangeColorsButton;

    /** Panel for setting the data of the player displayed on the <i>left</i>
     * side of the screen. */
    private PlayerDataPanel playerDataLeft;
    /** Panel for setting the data of the player displayed on the <i>right</i>
     * side of the screen. */
    private PlayerDataPanel playerDataRight;

    private GameSettingsPanel(MasterFrame frame) {
        super(null);

        setBackground(Constants.COUNTRY_COLOR_TOP);
        leftIsWhite = true;
        playerGap = 10;

        // general settings
        sameTCPs = new ChessCheckBox(Loc.SAME_BUDGET);
        sameTCPs.addActionListener(this);
        add(sameTCPs);

        sameMoveTimeLimit = new ChessCheckBox(Loc.SAME_MOVE_TIME_LIMIT);
        sameMoveTimeLimit.addActionListener(this);
        add(sameMoveTimeLimit);

        exchangePlayersButton = new JButton(Constants.EXCHANGE_PLAYERS);
        exchangePlayersButton.addActionListener(this);
        add(exchangePlayersButton);

        // colour indicators
        colorIndicatorLeft = Constants.WHITE_ICON.getImage();
        colorIndicatorRight = Constants.BLACK_ICON.getImage();

        exchangeColorsButton = new JButton(Constants.EXCHANGE_COLORS);
        exchangeColorsButton.addActionListener(this);
        add(exchangeColorsButton);

        // player data
        playerDataLeft = new PlayerDataPanel(frame);
        add(playerDataLeft);
        playerDataRight = new PlayerDataPanel(frame);
        add(playerDataRight);
    }

    /** Geneates.
     *
     * @since 1.0;
     * @param frame Window frame that holds everzthing together.
     * @return Optional with the new panel. Passing {@code null} as argument causes an empty
     * optional.
     */
    public static Optional<GameSettingsPanel> makeNew(MasterFrame frame) {
        if (frame == null) {
            return Optional.empty();
        }

        GameSettingsPanel msp = new GameSettingsPanel(frame);
        return Optional.ofNullable(msp);
    }

    /** Updates the localization across the entire panel. Simply returns if {@code null} is passed.
     *
     * @since 1.0;
     * @param loc New localization.
     */
    public void updateLocalization(Loc loc) {
        if (loc == null) {
            return;
        }

        sameTCPs.setText(loc.retrieveString(Loc.SAME_BUDGET));
        sameMoveTimeLimit.setText(loc.retrieveString(Loc.SAME_MOVE_TIME_LIMIT));

        playerDataLeft.updateLocalization(loc);
        playerDataRight.updateLocalization(loc);
    }

    /** Sets the reference font and updates all fonts used in the panel. Simply returns if
     * {@code null} is passed.
     *
     * @since 1.0;
     * @param font New reference font.
     */
    public void setFonts(Font font) {
        if (font == null) {
            return;
        }

        Font bold = Util.deriveBold(font);
        sameTCPs.setFont(bold);
        sameMoveTimeLimit.setFont(bold);
        playerDataLeft.setFonts(font);
        playerDataRight.setFonts(font);
    }

    /** Resizes and rearranges the panel and the GUI elements on it, adopting the given width.
     *
     * @param width New width, in pixels.
     * @since 1.0;
     * @return The height the panel has after all that rearranging, in pixels.
     */
    public int rearrange(int width) {
        int playerGapUsed = this.playerGap + width % 2;
        int y0 = EDGE; // y of highest component on screen
        int yCounter = y0;
        // components location and size
        int compX, compY, compW, compH;

        int playerAreaWidth = (width - playerGapUsed) / 2 - EDGE;
        int leftAreaX = EDGE;
        int rightAreaX = width - EDGE - playerAreaWidth;

        // general settings
        int checkBoxW = 25;
        int sameTimeBudgetHeight, sameMoveTimeLimitH;
        sameTimeBudgetHeight = 3 * sameTCPs.getTextHeight() / 2;
        sameMoveTimeLimitH = 3 * sameMoveTimeLimit.getTextHeight() / 2;
        compW = Math.max(sameTCPs.getTextWidth() + sameTCPs.getIconTextGap(),
                sameMoveTimeLimit.getTextWidth() + sameMoveTimeLimit.getIconTextGap());
        compW += checkBoxW;

        sameTCPs.setBounds(rightAreaX, yCounter, compW, sameTimeBudgetHeight);
        yCounter += sameTimeBudgetHeight;
        sameMoveTimeLimit.setBounds(rightAreaX, yCounter, compW, sameMoveTimeLimitH);
        yCounter += sameMoveTimeLimitH;

        // exchange-players button
        compH = sameTimeBudgetHeight + sameMoveTimeLimitH;
        compH = Math.min(compH, Constants.EXCHANGE_PLAYERS.getIconHeight());
        compW = Math.max(Constants.EXCHANGE_PLAYERS.getIconWidth(), (int) (.618 * playerAreaWidth));
        compX = (playerAreaWidth - compW) / 2 + EDGE; // align centre in left player area range
        compY = y0 + (yCounter - y0 - compH) / 2; // align centre relative to 'same...' check boxes
        exchangePlayersButton.setBounds(compX, compY, compW, compH);
        yCounter = Math.max(yCounter, compY + compH);

        // colour indicators
        yCounter += GEN_PIECE_GAP;
        compW = Constants.WHITE_ICON.getIconHeight();

        if (leftIsWhite) {
            colorIndicatorLeft = Constants.WHITE_ICON.getImage();
            colorIndicatorRight = Constants.BLACK_ICON.getImage();
        } else {
            colorIndicatorLeft = Constants.BLACK_ICON.getImage();
            colorIndicatorRight = Constants.WHITE_ICON.getImage();
        }
        colorIndicatorLeftX = (width - 2 * compW) / 4;
        colorIndicatorRightX = width - colorIndicatorLeftX - compW;
        colorIndicatorY = yCounter;

        // exchange-colours button
        compX = colorIndicatorRightX - colorIndicatorLeftX - compW; // space between colour indicators
        compW = Math.min(compX / 2, 150);
        compX = (width - compW) / 2; // align centre
        compH = Constants.WHITE_ICON.getIconHeight();
        exchangeColorsButton.setBounds(compX, yCounter, compW, compH);
        yCounter += compH;

        // player-data panels
        yCounter += PIECE_NAME_GAP;

        int playerDataHeight = playerDataLeft.rearrange(playerAreaWidth);
        playerDataHeight = Math.max(playerDataHeight, playerDataRight.rearrange(playerAreaWidth));
        playerDataLeft.setLocation(leftAreaX, yCounter);
        playerDataRight.setLocation(rightAreaX, yCounter);

        // finalize
        yCounter += playerDataHeight + EDGE;
        setSize(width, yCounter);
        setPreferredSize(new Dimension(width, yCounter));
        repaint();
        return yCounter;
    }

    /** Returns the entire player data for the <i>left</i> player.
     *
     * @since 1.0;
     * @return Left player's data.
     */
    public Optional<PlayerData> getPlayerDataLeft() {
        return getPlayerData(playerDataLeft);
    }

    /** Returns the entire player data for the <i>right</i> player.
     *
     * @since 1.0;
     * @return Right player's data.
     */
    public Optional<PlayerData> getPlayerDataRight() {
        if (!isSameTCPs() && !isSameMoveTimeLimit()) {
            return getPlayerData(playerDataRight);
        }

        // need to inject some data from left player into data for right player
        Optional<PlayerData> optLeft = getPlayerData(playerDataLeft);
        if (optLeft.isEmpty()) {
            return Optional.empty();
        }

        Optional<PlayerData> optRight = getPlayerData(playerDataRight);
        if (optRight.isEmpty()) {
            return Optional.empty();
        }

        PlayerData dataLeft = optLeft.get();
        PlayerData dataRight = optRight.get();
        PlayerData useData;

        // assemble data
        useData = isSameTCPs() ? dataLeft : dataRight;
        TimeControlPhase[] tcps = useData.getTimeControlPhases();

        useData = isSameMoveTimeLimit() ? dataLeft : dataRight;
        long moveTimeLimit = useData.getMoveTimeLimit();

        Optional<PlayerData> optNew = PlayerData.makeNew(dataRight.getName(), dataRight.getCountry(), tcps,
                dataRight.arePhasesAdditive(), moveTimeLimit, dataRight.getWarningThreshold());
        return optNew;
    }

    /** Extracts the player's data from the panel;
     *
     * @since 1.0;
     * @param pdp Player data panel we want to extract the data from.
     * @return Optional with the player data. Empty if something went wrong in the creation of the
     * data.
     */
    private Optional<PlayerData> getPlayerData(PlayerDataPanel pdp) {
        String name = pdp.getPlayerName();
        Country country = pdp.getSelectedCountry();
        TimeControlPhase[] tcps = pdp.getDeepClonedTCPs();
        boolean phasesAdditive = pdp.arePhasesAdditive();
        long moveTimeLimit = pdp.getMoveTimeLimit();
        long warningThreshold = pdp.getWarningThreshold();

        Optional<PlayerData> playerData = PlayerData.makeNew(name, country, tcps, phasesAdditive, moveTimeLimit,
                warningThreshold);
        return playerData;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(colorIndicatorLeft, colorIndicatorLeftX, colorIndicatorY, null);
        g2d.drawImage(colorIndicatorRight, colorIndicatorRightX, colorIndicatorY, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exchangePlayersButton) {
            exchangePlayers();
        } else if (e.getSource() == exchangeColorsButton) {
            exchangeColors();
        } else if (e.getSource() == sameTCPs) {
            changeTCPSymmetryState();
        } else if (e.getSource() == sameMoveTimeLimit) {
            changeMoveTimeLimitSymmetryState();
        }
    }

    /** Exchanges the sides of the playser.
     *
     * @since 1.0;
     */
    private void exchangePlayers() {
        // exchange references between panels
        PlayerDataPanel aux = playerDataRight;
        int y = aux.getY();
        int xr = aux.getX();
        int xl = playerDataLeft.getX();
        playerDataRight = playerDataLeft;
        playerDataLeft = aux;
        // exchange positions of panels
        playerDataLeft.setLocation(xl, y);
        playerDataRight.setLocation(xr, y);
        // when same TCPs, set them via the left one
        if (isSameTCPs()) {
            playerDataLeft.enableTCPSettings(true);
            playerDataRight.enableTCPSettings(false);
        }
        if (isSameMoveTimeLimit()) {
            playerDataLeft.enableMoveTimeLimitSetting(true);
            playerDataRight.enableMoveTimeLimitSetting(false);
        }
    }

    /** Echanges the colours of the players.
     *
     * @since 1.0;
     */
    private void exchangeColors() {
        leftIsWhite = !leftIsWhite;
        if (leftIsWhite) {
            colorIndicatorLeft = Constants.WHITE_ICON.getImage();
            colorIndicatorRight = Constants.BLACK_ICON.getImage();
        } else {
            colorIndicatorLeft = Constants.BLACK_ICON.getImage();
            colorIndicatorRight = Constants.WHITE_ICON.getImage();
        }
        repaint();
    }

    /** Enables or disables setting the time control phases for the right player, depending on the
     * result of {@code isSameTCPs()}.
     *
     * @since 1.0
     */
    private void changeTCPSymmetryState() {
        playerDataRight.enableTCPSettings(!isSameTCPs());
    }

    /** Enables or disables setting the move time limit for the right player, depending on the
     * result of {@code isSameTCPs()}.
     *
     * @since 1.0
     */
    private void changeMoveTimeLimitSymmetryState() {
        playerDataRight.enableMoveTimeLimitSetting(!isSameMoveTimeLimit());
    }

    private boolean isSameTCPs() {
        return sameTCPs.isSelected();
    }

    private boolean isSameMoveTimeLimit() {
        return sameMoveTimeLimit.isSelected();
    }

    /** Says if the left player plays with the white pieces.
     *
     * @since 1.0;
     * @return Does the left player plays with the white pieces?
     */
    public boolean isLeftIsWhite() {
        return leftIsWhite;
    }

}
