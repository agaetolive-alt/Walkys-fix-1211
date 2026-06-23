package walksy.optimizer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class Optimizer {
    public static void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null || client.interactionManager == null) return;
        
        // Only if holding end crystal + right click held
        if (!client.options.useKey.isPressed()) return;
        ItemStack mainHand = client.player.getMainHandStack();
        if (mainHand.getItem() != Items.END_CRYSTAL) return;
        
        HitResult hit = client.crosshairTarget;
        if (hit == null || hit.getType() != HitResult.Type.BLOCK) return;
        
        BlockHitResult blockHit = (BlockHitResult) hit;
        
        // Send packet directly, bypass client cooldown
        client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, blockHit);
        client.player.swingHand(Hand.MAIN_HAND);
        
        // Cancel vanilla input so it doesn't double-place with delay
        client.options.useKey.setPressed(false);
    }
}
