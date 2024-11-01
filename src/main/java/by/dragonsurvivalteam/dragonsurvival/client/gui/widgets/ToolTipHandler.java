package by.dragonsurvivalteam.dragonsurvival.client.gui.widgets;

import by.dragonsurvivalteam.dragonsurvival.DragonSurvival;
import by.dragonsurvivalteam.dragonsurvival.client.gui.screens.AbilityScreen;
import by.dragonsurvivalteam.dragonsurvival.client.gui.widgets.buttons.SkillProgressButton;
import by.dragonsurvivalteam.dragonsurvival.client.gui.widgets.buttons.generic.HelpButton;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.common.handlers.DragonFoodHandler;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.registry.datagen.Translation;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.joml.Vector2ic;

import java.awt.*;
import java.util.List;
import java.util.Objects;

import static by.dragonsurvivalteam.dragonsurvival.DragonSurvival.MODID;

@EventBusSubscriber(Dist.CLIENT)
public class ToolTipHandler {
    private static final ResourceLocation TOOLTIP_BLINKING = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/magic_tips_1.png");
    private static final ResourceLocation TOOLTIP = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/magic_tips_0.png");

    @Translation(key = "tooltip_changes", type = Translation.Type.CONFIGURATION, comments = "If enabled certain modifications to some tooltips will be made (e.g. dragon food items)")
    @ConfigOption(side = ConfigSide.CLIENT, category = "tooltips", key = "tooltip_changes")
    public static Boolean tooltipChanges = true;

    @Translation(key = "hide_unsafe_food", type = Translation.Type.CONFIGURATION, comments = "If enabled dragon food items with negative effects will not have the dragon food tooltips")
    @ConfigOption(side = ConfigSide.CLIENT, category = "tooltips", key = "hide_unsafe_food")
    public static Boolean hideUnsafeFood = true;

    @Translation(key = "dragon_food_tooltips", type = Translation.Type.CONFIGURATION, comments = "If enabled the color of dragon food item tooltips will change to match the dragon")
    @ConfigOption(side = ConfigSide.CLIENT, category = "tooltips", key = "dragon_food_tooltips")
    public static Boolean dragonFoodTooltips = true;

    private final static ResourceLocation ICONS = ResourceLocation.fromNamespaceAndPath(MODID, "food_tooltip_icon_font");

    @SubscribeEvent
    public static void checkIfDragonFood(ItemTooltipEvent tooltipEvent) {
        if (tooltipEvent.getEntity() != null) {
            Item item = tooltipEvent.getItemStack().getItem();
            List<Component> toolTip = tooltipEvent.getToolTip();

            if (DragonFoodHandler.getEdibleFoods(DragonTypes.CAVE).contains(item)) {
                toolTip.add(createFoodTooltip(item, DragonTypes.CAVE, ChatFormatting.RED, "\uEA02", "\uEA05"));
            }

            if (DragonFoodHandler.getEdibleFoods(DragonTypes.FOREST).contains(item)) {
                toolTip.add(createFoodTooltip(item, DragonTypes.FOREST, ChatFormatting.GREEN, "\uEA01", "\uEA04"));
            }

            if (DragonFoodHandler.getEdibleFoods(DragonTypes.SEA).contains(item)) {
                toolTip.add(createFoodTooltip(item, DragonTypes.SEA, ChatFormatting.DARK_AQUA, "\uEA03", "\uEA06"));
            }
        }
    }

    private static MutableComponent createFoodTooltip(final Item item, final AbstractDragonType type, final ChatFormatting color, final String nutritionIcon, final String saturationIcon) {
        MutableComponent component = Component.translatable("ds." + type.getTypeName() + ".dragon.food");
        FoodProperties properties = DragonFoodHandler.getDragonFoodProperties(item, type);

        String nutrition = "0";
        String saturation = "0";

        if (properties != null) {
            float nutritionValue = properties.nutrition();
            float saturationValue = properties.saturation();

            // 1 Icon = 2 points (e.g. 10 nutrition icons for a maximum food level of 20)
            nutrition = String.format("%.1f", nutritionValue / 2);
            saturation = String.format("%.1f", saturationValue / 2);
        }

        MutableComponent nutritionIconComponent = Component.literal(nutritionIcon).withStyle(Style.EMPTY.withFont(ICONS));
        MutableComponent nutritionComponent = Component.literal(": " + nutrition + " ").withStyle(color);

        MutableComponent saturationIconComponent = Component.literal(saturationIcon).withStyle(Style.EMPTY.withFont(ICONS));
        MutableComponent saturationComponent = Component.literal(" / " + saturation + " ").withStyle(color);

        return component.append(nutritionComponent).append(nutritionIconComponent).append(saturationComponent).append(saturationIconComponent);
    }

    @SubscribeEvent // Add certain descriptions to our items which use generic classes
    public static void addCustomItemDescriptions(ItemTooltipEvent event) {
        if (event.getEntity() != null && event.getEntity().level().isClientSide() && event.getItemStack() != ItemStack.EMPTY) {
            ResourceLocation location = event.getItemStack().getItem().builtInRegistryHolder().key().location();
            String languageKey = "";

            if (event.getItemStack().getItem() instanceof EnchantedBookItem) {
                ItemEnchantments enchantments = event.getItemStack().get(DataComponents.STORED_ENCHANTMENTS);

                // Only add it to single-entry enchanted books since the text is longer than usual enchantment descriptions
                if (enchantments != null && enchantments.size() == 1) {
                    Holder<Enchantment> holder = enchantments.entrySet().iterator().next().getKey();
                    //noinspection DataFlowIssue -> would only be null for 'Holder$Direct' which shouldn't be used here
                    languageKey = "ds.description." + holder.getKey().location().getPath();
                }
            } else if (location.getNamespace().equals(MODID)) {
                /* TODO
                    do this via mixin in 'ItemStack#getTooltipLines' at the point below?
                    so that the tooltip behaves the same as a regular tooltip
                    (above custom things like enchantments, attributes or the advanced tooltip)

                if (!this.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)) {
                    this.getItem().appendHoverText(this, tooltipContext, list, tooltipFlag);
                }
                */

                languageKey = "ds.description.add." + location.getPath();
            }

            if (!languageKey.isEmpty() && I18n.exists(languageKey)) {
                event.getToolTip().add(Component.translatable(languageKey));
            }
        }
    }

    private static boolean isBlinking;
    private static int tick;

    @SubscribeEvent
    public static void renderHelpTextCornerElements(RenderTooltipEvent.Pre event) {
        boolean render = isHelpText();

        if (!render) {
            return;
        }

        if (!isBlinking) {
            if (tick >= Functions.secondsToTicks(30)) {
                isBlinking = true;
                tick = 0;
            }
        } else {
            if (tick >= Functions.secondsToTicks(5)) {
                isBlinking = false;
                tick = 0;
            }
        }

        tick++;

        // Logic to determine width / height is from 'GuiGraphics#renderTooltipInternal'
        int width = 0;
        int height = event.getComponents().size() == 1 ? -2 : 0;

        for (ClientTooltipComponent component : event.getComponents()) {
            int componentWidth = component.getWidth(event.getFont());

            if (componentWidth > width) {
                width = componentWidth;
            }

            height += component.getHeight();
        }

        Vector2ic tooltipPosition = event.getTooltipPositioner().positionTooltip(event.getScreenWidth(), event.getScreenHeight(), event.getX(), event.getY(), width, height);

        int x = tooltipPosition.x();
        int y = tooltipPosition.y();

        int textureWidth = 128;
        int textureHeight = 128;

        event.getGraphics().blit(isBlinking ? TOOLTIP_BLINKING : TOOLTIP, x - 8 - 6, y - 8 - 6, 400, 1, 1 % textureHeight, 16, 16, textureWidth, textureHeight);
        event.getGraphics().blit(isBlinking ? TOOLTIP_BLINKING : TOOLTIP, x + width - 8 + 6, y - 8 - 6, 400, textureWidth - 16 - 1, 1 % textureHeight, 16, 16, textureWidth, textureHeight);

        event.getGraphics().blit(isBlinking ? TOOLTIP_BLINKING : TOOLTIP, x - 8 - 6, y + height - 8 + 6, 400, 1, 1 % textureHeight + 16, 16, 16, textureWidth, textureHeight);
        event.getGraphics().blit(isBlinking ? TOOLTIP_BLINKING : TOOLTIP, x + width - 8 + 6, y + height - 8 + 6, 400, textureWidth - 16 - 1, 1 % textureHeight + 16, 16, 16, textureWidth, textureHeight);

        event.getGraphics().blit(isBlinking ? TOOLTIP_BLINKING : TOOLTIP, x + width / 2 - 47, y - 16, 400, 16 + 2 * textureWidth + 1, 1 % textureHeight, 94, 16, textureWidth, textureHeight);
        event.getGraphics().blit(isBlinking ? TOOLTIP_BLINKING : TOOLTIP, x + width / 2 - 47, y + height, 400, 16 + 2 * textureWidth + 1, 1 % textureHeight + 16, 94, 16, textureWidth, textureHeight);
    }

    private static boolean isHelpText() {
        if (!tooltipChanges) {
            return false;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null || minecraft.screen == null) {
            return false;
        }

        for (GuiEventListener element : minecraft.screen.children()) {
            if (element instanceof HelpButton helpButton && helpButton.isHovered()) {
                return true;
            }
        }

        return false;
    }

    @SubscribeEvent
    public static void renderTooltipBorderInDragonColor(RenderTooltipEvent.Color event) {
        if (!tooltipChanges) {
            return;
        }

        boolean isAbilityScreen = Minecraft.getInstance().screen instanceof AbilityScreen;
        ItemStack stack = event.getItemStack();

        boolean isSeaFood = dragonFoodTooltips && !stack.isEmpty() && DragonFoodHandler.getEdibleFoods(DragonTypes.SEA).contains(stack.getItem());
        boolean isForestFood = dragonFoodTooltips && !stack.isEmpty() && DragonFoodHandler.getEdibleFoods(DragonTypes.FOREST).contains(stack.getItem());
        boolean isCaveFood = dragonFoodTooltips && !stack.isEmpty() && DragonFoodHandler.getEdibleFoods(DragonTypes.CAVE).contains(stack.getItem());

        boolean isDragonFood = isSeaFood || isForestFood || isCaveFood;
        boolean isSkillProgressButtonHovered = false;

        if (isAbilityScreen) {
            for (GuiEventListener widget : ((AbilityScreen) Minecraft.getInstance().screen).widgetList()) {
                if (widget instanceof SkillProgressButton && ((SkillProgressButton) widget).isHoveredOrFocused()) {
                    isSkillProgressButtonHovered = true;
                    break;
                }
            }
        }

        if (isHelpText()) {
            int top = new Color(154, 132, 154).getRGB();
            int bottom = new Color(89, 68, 89).getRGB();

            event.setBorderStart(top);
            event.setBorderEnd(bottom);
        } else if (isAbilityScreen || isDragonFood) {
            AbstractDragonType type = DragonUtils.getDragonType(DragonSurvival.PROXY.getLocalPlayer());
            Color topColor = null;
            Color bottomColor = null;

            if (type != null) {
                if (Objects.equals(type, DragonTypes.SEA) && isSkillProgressButtonHovered || isSeaFood) {
                    topColor = new Color(93, 201, 255);
                    bottomColor = new Color(49, 109, 144);
                } else if (Objects.equals(type, DragonTypes.FOREST) && isSkillProgressButtonHovered || isForestFood) {
                    topColor = new Color(0, 255, 148);
                    bottomColor = new Color(4, 130, 82);
                } else if (Objects.equals(type, DragonTypes.CAVE) && isSkillProgressButtonHovered || isCaveFood) {
                    topColor = new Color(255, 118, 133);
                    bottomColor = new Color(139, 66, 74);
                }
            }

            if (topColor != null) {
                event.setBorderStart(topColor.getRGB());
            }

            if (bottomColor != null) {
                event.setBorderEnd(bottomColor.getRGB());
            }
        }
    }
}