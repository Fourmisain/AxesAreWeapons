package io.github.fourmisain.axesareweapons.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

import static io.github.fourmisain.axesareweapons.common.AxesAreWeaponsCommon.isSpeedyWeb;

@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin extends MiningToolItem {
	protected ShovelItemMixin(float attackDamage, float attackSpeed, ToolMaterial material, Set<Block> effectiveBlocks, Settings settings) {
		super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
	}

	@ModifyReturnValue(method = "isSuitableFor", at = @At("RETURN"))
	public boolean axesareweapons$cobWebsAreSuitable(boolean original, @Local(argsOnly = true) BlockState state) {
		return original || isSpeedyWeb(this, state);
	}
}
