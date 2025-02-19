package net.ixbob.thepit.pvp;

import net.ixbob.thepit.holder.ConfigHolder;
import net.ixbob.thepit.util.SingletonUtil;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class PvpZoneManager {

    private static final Supplier<PvpZoneManager> instance = SingletonUtil.createSingletonLazy(PvpZoneManager::new);

    private final Set<BoundingBox> enablePvpZones = new HashSet<>();

    private PvpZoneManager() {
        reloadFromConfig();
    }

    private void reloadFromConfig() {
        enablePvpZones.clear();
        ConfigHolder.getInstance().getEnablePvpZonePoses().forEach(zone -> {
            List<Number> startPos = zone.get("from");
            List<Number> endPos = zone.get("to");
            enablePvpZones.add(new BoundingBox(
                    startPos.get(0).doubleValue(),
                    startPos.get(1).doubleValue(),
                    startPos.get(2).doubleValue(),
                    endPos.get(0).doubleValue(),
                    endPos.get(1).doubleValue(),
                    endPos.get(2).doubleValue()));
        });
    }

    public static PvpZoneManager getInstance() {
        return instance.get();
    }

    public boolean getPosIsInPvpZone(double x, double y, double z) {
        return enablePvpZones.stream().anyMatch(zone -> zone.contains(x, y, z));
    }

    public boolean getPosIsInPvpZone(Location location) {
        if (location.getWorld().getUID() != ConfigHolder.getInstance().getWorldUID()) {
            return false;
        }
        return getPosIsInPvpZone(location.getX(), location.getY(), location.getZ());
    }

}
