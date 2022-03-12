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

import java.awt.BasicStroke;
import java.awt.Color;

import javax.swing.ImageIcon;

/** Some constants.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public interface Constants {

    /** Width of border that becomes coloured when this panel becomes the one of the current player. */
    public static final byte MARK_BORDER_SIZE = 3;

    /** Directory with the images. */
    public static final String FLAG_DIR = "img/";

    /** Image icon for white. */
    public static final ImageIcon WHITE_ICON = Util.getImageIcon(FLAG_DIR + "white.png");
    /** Image icon for black. */
    public static final ImageIcon BLACK_ICON = Util.getImageIcon(FLAG_DIR + "black.png");
    /** Image icon for pause. */
    public static final ImageIcon PAUSE_ICON = Util.getImageIcon(FLAG_DIR + "pause.png");
    /** Image icon for continue. */
    public static final ImageIcon CONTINUE_ICON = Util.getImageIcon(FLAG_DIR + "continue.png");
    /** Image icon for settings. */
    public static final ImageIcon SETTINGS_ICON = Util.getImageIcon(FLAG_DIR + "settings.png");
    /** Image icon for exchange colours. */
    public static final ImageIcon EXCHANGE_COLORS = Util.getImageIcon(FLAG_DIR + "exchangeColors.png");
    /** Image icon for exchange players. */
    public static final ImageIcon EXCHANGE_PLAYERS = Util.getImageIcon(FLAG_DIR + "exchangePlayers.png");

    /** The empty string. */
    public static final String EMPTY_STRING = "";

    public static final Color COUNTRY_COLOR_BOTTOM = new Color(0, 96, 191);
    public static final Color COUNTRY_COLOR_TOP = new Color(0, 8, 90);
    /** Background color of the buttons on the clock panel when highlighting them. */
    public static final Color ROLL_OVER_COLOR = new Color(30, 30, 30);

    /** Basic stroke of width 2. */
    public static final BasicStroke STROKE_2 = new BasicStroke(2);
    /** Basic stroke of width {@code MARK_BORDER_SIZE}. */
    public static final BasicStroke STROKE_MARK_BORDER_SIZE = new BasicStroke(MARK_BORDER_SIZE);

    /** A version string that may appear here and there. */
    public static final String VERSION = "1.0";

    /** The maximum number of minutes you can set in the
     * {@link chessclock.gamesettings.AddTCPDialog AddTCPDialog}. With increments and cumulative
     * TCPs you may end up with more than this number of minutes on the clock, hence the "soft" in
     * the limit. */
    public static final int MINUTES_SOFT_LIMIT = 900;
}
