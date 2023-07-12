import json

# Defines are messages are sent and interpreted
class Message:
    
    @classmethod
    def zipDict(cls, d) -> str:
        return json.dumps(d, separators=(',',':'))
    
    @classmethod
    def genStart(cls, id, row, tableStr) -> bytes:
        return ("START " + id + " " + Message.zipDict(row) + " " + tableStr).encode()
    
    @classmethod
    def genUpdate(cls, id, row) -> bytes:
        return ("UPDATE " + id + " " + Message.zipDict(row)).encode()
    
    @classmethod
    def genNoChange(cls, id) -> bytes:
        return ("NO_CHANGE " + id).encode()
    
    @classmethod
    def genAckJoin(cls) -> bytes:
        return "ACK_JOIN".encode()
    
    @classmethod
    def genEnd(cls) -> bytes:
        return "END".encode()
    
    def __init__(self, rawMessage):
        self.contents = rawMessage.decode().split(" ")
        if len(self.contents) > 0:
            self.header = self.contents[0]
        else:
            self.header = ""

    def getHeader(self) -> str:
        return self.header
    
    def getContents(self) -> list:
        return self.contents
    
    def readUpdate(self) -> tuple:
        print(self.contents)
        return (self.contents[1], json.loads(self.contents[2]))
    
    def readNoChange(self) -> str:
        return self.contents[1]
    
    def readJoin(self) -> str:
        return self.contents[1]
    
    def readStart(self) -> str:
        return (json.loads(self.contents[2]), self.contents[3])

# Stores the initial neighbor and adjacency information
class Network:
    
    def __init__(self, table:str):
        self.table = json.loads(table)
    
    def getNeighbors(self, src_id, inputTable, accessible=True) -> list:
        neighbors = []
        for key in self.table[src_id].keys():
            if self.table[src_id][key] > 0 and (key in set(inputTable.keys()) or not accessible):
                neighbors.append(key)
        return neighbors
     
# Stores the DV during the DV algorithm   
class DVTable:
    
    def __init__(self, table:str = None):
        if table == None:
            self.table = {}
        elif type(table) == str:
            try:
                self.table:dict = json.loads(table)
            except json.JSONDecodeError:
                raise Exception(f"JSONDecodeError:\n{table}")
        elif type(table) == dict:
            self.table:dict = table
        else:
            raise Exception(f"type(table) == {type(table)}")
    
    def getTable(self) -> dict:
        return self.table
    
    # Compressed form with no spaces
    def getCompressed(self) -> str:
        return json.dumps(self.table, separators=(',',':'))
    
    def getCommandLineVer(self) -> str:
        return self.getCompressed()

    def setTable(self, table:str):
        self.table = json.loads(table)
    
    # Used to send a single DV
    def isolateRow(self, id):
        return (id, self.table[id])
    
    # Used to update a single DV in a larger table
    def updateRow(self, id, row):
        self.table[id] = row.copy()
    
    # Bellman-Ford algorithm
    def runBF(self, src_id:str, network:Network):
        print(f"\nrunBF on {src_id}")
        start = self.getPretty()
        change = False
        neighbors = network.getNeighbors(src_id, self.table)
        for key in self.table[src_id].keys():      # Iterate over all routers
            # Find the distances through each neighbor
            distances = [self.table[src_id][n] + self.table[n][key] for n in neighbors if self.table[n][key] != -1]
            # Find the shortest path to each router
            newValue = min([self.table[src_id][key]] if len(distances) == 0 else distances)
            if newValue < self.table[src_id][key] or (newValue > 0 and self.table[src_id][key] == -1): # If the shortest path is new
                change = True                      #     mark the chance
                self.table[src_id][key] = newValue #     and update the cost in the table
        end = self.getPretty()
        print(start)
        print(end)
        print(change)
        return change # Return whether there was an update or not
          
    # Visually pleasing print  
    def getPretty(self) -> str:
        results = []
        for key in sorted(self.table.keys()):
            buffers = [" "*(2-len(str(self.table[key][k]))) for k in sorted(self.table[key].keys())]
            results.append(key + " " + ", ".join([f"<{k}, {buffers[i]}{self.table[key][k]}>" for i, k in enumerate(sorted(self.table[key].keys()))]))
        return "\n".join(results)
            
    def __str__(self):
        return json.dumps(self.table, indent=4)
    
    def __len__(self):
        return len(self.table)
    
    def __getitem__(self, key):
        return self.table[key]
    
    def __setitem__(self, key, value):
        self.table[key] = value