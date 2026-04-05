package moviebooking.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Theater {

    private final String theaterId;
    private final String name;
    private final String location;
    private final List<Screen> screens;

    public Theater(String theaterId, String name, String location) {
        this.theaterId = theaterId;
        this.name = name;
        this.location = location;
        this.screens = new ArrayList<>();
    }

    public void addScreen(Screen screen) {
        screens.add(screen);
    }

    public String getTheaterId() { return theaterId; }
    public String getName()      { return name; }
    public String getLocation()  { return location; }

    public List<Screen> getScreens() {
        return Collections.unmodifiableList(screens);
    }
}
