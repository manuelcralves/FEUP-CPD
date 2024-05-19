# Dice Game

## Overview
This project is a simple client-server application for a dice game. Users can register, log in, and play a dice-rolling game with other connected clients.

## Features
- User registration and authentication
- Client-server communication
- Synchronized access to shared resources

## File Structure
- `GameClient.java`: Handles the client-side communication with the server.
- `GameServer.java`: Manages the server-side logic and client interactions.
- `UserData.java`: Manages user registration and authentication data.

## How to Compile

1. Open a terminal.
2. Navigate to the directory containing the `.java` files.
3. Compile the server and user data classes:
   ```sh
   javac *.java
   ```

## How to Run

### Step 1: Start the Server
1. Open a terminal.
2. Navigate to the directory containing the compiled `GameServer` class files.
3. Start the server:
   ```sh
   java GameServer
   ```

### Step 2: Start the Client
1. Open a new terminal.
2. Navigate to the directory containing the compiled `GameClient` class files (same as `GameServer`).
3. Start the client:
   ```sh
   java GameClient
   ```
4. Login/Register.
5. Play!

## How the Game Works

1. **Client Connection**:
   - The client connects to the server and receives a welcome message.
   - The client chooses to either register or log in.

2. **User Registration/Login**:
   - For registration, the client provides a username and password, which are sent to the server.
   - For login, the client sends existing credentials to the server for authentication.

3. **Playing the Game**:
   - Once authenticated, the server waits for 3 clients to join.
   - The server instructs clients to roll the dice.
   - Clients roll the dice by pressing Enter, which sends a random dice roll value to the server.
   - The server processes the dice roll and sends the result back to all clients.
   - Clients can type "disconnect" to exit the game. If they login again, they will be placed in the end of the round.
   - In the end of the 3 rounds, the player who has the highest sum will be the winner!

4. **Server Shutdown**:
   - The server can be shut down gracefully, notifying all connected clients.

