package io.github.fisher2911.customblockmining;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class BlockBreakManager implements Listener {

    private final CustomBlockMining plugin;
    private final Map<WorldPosition, BlockBreakData> blockBreakData;

    public BlockBreakManager(CustomBlockMining plugin) {
        this.plugin = plugin;
        this.blockBreakData = new ConcurrentHashMap<>();
    }

    /**
     *
     * @param tickFunction - Determines the total amount of ticks, for example if a player is holding a tool or not.
     * @param player - The player mining the block
     * @param position - The position of the block being broken
     * @param onBreak - Called when the block is broken, and passes the location of the block.
     */
    public void startMining(Function<Player, Integer> tickFunction, Player player, WorldPosition position, Consumer<WorldPosition> onBreak) {
        final BlockBreakData current = this.blockBreakData.get(position);
        final int entityId = current == null ? Random.nextInt(10_000, 20_000) : current.getEntityId();
        this.blockBreakData.put(position, new BlockBreakData(entityId, player, player.getInventory().getItemInMainHand(), tickFunction, onBreak));
    }

    /**
     *
     * @param position resets a block at a certain position to have no cracks
     */
    public void reset(WorldPosition position) {
        final BlockBreakData data = this.blockBreakData.get(position);
        if (data == null) return;
        data.reset(position);
    }

    /**
     *
     * @param position makes a block no longer have a custom mining time and makes it have no cracks
     */

    public void cancel(WorldPosition position) {
        final BlockBreakData data = this.blockBreakData.remove(position);
        if (data == null) return;
        data.reset(position);
    }

    @EventHandler
    private void onTickEnd(ServerTickEndEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () ->
                        this.blockBreakData.entrySet().removeIf(entry -> {
                            final WorldPosition position = entry.getKey();
                            final BlockBreakData data = entry.getValue();
                            data.send(position);
                            data.tick(position);
                            if (data.isBroken()) {
                                data.reset(position);
                                data.getOnBreak().accept(position);
                                return false;
                            }
                            return false;
                        })
        );
    }
}
