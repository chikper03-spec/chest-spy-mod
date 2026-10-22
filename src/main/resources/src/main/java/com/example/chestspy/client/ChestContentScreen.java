package com.example.chestspy.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;

public class ChestContentScreen extends Screen {
    private final DefaultedList<ItemStack> inventory;

    public ChestContentScreen(DefaultedList<ItemStack> inventory) {
        super(Text.literal("Содержимое сундука (Replay)"));
        this.inventory = inventory;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        
        context.drawText(this.textRenderer, this.title, this.width / 2 - 50, 20, 0xFFFFFF, true);

        int startX = this.width / 2 - (9 * 18) / 2;
        int startY = 40;

        for (int i = 0; i < Math.min(inventory.size(), 27); i++) {
            int row = i / 9;
            int col = i % 9;
            int x = startX + col * 18;
            int y = startY + row * 18;

            context.fill(x, y, x + 16, y + 16, 0x80000000);
            
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty()) {
                context.drawItem(stack, x, y);
                context.drawItemInSlot(this.textRenderer, stack, x, y);
            }
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
