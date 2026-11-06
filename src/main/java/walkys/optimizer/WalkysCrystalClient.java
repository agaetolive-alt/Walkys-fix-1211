package walksy.optimizer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

public class WalksyCrystalClient implements ClientModInitializer {

    // 1. Toggle variable - OG style
    public static boolean ENABLED = true;

    @Override
    public void onInitializeClient() {

        // 2. OG Command: /walkysoptimizer on / off
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                ClientCommandManager.literal("walkysoptimizer")
                    .then(ClientCommandManager.literal("off")
                        .executes(context -> {
                            ENABLED = false;
                            context.getSource().sendFeedback(Text.literal("Walkys Crystal Optimizer: OFF"));
                            return 1;
                        }))
                    .then(ClientCommandManager.literal("on")
                        .executes(context -> {
                            ENABLED = true;
                            context.getSource().sendFeedback(Text.literal("Walkys Crystal Optimizer: ON"));
                            return 1;
                        }))
            );
        });
    }

    // 3. Safety check - call this at top of your handler
    public static boolean shouldRun() {
        // Manual off check
        if (!ENABLED) return false;
        // Auto safety - only single player
        // We check this in handler with MinecraftClient, this is just the toggle part
        return true;
    }
}
