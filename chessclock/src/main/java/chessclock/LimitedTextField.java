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

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/** A {@link JTextField} that can take only a limited amount of characters.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public final class LimitedTextField extends JTextField {

    /**
     * @param text Text to be displayed.
     * @param charLimit Maximum number of chars the text field can contain.
     */
    public LimitedTextField(String text, int charLimit) {
        super(text);
        ini(charLimit);
    }

    /**
     * @param text Text to be displayed.
     * @param cols Number of columns.
     * @param charLimit Maximum number of chars in each column.
     */
    public LimitedTextField(String text, int cols, int charLimit) {
        super(text, cols);
        ini(charLimit);
    }

    private void ini(int charLimit) {
        ((LimitedCharDocument) getDocument()).setCharLimit(charLimit);
    }

    @Override
    protected Document createDefaultModel() {
        return new LimitedCharDocument();
    }

    private static class LimitedCharDocument extends PlainDocument {

        private int charLimit = 20;

        private void setCharLimit(int charLimit) {
            if (charLimit > 0) {
                this.charLimit = charLimit;
            }
        }

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str.isEmpty() || getLength() >= charLimit) {
                return;
            }

            int unifiedLength = getLength() + str.length();
            // second condition is against integer overflow
            if (unifiedLength <= charLimit && unifiedLength > 0) {
                super.insertString(offs, str, a);
            } else {
                String fits = str.substring(0, charLimit - getLength());
                super.insertString(offs, fits, a);
            }
        }

    }

}
