package walksy.optimizer;

import net.minecraft.client.MinecraftClient;

public class WalksyCrystalHandler {

    public static void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();

        // ===== SAFETY CHECK START - DO NOT DELETE =====

        // 1. Is game even loaded?
        if (client == null) return;
        if (client.world == null) return;
        if (client.player == null) return;

        // 2. Manual OFF check - /walkysoptimizer off
        if (!WalksyCrystalClient.ENABLED) return;

        // ===== SAFETY CHECK END =====

        // ===== YOUR OPTIMIZER CODE WOULD GO BELOW THIS LINE =====
        // It will ONLY run if:
        // - You typed /walkysoptimizer on
        // If any check above fails, it NEVER reaches here.

    }
}
