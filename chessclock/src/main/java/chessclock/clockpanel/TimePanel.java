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
import java.awt.RenderingHints;
import java.awt.font.TextLayout;

import javax.swing.JPanel;

import chessclock.Constants;
import localization.Loc;
import time.PlayerTimeDisplay;

/** This panel displays the time remaining on the clock.
 *
 * @author Dragonstb
 * @since 1.0;
 */
final class TimePanel extends JPanel implements PlayerTimeDisplay {

    /*
	 * The top right corner of the panel looks like this
	 *
	 * --------------------+ \ | / MARK_BORDER_EDGE ----------------+ | < | | \ TEXT_SPACING ------------+ | | / | | |
	 * text area | | | | | |
     */
    /** Standard colour. */
    private static final Color STANDARD_TEXT_COLOR = Color.white;
    /** Warning colour. */
    private static final Color WARNING_TEXT_COLOR = Color.red;
    /** Default background colour. */
    private static final Color DEFAULT_BG_COLOR = Color.black;
    /** Background colour when the time is up. */
    private static final Color YOU_HAVE_LOST_BG_COLOR = Color.red;

    /** Is this the time display for the active player? In that case, draw an additonal border. */
    private boolean active;
    /** Current colour for the time budget display. */
    private Color currentColor;

    /** Size of spacing between the mark border and the text area. */
    private static final byte TEXT_SPACING = 3;
    /** Sum of TEXT_SPACING and MARK_BORDER_SIZE. */
    private static final byte TOTAL_EDGE = Constants.MARK_BORDER_SIZE + TEXT_SPACING;

    /** A reference font the fonts used are derived from. */
    private Font referenceFont;

    /** Text displaying the seconds and the minutes remaining. */
    private String secMinText;
    /** Font used for displaying the remaining seconds and minutes. */
    private Font secMinFont;
    /** Text layout for the seconds and minutes text. */
    private TextLayout secMinTextLayout;
    /** Width of the text derived from the advance, i.e.,
     * {@code Math.ceil( secMinTextLayout.getAdvance() )}.
     */
    private int secMinWidth;
    /** Height of the main text body, i.e., {@code Math.ceil( secMinTextLayout.getAscent() )}.
     */
    private int secMinAscent;
    /** The height of the text derived from the metric values, i.e., {@code
     * Math.ceil( TextLayout.getAscent()+TextLayout.getDescent() )}.
     */
    private int secMinHeight;

    /** Text displaying the tens of seconds remaining.
     */
    private String tensText;
    /** Font used for displaying the remaining tens of a second.
     */
    private Font tensFont;
    /** Text layout for the tens of a second text.
     */
    private TextLayout tensTextLayout;
    /** Width of the tens of a second text derived from the advance, i.e.,
     * {@code Math.ceil( secMinTextLayout.getAdvance() )}.
     */
    private int tensWidth;

    /** Text displaying the remaining time for the current move. */
    private String moveRemText;
    /** Font used for displaying the remaining time for the current move. */
    private Font moveRemFont;
    /** Text layout for displaying the remaining time for the current move. */
    private TextLayout moveRemTextLayout;
    /** Width of the displayed remaining time for the current move, derived from the advance, i.e.,
     * {@code Math.ceil( secMinTextLayout.getAdvance() )}.
     */
    private int moveRemWidth;
    /** Height of the main text body displayed remaining time for the current move, i.e.,
     * {@code Math.ceil( secMinTextLayout.getAscent() )}.
     */
    private int moveRemAscent;
    /** The height of the displayed remaining time for the current move derived from the metric
     * values, i.e., null     {@code Math.ceil( TextLayout.getAscent()
	 * +TextLayout.getDescent() )}.
     */
    private int moveRemHeight;

    /** Localization. */
    private Loc loc;

    /** Generates.
     *
     * @since 1.0;
     * @param secondsMinutes
     * @param secondsMinutesFont
     * @param tens
     * @param tensFont
     * @param moveRem
     * @param moveRemFont
     * @param loc
     */
    TimePanel(String secondsMinutes, Font secondsMinutesFont, String tens, Font tensFont, String moveRem,
            Font moveRemFont, Loc loc) {
        super(null);
        currentColor = STANDARD_TEXT_COLOR;

        secMinFont = secondsMinutesFont;
        secMinText = secondsMinutes;
        updateSecMinLayout();

        tensText = tens;
        this.tensFont = tensFont;
        updateTensLayout();

        moveRemText = moveRem;
        this.moveRemFont = moveRemFont;
        updateMoveRemLayout();

        active = false;
        setBackground(DEFAULT_BG_COLOR);

        this.loc = loc;
    }

    /** Sets the font and updates the layout.
     *
     * @since 1.0;
     * @param secMinFont New font.
     */
    private void setSecMinFont(Font secMinFont) {
        this.secMinFont = secMinFont;
        updateSecMinLayout();
    }

    /** Sets the text and updates the layout.
     *
     * @since 1.0;
     * @param secMinText New text.
     */
    private void setSecMinText(String secMinText) {
        this.secMinText = secMinText;
        updateSecMinLayout();
    }

    /** Updates the layout and adjusts the numeric values used for the arrangement of the GUI
     * elements.
     *
     * @since 1.0;
     */
    private void updateSecMinLayout() {
        secMinTextLayout = new TextLayout(secMinText, secMinFont, getFontMetrics(secMinFont).getFontRenderContext());
        secMinWidth = (int) Math.ceil(secMinTextLayout.getAdvance());
        secMinAscent = (int) Math.ceil(secMinTextLayout.getAscent());
        secMinHeight = (int) Math.ceil(secMinTextLayout.getDescent() + secMinTextLayout.getAscent());
    }

    /** Updates the font and the layout.
     *
     * @since 1.0;
     * @param tensFont Few font.
     */
    private void setTensFont(Font tensFont) {
        this.tensFont = tensFont;
        updateTensLayout();
    }

    /** Sets the text and updates the layout.
     *
     * @since 1.0;
     * @param tensText New text.
     */
    private void setTensText(String tensText) {
        this.tensText = tensText;
        updateTensLayout();
    }

    /** Updates the layout and sets the numeric values used for the arrangement of the GUI elements.
     *
     * @since 1.0;
     */
    private void updateTensLayout() {
        tensTextLayout = new TextLayout(tensText, tensFont, getFontMetrics(tensFont).getFontRenderContext());
        tensWidth = (int) Math.ceil(tensTextLayout.getAdvance());
    }

    /** Sets the font and updates the layout.
     *
     * @since 1.0;
     * @param moveRemFont New text.
     */
    private void setMoveRemFont(Font moveRemFont) {
        this.moveRemFont = moveRemFont;
        updateMoveRemLayout();
    }

    /** Sets the text and updates the layout.
     *
     * @since 1.0;
     * @param moveRemText New text.
     */
    private void setMoveRemText(String moveRemText) {
        this.moveRemText = moveRemText;
        updateMoveRemLayout();
    }

    /** Updates the layout and sets the numeric values used for the arrangement of the GUI elements.
     *
     * @since 1.0;
     */
    private void updateMoveRemLayout() {
        String text = moveRemText;
        if (moveRemText.isEmpty()) {
            text = " ";
        }
        moveRemTextLayout = new TextLayout(text, moveRemFont, getFontMetrics(moveRemFont).getFontRenderContext());
        moveRemWidth = (int) Math.ceil(moveRemTextLayout.getAdvance());
        moveRemAscent = (int) Math.ceil(moveRemTextLayout.getAscent());
        moveRemHeight = (int) Math.ceil(moveRemTextLayout.getDescent() + moveRemTextLayout.getAscent());
    }

    /** Returns the width of the text area.
     *
     * @since 1.0;
     * @return The width of the text area, in pixels.
     */
    private int getAvailableWidth() {
        return getWidth() - 2 * TOTAL_EDGE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw border when active
        if (active) {
            g2d.setColor(Constants.COUNTRY_COLOR_TOP);
            g2d.setStroke(Constants.STROKE_MARK_BORDER_SIZE);
            int halfEdge = Constants.MARK_BORDER_SIZE / 2;
            g2d.drawRect(halfEdge, halfEdge, getWidth() - Constants.MARK_BORDER_SIZE,
                    secMinHeight + moveRemHeight + 2 * TOTAL_EDGE);
        }

        g2d.setColor(currentColor);

        g2d.setFont(secMinFont);
        g2d.drawString(secMinText, getWidth() - secMinWidth - tensWidth - TOTAL_EDGE, secMinAscent + TOTAL_EDGE);

        // yep, the baseline for the tens of a second is the same as for the seconds and the minutes
        g2d.setFont(tensFont);
        g2d.drawString(tensText, getWidth() - tensWidth - TOTAL_EDGE, secMinAscent + TOTAL_EDGE);

        g2d.setFont(moveRemFont);
        g2d.drawString(moveRemText, TOTAL_EDGE + (getAvailableWidth() - moveRemWidth) / 2,
                secMinHeight + moveRemAscent + TOTAL_EDGE);

    }

    /** Derives and sets the fonts for the time panels.
     *
     * @since 1.0;
     * @param referenceFont Font the other fonts are derived from.
     */
    private void deriveAndSetFonts(Font referenceFont) {
        int availW = getAvailableWidth();
        int size = referenceFont.getSize();
        // store current texts...
        String textMinSec = secMinText;
        String textTens = this.tensText;
        // ...and set "very wide" texts for a moment
        setSecMinText("999:99");
        setTensText(".9");
        setSecMinFont(referenceFont.deriveFont(Font.PLAIN, size));
        setTensFont(referenceFont.deriveFont(Font.PLAIN, 2 * size / 3));
        int currentW = secMinWidth + tensWidth;

        if (currentW < availW) {
            // increase as far as possible
            while (currentW < availW) {
                size++;
                setSecMinFont(referenceFont.deriveFont(Font.PLAIN, size));
                setTensFont(referenceFont.deriveFont(Font.PLAIN, 2 * size / 3));
                currentW = secMinWidth + tensWidth;
            }
            if (currentW > availW) {
                size--; // became a bit too large, but this size was fine, do you remember?
            }
        } else if (currentW > availW) {
            while (currentW > availW) {
                size--;
                setSecMinFont(referenceFont.deriveFont(Font.PLAIN, size));
                setTensFont(referenceFont.deriveFont(Font.PLAIN, 2 * size / 3));
                currentW = secMinWidth + tensWidth;
            }
        }

        Font newSecMinFont = referenceFont.deriveFont(Font.PLAIN, size);
        Font newTensFont = referenceFont.deriveFont(Font.PLAIN, 2 * size / 3);
        Font newMoveRemFont = referenceFont.deriveFont(Font.PLAIN, size / 2);

        // restore the correct texts
        setSecMinText(textMinSec);
        setTensText(textTens);

        setSecMinFont(newSecMinFont);
        setTensFont(newTensFont);
        setMoveRemFont(newMoveRemFont);
    }

    /** Adopts the given size for the panel and rescales and rearranges the GUI elements.
     *
     * @since 1.0;
     * @param width New width, in pixels.
     * @param height new height, in pixels.
     */
    void rearrange(int width, int height) {
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));

        deriveAndSetFonts(referenceFont);
    }

    /** Updates the localization.
     *
     * @since 1.0;
     * @param loc New localization.
     */
    void updateLocalization(Loc loc) {
        this.loc = loc;
    }

    /** Sets the reference font the fonts used on this panel are derived from.
     *
     * @since 1.0;
     * @param font New reference font.
     */
    void setReferenceFont(Font font) {
        referenceFont = font;
    }

    // ============ from interface PlayerTimeDisplay ============
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setClockTime(long time) {
        String[] timeStrings = loc.getTimeStrings(time);
        setSecMinText(timeStrings[0]);
        setTensText(timeStrings[1]);
    }

    @Override
    public void setMoveTime(long time) {
        String[] timeStrings = loc.getTimeStrings(time);
        setMoveRemText(timeStrings[0] + timeStrings[1]);
    }

    @Override
    public void useWarningColor(boolean use) {
        currentColor = use ? WARNING_TEXT_COLOR : STANDARD_TEXT_COLOR;
    }

    @Override
    public void setupForGame(boolean withMoveTimeLimit) {
        useWarningColor(false);
        setActive(false);
        setBackground(DEFAULT_BG_COLOR);
        if (!withMoveTimeLimit) {
            setMoveRemText(Constants.EMPTY_STRING);
        }
    }

    @Override
    public void setOutOfTime() {
        setBackground(YOU_HAVE_LOST_BG_COLOR);
        useWarningColor(false);
        setActive(false);
    }

    @Override
    public void updateDisplay() {
        repaint();
    }

}
