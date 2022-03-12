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

import javax.swing.JCheckBox;

/** A {@code JCheckBox} that can tell us the width and height of the label text.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public final class ChessCheckBox extends JCheckBox {

    /**
     * Generates as 'not selected'.
     *
     * @since 1.0;
     * @param text Text for the label.
     */
    public ChessCheckBox(String text) {
        this(text, false);
    }

    /**
     * @since 1.0;
     * @param text Text for the label.
     * @param selected Initialize as selected?
     */
    public ChessCheckBox(String text, boolean selected) {
        super(text, selected);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(TOP);
        setBackground(Color.black);
        setForeground(Color.white);
        setOpaque(false);
    }

    /**
     * Returns the width of the displayed text.
     *
     * @since 1.0;
     * @return The width of the displayed text.
     */
    public int getTextWidth() {
        TextLayout layout = new TextLayout(getText(), getFont(), getFontMetrics(getFont()).getFontRenderContext());
        return (int) Math.ceil(layout.getAdvance() - layout.getLeading());
    }

    /**
     * Returns the height of the displayed text.
     *
     * @since 1.0;
     * @return The height of the displayed text.
     */
    public int getTextHeight() {
        return (int) Math.ceil(getFontMetrics(getFont()).getHeight());
    }

}
