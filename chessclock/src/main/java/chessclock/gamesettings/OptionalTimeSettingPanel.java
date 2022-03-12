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
package chessclock.gamesettings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import chessclock.ChessCheckBox;
import chessclock.ChessLabel;
import chessclock.LimitedTextField;
import chessclock.Util;
import time.Constants;

/** A panel on the {@link PlayerDataPanel} with components for setting up an optional time limit for
 * each move.
 *
 * @author Dragonstb
 * @since 1.0;
 */
final class OptionalTimeSettingPanel extends JPanel implements ActionListener, FocusListener {

    /** Check box which enables or disables the optional time setting. */
    private final ChessCheckBox checkBox;
    /** A descriptive Label. */
    private final ChessLabel label;
    /** Text field for setting the time. */
    private final LimitedTextField textField;
    /** Set time in seconds. */
    private int seconds;

    /** Generates
     *
     * @since 1.0;
     */
    OptionalTimeSettingPanel() {
        super(null);
        setOpaque(false);

        checkBox = new ChessCheckBox("", false);
        checkBox.addActionListener(this);
        checkBox.setVerticalAlignment(JCheckBox.CENTER);
        checkBox.setHorizontalAlignment(JCheckBox.LEFT);
        add(checkBox);

        label = new ChessLabel("");
        label.setOpaque(false);
        label.setForeground(Color.white);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.LEFT);
        add(label);

        seconds = 600;
        textField = new LimitedTextField(String.valueOf(seconds), 5, 4);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setEnabled(checkBox.isSelected());
        textField.addActionListener(this);
        textField.addFocusListener(this);
        add(textField);
    }

    /** Updates the text on the label.
     *
     * @since 1.0;
     * @param displayText Text to be displayed.
     */
    void updateLocalization(String displayText) {
        label.setText(displayText);
    }

    /** Updates the fonts used.
     *
     * @since 1.0;
     * @param font Reference font.
     */
    void setFonts(Font font) {
        label.setFont(Util.deriveBold(font));
        textField.setFont(font);
    }

    /** Rearranges and resizes the elements.
     *
     * @since 1.0;
     * @param width New width, in pixels.
     * @param lineHeight new height, in pixels.
     * @param smallSpacing Width of a spacing between text field and label, in pixels.
     */
    void rearrange(int width, int lineHeight, int smallSpacing) {
        int checkBoxWidth = lineHeight;
        int textFieldWidth = (width - 3 * smallSpacing) / 4;
        int textFieldX = width - textFieldWidth;
        int checkTextX = checkBoxWidth;
        int checkTextWidth = textFieldX - checkTextX;

        checkBox.setBounds(0, 0, checkTextWidth, lineHeight);
        label.setBounds(checkTextX, 0, checkTextWidth, lineHeight);
        textField.setBounds(textFieldX, 0, textFieldWidth, lineHeight);

        setSize(width, lineHeight);
        setPreferredSize(new Dimension(width, lineHeight));
    }

    /** Parses the text in the text field to a number of seconds. If successful, the value is set as
     * the new number of seconds. If not, the text is restored to the old value. Pasing also fails
     * when the number is negative.
     *
     * @since 1.0;
     */
    private void parseSeconds() {
        try {
            int newValue = Integer.parseInt(textField.getText());
            if (newValue < 1) {
                throw new NumberFormatException();
            }
            seconds = newValue;
        } catch (java.lang.NumberFormatException exc) {

        }

        textField.setText(String.valueOf(seconds));
    }

    /** Returns the amount of seconds currently set.
     *
     * @since 1.0;
     * @return The seconds set.
     */
    int getSeconds() {
        return seconds;
    }

    boolean isSet() {
        return checkBox.isSelected();
    }

    /** Gets the time in the text field, but transformed to nanoseconds.
     *
     * @since 1.0;
     * @return The time set in nanoseconds or -1, if the check box is not checked.
     */
    long getEffectiveNanoseconds() {
        return checkBox.isSelected() ? seconds * Constants.NANOSEC_PER_SEC : -1;
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getComponent() == textField) {
            textField.selectAll();
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getComponent() == textField) {
            parseSeconds();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == checkBox) {
            updateTextFieldEnabling();
        } else if (e.getSource() == textField) {
            parseSeconds();
        }
    }

    void enableOTS(boolean enable) {
        checkBox.setEnabled(enable);
        updateTextFieldEnabling();
    }

    private void updateTextFieldEnabling() {
        boolean enable = checkBox.isSelected() && checkBox.isEnabled();
        textField.setEnabled(enable);
        if (!enable) {
            parseSeconds();
        }
    }

}
