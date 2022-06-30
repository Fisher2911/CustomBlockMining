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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockBreakAnimation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRemoveEntityEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PacketHelper {

    public static void sendBlockBreakAnimation(Player player, Location location, int entityId, byte damage) {
        final WrapperPlayServerBlockBreakAnimation packet = new WrapperPlayServerBlockBreakAnimation(
                entityId,
                new Vector3i(location.getBlockX(), location.getBlockY(), location.getBlockZ()),
                damage
        );
        sendPacketAsync(player, packet);
    }

    public static void sendMiningFatiguePacket(Player player) {
        final WrapperPlayServerEntityEffect packet = new WrapperPlayServerEntityEffect(
                player.getEntityId(),
                PotionTypes.MINING_FATIGUE,
                -1,
                Integer.MAX_VALUE,
                (byte) 0
        );
        sendPacketAsync(player, packet);
    }

    public static void removeMiningFatiguePacket(Player player) {
        sendPacketSilentlyAsync(player, new WrapperPlayServerRemoveEntityEffect(player.getEntityId(), PotionTypes.MINING_FATIGUE));
    }

    public static void sendPacketAsync(Player player, PacketWrapper<?> packet) {
        Bukkit.getScheduler().runTaskAsynchronously(
                CustomBlockMining.getPlugin(CustomBlockMining.class),
                () -> PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet)
        );
    }

    public static void sendPacketSilentlyAsync(Player player, PacketWrapper<?> packet) {
        Bukkit.getScheduler().runTaskAsynchronously(
                CustomBlockMining.getPlugin(CustomBlockMining.class),
                () -> PacketEvents.getAPI().getPlayerManager().sendPacketSilently(player, packet)
        );
    }

}
