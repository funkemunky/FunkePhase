package cc.funkemunky.funkephase.data;

import cc.funkemunky.funkephase.util.BoundingBox;
import cc.funkemunky.funkephase.util.PastLocation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class PlayerData {

    public Player player;
    public long lastDoorSwing;
    private BoundingBox boundingBox;
    private boolean onGround, enderPearlTeleport;
    public PastLocation locations = new PastLocation(10);

    public PlayerData(Player player) {
        this.player = player;
    }
}