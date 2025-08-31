package committee.nova.mods.bren.init.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

/**
 * @author: cnlimiter
 */
public class GunFireEvent extends Event {
    public Player player;
    public ItemStack stack;

    public GunFireEvent(Player player, ItemStack stack)
    {
        this.player = player;
        this.stack = stack;
    }
}
