from utility import *
import socket
import sys

class Server:
    
    def __init__(self, port_number:int, table:str):
        self.port_number = port_number
        self.table:DVTable = DVTable(table)
        self.network:Network = Network(table)
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.socket.bind(('localhost', self.port_number))
        self.routers = {}
        print(f"Server initialized! Address is ({self.socket.getsockname()[0]}, {self.port_number})")
        
    def send(self, message, id):
        self.socket.sendto(message, self.routers[id])
     
    # Waits until all routers are connected before sending the START message   
    def wait_for_routers(self):
        print("Waiting for routers to join...")
        while len(self.routers) < len(self.table):
            rawResponse, router_address = self.socket.recvfrom(1024)
            message = Message(rawResponse)
            if message.getHeader() == "JOIN":
                router_id = message.readJoin()
                self.routers[router_id] = router_address
                self.send(Message.genAckJoin(), router_id)
        
    # Send the START message with router specific row included as data
    def start_algorithm(self):
        for id in self.routers:
            self.send(Message.genStart(id, self.table[id], self.table.getCompressed()), id)
    
    # Main loop for forwarding update messages
    # Also tracks to stop execution once an equilibrium has been reached
    def enter_loop(self):
        completedRouters = [] # Keeps track of currently completed routers
        inARow = 0
        while len(completedRouters) < len(self.table) or inARow < 5:
            
            if len(completedRouters) >= len(self.table):
                inARow += 1
            else:
                inARow = 0
            
            rawResponse, _ = self.socket.recvfrom(1024)
            message = Message(rawResponse) # Parse message
            if message.getHeader() == "NO_CHANGE": # Keep track of routers that did not update
                id = message.readNoChange()
                if id not in completedRouters:
                    completedRouters.append(id)
            elif message.getHeader() == "UPDATE":
                id, row = message.readUpdate()
                try:
                    self.table.updateRow(id, row) # Update internal DV table
                except:
                    raise Exception(f"{id}, {row}")
                
                for send_id in self.network.getNeighbors(id, self.table.getTable(), accessible=False):
                    self.send(Message.genUpdate(id, row), send_id) # Forward UPDATE to neighbors
                    if send_id in completedRouters:
                        completedRouters.remove(send_id)
            print(sorted(list(set(self.table.getTable().keys())-set(completedRouters)))) # Print remaining routers (still need updating)
    
    # Final END confirmation messages
    def send_end_messages(self):
        for id in self.routers:
            self.send(Message.genEnd(), id)

def main():
    server = Server(int(sys.argv[1]), sys.argv[2])
    print("\n"*3 + server.table.getPretty() + "\n"*3)
    server.wait_for_routers()
    server.start_algorithm()
    server.enter_loop()
    server.send_end_messages()
    print("Completed")
    print("\n"*3 + server.table.getPretty() + "\n"*3)
    with open("output.txt", "w") as f:
        f.write(server.table.getPretty())

if __name__ == "__main__":
    main()