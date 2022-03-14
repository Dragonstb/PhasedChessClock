# Phased Chess Clock

In chess, the time control can be divided into different periods. In each period, the player may have a different amount of time available, with different increments. For example, you may have 90 minutes for the first 40 moves, 60 minutes for the next 20 moves, and 15 minutes for the remainder of the game.
This program allows to set up such a time control with such sequences, and you can set up different sequences for the players.

# Features

- Time control phases with their own amount of time, increment, and number of moves they are valid for. Each phase can be configured independently from the other phases.
- The two players' time control sequences are independent of each other. They may be absolutely the same or completely different.
- Optional, additional time limit for each move.
- Each player who wants to do so may set up a time below which the clock provides an optical signal that you better hurry up.
- Clock can be paused. Very handy when the pizza is delivered midgame.

# Usage

Running this program requires a Java Runtime Environment version 17 or above.

## Launch

So far, you can launch this application only via a command line interface.

Copy the chessclock-<version number>.jar and the chessClockWork-<version number>.jar into the same directoy an go into this directory. Type ```java -p . -m chessclock``` into your console and hit enter. If the directory with the java executable is not in your PATH, you have to enter the full path to the java executable.

## Settings screen

You start in the settings screen where you can set up the time control for each player individually. If both players are supposed to play with the same time control, you can hit the checkbox at the top. If checked you only need to setup the timing on the left side, and this will be adopted for the player on the right.

You can also enter a name and choose a country. For the latter, you can choose only from "Earth" (ok, and a fictional country intoduced for debugging). But that is fine, so I'm not inclined to extend this list for now. You can make your own fork if you wanna have more countries.

## Clock panel

Hitting *OK* sets up the clock and switches to the clock panel where the names, the remaining times and a move counter are displayed. The time is stopped and waits for you to begin.

The player in turn can be changed forth an back either by clicking into the area below the names or by hitting any key *except* for both `esc` and `space`.

Clicking on the *Pause* button at the top or pressing `space` pauses the clock. Resume it the same way.

Clicking the *Settings* button guides you back to the settings panel. **Note:** The clock continues running in the background. Clicking on *cancel* leads back to the running clock, while *OK* discards the current one and sets up a new one.

The *exit* button, as well as pressing `esc` closes the application.

# Project structure and licencing

This is a modular Gradle multi build, consisting of two sub projects: The GUI, and the clock work. Consequently, two java modules are created:
- The chessclock contains the GUI that displays the clock. It is also used to configure the timing setup. This module is published under the GPLv3 licence.
- The chessClockWork does the actual timing. It can tell any listener how much time  (if any) is left for each player. This module is published under the GPLv3 licence with a linking exception.

# Roadmap

At some time, other features may become incorporated into the project.
- Bronstein delay
- Games with three, or even four, players
- Linked programs that can be launched out of the box.

There is, however, it is not said when, or even if, this is going to happen.
