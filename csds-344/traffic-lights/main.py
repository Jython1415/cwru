"""Traffic Light Simulation"""

import time
import curses
import itertools


def print_traffic_lights(traffic_status, stdscr) -> None:
    """Prints the traffic lights to the screen"""

    stdscr.addstr(1, 0, "     N")
    stdscr.addstr(3, 0, f"     {traffic_status['north']}")
    stdscr.addstr(4, 0, "     |")
    stdscr.addstr(5, 0, f"W  {traffic_status['west']}-+-{traffic_status['east']}  E")
    stdscr.addstr(6, 0, "     |")
    stdscr.addstr(7, 0, f"     {traffic_status['south']}")
    stdscr.addstr(9, 0, "     S")


def validate_traffic_status(traffic_status) -> bool:
    """
    True means the status is valid, False means it is invalid.
    Traffic status should have 4 keys: north, south, west, east.
    The values must be either R, Y, or G.
    If either north or south is not R, then west and east cannot be not R.
    """

    if len(traffic_status) != 4:
        return False
    if not all([i in traffic_status for i in ["north", "south", "west", "east"]]):
        return False
    if not all([i in ["R", "Y", "G"] for i in traffic_status.values()]):
        return False
    if traffic_status["north"] != "R" or traffic_status["south"] != "R":
        if traffic_status["west"] != "R" or traffic_status["east"] != "R":
            return False
    return True


traffic_cycle = [
    ({"north": "R", "south": "R", "west": "R", "east": "R"}, 2),
    ({"north": "R", "south": "R", "west": "G", "east": "G"}, 6),
    ({"north": "R", "south": "R", "west": "Y", "east": "Y"}, 2),
    ({"north": "R", "south": "R", "west": "R", "east": "R"}, 2),
    ({"north": "G", "south": "G", "west": "R", "east": "R"}, 7),
    ({"north": "Y", "south": "Y", "west": "R", "east": "R"}, 2),
]


def main(stdscr) -> None:
    """Main function that runs the traffic light simulation"""

    stdscr.nodelay(True)

    pause = False
    start_time = time.time()
    last_update = start_time
    pause_time = start_time
    duration = 0
    traffic_cycle_loop = itertools.cycle(traffic_cycle)
    update = True
    while True:
        char = stdscr.getch()
        if char == ord("q"):
            break
        elif char == ord(" "):
            pause = not pause
            if pause:
                pause_time = time.time()

        current_time = time.time()
        elapsed_time = current_time - start_time
        if pause:
            pause_elapsed_time = current_time - pause_time
            elapsed_time -= pause_elapsed_time
        if elapsed_time >= duration:
            traffic_status, duration = next(traffic_cycle_loop)
            start_time = current_time
            if not validate_traffic_status(traffic_status):
                raise ValueError(f"Invalid traffic status: {traffic_status}")
            update = True

        if update or current_time - last_update >= 0.1:
            last_update = current_time
            update = False
            stdscr.clear()
            stdscr.addstr(0, 0, "Press q to quit, space to pause/unpause")
            print_traffic_lights(traffic_status, stdscr)
            stdscr.addstr(11, 0, f"Elapsed time: {elapsed_time:.2f}")
            stdscr.refresh()


if __name__ == "__main__":
    curses.wrapper(main)
