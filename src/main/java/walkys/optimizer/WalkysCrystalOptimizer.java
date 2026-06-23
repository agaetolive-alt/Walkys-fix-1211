package walksy.optimizer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class WalkysCrystalOptimizer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // THIS LINE WAS MISSING. THIS HOOKS OUR CODE TO RUN EVERY TICK.
        ClientTickEvents.END_CLIENT_TICK.register(Optimizer::onTick);
    }
}
