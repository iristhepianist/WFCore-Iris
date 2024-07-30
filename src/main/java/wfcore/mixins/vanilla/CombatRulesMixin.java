package wfcore.mixins.vanilla;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.util.CombatRules;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CombatRules.class)
public abstract class CombatRulesMixin {

    // here I just copied over the entire method, hope it won't cause issues.
    @ModifyReturnValue(method = "getDamageAfterAbsorb", at = @At(value = "RETURN"))
    private static float getDamageAfterAbsorb(float damage, float totalArmor, float toughnessAttribute) {
        float f = 2.0F + toughnessAttribute / 4.0F;
        float f1 = MathHelper.clamp(totalArmor - damage / f, totalArmor * 0.2F, 25.0F);
        return damage * (1.0F - f1 / 25.0F);
    }
}
