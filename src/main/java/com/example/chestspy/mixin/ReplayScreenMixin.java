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
        
        // Проверяем, что мы в реплее и нажата правая кнопка мыши (button 1)
        if (client.world != null && button == 1) {
            HitResult hit = client.crosshairTarget;
            
            if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHit = (BlockHitResult) hit;
                BlockEntity blockEntity = client.world.getBlockEntity(blockHit.getBlockPos());

                if (blockEntity instanceof ChestBlockEntity) {
                    // ТЕСТ: Создаем фейковый список вещей для проверки
                    DefaultedList<ItemStack> fakeInventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
                    fakeInventory.set(0, new ItemStack(Items.DIAMOND, 64)); // Положим алмаз для теста
                    fakeInventory.set(4, new ItemStack(Items.NETHERITE_SWORD));

                    // Открываем наше кастомное меню поверх реплея
                    client.execute(() -> client.setScreen(new ChestContentScreen(fakeInventory)));
                }
            }
        }
    }
}
