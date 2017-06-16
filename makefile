# makefile for snake
# command to build project:
# -> make
# command to run project:
# -> make run

objects = src/Snake.java src/snake/Body.java src/snake/Head.java src/snake/Point.java src/snake/SnakeFrame.java

all: $(objects)
	javac $(objects) -d out/

run:
	java -cp out/ Snake
