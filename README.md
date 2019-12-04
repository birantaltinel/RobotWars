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

### Architectural and Design choices
I made the choices that I saw necessary in order to quickly build a playable MVP version
in the limited time that I had.
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

## Running Instructions

### Prerequisites
Please download and install the following before going through the instructions:
* JDK 11 (https://jdk.java.net/11/)
* Gradle 4+ (https://gradle.org/install/) 

## Game Rules

### Features of the battlefield
* The battle takes place on a 500x500 arena with any number of robots. 
* The coordinate (0,0) is the upper-left corner.
* The compass system degrees are represented as ***East:0***, ***North:270***, ***West:180***, ***South:90***.


The robots can scan the arena, move around and fire rockets. However, the battle has rules:
* A robot can only have 2 missiles in the air at any given time. It can't fire another missile
until one of them hits the target location and explodes.
* A robot is declared the winner if it's the only one remaining in the arena.
* The maximum amount of playable turns are 5000. If there are no winners until then, the game
is declared as a draw.