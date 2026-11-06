package walksy.optimizer;

import net.fabricmc.api.ClientModInitializer;

public class WalkysCrystalOptimizer implements ClientModInitializer {

    // OG Walksy Config
    public static boolean ENABLED = true;
    public static boolean PVP_MACHINE_ENABLED = true;
    public static boolean CULL_HIDDEN_CRYSTALS = true;
    public static boolean OPTIMIZE_FAR_CRYSTALS = true;
    public static boolean DISABLE_CRYSTAL_BOBBING = true;
    public static boolean REDUCE_PARTICLES = true;
    public static double MAX_RENDER_DISTANCE = 64.0;
    public static double FAR_DISTANCE = 32.0;

    @Override
    public void onInitializeClient() {
        System.out.println("Walksy Crystal Optimizer Loaded - 1.21.11 - PVP Machine ON!");
    }

    public static boolean shouldRenderCrystal(double distanceSq) {
        if (!ENABLED) return true;
        if (distanceSq > MAX_RENDER_DISTANCE * MAX_RENDER_DISTANCE) return false;
        return true;
    }
}
