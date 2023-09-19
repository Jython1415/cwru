import socket
from utility import *
import sys

# Represents a client on the network
class Router:

    def __init__(self, server_address, router_id):
        self.server_address = server_address
        self.router_id = router_id
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.socket.bind(('localhost', 0))
        
    # Sends a JOIN message to the server and waits until a START message is received
    def establish_connection(self) -> DVTable:
        joinReceived = False # Stores whether the join message has been received
        while True:
            # Send JOIN if it has not been received yet
            if not joinReceived:
                message = f"JOIN {self.router_id}"
                self.socket.sendto(message.encode(), self.server_address)
                print("Join message sent. Waiting...")
        
            # Process new responses from the server
            rawResponse, _ = self.socket.recvfrom(1024)
            message = Message(rawResponse)
            if message.getHeader() == "START":
                print("Start message received")
                row, networkData = message.readStart()
                self.network = Network(networkData) # Network stores the original adjacency information
                table = DVTable()                   # Table is the distance vector table
                table[self.router_id] = row
                return table
            elif message.getHeader() == "ACK_JOIN": # ACK used so that JOIN messages are not spammed
                joinReceived = True
                print("Join ACK received")
    
    # Send function used to reduce repetition in code
    def send(self, message):
        self.socket.sendto(message, self.server_address)
    
    # Runs the DV algorithm by sending UPDATE messages if running BF causes a cost change
    def runDV(self, table:DVTable):
        print("DV Started")
        self.table = table
        
        print(self.table.getPretty()) # Print for visual confirmation (not visible if started through Start.py)
        self.send(Message.genUpdate(self.router_id, self.table[self.router_id])) # Initial update

        running = True
        while running:
            # Wait for response
            rawResponse, _ = self.socket.recvfrom(1024)
            
            message = Message(rawResponse) # Parse the input
            if message.getHeader() == "UPDATE":
                print("Update received")
                id, row = message.readUpdate()
                try:
                    self.table.updateRow(id, row)
                except:
                    raise Exception(f"Need to work on this exception message") # Meant for debugging
                if self.table.runBF(self.router_id, self.network): # Run BF. Return value is whether there was a change or not
                    self.send(Message.genUpdate(self.router_id, self.table[self.router_id])) # Send updated values to server
                    print("Update sent")
                else:
                    self.send(Message.genNoChange(self.router_id)) # Send "completed" status to the server
            elif message.getHeader() == "END":
                running = False
            else:
                print(message.getContents())

def main():
    router = Router((sys.argv[1], int(sys.argv[2])), sys.argv[3])
    table = router.establish_connection()
    router.runDV(table)
    print("Completed")

if __name__ == "__main__":
    main()