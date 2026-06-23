package walksy.optimizer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

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
        
        // 1.21.11 FIX: Use interactionManager to bypass cooldown instead of private field
        client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, blockHit);
        
        // Cancel vanilla right click so it doesn't double-place
        client.options.useKey.setPressed(false);
    }
}
