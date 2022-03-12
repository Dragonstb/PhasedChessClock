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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import chessclock.LimitedTextField;
import chessclock.MasterFrame;
import localization.Loc;
import time.Constants;
import time.TimeControlPhase;

/** A dialog popping up for adding a {@link TimeControlPhase time control phase}.
 *
 * @author Dragonstb
 * @since 1.0;
 */
final class AddTCPDialog extends JDialog implements ActionListener, FocusListener {

    /** Action command of the OK button. */
    private static final String OK_ACTION = "ok";
    /** Action command of the cancel button. */
    private static final String CANCEL_ACTION = "cancel";
    /** Action command of the check box. */
    private static final String CHECK_BOX_ACTION = "change";

    /** A check box which can be set so that the given time is for an infinite amount of moves,
     * i.e.e the remainder of the game.
     */
    private JCheckBox remainderCheckBox;

    private LimitedTextField minutesTextField;
    private LimitedTextField secondsTextField;
    private LimitedTextField movesTextField;
    private LimitedTextField incrementTextField;

    private short numMoves;
    private int numMinutes;
    private int numSeconds;
    private int numIncrement;

    private JButton ok;
    private JButton cancel;

    /** Time control phase derived from the settings made. When adding a new phase, this object is
     * created when the OK button is hitted.
     */
    private TimeControlPhase tcp;

    /** generates without an initial time control phase.
     *
     * @since 1.0;
     * @param owner The window frame.
     * @param font Font used.
     */
    AddTCPDialog(MasterFrame owner, Font font) {
        this(owner, font, null);
    }

    /** Generates.
     *
     * @since 1.0;
     * @param owner Window frame.
     * @param font Font used.
     * @param tcp Fill form fields with data from this time control phase. Default values are used
     * if this is {@code null}.
     */
    AddTCPDialog(MasterFrame owner, Font font, TimeControlPhase tcp) {
        super(owner, owner.getLoc().retrieveString(Loc.ADD_TCP));
        Loc loc = owner.getLoc();
        setModalityType(ModalityType.APPLICATION_MODAL);
        getContentPane().setLayout(null);
        int paneW, paneH;
        int gap = 10;

        boolean restOfTheGame;
        if (tcp == null) {
            restOfTheGame = false;
            numMinutes = 120;
            numSeconds = 0;
            numIncrement = 10;
        } else {
            int time = tcp.getTime();
            restOfTheGame = tcp.getMoves() == TimeControlPhase.REMAINDER;
            numMinutes = time / Constants.SEC_PER_MIN;
            numSeconds = time % Constants.SEC_PER_MIN;
            numIncrement = tcp.getIncrement();
        }

        // ========== moves ==========
        JLabel label = new JLabel(loc.retrieveString(Loc.MOVES));
        label.setFont(font);
        label.setBounds(gap, 2 * gap + 25, 170, 25);
        add(label);

        numMoves = tcp == null ? 40 : tcp.getMoves();
        movesTextField = new LimitedTextField(String.valueOf(numMoves), 5, 5);
        movesTextField.setBounds(label.getX() + label.getWidth() + gap, label.getY(), 60, 25);
        movesTextField.setHorizontalAlignment(JTextField.CENTER);
        movesTextField.addFocusListener(this);
        movesTextField.setEnabled(!restOfTheGame);
        add(movesTextField);

        // deduce required size
        paneW = movesTextField.getX() + movesTextField.getWidth() + gap;

        // ========== minutes ==========
        label = new JLabel(loc.retrieveString(Loc.MINUTES));
        label.setFont(font);
        label.setBounds(gap, movesTextField.getY() + movesTextField.getHeight() + gap, 70, 25);
        add(label);

        minutesTextField = new LimitedTextField(String.valueOf(numMinutes), 3, 3);
        minutesTextField.setBounds(label.getX() + label.getWidth() + gap, label.getY(), 40, 24);
        minutesTextField.setHorizontalAlignment(JTextField.CENTER);
        minutesTextField.addFocusListener(this);
        add(minutesTextField);

        // ========== seconds ==========
        label = new JLabel(loc.retrieveString(Loc.SECONDS));
        label.setFont(font);
        label.setBounds(minutesTextField.getX() + minutesTextField.getWidth() + gap, minutesTextField.getY(), 70, 25);
        add(label);

        secondsTextField = new LimitedTextField("00", 3, 2); // passed text doesn't matter, becomes overwritten
        setSecondsText(numSeconds);
        secondsTextField.setBounds(label.getX() + label.getWidth() + gap, minutesTextField.getY(), 40, 25);
        secondsTextField.setHorizontalAlignment(JTextField.CENTER);
        secondsTextField.addFocusListener(this);
        add(secondsTextField);

        paneW = Math.max(paneW, secondsTextField.getX() + secondsTextField.getWidth() + gap);

        // ========== remainder of game? ==========
        remainderCheckBox = new JCheckBox(loc.retrieveString(Loc.REMAINDER), restOfTheGame);
        remainderCheckBox.setFont(font);
        remainderCheckBox.setBounds(gap, gap, paneW - 2 * gap, 25);
        remainderCheckBox.setActionCommand(CHECK_BOX_ACTION);
        remainderCheckBox.addActionListener(this);
        add(remainderCheckBox);

        // ========== increment ==========
        label = new JLabel(loc.retrieveString(Loc.INCREMENT));
        label.setFont(font);
        label.setBounds(gap, secondsTextField.getY() + secondsTextField.getHeight() + gap, 170, 25);
        add(label);

        incrementTextField = new LimitedTextField(String.valueOf(numIncrement), 5, 3);
        incrementTextField.setBounds(label.getX() + label.getWidth() + gap, label.getY(), 60, 25);
        incrementTextField.setHorizontalAlignment(JTextField.CENTER);
        incrementTextField.addFocusListener(this);
        add(incrementTextField);

        // ========== buttons ==========
        int compW = (paneW - 3 * gap) / 2;
        ok = new JButton(loc.retrieveString(Loc.OK));
        ok.setActionCommand(OK_ACTION);
        ok.addActionListener(this);
        ok.setBounds(gap, incrementTextField.getY() + incrementTextField.getHeight() + gap, compW, 40);
        ok.setFont(font);
        add(ok);

        cancel = new JButton(loc.retrieveString(Loc.CANCEL));
        cancel.setActionCommand(CANCEL_ACTION);
        cancel.addActionListener(this);
        cancel.setBounds(ok.getX() + ok.getWidth() + gap, ok.getY(), ok.getWidth(), ok.getHeight());
        cancel.setFont(font);
        add(cancel);

        // ========== finalize ==========
        paneH = ok.getY() + ok.getHeight() + gap;

        getContentPane().setPreferredSize(new Dimension(paneW, paneH));
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setResizable(false);
        setVisible(true);
    }

    /** Returns the time control phase build by this dialog. If no proper control phase can be
     * created from the text field input, an empty optinoal is returned.
     *
     * @since 1.0;
     * @return The time control phase.
     */
    Optional<TimeControlPhase> getTCP() {
        return Optional.ofNullable(tcp);
    }

    @Override
    public void focusGained(FocusEvent e) {
        Component c = e.getComponent();
        if (c == movesTextField || c == minutesTextField || c == secondsTextField || c == incrementTextField) {
            ((JTextField) c).selectAll();
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        Component c = e.getComponent();
        if (c == movesTextField) {
            parseMoves();
        } else if (c == minutesTextField) {
            parseMinutes();
        } else if (c == secondsTextField) {
            parseSeconds();
        } else if (c == incrementTextField) {
            parseIncrement();
        }
    }

    /** Parses the text in the 'moves' text field and sets the number of moves if the text is a
     * proper one. If not, the old text becomes restored.
     *
     * @since 1.0;
     */
    private void parseMoves() {
        int parsedMoves;
        try {
            parsedMoves = Integer.parseInt(movesTextField.getText());
            if (parsedMoves > 0) {
                numMoves = (short) Math.min(parsedMoves, Short.MAX_VALUE);
                movesTextField.setText(String.valueOf(numMoves));
            }
        } catch (java.lang.NumberFormatException e) {
        }
    }

    /** Parses the text in the 'minutes' text field and sets the number of moves if the text is
     * reasonable. If not, the old text becomes restored.
     *
     * @since 1.0;
     */
    private void parseMinutes() {
        int newNumMinutes;
        try {
            newNumMinutes = Integer.parseInt(minutesTextField.getText());
            if (newNumMinutes >= 0) {
                numMinutes = Math.min(newNumMinutes, chessclock.Constants.MINUTES_SOFT_LIMIT);
                minutesTextField.setText(String.valueOf(numMinutes));
            }
        } catch (NumberFormatException e) {
        }
    }

    /** Parses the text in the 'seconds' text field and sets the number of moves if the text is
     * reasonable. If not, the old text becomes restored.
     *
     * @since 1.0;
     */
    private void parseSeconds() {
        int newNumSeconds;
        try {
            newNumSeconds = Integer.parseInt(secondsTextField.getText());
            if (newNumSeconds >= 0 && newNumSeconds < 60) {
                numSeconds = newNumSeconds;
                setSecondsText(numSeconds);
            }
        } catch (java.lang.NumberFormatException e) {
        }
    }

    /** Parses the text in the 'increment' text field and sets the number of moves if the text is
     * reasonable. If not, the old text becomes restored.
     *
     * @since 1.0;
     */
    private void parseIncrement() {
        int newNumIncrement;
        try {
            newNumIncrement = Integer.parseInt(incrementTextField.getText());
            if (newNumIncrement >= 0) {
                numIncrement = newNumIncrement;
                incrementTextField.setText(String.valueOf(numIncrement));
            }
        } catch (NumberFormatException e) {
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case OK_ACTION -> {
                if (numMinutes > 0 || numSeconds > 0) {
                    int time = Constants.SEC_PER_MIN * numMinutes + numSeconds;
                    if (remainderCheckBox.isSelected()) {
                        numMoves = TimeControlPhase.REMAINDER;
                    }
                    tcp = TimeControlPhase.makeNew(numMoves, time, numIncrement);
                }
                dispose();
            }
            case CANCEL_ACTION -> {
                dispose();
            }
            case CHECK_BOX_ACTION ->
                movesTextField.setEnabled(!remainderCheckBox.isSelected());
        }
    }

    private void setSecondsText(int seconds) {
        if (seconds < 9) {
            secondsTextField.setText("0" + String.valueOf(numSeconds));
        } else {
            secondsTextField.setText(String.valueOf(numSeconds));
        }
    }

}
