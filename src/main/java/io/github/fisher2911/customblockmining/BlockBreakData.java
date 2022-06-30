package io.github.fisher2911.customblockmining;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

public class BlockBreakData {

    public static final byte MAX_DAMAGE = 10;

    private final int entityId;
    private final Player player;
    private ItemStack breakingWith;
    private Function<Player, Integer> totalTickFunction;
    private final Consumer<WorldPosition> onBreak;
    private int currentTicks;

    public BlockBreakData(int entityId, Player player, ItemStack breakingWith, Function<Player, Integer> totalTickFunction, Consumer<WorldPosition> onBreak) {
        this.entityId = entityId;
        this.player = player;
        this.breakingWith = breakingWith;
        this.totalTickFunction = totalTickFunction;
        this.onBreak = onBreak;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setTotalTicks(int totalTicks) {
        this.totalTickFunction = p -> totalTicks;
        this.currentTicks = 0;
    }

    public Consumer<WorldPosition> getOnBreak() {
        return onBreak;
    }

    public boolean isBroken() {
        return this.calculateDamage() == MAX_DAMAGE - 1;
    }

    public void reset(WorldPosition position) {
        this.currentTicks = 0;
        PacketHelper.sendBlockBreakAnimation(this.player, position.toLocation(), this.entityId, (byte) -1);
    }

    public void tick(WorldPosition position) {
        final ItemStack held = this.player.getInventory().getItemInMainHand();
        if (!(held.isSimilar(this.breakingWith))) {
            this.reset(position);
            this.breakingWith = held;
            return;
        }
        this.currentTicks++;
    }

    public byte calculateDamage() {
        final double percentage = (double) this.currentTicks / this.totalTickFunction.apply(this.player);
        final double damage = MAX_DAMAGE * percentage;
        return (byte) (Math.min(damage, MAX_DAMAGE) - 1);
    }

    public void send(WorldPosition position) {
        final byte calculatedDamage = this.calculateDamage();
        PacketHelper.sendBlockBreakAnimation(this.player, position.toLocation(), this.entityId, calculatedDamage);
    }

    public Player getPlayer() {
        return player;
    }
}
