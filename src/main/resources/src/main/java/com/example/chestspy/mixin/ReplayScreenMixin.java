package com.example.chestspy.mixin;

import com.example.chestspy.client.ChestContentScreen;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ReplayScreenMixin {

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void onReplayMouseClicked(double mouseX, double mouseY, int button, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        if (client.world != null && button == 1) {
            HitResult hit = client.crosshairTarget;
            
            if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHit = (BlockHitResult) hit;
                BlockEntity blockEntity = client.world.getBlockEntity(blockHit.getBlockPos());

                if (blockEntity instanceof ChestBlockEntity) {
                    DefaultedList<ItemStack> fakeInventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
                    fakeInventory.set(0, new ItemStack(Items.DIAMOND, 64)); 
                    fakeInventory.set(4, new ItemStack(Items.NETHERITE_SWORD));

                    client.execute(() -> client.setScreen(new ChestContentScreen(fakeInventory)));
                }
            }
        }
    }
}
