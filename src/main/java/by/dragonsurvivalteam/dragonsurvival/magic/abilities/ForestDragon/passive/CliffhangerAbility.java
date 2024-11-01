package by.dragonsurvivalteam.dragonsurvival.magic.abilities.ForestDragon.passive;


import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.config.server.dragon.ForestDragonConfig;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.passive.PassiveDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.registry.datagen.Translation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

import static by.dragonsurvivalteam.dragonsurvival.DragonSurvival.MODID;

@RegisterDragonAbility
public class CliffhangerAbility extends PassiveDragonAbility {
    @Translation(key = "cliff_hander", type = Translation.Type.CONFIGURATION, comments = "Enable / Disable the cliff hanger ability")
    @ConfigOption(side = ConfigSide.SERVER, category = {"forest_dragon", "magic", "abilities", "passive"}, key = "cliffHanger")
    public static Boolean cliffHanger = true;

    @Override
    public Component getDescription() {
        return Component.translatable("ds.skill.description." + getName(), 3 + getHeight() + ForestDragonConfig.fallReduction);
    }

    @Override
    public int getSortOrder() {
        return 4;
    }

    @Override
    public String getName() {
        return "cliffhanger";
    }

    @Override
    public AbstractDragonType getDragonType() {
        return DragonTypes.FOREST;
    }

    @Override
    public ResourceLocation[] getSkillTextures() {
        return new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/forest/cliffhanger_0.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/forest/cliffhanger_1.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/forest/cliffhanger_2.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/forest/cliffhanger_3.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/forest/cliffhanger_4.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/forest/cliffhanger_5.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/forest/cliffhanger_6.png")};
    }

    public int getHeight() {
        return getLevel();
    }

    @Override
    public ArrayList<Component> getLevelUpInfo() {
        ArrayList<Component> list = super.getLevelUpInfo();
        list.add(Component.translatable("ds.skill.range.blocks", "+1"));
        return list;
    }

    @Override
    public int getMaxLevel() {
        return 6;
    }

    @Override
    public int getMinLevel() {
        return 0;
    }

    @Override
    public boolean isDisabled() {
        return super.isDisabled() || !cliffHanger;
    }
}