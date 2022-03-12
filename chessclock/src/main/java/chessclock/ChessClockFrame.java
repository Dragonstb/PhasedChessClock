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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import chessclock.clockpanel.ClockPanel;
import chessclock.settingspanel.SettingsPanel;
import localization.Loc;
import time.TimeBudgetConstraint;
import time.TimeControl;

/** Is the main class and integrates the components.
 *
 * @author Dragonstb
 * @since 1.0;
 */
public final class ChessClockFrame extends MasterFrame implements KeyListener, WindowListener {

    private static final Logger LOG = Logger.getLogger(ChessClockFrame.class.getName());

    private LoadPanel loadPanel;
    private JPanel currentPanel;
    /** The one chamber of the application's heart. */
    private TimeControl timeControl;

    /** A standard font all fonts are derived from. */
    private Font referenceFont;

    private ChessClockFrame(String title) throws HeadlessException {
        super(title);

        int width = 1200;
        int height = 675;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.pink);
        setForeground(Color.lightGray);

        referenceFont = new JPanel().getFont();

        loadPanel = new LoadPanel(width, height);
        currentPanel = loadPanel;
        getContentPane().add(loadPanel);

        // closing via the window listener operation only
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(true);

        setLocation(20, 20);
        validate();
        pack();
        setVisible(true);
    }

    public static ChessClockFrame makeNew(String windowTitle) throws HeadlessException {
        String title = !windowTitle.isBlank() ? windowTitle.trim() : "ChessClock";
        ChessClockFrame ccf = new ChessClockFrame(title);
        return ccf;
    }

    /** Sets the font as reference font and updates the fonts in the settings panel as well as in
     * the clock panel.
     *
     * @since 1.0;
     * @param font Font used from now on.
     */
    void setReferenceFont(Font font) {
        referenceFont = font;
        getSettingsPanel().setFonts(referenceFont);
        getClockPanel().setFonts(referenceFont);
    }

    /** Updates the localization in the entire application. All texts become translated. Simply
     * returns if {@code null} is passed.
     *
     * @since 1.0;
     * @param loc New localization.
     */
    void updateLocalization(Loc loc) {
        if (loc == null) {
            return;
        }

        setLoc(loc);
        getSettingsPanel().updateLocalization(loc);
        getClockPanel().updateLocalization(loc);
    }

    /** removes the {@link LoadPanel loading panel} and switches to the
     * {@link SettingsPanel settings panel}. Invoke once after the initialization has completed.
     *
     * @since 1.0;
     */
    void iniDone() {
        getContentPane().remove(loadPanel);
        goToSettingsPanel();
        loadPanel = null; // no need for it anymore
    }

    /** Switches to the clock panel.
     *
     * @since 1.0;
     */
    @Override
    public void goToClockPanel() {
        currentPanel = getClockPanel();
        getSettingsPanel().setVisible(false);
        currentPanel.setVisible(true);
        repaint();
        requestFocus();
    }

    /** Switches to the settings panel.
     *
     * @since 1.0;
     */
    @Override
    public void goToSettingsPanel() {
        currentPanel = getSettingsPanel();
        getClockPanel().setVisible(false);
        currentPanel.setVisible(true);
        repaint();
    }

    /** Makes all the changes required after changing the game settings and returns to the clock
     * panel.
     *
     * @since 1.0;
     */
    @Override
    public void acceptSettings(PlayerData dataLeft, PlayerData dataRight, boolean isLeftWhite) {
        Optional<TimeBudgetConstraint> optLeft = makeTimeBudgetCosnstraint(dataLeft);
        Optional<TimeBudgetConstraint> optRight = makeTimeBudgetCosnstraint(dataRight);

        if (optLeft.isEmpty() || optRight.isEmpty()) {
            // should not happen, actually. SettingsPanel checks the PlayerData before calling this method
            Util.showMessage(this, Loc.PLAYER_DATA_ERROR, JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        timeControl.setupNewGameTiming(optLeft.get(), optRight.get(), isLeftWhite);

        ClockPanel clockPanel = getClockPanel();
        clockPanel.setDataLeft(dataLeft.getName(), dataLeft.getCountry(), getLoc());
        clockPanel.setDataRight(dataRight.getName(), dataRight.getCountry(), getLoc());
        clockPanel.arrangeColorIndicators(isLeftWhite);
        clockPanel.rearrange(getContentPane().getWidth(), getContentPane().getHeight());

        goToClockPanel();
    }

    /** Shuts everything down and exits application.
     *
     * @since 1.0;
     */
    @Override
    public void exitApplication() {
        if (timeControl != null) {
            timeControl.endSignalClock();
            timeControl.wakeSignalClockUp();
        }
        System.exit(0);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            exitApplication();
        } else if (currentPanel == getClockPanel()) {
            if (e.getKeyCode() != KeyEvent.VK_SPACE) {
                timeControl.notifyMoveDone();
            } else {
                timeControl.notifyPauseChangeRequest();
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        exitApplication();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    /** Sets the time control.
     *
     * @since 1.0;
     * @param timeControl The time control.
     */
    void setTimeControl(TimeControl timeControl) {
        this.timeControl = timeControl;
    }

    /** Let's start!
     *
     * @param args CL arguments
     */
    public static void main(String[] args) {
        Logger.getGlobal().setLevel(Level.OFF);

//        Policy policy;
//        try {
//            URL url = Util.getResourceURL("policy/policy.txt");
//            URIParameter parm = new URIParameter(url.toURI());
//            policy = Policy.getInstance("JavaPolicy", parm);
//            Policy.setPolicy(policy);
//        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "Could not install security policy: " + e.getClass().getSimpleName());
//            e.printStackTrace();
//            System.exit(15);
//            return;
//        }
//
//        SecurityManager securityManager = new SecurityManager();
        // System.setSecurityManager(securityManager);
        ChessClockFrame frame = null;
        try {
            frame = new ChessClockFrame("ChessClock");
        } catch (HeadlessException e) {
        }

        if (frame != null) {
            LaunchThread lt;
            lt = new LaunchThread(frame);
            lt.start();
        } else {
            LOG.log(Level.SEVERE, "Could not initialize window. Shutting down");
            System.exit(10);
        }
    }

    /**
     * @since 1.0;
     * @param data
     * @return
     */
    private Optional<TimeBudgetConstraint> makeTimeBudgetCosnstraint(PlayerData data) {
        if (data == null) {
            return Optional.empty();
        }

        Optional<TimeBudgetConstraint> opt = TimeBudgetConstraint.makeNew(data.getTimeControlPhases(),
                data.getMoveTimeLimit(), data.getWarningThreshold(), data.arePhasesAdditive());
        return opt;
    }
}
