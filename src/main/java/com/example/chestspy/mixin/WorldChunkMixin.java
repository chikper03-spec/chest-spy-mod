package com.example.chestspy.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldChunk.class)
public class WorldChunkMixin {

    @Inject(method = "addBlockEntity", at = @At("TAIL"))
    private void onBlockEntityAdded(BlockEntity blockEntity, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        if (blockEntity instanceof ChestBlockEntity && client.interactionManager != null && client.player != null) {
            BlockPos pos = blockEntity.getPos();
            
            if (client.player.getPos().squaredDistanceTo(Vec3d.ofCenter(pos)) <= 36.0) {
                BlockHitResult hitResult = new BlockHitResult(
                    Vec3d.ofCenter(pos), Direction.UP, pos, false
                );
                
                PlayerInteractBlockC2SPacket packet = new PlayerInteractBlockC2SPacket(
                    Hand.MAIN_HAND, hitResult, 0
                );
                
                client.player.networkHandler.sendPacket(packet);
            }
        }
    }
}
