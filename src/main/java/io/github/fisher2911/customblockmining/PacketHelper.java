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
