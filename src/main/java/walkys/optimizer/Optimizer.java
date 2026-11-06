package walksy.optimizer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class Optimizer {

    public static void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        // Only when holding crystal
        ItemStack mainHand = client.player.getMainHandStack();
        if (mainHand.getItem() != Items.END_CRYSTAL) return;

        // Walkys core only: Remove the vanilla place delay
        // Do NOT call interactBlock here, let vanilla do the place
        // Do NOT call swingHand here
        client.itemUseCooldown = 0;
    }
}
