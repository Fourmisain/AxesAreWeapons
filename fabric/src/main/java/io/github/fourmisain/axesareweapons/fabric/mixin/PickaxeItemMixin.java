package io.github.fourmisain.axesareweapons.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.isSpeedyWeb;

@Mixin(PickaxeItem.class)
public abstract class PickaxeItemMixin extends MiningToolItem {
	protected PickaxeItemMixin(float attackDamage, float attackSpeed, ToolMaterial material, Set<Block> effectiveBlocks, Settings settings) {
		super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
	}

	@ModifyReturnValue(method = "getMiningSpeedMultiplier", at = @At("RETURN"))
	public float axesareweapons$cobWebsAreSpeed(float miningSpeed, @Local(argsOnly = true) BlockState state) {
		if (isSpeedyWeb(this, state)) {
			return Math.max(miningSpeed, 15f);
		}

		return miningSpeed;
	}

	@ModifyReturnValue(method = "isSuitableFor", at = @At("RETURN"))
	public boolean axesareweapons$cobWebsAreSuitable(boolean original, @Local(argsOnly = true) BlockState state) {
		return original || isSpeedyWeb(this, state);
	}
}
