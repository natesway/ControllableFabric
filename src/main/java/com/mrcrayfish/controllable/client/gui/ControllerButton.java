package com.mrcrayfish.controllable.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.Controller;
import com.mrcrayfish.controllable.client.gui.screens.ControllerLayoutScreen;
import net.minecraft.client.gui.GuiComponent;

/**
 * Author: MrCrayfish
 */
public class ControllerButton extends GuiComponent
{
    protected ControllerLayoutScreen screen;
    protected int button;
    private int x, y;
    private int u, v;
    private int width, height;
    private int scale;
    private boolean hovered;

    public ControllerButton(ControllerLayoutScreen screen, int button, int x, int y, int u, int v, int width, int height, int scale)
    {
        this.screen = screen;
        this.button = button;
        this.x = x;
        this.y = y;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.scale = scale;
    }

    public void draw(PoseStack poseStack, int x, int y, int mouseX, int mouseY, boolean selected)
    {
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, ControllerLayoutScreen.TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int buttonU = this.u;
        int buttonV = this.v;
        int buttonX = x + this.x * this.scale;
        int buttonY = y + this.y * this.scale;
        int buttonWidth = this.width * this.scale;
        int buttonHeight = this.height * this.scale;
        Controller controller = Controllable.getController();
        this.hovered = mouseX >= buttonX && mouseY >= buttonY && mouseX < buttonX + buttonWidth && mouseY < buttonY + buttonHeight;
        if(this.hovered)
        {
            buttonV += this.height * 2;
        }
        else if(controller != null && this.screen.isButtonPressed(this.button) || selected)
        {
            buttonV += this.height;
        }
        blit(poseStack, buttonX, buttonY, this.width * this.scale, this.height * this.scale, buttonU, buttonV, this.width, this.height, 256, 256);
        RenderSystem.disableBlend();

        // Draws an exclamation if the button has no button assigned to it!
        if(this.isMissingMapping())
        {
            RenderSystem.setShaderTexture(0, ControllerLayoutScreen.TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            blit(poseStack, buttonX + (buttonWidth - 4) / 2, buttonY + (buttonHeight - 15) / 2, 4, 15, 88, 0, 4, 15, 256, 256);
        }
    }

    public int getButton()
    {
        return this.button;
    }

    public boolean isHovered()
    {
        return this.hovered;
    }

    public boolean isMissingMapping()
    {
        return !this.screen.getReassignments().values().contains(this.button) && this.screen.remap(this.button) != this.button;
    }
}
