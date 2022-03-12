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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JPanel;

import chessclock.Constants;
import chessclock.Country;
import chessclock.MasterFrame;
import localization.Loc;
import time.ClockDisplay;
import time.TimeControl;

/** The clock panel displays the clock.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public final class ClockPanel extends JPanel implements MouseListener, ClockDisplay {

    private static final byte ELEMENT_BORDER_WIDTH = 2;
    /** Spacing between the time labels and the edge of the panel. */
    private static final byte TIME_EDGE_SPACING = 10;

    /** The frame holding all GUI elements together. */
    private MasterFrame frame;
    /** The time control that knows the time this panel shall display. */
    private TimeControl timeControl;

    /** Y location of names line. */
    private int identityY;
    /** Width of the image that shows which player plays the pieces of which color. */
    private int colorIndicatorWidth;
    /** X position of color indocator pic for the player on the left. */
    private int colorIndicatorXL;
    /** Y position of color indicator pic for the player on the right. */
    private int colorIndicatorXR;

    /** {@link IdentityPanel IdentityPanel} on the left side. */
    private IdentityPanel identityPanelLeft;
    /** {@link IdentityPanel IdentityPanel} on the right side. */
    private IdentityPanel identityPanelRight;

    /** Height of the gap between the identiy panels and the time panels. */
    private int identityTimeGapHeight = 70;
    /** Width of the gap between the time panels. */
    private int timePanelsGapWidth = 90;

    /** Colour indicator for left player. */
    private Image colorIndicatorLeft;
    /** Colour indicator for right player. */
    private Image colorIndicatorRight;

    /** Height of gap between the buttons on the top and the string "move". */
    private int buttonAreaMoveStringDisplayGap = 40;
    /** Panel with some buttons. */
    private ToolBarPanel toolBarPanel;

    /** Panel displaying remaining time for left player. */
    private TimePanel timePanelLeft;
    /** Panel displaying remaining time for right player. */
    private TimePanel timePanelRight;

    /** Panel displaying the number of the current move. */
    private MovePanel movePanel;

    /** A label susceptible for clicks which are hits on the clock. Clicking here stops the clock
     * for the one player and starts it for the other. */
    private JLabel leverPanel;
    /** The text overlaying the time panels while the clock is paused. */
    private String pausedString;

    private ClockPanel(MasterFrame frame, int width, int height) {
        super(null);
        setSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        this.frame = frame;
        setBackground(Color.black);
        setForeground(Color.white);
        setOpaque(true);
        setVisible(false);

        colorIndicatorLeft = Constants.WHITE_ICON.getImage();
        colorIndicatorRight = Constants.BLACK_ICON.getImage();

        boolean isLeft = true;
        identityPanelLeft = new IdentityPanel(isLeft);
        add(identityPanelLeft);
        identityPanelRight = new IdentityPanel(!isLeft);
        add(identityPanelRight);

        toolBarPanel = new ToolBarPanel(this);
        add(toolBarPanel);

        Font font = new JLabel().getFont();

        timePanelLeft = new TimePanel("999:99", font, ".9", font, "999:99.9", font, frame.getLoc());
        add(timePanelLeft);
        timePanelRight = new TimePanel("999:99", font, ".9", font, "999:99.9", font, frame.getLoc());
        add(timePanelRight);

        timePanelLeft.setActive(true);

        movePanel = new MovePanel("999", font, "move", font);
        add(movePanel);

        leverPanel = new JLabel(Constants.EMPTY_STRING);
        leverPanel.setForeground(new Color(255, 30, 30, 230));
        leverPanel.setOpaque(false);
        leverPanel.setVerticalAlignment(JLabel.CENTER);
        leverPanel.setHorizontalAlignment(JLabel.CENTER);
        leverPanel.addMouseListener(this);
        // add above time panels
        add(leverPanel, Math.min(getComponentZOrder(timePanelLeft), getComponentZOrder(timePanelRight)) - 1);
    }

    /** Creates a new ClockPanel.
     *
     * @since 1.0;
     * @param frame Frame the panel gets attached to.
     * @param width Initial width.
     * @param height Initial height.
     * @return Optional with the new clock panel. The optional is empty if you pass {@code null} as
     * frame or a non-positive width or height.
     */
    public static Optional<ClockPanel> makeNew(MasterFrame frame, int width, int height) {
        if (frame == null || width < 1 || height < 1) {
            return Optional.empty();
        }

        ClockPanel panel = new ClockPanel(frame, width, height);
        return Optional.ofNullable(panel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(colorIndicatorLeft, colorIndicatorXL, identityY, null);
        g2d.drawImage(colorIndicatorRight, colorIndicatorXR, identityY, null);
    }

    /** Sets the fonts used and updates the displays accordingly. Simply returns if {@code null} is
     * passed.
     *
     * @since 1.0;
     * @param referenceFont The font.
     */
    public void setFonts(Font referenceFont) {
        if (referenceFont == null) {
            return;
        }

        toolBarPanel.setFonts(referenceFont);

        identityPanelLeft.setFonts(referenceFont);
        identityPanelRight.setFonts(referenceFont);

        movePanel.applyFonts(referenceFont, identityPanelLeft.getNameHeight());

        // string "pause"
        Font font = referenceFont.deriveFont(Font.BOLD, 150);
        leverPanel.setFont(font);

        // time panels
        timePanelLeft.setReferenceFont(referenceFont);
        timePanelRight.setReferenceFont(referenceFont);
    }

    /** Rearranges and resizes all GUI elements on this panel to a new width and height. Just
     * returns if a non-positive width or height is passed.
     *
     * @since 1.0;
     * @param width New width, in pixels.
     * @param height New height, in pixels.
     */
    public void rearrange(int width, int height) {
        if (width < 1 || height < 1) {
            return;
        }

        setSize(width, height);
        setPreferredSize(new Dimension(width, height));

        // identityTimeGapHeight = 70;
        int timePanelsGapWidth = this.timePanelsGapWidth;
        if (width % 2 != timePanelsGapWidth % 2) // one even, the other odd
        {
            timePanelsGapWidth++; // ensures that width - timePanelsGapWidth is even
        }
        // tool bar
        toolBarPanel.rearrange(width);

        // move panel
        String text = movePanel.getMoveNumberAsString(); // store current text
        movePanel.setMoveNumber("999"); // set this one as "very wide" text
        int moveNumberWidth = movePanel.getTextWidth() + 10 * ELEMENT_BORDER_WIDTH; // text width with such a wide text
        if (width % 2 != moveNumberWidth % 2) // one is even and the other is odd
        {
            moveNumberWidth++; // ensures that width - moveNumberWidth is even
        }
        int moveNumberHeight = movePanel.getTextHeight();
        int moveNumberX = (width - moveNumberWidth) / 2;
        int moveNumberY = toolBarPanel.getHeight() + buttonAreaMoveStringDisplayGap;
        movePanel.setMoveNumber(text); // restore old text
        movePanel.setBounds(moveNumberX, moveNumberY, moveNumberWidth, moveNumberHeight);

        // colour indicators, immediately adjacent to the move panel
        colorIndicatorWidth = colorIndicatorLeft.getWidth(null);
        colorIndicatorXL = moveNumberX - colorIndicatorWidth;
        colorIndicatorXR = moveNumberX + moveNumberWidth;

        // width available for one set of flag, country and name (incl. borders)
        int remainder = moveNumberX - colorIndicatorWidth;

        // identity panels
        identityY = moveNumberY + moveNumberHeight - movePanel.getMoveNumberDescent()
                - identityPanelLeft.getIdentityHeight();

        identityPanelLeft.rearrange(remainder);
        identityPanelLeft.setLocation(0, identityY);

        identityPanelRight.rearrange(remainder);
        identityPanelRight.setLocation(width - remainder, identityY);

        // lever panel
        int leverPanelY = identityY + identityPanelLeft.getHeight();// identityHeight;
        int leverPanelH = height - leverPanelY;
        leverPanel.setBounds(0, leverPanelY, width, leverPanelH);

        // time panels
        int timePanelY = leverPanelY + identityTimeGapHeight;
        int timePanelXL = TIME_EDGE_SPACING;
        int timePanelXR = (width + timePanelsGapWidth) / 2;
        int timePanelWidth = (width - timePanelsGapWidth) / 2 - timePanelXL;
        int timePanelH = height - timePanelY - TIME_EDGE_SPACING;

        timePanelLeft.rearrange(timePanelWidth, timePanelH);
        timePanelLeft.setLocation(timePanelXL, timePanelY);

        timePanelRight.rearrange(timePanelWidth, timePanelH);
        timePanelRight.setLocation(timePanelXR, timePanelY);
    }

    /** Updates the localization. Texts become translated within this method. Simply returns if
     * {@code null} is passed.
     *
     * @since 1.0;
     * @param loc New localization.
     */
    public void updateLocalization(Loc loc) {
        if (loc == null) {
            return;
        }

        toolBarPanel.updateLocalization(loc);
        movePanel.setMoveString(loc.retrieveString(Loc.MOVE));
        identityPanelLeft.updateLocalization(loc);
        identityPanelRight.updateLocalization(loc);
        pausedString = loc.retrieveString(Loc.PAUSED);
        timePanelLeft.updateLocalization(loc);
        timePanelRight.updateLocalization(loc);
    }

    /** Tells the {@link chessclock.ChessClockFrame frame} to switch to the setting panel.
     *
     * @since 1.0;
     */
    void goToSettings() {
        frame.goToSettingsPanel();
    }

    /** Tells the {@link time.TimeControl time control} that is asked to toggle the pause mode.
     */
    void pauseClock() {
        timeControl.notifyPauseChangeRequest();
    }

    /** Sets the data displayed for the left player.
     *
     * @since 1.0;
     * @param name New text for left player's name field.
     * @param country New country for the left player.
     * @param loc Localization used to get the country code.
     */
    public void setDataLeft(String name, Country country, Loc loc) {
        setIdentityData(name, country, loc, identityPanelLeft);
    }

    /** Sets the displayed for for the right player.
     *
     * @since 1.0;
     * @param name New text for right player's name field.
     * @param country New country for the right player.
     * @param loc Localization used to get the country code.
     */
    public void setDataRight(String name, Country country, Loc loc) {
        setIdentityData(name, country, loc, identityPanelRight);
    }

    /** Updates the displayed data in the given {@link IdentityPanel IdentityPanel}. Simply returns
     * if {@code null} is passed in any of the arguments.
     *
     * @param name Player's name.
     * @param country Player's country.
     * @param loc Localization used to get the country code.
     * @param identityPanel Panel which is to be updated.
     */
    private void setIdentityData(String name, Country country, Loc loc, IdentityPanel identityPanel) {
        if (name == null || country == null || loc == null || identityPanel == null) {
            return;
        }

        identityPanel.updateData(name, country, loc);
    }

    /** Sets the colour indicators.
     *
     * @since 1.0;
     * @param leftIsWhite When {@code true}, the left player is indicated with the white pieces.
     */
    public void arrangeColorIndicators(boolean leftIsWhite) {
        if (leftIsWhite) {
            colorIndicatorLeft = Constants.WHITE_ICON.getImage();
            colorIndicatorRight = Constants.BLACK_ICON.getImage();
        } else {
            colorIndicatorLeft = Constants.BLACK_ICON.getImage();
            colorIndicatorRight = Constants.WHITE_ICON.getImage();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        timeControl.notifyMoveDone();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    /** Sets the time control and attaches the time panels to it.<br>
     * <b>Breaks down the app if {@code null} is passed</b> Though this never happens in the code at
     * the time when this sentence has been written, you better deal with it properly.
     *
     * @since 1.0;
     * @param timeControl The time control.
     */
    public void setTimeControl(TimeControl timeControl) {
        this.timeControl = timeControl;
        timeControl.setPlayerTimeDisplay(0, timePanelLeft);
        timeControl.setPlayerTimeDisplay(1, timePanelRight);
    }

    // ============ from interface ClockDisplay ============
    /** Determines if the 'paused' string becomes displayed ({@code true}) or hidden
     * ({@code false}).
     *
     * @since 1.0;
     * @param paused Paused string to be displayed?
     */
    @Override
    public void setPausedState(boolean paused) {
        if (paused) {
            leverPanel.setText(pausedString);
        } else {
            leverPanel.setText(Constants.EMPTY_STRING);
        }
    }

    @Override
    public void setMoveNumber(int moveNo) {
        if (moveNo >= 0) {
            movePanel.setMoveNumber(String.valueOf(moveNo));
        } else {
            movePanel.setMoveNumber(frame.getLoc().retrieveString(Loc.NOT_RUNNING_MOVE));
        }
    }

    @Override
    public void notifyStoppingClock() {
        repaint();
    }

}
