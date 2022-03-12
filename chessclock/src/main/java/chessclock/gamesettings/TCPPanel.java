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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import chessclock.MasterFrame;
import chessclock.Util;
import localization.Loc;
import time.Constants;
import time.TimeControlPhase;

/** A panel on the {@link PlayerDataPanel} that has all components needed to set up the list of
 * {@link TimeControlPhase time control phases} (TCP) for a player.
 *
 * @author Dragonstb
 * @since 1.0;
 */
final class TCPPanel extends JPanel implements ActionListener {

    /** A limit to the number of time control phases. */
    private static final byte MAX_TCPS = 30;

    /** List of the currently configured time control phases. In the game, the TCPs appear in the
     * same order as in this list. */
    private final JList<TCPContainer> cpList;
    /** The scroll pane the {@code cpList} lives in. */
    private final JScrollPane scrollPane;
    /** Adds a new time control phase above (before) the currently selected phase. */
    private final JButton addCpAbove;
    /** Adds a new time control phase below (after) the selected phase. */
    private final JButton addCpBelow;
    /** Shifts the currently selected time control phase one field up (earlier). */
    private final JButton raiseCP;
    /** Shifts the currently selected time control phase one position down (later). */
    private final JButton lowerCP;
    /** Removes the currently selected time control phase. */
    private final JButton removeCP;
    /** Edits the currently selected time control phase. */
    private final JButton editCP;
    /** The list model managing the list. */
    private final DefaultListModel<TCPContainer> listModel;

    /** Height of buttons in pixels. */
    private final int buttonHeight = 25;

    /** Window frame that holds everything together. Needed as owner for the dialogs popping up when
     * editing a TCP. */
    private final MasterFrame window;

    /** Generates.
     *
     * @since 1.0;
     * @param frame Window frame that holds everything together.
     */
    TCPPanel(MasterFrame frame) {
        super(null);
        setOpaque(false);
        window = frame;

        listModel = new DefaultListModel<>();
        cpList = new JList<>(listModel);
        cpList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cpList.setLayoutOrientation(JList.VERTICAL);
        cpList.setVisibleRowCount(3);
        TimeControlPhase initialTCP = TimeControlPhase.makeNew((short) 40, 120 * Constants.SEC_PER_MIN, 10);
        TCPContainer iniTCPContainer = new TCPContainer(initialTCP, null);
        listModel.addElement(iniTCPContainer);
        scrollPane = new JScrollPane(cpList);
        add(scrollPane);

        raiseCP = addButton();
        lowerCP = addButton();
        addCpBelow = addButton();
        addCpAbove = addButton();
        removeCP = addButton();
        editCP = addButton();
    }

    /** Shortcut for adding a button.
     *
     * @since 1.0;
     * @return A button with {@code this} as action listener;
     */
    private JButton addButton() {
        JButton button = new JButton();
        button.addActionListener(this);
        add(button);
        return button;
    }

    /** Updates the localization.
     *
     * @since 1.0;
     * @param loc New localization.
     */
    void updateLocalization(Loc loc) {
        if (loc == null) {
            return;
        }

        raiseCP.setText(loc.retrieveString(Loc.TCP_UP));
        lowerCP.setText(loc.retrieveString(Loc.TCP_DOWN));
        addCpAbove.setText(loc.retrieveString(Loc.ADD_TCP_ABOVE));
        addCpBelow.setText(loc.retrieveString(Loc.ADD_TCP_BELOW));
        removeCP.setText(loc.retrieveString(Loc.REMOVE_TCP));
        editCP.setText(loc.retrieveString(Loc.EDIT_TCP));

        listModel.elements().asIterator().forEachRemaining(cnt -> cnt.updateLocalization(loc));
    }

    /** Sets the fonts used.
     *
     * @since 1.0;
     * @param font Reference font all fonts are derived from.
     */
    void setFonts(Font font) {
        Font bold = Util.deriveBold(font);
        raiseCP.setFont(bold);
        lowerCP.setFont(bold);
        addCpAbove.setFont(bold);
        addCpBelow.setFont(bold);
        removeCP.setFont(bold);
        editCP.setFont(bold);
        cpList.setFont(bold);
    }

    /** Resizes and rearranges the panel and all of its GUI elements.
     *
     * @since 1.0;
     * @param width Width in pixels.
     * @return Newly computed height of panel, in pixels.
     */
    int rearrange(int width, int smallSpacing) {

        int fullWidth = width;
        int quarterWidth = (fullWidth - 3 * smallSpacing) / 4;
        int btnX1 = 0;
        int btnX2 = btnX1 + quarterWidth + smallSpacing;
        int btnX3 = btnX2 + quarterWidth + smallSpacing;
        int btnX4 = btnX3 + quarterWidth + smallSpacing;
        int y = 0;

        scrollPane.setBounds(btnX1, y, btnX4 - smallSpacing, 2 * buttonHeight + smallSpacing);
        raiseCP.setBounds(btnX4, y, quarterWidth, buttonHeight);
        y += buttonHeight + smallSpacing;
        lowerCP.setBounds(btnX4, y, quarterWidth, buttonHeight);
        y += buttonHeight + smallSpacing;
        addCpBelow.setBounds(btnX1, y, quarterWidth, buttonHeight);
        addCpAbove.setBounds(btnX2, y, quarterWidth, buttonHeight);
        removeCP.setBounds(btnX3, y, quarterWidth, buttonHeight);
        editCP.setBounds(btnX4, y, quarterWidth, buttonHeight);

        // new height
        y += buttonHeight;
        setSize(width, y);
        setPreferredSize(new Dimension(width, y));
        return y;
    }

    /** Raises the TCP at the given index by one position (i.e. to a lower index). Just returns if
     * {@code index} is out of bounds.
     *
     * @since 1.0;
     */
    private void raiseEntry(int index) {
        if (index < 1 || index >= listModel.size()) {
            return;
        }

        int newIndex = index - 1;
        TCPContainer s = listModel.remove(index);
        listModel.add(newIndex, s);
        goTo(newIndex);
    }

    /** Lowers the TCP at the given index by one position (i.e. to a higher index). Just returns if
     * {@code index} is out of bounds.
     *
     * @since 1.0;
     */
    private void lowerEntry(int index) {
        if (index < 0 || index >= listModel.size() - 1) {
            return;
        }

        int newIndex = index + 1;
        TCPContainer s = listModel.remove(index);
        listModel.add(newIndex, s);
        goTo(newIndex);
    }

    /** Removes the TCP at the given index. Just returns if {@code index} is out of bounds or if the
     * TCP is the last one left.
     *
     * @since 1.0;
     */
    private void removeEntry(int index) {
        if (listModel.size() == 1 || !isValidIndex(index)) {
            return;
        }

        listModel.remove(index);
        if (listModel.size() < 2) // disable button when only one entry left
        {
            removeCP.setEnabled(false);
        }

        enableAddTCPButtons(true);
        goTo(index);
    }

    /** Opens the dialog for editing a TCP and trys to add the newly created one. Just returns if
     * the argument is out of bounds or if the list is full already.
     *
     * @since 1.0;
     * @param index
     */
    private void openDialogAddEntry(int index) {
        if (listModel.size() >= MAX_TCPS) // sorry, we're full
        {
            return;
        }

        // open dialog
        AddTCPDialog dialog = new AddTCPDialog(window, raiseCP.getFont());

        // TimeControlPhase tcp;
        Optional<TimeControlPhase> tcpOpt = dialog.getTCP();
        if (tcpOpt.isPresent()) {
            executeAddEntry(index, tcpOpt.get());
        }
    }

    /** Adds the TCP to the list and also selects it.
     *
     * @since 1.0;
     * @param index Index the TCP has after it has been added. This is clamped to the valid range.
     * @param tcp TCP to be added.
     */
    private void executeAddEntry(int index, TimeControlPhase tcp) {
        TCPContainer container = new TCPContainer(tcp, window.getLoc());
        // note: allowed range is up to one greater than the number of existing entries
        int useIndex = Math.max(0, Math.min(index, listModel.size()));

        listModel.add(useIndex, container);
        goTo(useIndex);

        removeCP.setEnabled(true); // enable remove button, as there are at least two entries now.
        if (listModel.size() >= MAX_TCPS) {
            enableAddTCPButtons(false); // we are full now, disable buttons.
        }
    }

    /** Opens the dialog for editing an TCP. Just returns if the argument is out of bounds.
     *
     * @since 1.0;
     * @param index Index of the entry to be edited.
     */
    private void openDialogEditEntry(int index) {
        if (!isValidIndex(index)) {
            return;
        }

        TimeControlPhase tcp = listModel.get(index).getTcp();
        AddTCPDialog dialog = new AddTCPDialog(window, raiseCP.getFont(), tcp);

        Optional<TimeControlPhase> tcpOpt = dialog.getTCP();
        if (tcpOpt.isPresent()) {
            executeEditEntry(index, tcpOpt.get());
        }
    }

    /** Replaces the TCP at the given index with the TCP in the argument and selects the entry.
     *
     * @since 1.0;
     * @param index Index of replacement.
     * @param tcp TCP that takes the place.
     */
    private void executeEditEntry(int index, TimeControlPhase tcp) {
        TCPContainer container = new TCPContainer(tcp, window.getLoc());
        listModel.remove(index);
        listModel.add(index, container);
        goTo(index);
    }

    /** Selects and jumps to the given entry.
     *
     * @since 1.0;
     * @param index Index of the target entry. Becomes clamped to the valid range.
     */
    private void goTo(int index) {
        int newIndex = Math.max(0, Math.min(index, listModel.size() - 1));
        cpList.setSelectedIndex(newIndex);
        cpList.ensureIndexIsVisible(newIndex);
    }

    /** Checks if the index is within the valid range of the list for most (but not all!) cases.
     *
     * @since 1.0;
     * @param index Index of interest.
     * @return Is valid index?
     */
    private boolean isValidIndex(int index) {
        return index >= 0 && index < listModel.size();
    }

    /** Enables or disables the GUI elements.
     *
     * @since 1.0;
     * @param enable Enable?
     */
    void enableTCPSettings(boolean enable) {
        raiseCP.setEnabled(enable);
        lowerCP.setEnabled(enable);
        editCP.setEnabled(enable);
        removeCP.setEnabled(enable);
        addCpAbove.setEnabled(enable);
        addCpBelow.setEnabled(enable);
        cpList.setEnabled(enable);
    }

    /** Enables or disables the buttons for adding a new TCP.
     *
     * @since 1.0;
     * @param enable Enable?
     */
    private void enableAddTCPButtons(boolean enable) {
        addCpAbove.setEnabled(enable);
        addCpBelow.setEnabled(enable);
    }

    /**
     * @since 1.0;
     * @return A deep copy of the current list of TCPs.
     */
    TimeControlPhase[] getDeepClonedTCPs() {
        TimeControlPhase tcp;
        ArrayList<TimeControlPhase> list = new ArrayList<>();
        Optional<TimeControlPhase> opt;

        // get all until first one that has 'moves' set to "remainder" (including)
        for (int index = 0; index < listModel.size(); index++) {
            tcp = listModel.get(index).getTcp();
            opt = TimeControlPhase.makeNew(tcp);
            list.add(opt.get()); // we now that this optional is always present
            if (tcp.getMoves() == TimeControlPhase.REMAINDER) {
                break;
            }
        }

        TimeControlPhase[] tcps = new TimeControlPhase[list.size()];
        list.toArray(tcps);

        return tcps;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == raiseCP) {
            raiseEntry(cpList.getSelectedIndex());
        } else if (e.getSource() == lowerCP) {
            lowerEntry(cpList.getSelectedIndex());
        } else if (e.getSource() == removeCP) {
            removeEntry(cpList.getSelectedIndex());
        } else if (e.getSource() == addCpAbove) {
            openDialogAddEntry(cpList.getSelectedIndex());
        } else if (e.getSource() == addCpBelow) {
            openDialogAddEntry(cpList.getSelectedIndex() + 1);
        } else if (e.getSource() == editCP) {
            openDialogEditEntry(cpList.getSelectedIndex());
        }
    }

}
