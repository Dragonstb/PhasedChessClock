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
package chessclock.about;

import java.awt.Color;
import java.awt.Font;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

import chessclock.Constants;
import localization.Loc;

/** The panel with the infos "about this application". This panel goes to the tabbed pane of the
 * {@link chessclock.settingspanel.SettingsPanel SettingsPanel}.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public final class AboutPanel extends JPanel {

    private static final Logger LOG = Logger.getLogger(AboutPanel.class.getName());
    private static final byte BORDER = 5;

    private JEditorPane about;

    /** Generates.
     *
     * @since 1.0;
     */
    private AboutPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Constants.COUNTRY_COLOR_TOP);
        setVisible(false);

        about = new JEditorPane("text/html", getDefaultAbout());
        about.setOpaque(true);
        about.setEditable(false);
        about.setBackground(Color.LIGHT_GRAY);
        about.setForeground(Color.WHITE);
        add(about);

        // add(Box.createVerticalStrut(BORDER));
    }

    public static AboutPanel makeNew() {
        AboutPanel ap = new AboutPanel();
        return ap;
    }

    /** retranslates the panel. Simply returns if {@code loc} is {@code null}.
     *
     * @since 1.0;
     * @param loc Localization.
     */
    public void updateLocalization(Loc loc) {
        if (loc == null) {
            return;
        }

        String text = switch (loc.getLanguageCode().substring(0, 2)) {
            default ->
                getDefaultAbout();
        };

        about.setText(text);
    }

    /** Sets the font used on this panel. Simply returns if {@code null} is passed.
     *
     * @since 1.0;
     * @param font Reference font.
     */
    public void setFonts(Font font) {
        if (font == null) {
            return;
        }

        about.setFont(font);
    }

    /** Gets the text displayed on the panel. For formating, HTML tags are included.
     *
     * @since 1.0;
     * @return The HTML text.
     */
    private String getDefaultAbout() {
        StringBuilder buf = new StringBuilder();
        buf.append("<center>");
        buf.append("<b>ChessClock</b><br>");
        buf.append("version " + Constants.VERSION + "<br>");
        buf.append("by Dragonstb (c) 2022<br>");
        buf.append("This software is distributed under the GNU General Public License, version 3<br>");
        buf.append("http://www.gnu.org/licenses/");
        buf.append("</center>");
        return buf.toString();
    }
}
