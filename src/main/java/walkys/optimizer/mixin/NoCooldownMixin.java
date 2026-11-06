package walksy.optimizer.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import walksy.optimizer.WalkysCrystalOptimizer;

@Mixin(MinecraftClient.class)
public class NoCooldownMixin {

    @Shadow
    private int attackCooldown;

    @Inject(method = "tick", at = @At("HEAD"))
    private void walksy_zeroDelay(CallbackInfo ci) {
        if (!WalkysCrystalOptimizer.PVP_MACHINE_ENABLED) return;

        // OG Walksy PVP Machine: 0 delay
        // Vanilla sets attackCooldown = 5 after every hit, we force it to 0 every tick
        // So Left Click (break) + Right Click (place) can happen SAME tick
        this.attackCooldown = 0;
    }
}
