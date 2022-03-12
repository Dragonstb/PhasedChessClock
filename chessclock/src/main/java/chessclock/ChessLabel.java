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

import java.awt.Color;
import java.awt.font.TextLayout;

import javax.swing.JLabel;

/** A {@link javax.swing.JLabel} with additional functions for retrieving width and height of the
 * displayed string.<br>
 * <br>
 * <b>User input that it displayed on a chess label (chess label on the
 * {@link chessclock.clockpanel.IdentityPanel identity panels}), enters the program here, hence this class participates in
 * the boundary of trust against user input in text fields. </b> As counter measurement, the
 * "html.disable" property is activated, so that entering a html statement as name is displayed as
 * is rather than interpreted. (Additionally, the text fields where you enter the players' names
 * limit the number of characters in their document model.)
 *
 * @author Dragonstb
 * @since 1.0;
 */
public final class ChessLabel extends JLabel {

    /** Generates via {@code super(text)}.
     *
     * @since 1.0;
     * @param text Label.
     */
    public ChessLabel(String text) {
        super(text);
        setForeground(Color.white);
        setBackground(Color.black);
        putClientProperty("html.disable", true);
        setOpaque(false);
    }

    /** Returns the width of the displayed text.
     *
     * @since 1.0;
     * @return The width of the displayed text.
     */
    public int getTextWidth() {
        TextLayout layout = new TextLayout(getText(), getFont(), getFontMetrics(getFont()).getFontRenderContext());
        return (int) Math.ceil(layout.getBounds().getWidth());
    }

    /** Returns the height of the displayed text.
     *
     * @since 1.0;
     * @return The height of the displayed text.
     */
    public int getTextHeight() {
        TextLayout layout = new TextLayout(getText(), getFont(), getFontMetrics(getFont()).getFontRenderContext());
        return (int) Math.ceil(layout.getBounds().getHeight());
    }

}
