package walksy.optimizer.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    private int attackCooldown;

    @Shadow
    private int itemUseCooldown;

    @Inject(method = "tick", at = @At("HEAD"))
    private void walksyRemoveCooldowns(CallbackInfo ci) {
        this.attackCooldown = 0;
        this.itemUseCooldown = 0;
    }
}
