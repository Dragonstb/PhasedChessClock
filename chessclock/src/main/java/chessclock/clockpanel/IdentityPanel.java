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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chessclock.ChessLabel;
import chessclock.Constants;
import chessclock.Countries;
import chessclock.Country;
import localization.Loc;

/** A panel that displays infos about a player (name, home country). It is located on the
 * {@link ClockPanel ClockPanel}.
 *
 * @author Dragonstb
 * @since 1.0;
 */
final class IdentityPanel extends JPanel {

    /** Width of the border around the name field. */
    private static final int ELEMENT_BORDER_WIDTH = 2;
    private static final int COUNTRY_NAME_SPACING = 15;
    private static final int COUNTRY_ADD_WIDTH = 10;

    /** A label displaying the player's name. */
    private final ChessLabel nameLabel;
    /** A label displaying the country code of the player's home country. */
    private final ChessLabel countryCodeLabel;
    /** The image of the flag of the player's home country. */
    private ImageIcon flag;
    /** The localization key for the country code. */
    private String countryCodeKey;

    /** Height of the panel, in pixels. */
    private int identityHeight;
    /** X position of the country code, in pixels. */
    private int countryX;
    /** Width of the country code field, in pixels. */
    private int countryWidth;
    /** X position of the name field, in pixels. */
    private int nameX;
    /** Width of the name field, in pixels. */
    private int nameWidth;
    /** Height of the name field, in pixels. */
    private int nameHeight;
    /** Y position of the name string. */
    private int nameY;
    /** Y position of the flag. */
    private int flagY;
    /** X position of the flag. */
    private int flagX;
    /** X position of the box drawed around the flag. */
    private int boundX;

    /** Font used. */
    private Font nameFont;
    /** Is this panel on the left side of the {@link ClockPanel clock panel}?. Used to determine the
     * exact arrangement of the elements. */
    private final boolean left;

    /** We wanna have a fancy color gradient as backgroudn for the country code. */
    private final GradientPaint gradientPaint;

    /** Generates.
     *
     * @since 1.0;
     * @param left Is on the left side of the {@link ClockPanel clock panel}?
     */
    IdentityPanel(boolean left) {
        super(null);
        setOpaque(false);

        // use earth as default
        Country country = Countries.EARTH;
        flag = country.getFlag();
        countryCodeKey = country.getCodeKey();

        this.left = left;
        nameHeight = 42;
        identityHeight = 2 * ELEMENT_BORDER_WIDTH + nameHeight;
        nameY = flagY = ELEMENT_BORDER_WIDTH;

        gradientPaint = new GradientPaint(0, identityHeight - 1, Constants.COUNTRY_COLOR_BOTTOM, 0, 0,
                Constants.COUNTRY_COLOR_TOP);

        nameLabel = new ChessLabel("- no name -");
        nameLabel.setBackground(Color.black);
        nameLabel.setForeground(Color.white);
        nameLabel.setHorizontalAlignment(left ? JLabel.LEFT : JLabel.RIGHT);
        nameLabel.setVerticalAlignment(JLabel.CENTER);
        nameLabel.setOpaque(false);
        add(nameLabel);

        countryCodeLabel = new ChessLabel("ANY"); // we don't have a localization yet
        countryCodeLabel.setHorizontalAlignment(JLabel.CENTER);
        countryCodeLabel.setVerticalAlignment(JLabel.CENTER);
        countryCodeLabel.setForeground(Color.white);
        countryCodeLabel.setOpaque(false);
        add(countryCodeLabel);

    }

    /** Set the fonts used and updates the display accordingly. Simply returns if {@code null} is
     * passed;
     *
     * @since 1.0;
     * @param referenceFont New font.
     */
    void setFonts(Font referenceFont) {
        if (referenceFont == null) {
            return;
        }

        nameFont = referenceFont.deriveFont(Font.BOLD, 3 * nameHeight / 4);
        adjustNameSqueezing(); // this method begins with setting the font of the name label to 'nameFont'

        // country codes
        Font font = referenceFont.deriveFont(Font.PLAIN, 3 * nameHeight / 4);
        countryCodeLabel.setFont(font);
    }

    /** Adopts a new width. The display also becomes updated in this process.
     *
     * @since 1.0;
     * @param width New width, in pixels.
     */
    void rearrange(int width) {
        setSize(width, identityHeight);
        setPreferredSize(new Dimension(width, identityHeight));

        int flagWidth = flag.getIconWidth();
        countryWidth = countryCodeLabel.getTextWidth() + COUNTRY_ADD_WIDTH;

        if (left) {
            flagX = ELEMENT_BORDER_WIDTH;
            // left side country code
            countryX = flagX + flagWidth + ELEMENT_BORDER_WIDTH;
            // left side name
            // +-- there are borders both on the left and right of the flag
            nameX = countryX + countryWidth + COUNTRY_NAME_SPACING;
            // +-- there is a border between name and colour indicator, too
            nameWidth = width - nameX - ELEMENT_BORDER_WIDTH;
        } else {
            // right side flag
            flagX = width - ELEMENT_BORDER_WIDTH - flagWidth;

            // right side country code
            countryX = flagX - ELEMENT_BORDER_WIDTH - countryWidth;

            // right side name
            // +-- there is a border between name and colour indicator
            nameX = ELEMENT_BORDER_WIDTH;
            nameWidth = countryX - nameX - COUNTRY_NAME_SPACING;
        }

        boundX = flagX - ELEMENT_BORDER_WIDTH;
        nameLabel.setBounds(nameX, nameY, nameWidth, nameHeight);
        countryCodeLabel.setBounds(countryX, 0, countryWidth, identityHeight);
        adjustNameSqueezing();
    }

    /** Updates the localization used and immediatly changed the country code to the new loc. Just
     * returns if {@code null} is passed.
     *
     * @param loc New localization.
     */
    void updateLocalization(Loc loc) {
        if (loc == null) {
            return;
        }

        countryCodeLabel.setText(loc.retrieveString(countryCodeKey));
    }

    void updateData(String name, Country country, Loc loc) {
        nameLabel.setText(name);
        flag = country.getFlag();
        countryCodeKey = country.getCodeKey();
        updateLocalization(loc);
        // you may ask now why 'rearrange()' is not called. The width of the flag may not fit anymore to its frame.
        // Well, that's true, but 'rearrange()' is called when accepting the settings, which is (for now) the only
        // situation where updateData(..) is called, too (and even before 'rearrange()', isn't it wonderful?).
    }

    /** Checks if the name still fits in its Label and squeezes it if not.
     *
     * @since 1.0;
     */
    private void adjustNameSqueezing() {
        nameLabel.setFont(nameFont);
        // squeeze text when too long
        if (nameLabel.getTextWidth() >= nameLabel.getWidth()) {
            // the .97 is an additional safety factor, if too large, the name might still be too long for the JLabel to
            // fully display the text
            double factor = nameLabel.getWidth() * .97 / nameLabel.getTextWidth();
            AffineTransform transform = AffineTransform.getScaleInstance(factor, 1);
            nameLabel.setFont(nameFont.deriveFont(transform));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // flag left
        g2d.setColor(Color.gray);
        g2d.drawRect(boundX, 0, flag.getIconWidth() + 2 * ELEMENT_BORDER_WIDTH - 1, identityHeight - 1);
        g2d.drawImage(flag.getImage(), flagX, flagY, null);

        // country code left
        g2d.setPaint(gradientPaint);
        g2d.fillRect(countryX, 0, countryWidth, identityHeight);

        // border around name
        g2d.setStroke(Constants.STROKE_2);
        g2d.setColor(Constants.COUNTRY_COLOR_BOTTOM);
        if (left) {
            // left name border, lower line
            g2d.drawLine(nameX - COUNTRY_NAME_SPACING + 1, identityHeight - 1, nameX + nameWidth + 1,
                    identityHeight - 1);
            // left name border, upper line
            g2d.drawLine(nameX - COUNTRY_NAME_SPACING + 1, 1, nameX + nameWidth + 1, 1);
            // left name border, inner line
            g2d.drawLine(nameX + nameWidth + 1, 1, nameX + nameWidth + 1, identityHeight - 1);
        } else {
            // right name border, lower line
            g2d.drawLine(nameX - 1, identityHeight - 1, nameX + nameWidth + COUNTRY_NAME_SPACING - 1,
                    identityHeight - 1);
            // right name border, upper line
            g2d.drawLine(nameX - 1, 1, nameX + nameWidth + COUNTRY_NAME_SPACING - 1, 1);
            // right name border, inner line
            g2d.drawLine(nameX - 1, 1, nameX - 1, identityHeight - 1);
        }
    }

    /**
     * Returns the height of the name field.
     *
     * @since
     * @return The height of the name field, in pixels.
     */
    int getNameHeight() {
        return nameHeight;
    }

    /** Returns the height of the identity panel.
     *
     * @since 1.0;
     * @return The heigth of the identity panel, in pixels.
     */
    int getIdentityHeight() {
        return identityHeight;
    }

}
