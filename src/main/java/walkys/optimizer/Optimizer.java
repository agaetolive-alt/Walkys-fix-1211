package walksy.optimizer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class Optimizer {
    public static void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        
        // Only run if holding end crystal + right clicking
        if (!client.options.useKey.isPressed()) return;
        ItemStack mainHand = client.player.getMainHandStack();
        if (mainHand.getItem() != Items.END_CRYSTAL) return;
        
        // Get what player is looking at
        HitResult hit = client.crosshairTarget;
        if (hit == null || hit.getType() != HitResult.Type.BLOCK) return;
        
        BlockHitResult blockHit = (BlockHitResult) hit;
        BlockPos pos = blockHit.getBlockPos();
        
        // 1.21.11 BYPASS: Set cooldown to 0 every tick
        client.itemUseCooldown = 0;
        
        // 1.21.11 BYPASS: Send packet directly, skip client prediction
        PlayerInteractBlockC2SPacket packet = new PlayerInteractBlockC2SPacket(
            Hand.MAIN_HAND, 
            blockHit, 
            0 // sequence number - 0 works for instant
        );
        
        client.getNetworkHandler().sendPacket(packet);
        
        // Swing hand client-side so it looks normal
        client.player.swingHand(Hand.MAIN_HAND);
    }
}
