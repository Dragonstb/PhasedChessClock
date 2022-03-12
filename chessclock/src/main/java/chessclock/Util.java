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

import java.awt.Font;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import localization.Loc;

/** Some handy utility methods.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public final class Util {

    /** Derives the bold version of the given font.
     *
     * @since 1.0;
     * @param font Base font.
     * @return Bold version.
     */
    public static Font deriveBold(Font font) {
        return font.deriveFont(Font.BOLD);
    }

    /** Displays a {@code JOptionPane}.
     *
     * @since 1.0;
     * @param frame The hosting frame. We need to ask it for the localization.
     * @param messageKey Localization key to the message text.
     * @param messageType The message type, a constant field of {@code JOptionPane}.
     */
    public static void showMessage(MasterFrame frame, String messageKey, int messageType) {
        String message = frame.getLoc().retrieveString(messageKey);
        String title = frame.getLoc().retrieveString(Loc.EXCEPTION_TITLE);
        JOptionPane.showMessageDialog(frame, message, title, messageType);
    }

    /** Gets the path of a ressource.
     *
     * @since 1.0;
     * @param fileName File name without path.
     * @return Path and file name.
     * @deprecated But we need the URL.
     */
    @Deprecated
    public static String getResourcePath(String fileName) {
        return ".chessclock-" + Constants.VERSION + ".jar!/" + fileName;
    }

    /**
     * @since 1.0;
     * @param fileName File name of a ressource, without path.
     * @return URL to that ressource.
     */
    public static URL getResourceURL(String fileName) {
        URL url = Util.class.getClassLoader().getResource(fileName);
        if (url == null)
			try {
            url = new URL("file:" + fileName);
        } catch (MalformedURLException e) {
        }
        return url;
    }

    /**
     * @since 1.0;
     * @param fileName File name of an image ressource.
     * @return The image.
     */
    public static ImageIcon getImageIcon(String fileName) {
        URL url = getResourceURL(fileName);
        if (url != null) {
            return new ImageIcon(url);
        }
        return new ImageIcon(fileName);
    }

}
