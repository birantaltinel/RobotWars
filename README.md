# Robot Wars

Author: Birant Altinel

## Architecture of the game

### General architecture

The project is entirely implemented in Java 11.

It consists of a central ***Arena*** class
which initializes and orchestrates all other entities.
It manages the turns and all the interacting objects.
It is also the entry point of the application.
It manages the lifecycle of:

* ArenaGUI
* Robots
* Rockets

The ***ArenaGUI*** class is responsible for creating and managing
the graphical user interface where the battle is animated and specific
information regarding the robots are shown. The lifecycle of ***ArenaGUI*** is
managed by the ***Arena*** class.

The ***RobotLoader*** is the first point of contact for the robot description files
that are parsed, compiled and loaded into memory as Java objects belonging to the
***Robot*** class.

***Robot*** class represents the robots and their capabilities. The robot objects
run in their own threads and their lifecycle is managed by ***Arena***.

***Rocket*** class and its objects represent the rockets fired by the robots
during the battle. They are created by the robot that fires them, but then their control
is handed over to ***Arena***.

Lastly, the project structure consists of a ***Location*** class which represents a basic
(x,y) location in the arena coordinate system, and a static ***Utils*** class that implements
some geometric and other helper functions used throughout the game.

The project structure also contains four sample robot AI files.

### Used tools and libraries

The game is implemented in Java 11, with the following libraries and tools:

* Gradle, for managing dependencies.
* Java Swing, for the graphical user interface.
* Lombok, to generate boilerplate code at compile time to keep development clean and fast.
* Openhft CompilerUtils, to dynamically compile and load code into the memory.

### Architectural Choices and Implementation Tradeoffs

I made the choices that I saw necessary in order to quickly build a playable MVP version
in the limited time that I had, meanwhile trying to keep a well software structure which is maintainable
and extendable.

* I chose to use the Swing library instead of going ASCII style, because it was convenient enough to quickly
build a GUI with basic components and animation. The best part is the fact that it's quite easy to
quickly extend and enrich the interface of the game, using the hierarchical structure of Swing
that makes it really easy to add, remove and animate visual components.
* I chose the robot language to be the subset of the Java language, because in this way I could both
be able to make use of dynamically loading code and build the application in short amount of time. 
* I tried to employ good object-oriented practices in order to have the possibility of
more easily extending the game with new types of entities, even though I must admit I didn't
have time to use enough design patterns.
* I chose to make the coordinate system start from the upper-left corner as the Swing library
to avoid extending the development time.
* Simplify the game logic as much as possible in order to make it possible for Robot AI developers to easily write Robot AI, and focus on
strategies instead of geometric calculations.

### Things to improve

The game is quite basic in its mechanics and the visual representation if far from stunning.
However, if I had more time, there are certainly things that I would improve.

* Instead of forcing the robot programmers to implement a strategy for every turn, I would let them
implement the logic for the entire duration of the battle and handle the robot programs through a simulation
of the computer resources as can be found in CRobots and similar projects.
* Handling different speeds for robots and rockets is quite buggy, and this would be one
of the first things I would improve after the major change mentioned in the previous point.
* Implementing more realistic physical interactions; such as crashing into each other, acceleration,
more realistic cannon reloading etc.
* I would definitely improve the UI to create a better representation of the battle which makes it
easier to understand what's going on in the battlefield.
* I would base all variables in configuration files in order to quickly adjust various settings.
* Create a security mechanism that prevents the dynamically loaded code to access the forbidden resources.

## Running Instructions

### Prerequisites

Please download and install the following before going through the step-by-step instructions:

* JDK 11 (<https://jdk.java.net/11/)>
* Gradle 4+ (<https://gradle.org/install/)>

### Step-by-step instructions
* Place the robot files you'd like to run inside **Robots** folder within the project. 
Please put a maximum of 4 robot files as it's not yet tested with more than 4 robots.
* Open the terminal/command prompt.
* Set default JDK version to 11.
  * **On MacOS**:
    > export JAVA_HOME= /usr/libexec/java_home -v 11
  * **On Windows**, please follow the following tutorial:
    <https://javatutorial.net/set-java-home-windows-10>
  * **On Linux**:
    * Find the java installation path by running
      > update-alternatives --config java
    * Then open the '/etc/environment' folder with a text editor and add the following:
      > JAVA_HOME=\<PATH\>
      > export JAVA_HOME
    * Save and exit. Lastly run the following command to reload the system PATH:
      > . /etc/environment
* Navigate to the project folder within the terminal/command prompt.
* Lastly, run:
    > gradle clean build run

The application should compile and start the battle automatically 
after running the last command.

### Features of the battlefield

* The battle takes place on a 500x500 arena with any number of robots.
* The coordinate (0,0) is the upper-left corner.
* The compass system degrees are represented as ***East:0***, ***North:270***, ***West:180***, ***South:90***.

### Graphical user interface

The GUI is simple and straightforward. When the application is started, the window representing the battle will automatically open.

The interface consists of 2 parts:

* On the left, you will see the ***battlefield*** which will animate the robots and the rockets. The robots are represented as green squares with 20x20 pixels in size whereas rockets are represented as red squares 5x5 pixels in size.
* On the right, you will see the ***scoreboard*** that will show the relevant information regarding the robots on the battlefield, such as their location in cartesian coordinates, their health,
and their movement direction.

### Rules

1. The robots can scan the arena, move around and fire rockets.
2. A robot can only have 2 missiles in the air at any given time. It can't fire another missile
until one of them hits the target location and explodes.
3. A robot is declared the winner if it's the only one remaining in the arena.
4. The maximum amount of playable turns are 5000. If there are no winners until then, the game
is declared as a draw.
5. The robots can call the move function as many times they want during a turn, but only the last one called will be effective.
6. Similarly, the robots can call the scan function any number of times during a turn, but all of the calls will yield the same result.
7. The robots can fire up to 2 rockets during their turn, which is also the maximum number of allowed rockets in the air at any given time. They must choose wisely.

### How to write a Robot AI

The robot language is a strict subset of the standard Java language. It consists of the ***standard Java syntax***, ***the custom robot functions*** with only the ***java.lang*** library content available for programming. It's not possible to import any other standard or external library.

The content of the Robot AI file should describe the actions a robot will make ***at each turn***. 
This means that this code will be invoked at every turn for every robot in the battlefield. 
If the robot code for the turn exceeds the timeout of 20 milliseconds,
the robot will be kicked out of battle.

The extension of the robot AI description file must be **.robot** such as **Destroyer.robot** without any spaces in the name.

The custom robot functions available to the programmers are:

* ***double[] scan()*** : Performs a scan within 300 units range of the robot and returns the location of the closest enemy.
  * **parameters***: none.
  * **returns***: double[], containing the location of the closest enemy with the x coordinate at index 0, and the y coordinate at index 1. It returns **null** if no robot is found within the scanning range.

* ***void move(double direction, int speed)*** : Starts moving the robot in the given direction(0-359) with the given speed(0 or 1).
  * **parameters**: direction in angles as a double, and the speed as an int.
  * **returns**: void.

* ***void fire(double targetX, double targetY)*** : Fires a missile towards the given location. The missile is not fired if the location is beyond the range or if the cannon is not reloaded yet.
  * **parameters**: the cartesian coordinates of the target location as doubles.
  * **returns**: void.

* ***boolean isCannonReloaded()*** : Returns true if the cannon has finished reloading and can fire another missile, false otherwise.
  * **parameters**: none.
  * **returns**: a boolean value, stating if the cannon is reloaded or not.

* ***int getHealth()*** : Retrieves the remaining health of the robot.
  * **parameters**: none.
  * **returns**: The current health of the robot, which starts at 100 in the beginning.

* ***int getSpeed()*** : Retrieves the current speed of the robot.
  * **parameters**: none.
  * **returns**: The current speed of the robot, which is either 0 or 1.

* ***double getDirection()*** : Retrieves the direction the robot is currently facing in the coordinate system in degrees.
  * **parameters**: none.
  * **returns**: The current direction of the robot in degrees. 0 degree represents the east, where as 90 degree represents the south, 180 the west and 270 the north.

* ***double getXCoordinate()*** : Retrieves the x coordinate of the current location of the robot:
  * **parameters**: none.
  * **returns**: x coordinate of the robot as a double.

* ***double getYCoordinate()*** : Retrieves the y coordinate of the current location of the robot:
  * **parameters**: none.
  * **returns**: y coordinate of the robot as a double.

Using these functions and the standard Java syntax, and optionally the various structures available in the **java.lang** package; the programmers can
create a robot AI.