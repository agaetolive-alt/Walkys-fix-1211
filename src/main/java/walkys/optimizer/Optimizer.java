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
        ((MinecraftClientAccessor) client).setItemUseCooldown(0);
    }
}
