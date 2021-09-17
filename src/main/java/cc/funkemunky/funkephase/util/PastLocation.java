package cc.funkemunky.funkephase.util;

import org.bukkit.Location;
import java.util.Deque;
import java.util.LinkedList;

public class PastLocation {
    private final int max;
    public final Deque<Location> locations = new LinkedList<>();

    public PastLocation(int max) {
        this.max = max;
    }

    public void addLocation(Location box) {
        synchronized (locations) {
            if(locations.size() >= max) {
                locations.removeFirst();
            }

            locations.add(box);
        }
    }
}
