package by.dragonsurvivalteam.dragonsurvival.registry.dragon.datapacks;

import by.dragonsurvivalteam.dragonsurvival.common.codecs.Condition;
import by.dragonsurvivalteam.dragonsurvival.common.codecs.MiscCodecs;
import by.dragonsurvivalteam.dragonsurvival.common.codecs.Modifier;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.registry.DSAttributes;
import by.dragonsurvivalteam.dragonsurvival.registry.DSItems;
import by.dragonsurvivalteam.dragonsurvival.registry.datagen.Translation;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.DragonStage;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.DragonStages;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;
import java.util.Optional;

import static by.dragonsurvivalteam.dragonsurvival.common.codecs.ModifierType.DRAGON_STAGE;

public class AncientDatapack {
    @Translation(type = Translation.Type.STAGE, comments = "Ancient")
    public static ResourceKey<DragonStage> ancient = DragonStages.key("ancient");

    public static void register(final BootstrapContext<DragonStage> context) {
        context.register(ancient, ancient());
        context.register(DragonStages.adult, DragonStages.adult(ancient));
    }

    public static DragonStage ancient() {
        return new DragonStage(
                new MiscCodecs.Bounds(60, 300),
                Functions.daysToTicks(40),
                Optional.empty(),
                List.of(
                        /* Constant */
                        Modifier.constant(DRAGON_STAGE, Attributes.SUBMERGED_MINING_SPEED, 3, AttributeModifier.Operation.ADD_VALUE, DragonTypes.SEA.getTypeNameLowerCase()),
                        Modifier.constant(DRAGON_STAGE, Attributes.STEP_HEIGHT, 0.5f, AttributeModifier.Operation.ADD_VALUE),
                        Modifier.constant(DRAGON_STAGE, Attributes.ATTACK_DAMAGE, 3, AttributeModifier.Operation.ADD_VALUE),
                        Modifier.constant(DRAGON_STAGE, Attributes.JUMP_STRENGTH, 0.1f, AttributeModifier.Operation.ADD_VALUE),
                        Modifier.constant(DRAGON_STAGE, Attributes.SAFE_FALL_DISTANCE, 1, AttributeModifier.Operation.ADD_VALUE),
                        Modifier.constant(DRAGON_STAGE, DSAttributes.DRAGON_BREATH_RANGE, 4, AttributeModifier.Operation.ADD_VALUE),
                        /* Per size */
                        Modifier.perSize(DRAGON_STAGE, Attributes.ENTITY_INTERACTION_RANGE, 0.01f, AttributeModifier.Operation.ADD_VALUE),
                        Modifier.perSize(DRAGON_STAGE, Attributes.BLOCK_INTERACTION_RANGE, 0.01f, AttributeModifier.Operation.ADD_VALUE),
                        Modifier.perSize(DRAGON_STAGE, Attributes.MAX_HEALTH, 0.5f, AttributeModifier.Operation.ADD_VALUE),
                        Modifier.perSize(DRAGON_STAGE, DSAttributes.DRAGON_BREATH_RANGE, 0.05f, AttributeModifier.Operation.ADD_VALUE),
                        Modifier.perSize(DRAGON_STAGE, Attributes.STEP_HEIGHT, 0.015f, AttributeModifier.Operation.ADD_VALUE),
                        Modifier.perSize(DRAGON_STAGE, Attributes.ATTACK_DAMAGE, 0.05f, AttributeModifier.Operation.ADD_VALUE),
                        Modifier.perSize(DRAGON_STAGE, Attributes.JUMP_STRENGTH, 0.0015f, AttributeModifier.Operation.ADD_VALUE),
                        Modifier.perSize(DRAGON_STAGE, Attributes.SAFE_FALL_DISTANCE, 0.015f, AttributeModifier.Operation.ADD_VALUE),
                        Modifier.perSize(DRAGON_STAGE, DSAttributes.BLOCK_BREAK_RADIUS, 0.01f, AttributeModifier.Operation.ADD_VALUE)
                        ),
                1,
                3,
                List.of(
                        MiscCodecs.GrowthItem.create(Functions.hoursToTicks(1), DSItems.ELDER_DRAGON_HEART.value()),
                        MiscCodecs.GrowthItem.create(Functions.hoursToTicks(-1), DSItems.STAR_BONE.value())
                ),
                Optional.of(Condition.defaultNaturalGrowthBlocker()),
                Optional.empty(),
                Optional.of(new MiscCodecs.DestructionData(120, 120, 0.05))
        );
    }
}
