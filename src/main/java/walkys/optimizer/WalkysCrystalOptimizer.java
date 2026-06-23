package walksy.optimizer;

import net.fabricmc.api.ClientModInitializer;

public class WalkysCrystalOptimizer implements ClientModInitializer {
    public static final Optimizer optimizer = new Optimizer();
    
    @Override
    public void onInitializeClient() {
        // Works now
    }
    
    public static void tick() {
        Optimizer.onTick(net.minecraft.client.MinecraftClient.getInstance());
    }
}
