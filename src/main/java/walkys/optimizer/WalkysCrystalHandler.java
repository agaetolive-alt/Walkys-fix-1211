package walksy.crystal;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class WalksyCrystalHandler {
    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!WalksyCrystalOptimizer.ENABLED) return;
            if (client.player == null || client.world == null) return;

            // Safety: only single player / offline world
            if (!client.isInSingleplayer()) return;

            WalksyCrystalOptimizer.ticksSinceLastPlace++;

            boolean left = client.options.attackKey.isPressed();
            boolean right = client.options.useKey.isPressed();

            // OG Walkys style: you hold both buttons, you click every time
            if (left && right) {
                if (WalksyCrystalOptimizer.ticksSinceLastPlace >= WalksyCrystalOptimizer.PLACE_DELAY_TICKS) {
                    if (client.interactionManager != null) {
                        // Reset use cooldown so place goes through same tick - 0 delay
                        client.itemUseCooldown = 0;
                        WalksyCrystalOptimizer.ticksSinceLastPlace = 0;
                    }
                }
            }
        });
    }
}
