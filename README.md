# Distributed Shared White Board
Shared whiteboards allow multiple users to draw simultaneously on a canvas. There are multiple examples found on the Internet that support a range of features such as freehand drawing with the mouse, drawing lines and shapes such as circles and squares that can be moved and resized, and inserting text.

The shared whiteboard is built using java and it has a GUI. It supports multi-threading. The network communication between the server and the client is implemented using RMI.

## Features
* The first user who joins the whiteboard will become the manager.
* Manager can save the whiteboard and be able to load the saved whitebaord.
* Manager can kick out a certain user.
* Multiple users can draw on a shared interactive canvas.
* The system support a single whiteboard that is  shared between all of the clients.
* Users can draw shapes, append texts, and also be able to choose different colour to draw these features.
* A chat window that allows users to communicate with each other.

## How to Run
* Open a terminal. Navigate to the repository folder. Enter the command 'java -jar server.jar' to run the Whiteboard server.
* Open another terminal. Navigate to the repository folder. Enter the command 'java -jar client.jar' to run the Whiteboard client.
