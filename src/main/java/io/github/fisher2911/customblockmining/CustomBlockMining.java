package io.github.fisher2911.customblockmining;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRemoveEntityEffect;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomBlockMining extends JavaPlugin {

    private BlockBreakManager blockBreakManager;

    @Override
    public void onLoad() {
        final PacketEventsAPI<Plugin> api = SpigotPacketEventsBuilder.build(this);
        PacketEvents.setAPI(api);
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI().init();
        this.blockBreakManager = new BlockBreakManager(this);
        this.registerListeners();
    }

    @Override
    public void onDisable() {
    }

    public BlockBreakManager getBlockBreakManager() {
        return this.blockBreakManager;
    }

    private boolean isCustomBreak(WorldPosition worldPosition) {
        final Position position = worldPosition.getPosition();
        return position.getX() % 2 == 0 && position.getZ() % 2 == 0;
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(this.blockBreakManager, this);
        PacketEvents.getAPI().getEventManager().registerListener(new PacketListenerAbstract() {
            @Override
            public void onPacketSend(PacketSendEvent event) {
                if (event.getPacketType() != PacketType.Play.Server.REMOVE_ENTITY_EFFECT) return;
                final WrapperPlayServerRemoveEntityEffect packet = new WrapperPlayServerRemoveEntityEffect(event);
                if (!(event.getPlayer() instanceof final Player player)) return;
                if (player.getGameMode() == GameMode.CREATIVE) return;
                if (packet.getPotionType() == PotionTypes.MINING_FATIGUE) event.setCancelled(true);
            }
        });
    }

    private void registerTest() {
        PacketEvents.getAPI().getEventManager().registerListener(
                new BlockMineListener(
                        this,
                        (player, pos) -> {
                            final boolean isCustom = this.isCustomBreak(pos);
                            if (!isCustom) return true;
                            this.blockBreakManager.reset(pos);
                            return false;
                        },
                        (player, pos) -> {
                            final boolean isCustom = this.isCustomBreak(pos);
                            if (!isCustom) return true;
                            player.sendMessage("You are now mining " + pos.getPosition().toString());
                            this.blockBreakManager.startMining(p -> 60, player, pos, block -> player.sendMessage("You mined a custom block: " + block.toString()));
                            return false;
                        }
                )
        );
    }
}
