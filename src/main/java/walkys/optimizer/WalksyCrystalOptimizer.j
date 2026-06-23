package walksy.optimizer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class WalkysCrystalOptimizer implements ClientModInitializer {
    private static Optimizer optimizer;
    
    @Override
    public void onInitializeClient() {
        optimizer = new Optimizer();
        // This makes tick() run every client frame
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            optimizer.tick();
        });
    }
    
    public static Optimizer getOptimizer() {
        return optimizer;
    }
}
