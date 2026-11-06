package walksy;

import net.fabricmc.api.ClientModInitializer;
import walksy.crystal.WalksyCrystalHandler;

public class WalksyCrystalClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WalksyCrystalHandler.init();
        System.out.println("[Walkys Crystal Optimizer] Loaded - SP 0 tick - 1.21.11");
    }
}
