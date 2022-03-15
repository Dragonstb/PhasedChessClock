Architecture of the phased chess clock

# Introduction

In chess, games can be played under a time control that limits the amount of time a player can think about the moves to make. If you play at home and want to aplly a time control as well, you either need to buy a chess clock or use a chess clock program like this one.

In competitions, the time control is usually divided into different periods. The players may have a different amount of time for planning their moves in each period, and each period may last for different number of moves.

This program is supposed to provide software realization of a chess clock where you can set up time controls with different periods.

## What this program is supposed to be able to do
- Setting up sequences of time control phases (TCP).
- Different of such sequences for each player possible, but not necessary.
- Setup the amount of time available for planning the moves in each TCP.
- Setup the increment that is added to the time left after making a move in each TCP.
- Setting the number of moves a TCP last in each TCP, including the possibility that a TCP lasts for the remainder of the game.
- Settings of a TCP are independent from all other TCPs.
- Simple and fast way for telling the clock that you made your move.
- A switch allowing for setting up the sequence of TCPs for one player, but using it for both players in the game.
- Clearly indicate if the time has run out, and for whom, without the possibility to just click it away.
- Optional, additional time limit for each move, individually set for each player.
- Optional time threshold below the chess clock gives an optical warning to the player, individually set for each player.
- Setting a name in a form field so that it becomes clear which of the two displayed is yours.
- Pausing a running game, and resuming it.
- Easily switch sides of the players.
- Easily switch colours played with.
- Minimize numbers of clicks to be made by the users.

# Boundary conditions

## Players

The two players sit face to face to each other, with the board between them. The clock usually is placed next to the board.

The clock becomes signaled about a move made by a device being pressed. We can expect this device (presumably a keyboard or a touch sensitive screen) to be placed next to the board, in reach of both players.

The display is also assumed to be placed where both players can see it in most cases. Neither the distance nor the window size used can be foreseen.

## Computer

Modern computers provide an internal clock than can be used for the timing. As mentioned above, the size of the window may vary.

The fonts installed vary from machine to machine. Therefore, it cannot be assumed that a certain font is available.

Some elements on the GUI need to allow for an input more or less free to the user's choice. This needs to be filtered for input which is useful and does no harm to the machine.


# Context and environment

This program runs just locally. Interaction is made only with the users in front of the screen. There is no network connection. Data read from the hard drive is limited to the pictures used in the application and the fonts loaded.

As this is a java program, it runs in a JVM.

Inputs are made by the users only locally. There are two types inputs:
- Inputs in the GUI for setting up the clock or changing the state of the application.
- Typing keys for signaling that a move has been made or pausing/resuming a running game.

The output is delivered to the user solemnly by the screen.

# Bricks of the program

The phased chess clock is divided into two parts: the GUI and the actual clock work.

For the GUI, there is a top down hierarchy. The window frame references the two main panels, one for displaying the clock and one for the settings and options. These panels reference child elements which are larger parts on the panels. These child panels may refer to other, smaller GUI elements on them, and so on.

*tbc... especially pictures*

# Runtime

## Launch phase

You can nothing do here except for exiting again (for example via `Ctrl + C`). The program loads and initializes the GUI and the clock work. The signal clock is halted.

After launching, the application switches to the settings panel.

## Settings panel

In the settings panel, users may setup everything as they need. The settings remain stored in the GUI until again changed by the user. Switching the tabs or even to the clock panel won't change the contents of the form fields.

Clicking the *cancel* button leads immediately to the clock panel, without touching or even notifying the clock work.

In contrast, clicking *ok* still leads to the clock panel, but also reads the settings from the form fields, collects them, and sends them to the clock work. This resets the clock work sets up the time control for a new game, using the new data. a possibly existing time control is discarded. The signal clock is halted.

Clicking the *exit* button terminates the thread of the signal clock and exits.

## Clock panel

Besides ticking down mercilessly, the time of the player at turn is surrounded by a dark blue border.

The state the clock panel displays after a new time control has been set up depends on the choice made by the user if the first move already counts against the time. If so, the clock panel displays a neutral state, with no number given in the move counter and no player being highlighted. If the first move comes for free, the move number is set to unity, and the the time for player with the white pieces is highlighted, but not running though.

In the clock panel, clicking into the area with the time displays or hitting any key except `esc` or `space` signals the clock that a move has been made. The clock work changes some internal states and informs the GUI. Following this notification, the clock panels adjusts the display.

As there is a move counter in the clock work, the signal clock may be unlocked when a move is done.

A running signal clock calls the update method of the central time control once per loop iteration. The time is taken from the player in turn and the new time is sent to the clock panel. If the time runs out in a frame, this is also told to the clock panel, the clock work switches to a "game has ended" state, and the signal clock is halted.

Only in a state of the clock work where the time is actually running down, the clock work can be paused and resumed. To do so, the users can press the *pause* button on the clock panel or hit `space`.
Pausing the game causes the signal clock to be halted. Resuming the game unlocks the signal clock.

If the clock work is in a non-running state, pause signals are ignored.

Pressing the *Settings* button yields the settings panel to appear on the screen. It does not, however, affects the clock work in anyway. If the time is running, it keeps on running in the background while the settings screen is displayed. (However, you will see this only if you go back via the *cancel* button, as described above).

Pressing `esc` terminates the thread the signal clock runs in and shuts the application down.

All key presses react on release of the key.

## Signal clock

The signal clocks runs in a separate thread. This mainly consitst of a loop that calls the update method of the tie control. The time required for an iteration is passed as the argument for the update.

In each iteration, a boolean is checked if the signal clock may halt. If so, the thread becomes dormant by calling wait(). The boolean can be set from the outside to "sleeping". Unlocking the thread is done by setting the boolean to "running" and calling notify() on the signal clock. The access to this boolean is not synchronized with the signal clock thread. Thus it may happen that the sleeping signal is detected in the next iteration. This doesn't matter as the time needed for an iteration is very short.

The termination signal allows the signal clock to leave the update loop, leading it to the end of the thread. The signal clock is woken up in case if it is dormant when the termination signal arrives.

## Switching the player in turn

In the clock work, the switch-player signal also just sets a boolean that the player in turn is to be changed. The change is the update method, and the boolean is resetted. This could leave to a signal being missed if you manage to send two signals within one frame. But again, due to the short time of a frame, this should not matter. The length of the short move could not be resolved anyway and would be practically zero.

*tbc... especially pictures*

# Further chapters

*tbc*

