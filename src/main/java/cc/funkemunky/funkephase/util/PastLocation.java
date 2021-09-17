package cc.funkemunky.funkephase.util;

import cc.funkemunky.api.utils.world.types.SimpleCollisionBox;

import java.util.Deque;
import java.util.LinkedList;

public class PastLocation {
    private final int max;
    public final Deque<SimpleCollisionBox> boundingBoxes = new LinkedList<>();

    public PastLocation(int max) {
        this.max = max;
    }

    public void addLocation(SimpleCollisionBox box) {
        synchronized (boundingBoxes) {
            if(boundingBoxes.size() >= max) {
                boundingBoxes.removeFirst();
            }

            boundingBoxes.add(box);
        }
    }
}
