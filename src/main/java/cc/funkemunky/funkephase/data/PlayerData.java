package cc.funkemunky.funkephase.data;

import cc.funkemunky.api.reflections.impl.MinecraftReflection;
import cc.funkemunky.api.utils.BoundingBox;
import cc.funkemunky.funkephase.util.PastLocation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@Setter
public class PlayerData {

    public Player player;
    public long lastDoorSwing;
    public Location lastTeleport;
    public long lastTeleportTime;
    private BoundingBox boundingBox;
    private boolean onGround, enderPearlTeleport;
    private Object playerConnection;
    public PastLocation locations = new PastLocation(3);

    public PlayerData(Player player) {
        this.player = player;
    }

    public Object getPlayerConnection() {
        if(playerConnection == null) {
            this.playerConnection = MinecraftReflection.getPlayerConnection(player);
        }

        return this.playerConnection;
    }
}