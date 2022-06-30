package io.github.fisher2911.customblockmining;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

public class BlockMineListener extends PacketListenerAbstract {

    private final CustomBlockMining plugin;
    private final BiFunction<Player, WorldPosition, Boolean> onBlockMineEnd;
    private final BiFunction<Player, WorldPosition, Boolean> onBlockMineStart;

    /**
     *
     * @param onBlockMineEnd called when a player stops mining a block. Returns true if the player should be given mining fatigue.
     *                       (i.e. not a block with a custom break time).
     * @param onBlockMineStart called when a player starts to mine a block. Returns true if a player's mining fatigue should be removed
     *                         (i.e. not a block with a custom break time).
     */
    public BlockMineListener(
            CustomBlockMining plugin, BiFunction<Player, WorldPosition, Boolean> onBlockMineEnd,
            BiFunction<Player, WorldPosition, Boolean> onBlockMineStart
    ) {
        this.plugin = plugin;
        this.onBlockMineEnd = onBlockMineEnd;
        this.onBlockMineStart = onBlockMineStart;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.PLAYER_DIGGING) return;
        final WrapperPlayClientPlayerDigging packet = new WrapperPlayClientPlayerDigging(event);
        if (!(event.getPlayer() instanceof final Player player)) return;
        if (player.getGameMode() == GameMode.CREATIVE) return;
        final Vector3i vector3i = packet.getBlockPosition();
        final Position position = new Position(vector3i.getX(), vector3i.getY(), vector3i.getZ());
        final WorldPosition worldPosition = new WorldPosition(player.getWorld(), position);
        if (packet.getAction() == DiggingAction.CANCELLED_DIGGING || packet.getAction() == DiggingAction.FINISHED_DIGGING) {
            if (this.onBlockMineEnd.apply(player, worldPosition)) {
                PacketHelper.sendMiningFatiguePacket(player);
            }
            this.plugin.getBlockBreakManager().cancel(worldPosition);
            return;
        }
        if (packet.getAction() == DiggingAction.START_DIGGING) {
            if (this.onBlockMineStart.apply(player, worldPosition)) {
                PacketHelper.removeMiningFatiguePacket(player);
            }
        }
    }

}
