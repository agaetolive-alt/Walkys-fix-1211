package walksy.optimizer;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class WalksyCrystalOptimizerMod implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        // empty
    }

    // FIX 1: 1.21.11 crash fix - wrapDegrees instead of method_15362
    private Vec3d lookVec(float yaw, float pitch) {
        float wrappedYaw = MathHelper.wrapDegrees(yaw);
        float f = MathHelper.cos(-wrappedYaw * 0.017453292F - 3.1415927F);
        float g = MathHelper.sin(-wrappedYaw * 0.017453292F - 3.1415927F);
        float h = -MathHelper.cos(-pitch * 0.017453292F);
        float i = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d(g * h, i, f * h);
    }

    // FIX 2: Inventory crash fix - checks full hotbar not just main hand
    public void useOwnTicks() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null || mc.world == null) return;
        
        ItemStack crystalStack = ItemStack.EMPTY;
        for (int i = 0; i < 9; i++) {
            ItemStack slot = player.getInventory().getStack(i);
            if (!slot.isEmpty() && slot.getItem() == Items.END_CRYSTAL) {
                crystalStack = slot;
                break;
            }
        }
        
        if (!crystalStack.isEmpty()) {
            Vec3d lookPos = lookVec(player.getYaw(), player.getPitch());
            // Original Walksy logic would continue here
        }
    }

    private Vec3d generalLookPos() {
        MinecraftClient mc = MinecraftClient.getInstance();
        return lookVec(mc.player.getYaw(), mc.player.getPitch());
    }
}
