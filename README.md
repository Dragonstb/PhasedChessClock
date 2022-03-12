# Phased Chess Clock

In chess, the time control can be divided into different periods. In each period, the player may have a different amount of time available, with different increments. For example, you may have 90 minutes for the first 40 moves, 60 minutes for the next 20 moves, and 15 minutes for the remainder of the game.
This program allows to set up such a time control with such sequences, and you can set up different sequences for the players.

# Features

- Time control phases with their own amount of time, increment, and number of moves they are valid for. Each phase can be configured independently from the other phases.
- The two players' time control sequences are independent of each other. They may be absolutely the same or completely different.
- Optional, additional time limit for each move.
- Each player who wants to do so may set up a time below which the clock provides an optical signal that you better hurry up.

# Usage

Running this program requires a Java Runtime Environment.
Copy the chessclock-<version number>.jar and the chessClockWork-<version number>.jar into the same directoy an go into this directory. Type
  ```java -p . -m chessclock```
and hit enter. If the directory with the java executable is not in your PATH, you have to enter the full path to the java executable.

# Project structure and licencing

This is a modular Gradle multi build, consisting of two sub projects: The GUI, and the clock work. Consequently, two java modules are created:
- The chessclock contains the GUI that displays the clock. It is also used to configure the timing setup. This module is published under the GPLv3 licence.
- The chessClockWork does the actual timing. It can tell any listener how much time  (if any) is left for each player. This module is published under the GPLv3 licence with a linking exception.

# Roadmap

At some time, other features may become incorporated into the project.
- Bronstein delay
- Games with three, or even four, players
- Linked programs that can be launched out of the box.
