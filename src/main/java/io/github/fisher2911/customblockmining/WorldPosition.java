/*
 * Copyright 2022 Fisher2911
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
