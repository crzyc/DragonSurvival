package by.dragonsurvivalteam.dragonsurvival.client.gui.widgets.buttons.generic;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static by.dragonsurvivalteam.dragonsurvival.DragonSurvival.MODID;

public class ExtendedCheckbox extends AbstractButton {
    public interface OnValueChange {
        void onValueChange(ExtendedCheckbox pCheckbox, boolean pValue);
    }

    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/textbox.png");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/checkbox.png");

    public boolean selected;

    private final int renderWidth;
    private final Consumer<ExtendedCheckbox> pressable;
    private final ExtendedCheckbox.OnValueChange onValueChange;

    public ExtendedCheckbox(int pX, int pY, int pWidth, int renderWidth, int pHeight, Component pMessage, boolean pSelected, Consumer<ExtendedCheckbox> pressable) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.onValueChange = (checkbox, selected) -> { /* Nothing to do */ };
        this.selected = pSelected;
        this.pressable = pressable;
        this.renderWidth = renderWidth;
    }

    @Override
    public void onPress() {
        this.selected = !this.selected;
        this.onValueChange.onValueChange(this, this.selected);
        pressable.accept(this);
    }

    public boolean selected() {
        return this.selected;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        pNarrationElementOutput.add(NarratedElementType.TITLE, this.createNarrationMessage());
        if (this.active) {
            if (this.isFocused()) {
                pNarrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.checkbox.usage.focused"));
            } else {
                pNarrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.checkbox.usage.hovered"));
            }
        }
    }

    @Override
    public void renderWidget(@NotNull final GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Font fontrenderer = minecraft.font;

        if (height > 10) {
            guiGraphics.blitWithBorder(BACKGROUND_TEXTURE, getX(), getY(), 0, 0, width, height, 32, 32, 10, 10, 10, 10);
            guiGraphics.blitWithBorder(BACKGROUND_TEXTURE, getX(), getY(), 0, 0, renderWidth, height, 32, 32, 10, 10, 10, 10);
        }

        if (height > 10) {
            float widthMod = (renderWidth - 4) / 36f;
            float heightMod = (height - 4) / 36f;

            float u = isHoveredOrFocused() || isFocused() ? renderWidth - 4 : 0.0F;
            float v = selected() ? height - 4 : 0.0F;

            guiGraphics.blit(TEXTURE, getX() + 2, getY() + 2, u, v, renderWidth - 4, height - 4, (int) (72 * widthMod), (int) (72f * heightMod));
            MutableComponent message = Component.empty().append(getMessage());

            if (active) {
                message = message.withStyle(ChatFormatting.DARK_GRAY);
            }

            guiGraphics.drawString(fontrenderer, message, getX() + renderWidth + 2, getY() + (height - 8) / 2, 14737632);
        } else {
            float widthMod = renderWidth / 36f;
            float heightMod = height / 36f;

            float u = isHoveredOrFocused() || isFocused() ? renderWidth : 0.0F;
            float v = selected() ? height : 0.0F;

            guiGraphics.blit(TEXTURE, getX(), getY(), u, v, renderWidth, height, (int) (72 * widthMod), (int) (72f * heightMod));
            MutableComponent message = Component.empty().append(getMessage());

            if (active) {
                message = message.withStyle(ChatFormatting.DARK_GRAY);
            }

            guiGraphics.drawString(fontrenderer, message, getX() + renderWidth + 2, getY() + (height - 8) / 2, 14737632);
        }
    }
}