package cc.funkemunky.funkephase.util;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.utils.BoundingBox;
import cc.funkemunky.funkephase.data.PlayerData;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PastLocation {
    private int max;
    private List<BoundingBox> boundingBoxes = new ArrayList<>();

    public PastLocation(int max) {
        this.max = max;
    }

    public void addLocation(BoundingBox box, PlayerData data) {
        if(box.grow(0.2f,0.2f,0.2f).getCollidingBlockBoxes(data.player).size() > 0) return;

        if(boundingBoxes.size() >= max) {
            boundingBoxes.remove(0);
        }

        boundingBoxes.add(box);
    }
}
