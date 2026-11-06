package walksy.optimizer.mixin;

import net.minecraft.client.MinecraftClient;
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
        if (!WalkysCrystalOptimizer.OPTIMIZE_FAR_CRYSTALS) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        double distSq = crystal.squaredDistanceTo(client.player);
        double max = WalkysCrystalOptimizer.MAX_RENDER_DISTANCE;
        if (distSq > max * max) {
            ci.cancel();
        }
    }
}
