import json
from utility import *
import subprocess
import shlex
import sys

serverPortNum = 5000

# Start Server.py
def startServer(table:DVTable) -> int:
    process = subprocess.Popen(["python3", "src/Server.py", str(serverPortNum), table.getCommandLineVer()], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    return process.pid

# Start all Router.py required with their own id's
def startRouters(table:DVTable, excludeList=None):
    result = []
    for router in table.getTable().keys():
        if excludeList == None or router not in excludeList:
            command = f"python3 src/Router.py localhost {serverPortNum} {router}"
            process = subprocess.Popen(shlex.split(command), stdout=subprocess.PIPE, stderr=subprocess.PIPE)
            result.append(process.pid)
    
    return result

# Loads configuration from config_file.txt
def loadConfig() -> DVTable:
    table = {}
    with open('config_file.txt', 'r') as f:
        table = json.load(f)
    return DVTable(json.dumps(table))

def main():
    table = loadConfig()
    
    if len(sys.argv) == 1: # No options, normal execution
        _ = startServer(table)
        _ = startRouters(table)
    else: # Execution with options
        options = sys.argv[1:]
        if "-s" in options: # Server only
            s_pid = startServer(table)
            print(s_pid)
        elif "-r" in options: # Routers only
            if len(sys.argv) == 3:
                print(f"-r with exclude list: {sys.argv[2]}")
                results = startRouters(table, sys.argv[2].split(","))
            else:
                print("-r with no exclude list")
                results = startRouters(table)
            print("\n".join([str(r) for r in results]))
    print("Started and done")
    
if __name__ == "__main__":
    main()