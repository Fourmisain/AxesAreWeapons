package fourmisain.axesareweapons.mixin;

import fourmisain.axesareweapons.AxesAreWeapons;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

import static fourmisain.axesareweapons.AxesAreWeapons.CONFIG;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "getPossibleEntries", at = @At("RETURN"))
	private static void getPossibleEntries(int power, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
		List<EnchantmentLevelEntry> entries = cir.getReturnValue();

		if (stack.getItem() instanceof AxeItem) {
			if (CONFIG.enableDamageInEnchantingTable) {
				for (Enchantment enchantment : Arrays.asList(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS)) {
					addEntry(entries, power, enchantment);
				}
			}

			if (CONFIG.enableForEnchantingTable) {
				if (CONFIG.enableLooting)    addEntry(entries, power, Enchantments.LOOTING);
				if (CONFIG.enableKnockback)  addEntry(entries, power, Enchantments.KNOCKBACK);
				if (CONFIG.enableFireAspect) addEntry(entries, power, Enchantments.FIRE_ASPECT);
			}
		}
	}

	@Unique
	private static void addEntry(List<EnchantmentLevelEntry> entries, int power, Enchantment enchantment) {
		// add appropriate enchantment level for the given power
		for (int level = enchantment.getMaxLevel(); level >= enchantment.getMinLevel(); level--) {
			if (enchantment.getMinPower(level) <= power && power <= enchantment.getMaxPower(level)) {
				entries.add(new EnchantmentLevelEntry(enchantment, level));
				break;
			}
		}
	}
}
