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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;

import javax.swing.JPanel;

/** The move panel displays the number of the current move and the localized string 'move' above it.
 * The baseline of the move string is its ascent below the top edge of the panel. The baseline of
 * the move number is the descent of the move string plus the ascent of the move number below the
 * baseline of the move string. Also, the baseline of the move number is vertically aligned to the
 * bottom edge of the identity line of the {@link ClockPanel clock panel}.
 *
 * @author Dragonstb
 * @since 1.0;
 */
final class MovePanel extends JPanel {

    /** The string displaying the number of the move. */
    private String moveNumber;
    /** The font used for the move number. */
    private Font moveNumberFont;
    /** The text layout for the move number. */
    private TextLayout moveNumberLayout;
    /** Ceiled advance of move number layout. */
    private int moveNumberWidth;

    /** The string displaying 'move', or a translated version. */
    private String moveString;
    /** The font used for the move string. */
    private Font moveStringFont;
    /** The text layout for the move string. */
    private TextLayout moveStringLayout;
    /** Ceiled advance of move string layout. */
    private int moveStringWidth;

    /** Generates with black background. Also creates the text layouts.
     *
     * @since 1.0;
     * @param moveNumber The string displaying the number of the move.
     * @param moveNumberFont The font used for the move number.
     * @param moveString The text layout for the move number.
     * @param moveStringFont The font used for the move string.
     */
    MovePanel(String moveNumber, Font moveNumberFont, String moveString, Font moveStringFont) {
        super(null);

        this.moveNumber = moveNumber;
        this.moveNumberFont = moveNumberFont;
        updateMoveNumberLayout();

        this.moveString = moveString;
        this.moveStringFont = moveStringFont;
        updateMoveStringLayout();

        setBackground(Color.black);
    }

    /** Returns the text width. This is the maximum of the advances of the two {@link java.awt.font.TextLayout text
     * layouts}.
     *
     * @since 1.0;
     * @return Maximum advance of the two text layouts, ceiled.
     */
    int getTextWidth() {
        return (int) Math.ceil(Math.max(moveNumberWidth, moveStringWidth));
    }

    /** Returns the effectively used height of the text area. Here this is the total of the ascents
     * and descents of the two {@link java.awt.font.TextLayout text layouts}.
     *
     * @since 1.0;
     * @return Used text height.
     */
    int getTextHeight() {
        return (int) Math.ceil(moveStringLayout.getAscent() + moveStringLayout.getDescent()
                + moveNumberLayout.getAscent() + moveNumberLayout.getDescent());
    }

    /** Returns the floored descent of the move number text layout.
     *
     * @since 1.0;
     * @return Descent of move number.
     */
    int getMoveNumberDescent() {
        return (int) Math.floor(moveNumberLayout.getDescent());
    }

    /** Updates the text for the move string. Also updates the text layout and the width field.
     *
     * @since 1.0;
     * @param moveString New text for the move string.
     */
    void setMoveNumber(String moveNumber) {
        if (moveNumber == null) {
            return;
        }

        this.moveNumber = moveNumber;
        updateMoveNumberLayout();
        repaint();
    }

    /** Updates the text layout of the moveNumber, as well as the width.
     *
     * @since 1.0;
     */
    private void updateMoveNumberLayout() {
        String useString = !moveNumber.isEmpty() ? moveNumber : " ";
        moveNumberLayout = new TextLayout(useString, moveNumberFont,
                getFontMetrics(moveNumberFont).getFontRenderContext());
        moveNumberWidth = (int) Math.ceil(moveNumberLayout.getAdvance());
    }

    /** Returns the move number.
     *
     * @since 1.0;
     * @return The move number.
     */
    String getMoveNumberAsString() {
        return moveNumber;
    }

    /** Updates the text for the move string. Also updates the text layout and the width field.
     *
     * @since 1.0;
     * @param moveString New text for the move string.
     */
    void setMoveString(String moveString) {
        if (moveString == null) {
            return;
        }

        this.moveString = moveString;
        updateMoveStringLayout();
    }

    /** Updates the layout for the move string, as well as the width.
     *
     * @since 1.0;
     */
    private void updateMoveStringLayout() {
        String useString = !moveString.isEmpty() ? moveString : " ";
        moveStringLayout = new TextLayout(useString, moveStringFont,
                getFontMetrics(moveStringFont).getFontRenderContext());
        moveStringWidth = (int) Math.ceil(moveStringLayout.getAdvance());
    }

    /** Changes the fonts used.
     *
     * @since 1.0;
     * @param referenceFont The reference font the used fonts are derived from.
     * @param referenceHeight The reference height used for deriving the correct font size.
     */
    void applyFonts(Font referenceFont, int referenceHeight) {
        if (referenceFont == null || referenceHeight < 1) {
            return;
        }

        moveNumberFont = referenceFont.deriveFont(Font.BOLD, 7 * referenceHeight / 4);
        updateMoveNumberLayout();

        moveStringFont = referenceFont.deriveFont(Font.PLAIN, referenceHeight / 2);
        updateMoveStringLayout();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.white);

        g2d.setFont(moveNumberFont);
        g2d.drawString(moveNumber, (getWidth() - moveNumberWidth) / 2,
                moveStringLayout.getAscent() + moveStringLayout.getDescent() + moveNumberLayout.getAscent());

        g2d.setFont(moveStringFont);
        g2d.drawString(moveString, (getWidth() - moveStringWidth) / 2, moveStringLayout.getAscent());
    }

}
