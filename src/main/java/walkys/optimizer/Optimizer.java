package walksy.optimizer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class Optimizer {
    private static int placeCooldown = 0;
    private static int breakCooldown = 0;
    private static int ticksSincePlace = 99;

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(Optimizer::onTick);
    }

    public static void onTick(MinecraftClient client) {
        if (client == null || client.player == null || client.world == null) return;
        if (client.currentScreen != null) return;
        if (client.interactionManager == null) return;

        ticksSincePlace++;
        if (placeCooldown > 0) placeCooldown--;
        if (breakCooldown > 0) breakCooldown--;

        // FIXED FOR 1.21.11: Use Accessor, not direct field
        MinecraftClientAccessor accessor = (MinecraftClientAccessor) client;
        accessor.setAttackCooldown(0);
        accessor.setItemUseCooldown(0);

        boolean left = client.options.attackKey.isPressed();
        boolean right = client.options.useKey.isPressed();

        // INSTANT BREAK - Old Walkys
        if (left && breakCooldown <= 0) {
            HitResult hit = client.crosshairTarget;
            EndCrystalEntity target = null;
            if (hit instanceof EntityHitResult eh && eh.getEntity() instanceof EndCrystalEntity c) {
                target = c;
            } else {
                Box box = client.player.getBoundingBox().expand(6.0);
                double best = 999;
                for (EndCrystalEntity c : client.world.getEntitiesByClass(EndCrystalEntity.class, box, e -> true)) {
                    double d = client.player.distanceTo(c);
                    if (d < best && d < 6.0) { best = d; target = c; }
                }
            }
            if (target != null && ticksSincePlace >= 1) {
                client.interactionManager.attackEntity(client.player, target);
                client.player.swingHand(Hand.MAIN_HAND);
                breakCooldown = 1;
            }
        }

        // FAST PLACE - Old Walkys, you place yourself, we just remove delay
        if (right && placeCooldown <= 0) {
            HitResult hit = client.crosshairTarget;
            if (hit instanceof BlockHitResult blockHit) {
                BlockPos pos = blockHit.getBlockPos();
                boolean isObsidian = client.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN;
                boolean isBedrock = client.world.getBlockState(pos).getBlock() == Blocks.BEDROCK;
                if ((isObsidian || isBedrock) && client.player.getMainHandStack().getItem() == Items.END_CRYSTAL) {
                    Box checkBox = new Box(pos.up());
                    boolean hasCrystal = !client.world.getEntitiesByClass(EndCrystalEntity.class, checkBox, e -> true).isEmpty();
                    if (!hasCrystal) {
                        client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, blockHit);
                        client.player.swingHand(Hand.MAIN_HAND);
                        accessor.setItemUseCooldown(0);
                        placeCooldown = 1;
                        ticksSincePlace = 0;
                    }
                }
            }
        }
    }
}
