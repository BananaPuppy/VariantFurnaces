package net.bananapuppy.variantfurnaces.registries.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bananapuppy.variantfurnaces.MainClass;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class VFurnaceScreen extends HandledScreen<VFurnaceScreenHandler>{
    public VFurnaceScreen(VFurnaceScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
    }
    private static final Identifier textureId = new Identifier(MainClass.MOD_ID, "textures/gui/container/vfurnace.png");

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2; //Centers Title
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, textureId);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(textureId, x, y, 0, 0, backgroundWidth, backgroundHeight);

        renderProgress(context, x, y);
    }

    private void renderProgress(DrawContext context, int x, int y){
        int p = this.handler.getCookProgress();
        context.drawTexture(textureId, x + 79, y + 34, 176, 14, p + 1, 16);
        if(handler.isBurning()){
            p = this.handler.getFuelProgress();
            context.drawTexture(textureId, x + 56, y + 36 + 12 - p, 176, 12 - p, 14, p + 1);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);

        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
