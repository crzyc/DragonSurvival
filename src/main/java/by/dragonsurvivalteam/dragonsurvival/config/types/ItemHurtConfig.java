package by.dragonsurvivalteam.dragonsurvival.config.types;

import by.dragonsurvivalteam.dragonsurvival.config.ConfigUtils;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class ItemHurtConfig implements CustomConfig {
    private static final int DAMAGE = 2;

    private final Predicate<ItemStack> predicate;
    private final float damage;
    private final String originalData;

    private ItemHurtConfig(final Predicate<ItemStack> predicate, float damage, final String originalData) {
        this.predicate = predicate;
        this.damage = damage;
        this.originalData = originalData;
    }

    @Override
    public String convert() {
        return originalData;
    }

    @Override
    public String toString() {
        return convert();
    }

    @Override
    public boolean equals(final Object object) {
        return super.equals(object) || object instanceof ItemHurtConfig config && config.convert().equals(convert());
    }

    public float getDamage(final ItemStack stack) {
        return predicate.test(stack) ? damage : 0;
    }

    public static ItemHurtConfig of(final String location, int damage) {
        Predicate<ItemStack> predicate = ConfigUtils.itemStackPredicate(location.split(":"));
        return new ItemHurtConfig(predicate, damage, location + ":" + damage);
    }

    public static ItemHurtConfig of(final String data) {
        String[] splitData = data.split(":");
        Predicate<ItemStack> predicate = ConfigUtils.itemStackPredicate(splitData);
        float damage = Float.parseFloat(splitData[DAMAGE]);
        return new ItemHurtConfig(predicate, damage, data);
    }

    public static boolean validate(final String data) {
        String[] splitData = data.split(":");

        if (!ConfigUtils.validateResourceLocation(splitData)) {
            return false;
        }

        return ConfigUtils.validateFloat(splitData[DAMAGE]);
    }
}
