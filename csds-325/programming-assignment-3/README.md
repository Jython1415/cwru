# CSDS325 PA3

Programming assignment 3 for CSDS325 (Networks)

## Instructions

1. Navigate to the project folder `~/csds325-pa3/`

2. Run Start.py: `python3 src/Start.py`

    Start.py will start the server and the clients based on the config file.

3. The output will be in output.txt. It is newly generated each time the script is run.

4. See the code for documentation

## Dependencies

- Python 3.10.0
- Libraries: socket, sys, json, subprocess, and shlex

## Work Log

1. Figure out what I'm doing

    My basic plan for the organization is described below.

    **Client**

    - DV
    - Connect to server
    - Algorithm

    **Server**

    - Store and parse the config info
    - Wait for connections
    - Send initial DV
    - Send update messages and monitor for stabilization

    **Main**

    - Read the config file
    - Start server
    - Start clients

2. Flesh out the client capabilities

    - Python class called "Client"
    - Takes the server address (IP and Port) as input
    - Binds itself to any port
    - Establishes as connection to the server by sending a "JOIN" message
    - Waits for a response from the server ("START") before starting the DV algorithm

3. Code a basic outline of the client

    Done with help from ChatGPT

4. Flesh out the server specifications

    - Python class called "Server"
    - Takes a port number as input
    - Takes an integer as input which is the number of clients the server is expecting
    - Binds a UDP socket to the input port
    - Waits for a "JOIN" message to be received from all clients
    - Once all "JOIN" messages are received, the server sends a "START" message to all the clients

5. Polish what I have so far

    Done. Next I need to create a way the sockets can be started, and make sure they are communicating with each other.

6. Get the routers to start and connect to the server.

    1. Get json data into the server properly

    2. Get the routers started in terminal shells

    3. Rework Start.py so I can start just the clients

    4. Rework Start.py so I can specify the routers to start

    Connection done! Finally. This took so much debugging.

7. Implement the DV algorithm

    1. Look at an example to get idea for how I can implement it

    2. Add update row and isolate row to DVTable

    3. Fix server start command

    4. Flesh out router DV function

    5. Add server forwarding

    6. Implement BF in DVTable

    7. Add distance to self

    8. Debug

8. Polish for submission

    1. Add final printing

    2. Take forever to debug

    3. Finish README

    4. Add documentation to all files
