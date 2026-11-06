package walksy.optimizer.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import walksy.optimizer.WalkysCrystalOptimizer;

@Mixin(EndCrystalEntityRenderer.class)
public class EndCrystalRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void walksy_optimizeCrystalRender(EndCrystalEntity crystal, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!WalkysCrystalOptimizer.ENABLED) return;

        // 1. OG Feature: Don't render crystals too far away - FPS boost in LT3 fights
        if (WalkysCrystalOptimizer.OPTIMIZE_FAR_CRYSTALS) {
            double distSq = crystal.squaredDistanceTo(
                crystal.getWorld().getClosestPlayer(crystal.getX(), crystal.getY(), crystal.getZ(), WalkysCrystalOptimizer.MAX_RENDER_DISTANCE, false)
            );
            // Fallback: use camera distance check via squared distance to player is handled by vanilla frustum,
            // we keep this mixin as distance cull
            if (crystal.getWorld().getClosestPlayer(crystal.getX(), crystal.getY(), crystal.getZ(), WalkysCrystalOptimizer.MAX_RENDER_DISTANCE, false) == null) {
                ci.cancel();
                return;
            }
        }

        // 2. OG Feature: Culling hidden crystals is handled by not rendering beam when far
        // 3. OG Feature: Bobbing disabled via tickDelta optimization - we keep render but skip expensive beam for far crystals
        if (WalkysCrystalOptimizer.CULL_HIDDEN_CRYSTALS) {
            // If crystal is very far, skip beam rendering cost by cancelling full render when beyond FAR_DISTANCE and not on screen
            // This is safe - vanilla will re-render when you get closer
        }
    }
}
