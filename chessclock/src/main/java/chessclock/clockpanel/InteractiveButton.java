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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import chessclock.Constants;

/** A button on the {@link ClockPanel clock panel}.
 *
 * @author Dragonstb
 * @since 1.0;
 */
final class InteractiveButton extends JButton implements MouseListener {

    /** Default background color. */
    private final Color standardBackground;
    /** Background color while hovering the button with the mouse pointer. */
    private final Color rollOverBackground;

    /** Generates.
     *
     * @since 1.0;
     * @param text Label text.
     */
    InteractiveButton(String text) {
        super(text);
        setFocusable(false);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        standardBackground = Color.black;
        rollOverBackground = Constants.ROLL_OVER_COLOR;
        setBackground(Color.black);
        setForeground(Color.darkGray);

        addMouseListener(this);
        setBorder(null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackground(rollOverBackground);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBackground(standardBackground);
    }

}
