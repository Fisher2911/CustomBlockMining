package io.github.fisher2911.customblockmining;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

public class WorldPosition {

    private final World world;
    private final Position position;

    public WorldPosition(World world, Position position) {
        this.world = world;
        this.position = position;
    }

    public World getWorld() {
        return world;
    }

    public Position getPosition() {
        return position;
    }

    public Location toLocation() {
        return new Location(world, position.getX(), position.getY(), position.getZ());
    }

    public static WorldPosition fromLocation(Location location) {
        return new WorldPosition(location.getWorld(), new Position(location.getX(), location.getY(), location.getZ()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final WorldPosition that = (WorldPosition) o;
        return Objects.equals(getWorld(), that.getWorld()) && Objects.equals(getPosition(), that.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWorld(), getPosition());
    }

    @Override
    public String toString() {
        return "WorldPosition{" +
                "world=" + world +
                ", position=" + position +
                '}';
    }
}
