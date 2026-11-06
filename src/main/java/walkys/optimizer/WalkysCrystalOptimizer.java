package walksy.optimizer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import walksy.optimizer.mixin.MinecraftClientAccessor;

public class WalkysCrystalOptimizer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            MinecraftClientAccessor accessor = (MinecraftClientAccessor) (Object) MinecraftClient.getInstance();
            accessor.setAttackCooldown(0);
            accessor.setItemUseCooldown(0);
        });
        System.out.println("Walksy Crystal Optimizer Loaded - 1.21.11 Fixed!");
    }
}
