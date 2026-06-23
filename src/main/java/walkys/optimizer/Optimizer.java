package walksy.optimizer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.block.Blocks;

public class Optimizer {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public void tick() {
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) return;
        
        // Only run if holding end crystal
        if (player.getMainHandStack().getItem() != Items.END_CRYSTAL && 
            player.getOffHandStack().getItem() != Items.END_CRYSTAL) return;

        HitResult hit = raycast(player);
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = BlockPos.ofFloored(hit.getPos());
            if (isObsidianOrBedrock(pos) && canPlaceCrystal(pos)) {
                stopItemUse();
            }
        }
    }

    private HitResult raycast(ClientPlayerEntity player) {
        Vec3d start = player.getCameraPosVec(1.0f);
        Vec3d end = start.add(player.getRotationVec(1.0f).multiply(5.0));
        return client.world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
    }

    private boolean isObsidianOrBedrock(BlockPos pos) {
        return client.world.getBlockState(pos).isOf(Blocks.OBSIDIAN) || 
               client.world.getBlockState(pos).isOf(Blocks.BEDROCK);
    }

    private boolean canPlaceCrystal(BlockPos pos) {
        Box box = new Box(pos.up());
        return client.world.getOtherEntities(null, box).isEmpty();
    }

    public void stopItemUse() {
        if (client.interactionManager != null) {
            client.interactionManager.stopUsingItem(client.player);
        }
    }
}
