package by.dragonsurvivalteam.dragonsurvival.magic.abilities.SeaDragon.passive;

import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.config.server.dragon.SeaDragonConfig;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.passive.PassiveDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.registry.datagen.Translation;
import by.dragonsurvivalteam.dragonsurvival.registry.datagen.lang.LangKey;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

import static by.dragonsurvivalteam.dragonsurvival.DragonSurvival.MODID;

@Translation(type = Translation.Type.ABILITY_DESCRIPTION, comments = {
        "■ Increases your capacity for hydration while outside of water. Will help you to survive while venturing onto land, or even in the Nether.\n",
        "■ Duration: §2%s§rs"
})
@Translation(type = Translation.Type.ABILITY, comments = "Water")
@RegisterDragonAbility
public class WaterAbility extends PassiveDragonAbility {
    @Translation(key = "water", type = Translation.Type.CONFIGURATION, comments = "Enable / Disable the water ability")
    @ConfigOption(side = ConfigSide.SERVER, category = {"sea_dragon", "magic", "abilities", "passive"}, key = "water")
    public static Boolean water = true;

    @Override
    public Component getDescription() {
        return Component.translatable(Translation.Type.ABILITY_DESCRIPTION.wrap(getName()), getDuration() + Functions.ticksToSeconds(SeaDragonConfig.seaTicksWithoutWater));
    }

    @Override
    public int getSortOrder() {
        return 3;
    }

    @Override
    public String getName() {
        return "water";
    }

    @Override
    public AbstractDragonType getDragonType() {
        return DragonTypes.SEA;
    }

    @Override
    public ResourceLocation[] getSkillTextures() {
        return new ResourceLocation[]{
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/sea/water_0.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/sea/water_1.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/sea/water_2.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/sea/water_3.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/sea/water_4.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/sea/water_5.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/sea/water_6.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/sea/water_7.png")};
    }


    public int getDuration() {
        return 60 * getLevel();
    }

    @Override
    public ArrayList<Component> getLevelUpInfo() {
        ArrayList<Component> list = super.getLevelUpInfo();
        list.add(Component.translatable(LangKey.ABILITY_DURATION, "+60"));
        return list;
    }

    @Override
    public int getMaxLevel() {
        return 7;
    }

    @Override
    public int getMinLevel() {
        return 0;
    }

    @Override
    public boolean isDisabled() {
        return super.isDisabled() || !water;
    }
}