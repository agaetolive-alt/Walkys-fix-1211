package walksy.optimizer;

import net.fabricmc.api.ClientModInitializer;

public class WalkysCrystalOptimizer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Optimizer.init();
        System.out.println("Walksy Crystal Optimizer Loaded - 1.21.11 Fixed!");
    }
}
