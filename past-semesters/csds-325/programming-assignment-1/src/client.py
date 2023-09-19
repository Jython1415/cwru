# IMPORTS
import socket
import sys
import threading

# Constants
bufferSize = 2048

# Global variables
threads = []             # Stores references to thread objects
clientSocket = None      # Stores a global reference to the clientSocket
serverAddressPort = None # Stores the server address and port that is passed as an argument in the command line

# Main function
def main():
    global clientSocket
    
    initialize()    

# Initialize function is called at the start of the main function
def initialize():
    global clientSocket
    global serverAddressPort
    
    # Read server port and ip
    serverAddressPort = (sys.argv[1], int(sys.argv[2]))
    
    # Create the client socket
    clientSocket = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)
    
    # Create threads
    threads.append(threading.Thread(target=receive))
    threads.append(threading.Thread(target=send))
    
    # Greet the server
    clientSocket.sendto("GREETING".encode(), serverAddressPort)
   
    # Start threads
    for t in threads:
        t.start()

# Receives a message from the server
def receive():
    global clientSocket
    global serverAddressPort
    
    while True:
        msg = clientSocket.recvfrom(bufferSize)[0].decode()
        
        # Parse the message
        if (msg[:8] == "INCOMING"):
            print(msg[8:])
        else:
            # Reports errors to the user
            print(f"Invalid message received: {msg}")
    
# Send message
def send():
    global clientSocket
    global serverAddressPort
    
    while True:
        msg = input("") # User types into a blank line
        messageData = ("MESSAGE" + msg).encode() # The message has the "MESSAGE" preface added to it
        clientSocket.sendto(messageData, serverAddressPort) 

if __name__ == "__main__":
    main()