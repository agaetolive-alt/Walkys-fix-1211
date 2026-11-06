package walksy.optimizer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import walksy.optimizer.mixin.MinecraftClientAccessor;

public class Optimizer {

    public static void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        ItemStack mainHand = client.player.getMainHandStack();
        if (mainHand.getItem() != Items.END_CRYSTAL) return;

        // Walkys core - now using Accessor because field is private in 1.21.11
        // No extra packet, no interactBlock, just remove delay
        ((MinecraftClientAccessor) client).setItemUseCooldown(0);
    }
}
