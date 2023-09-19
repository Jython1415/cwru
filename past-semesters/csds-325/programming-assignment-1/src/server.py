import socket
import sys

# Constants
bufferSize = 2048

# Global variables
clients = set()     # Used to keep track of active clients. A set is used because each client should be in the set only once
serverSocket = None # Global reference to the server socket

# Main function that is executed when this file is run
def main():
    global serverSocket
    
    serverSocket = initialize()
    
    while True:
        receive(bufferSize) # The receive function calls the send function when it receives a message

# Initialize function is called at the start of the main function
def initialize() -> socket:
    
    # Set up the server socket
    serverIP = socket.gethostbyname(socket.gethostname())
    serverPort = int(sys.argv[1]) # Port is determined by the user
    serverSocket = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)
    serverSocket.bind((serverIP, serverPort))
    
    # Print message
    print(f"Server initialized: {serverIP}:{str(serverPort)}")
    
    return serverSocket

# Receives and parses a message from a client
def receive(bufferSize):
    global clients
    global serverSocket
    
    # Wait for messages from clients
    bytesAddressPair = serverSocket.recvfrom(bufferSize)
    
    # Check message type
    msg = bytesAddressPair[0].decode()
    if (msg[:8] == "GREETING"):
        # Add the client address and port to the set of clients
        clients.add(bytesAddressPair[1])
    elif (msg[:7] == "MESSAGE"):
        # Parse the message and send it to all other clients
        text = msg[7:]
        # The message is sent in the format that it needs to be in on the client end
        send(f"From<{bytesAddressPair[1][0]}:{bytesAddressPair[1][1]}>: {text}", bytesAddressPair[1])
    else:
        # Print an error message
        print(f"Error: {msg}")

# Send messages to clients
def send(msg, sender):
    global serverSocket
    
    for c in clients:
        # Send to all clients that are NOT the client that initially sent the message
        if c[0] != sender[0] or c[1] != sender[1]:
            serverSocket.sendto(("INCOMING" + msg).encode(), c)

if __name__ == "__main__":
    main()