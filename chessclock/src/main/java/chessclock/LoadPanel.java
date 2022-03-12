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
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/** Panel displayed while loading and initializing the application.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public final class LoadPanel extends JPanel {

    /** Generates with {@code null} as layout manager and with the given size.
     * <b>No check for consistent arguments!</b>
     *
     * @since 1.0;
     * @param width Width in pixels.
     * @param height Height in pixels.
     */
    public LoadPanel(int width, int height) {
        super(null);
        setBackground(Color.black);
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.gray);
        g.drawString("initialize", getWidth() / 2 - 20, getHeight() / 2 - 10);
    }

}
