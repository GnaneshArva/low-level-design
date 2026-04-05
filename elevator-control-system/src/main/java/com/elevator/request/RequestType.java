package com.elevator.request;

public enum RequestType {
    INTERNAL,   // pressed inside elevator (destination floor)
    EXTERNAL,   // pressed on floor panel (up/down)
    EMERGENCY   // priority override — processes before normal requests
}
