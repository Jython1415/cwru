import json
import time

def main():
    startTime = time.time_ns()
    with open("config_file.txt", "r") as r:
        d = json.load(r)
    with open("config_compressed.txt", "w") as w:
        t = json.dumps(d, separators=(",",":"))
        t2 = '"' + t.replace('"','\\"') + '"'
        w.write("\n".join([t,t2]))
    endTime = time.time_ns()
    print(f"Completed in {round((endTime - startTime)/1000000, 3)}ms")
    
if __name__ == "__main__":
    main()