"""This is a test file for main.py that uses pytest to test the validate_traffic_status function."""

import pytest
from main import validate_traffic_status

@pytest.mark.parametrize("status, expected", [
    ({"north": "R", "south": "R", "west": "R", "east": "R"}, True),
    ({"north": "R", "south": "R", "west": "Y", "east": "Y"}, True),
    ({"north": "R", "south": "R", "west": "G", "east": "G"}, True),
    ({"north": "Y", "south": "Y", "west": "R", "east": "R"}, True),
    ({"north": "G", "south": "G", "west": "R", "east": "R"}, True),
    ({"north": "Y", "south": "R", "west": "Y", "east": "R"}, False),
    ({"north": "R", "south": "Y", "west": "R", "east": "Y"}, False),
    ({"north": "G", "south": "R", "west": "G", "east": "R"}, False),
    ({"north": "R", "south": "G", "west": "R", "east": "G"}, False),
    ({"north": "Y", "south": "Y", "west": "Y", "east": "Y"}, False),
    ({"north": "G", "south": "G", "west": "G", "east": "G"}, False),
])
def test_validate_traffic_status(status, expected) -> None:
    """Tests the validate_traffic_status function"""
    assert validate_traffic_status(status) == expected