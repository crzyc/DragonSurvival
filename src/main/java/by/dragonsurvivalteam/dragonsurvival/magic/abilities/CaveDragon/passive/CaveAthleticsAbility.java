package by.dragonsurvivalteam.dragonsurvival.magic.abilities.CaveDragon.passive;

import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.passive.AthleticsAbility;
import by.dragonsurvivalteam.dragonsurvival.registry.datagen.Translation;
import net.minecraft.resources.ResourceLocation;

import static by.dragonsurvivalteam.dragonsurvival.DragonSurvival.MODID;

@Translation(type = Translation.Type.ABILITY_DESCRIPTION, comments = {
        "■ Standing on stone surfaces will give you §2Speed %s§r\n",
        "■ Duration: §2%s§rs"
})
@Translation(type = Translation.Type.ABILITY, comments = "Cave Athletics")
@RegisterDragonAbility
public class CaveAthleticsAbility extends AthleticsAbility {
    @Translation(key = "cave_athletics", type = Translation.Type.CONFIGURATION, comments = "Enable / Disable the cave athletics ability")
    @ConfigOption(side = ConfigSide.SERVER, category = {"cave_dragon", "magic", "abilities", "passive"}, key = "cave_athletics")
    public static Boolean caveAthletics = true;

    @Override
    public int getSortOrder() {
        return 2;
    }

    @Override
    public String getName() {
        return "cave_athletics";
    }

    @Override
    public AbstractDragonType getDragonType() {
        return DragonTypes.CAVE;
    }

    @Override
    public ResourceLocation[] getSkillTextures() {
        return new ResourceLocation[]{
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/cave/cave_athletics_0.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/cave/cave_athletics_1.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/cave/cave_athletics_2.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/cave/cave_athletics_3.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/cave/cave_athletics_4.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/cave/cave_athletics_5.png")
        };
    }

    @Override
    @SuppressWarnings("RedundantMethodOverride")
    public int getMaxLevel() {
        return 5;
    }

    @Override
    @SuppressWarnings("RedundantMethodOverride")
    public int getMinLevel() {
        return 0;
    }

    @Override
    public boolean isDisabled() {
        return super.isDisabled() || !caveAthletics;
    }
}