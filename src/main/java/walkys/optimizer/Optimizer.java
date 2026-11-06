package walksy.optimizer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.block.Blocks;

public class Optimizer {
    private static int placeCooldown = 0;
    private static int breakCooldown = 0;
    private static EndCrystalEntity lastPlaced = null;
    private static int ticksSincePlace = 99;

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client == null || client.player == null || client.world == null) return;
            if (client.currentScreen != null) return;
            if (client.interactionManager == null) return;

            ticksSincePlace++;
            if (placeCooldown > 0) placeCooldown--;
            if (breakCooldown > 0) breakCooldown--;

            // ========== FEATURE 1: OLD WALKSY - NO DELAY ==========
            // This is the main optimizer. Mojang adds delay, we remove it every tick.
            client.attackCooldown = 0;
            client.itemUseCooldown = 0;

            boolean left = client.options.attackKey.isPressed();
            boolean right = client.options.useKey.isPressed();

            // ========== FEATURE 2: INSTANT BREAK ==========
            // Old Walkys: If you look at crystal, break it instantly, even if you just placed it
            // New fix: Wait 1 tick after place, then break - otherwise 1.21.11 ignores packet
            if (left && breakCooldown <= 0) {
                HitResult hit = client.crosshairTarget;
                EndCrystalEntity target = null;

                if (hit instanceof EntityHitResult eh && eh.getEntity() instanceof EndCrystalEntity c) {
                    target = c;
                } else {
                    // Old Walkys also broke crystal near crosshair, not only exact hit
                    Box box = client.player.getBoundingBox().expand(6.0);
                    double best = 999;
                    for (EndCrystalEntity c : client.world.getEntitiesByClass(EndCrystalEntity.class, box, e -> true)) {
                        double d = client.player.distanceTo(c);
                        if (d < best && d < 6.0) {
                            best = d;
                            target = c;
                        }
                    }
                }

                if (target != null && ticksSincePlace >= 1) {
                    client.interactionManager.attackEntity(client.player, target);
                    client.player.swingHand(Hand.MAIN_HAND);
                    breakCooldown = 1; // Old Walkys was 0 delay, but 1.21.11 needs 1 tick or it double-attacks
                }
            }

            // ========== FEATURE 3: FAST PLACE ==========
            // Old Walkys: Right click on obsidian/bedrock = place with NO 4-tick delay
            // This is NOT auto-place. You still have to click right yourself.
            // We just remove the delay so your manual click places instantly.
            if (right && placeCooldown <= 0) {
                HitResult hit = client.crosshairTarget;
                if (hit instanceof BlockHitResult blockHit) {
                    BlockPos pos = blockHit.getBlockPos();
                    boolean isObsidian = client.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN;
                    boolean isBedrock = client.world.getBlockState(pos).getBlock() == Blocks.BEDROCK;

                    if ((isObsidian || isBedrock) && client.player.getMainHandStack().getItem() == Items.END_CRYSTAL) {
                        // Only place if there is no crystal already there
                        Box checkBox = new Box(pos.up());
                        boolean hasCrystal = !client.world.getEntitiesByClass(EndCrystalEntity.class, checkBox, e -> true).isEmpty();
                        if (!hasCrystal) {
                            // 1.21.11 fix: Must use interactBlock with correct hand + hit result
                            client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, blockHit);
                            client.player.swingHand(Hand.MAIN_HAND);
                            client.itemUseCooldown = 0; // Remove place delay instantly
                            placeCooldown = 1;
                            ticksSincePlace = 0;
                        }
                    }
                }
            }

            // ========== FEATURE 4: BOTH BUTTONS = PLACE + BREAK COMBO ==========
            // Old Walkys signature move: Hold LEFT + RIGHT together
            // = Place crystal (your aim) -> Next tick break it -> Repeat
            // This is why OG felt so fast - it did place + break in 2 ticks, not 8 ticks
            if (left && right) {
                // The code above already handles it:
                // Right part places instantly, Left part breaks instantly next tick
                // No spam needed, just HOLD both - old Walkys style
            }

            // ========== FEATURE 5: CRYSTAL OPTIMIZER - NO GHOST CRYSTALS ==========
            // Old Walkys also removed the client-side ghost crystal that stays after break
            // 1.21.11 fix: We attack + swing same tick so server + client stay in sync
        });
    }
}
