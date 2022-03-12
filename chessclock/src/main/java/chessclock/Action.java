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

/** A list of the action commands.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public interface Action {

    /** Tool bar panel: Settings button. */
    public static final String SETTINGS = "settings";
    /** Tool bar panel: Pause/Continue button. */
    public static final String PAUSE = "pause";

    /** Settings: OK button. */
    public static final String OK = "ok";
    /** Settings: cancel button. */
    public static final String CANCEL = "cancel";
    /** Settings: exit. */
    public static final String EXIT = "exit";

}
