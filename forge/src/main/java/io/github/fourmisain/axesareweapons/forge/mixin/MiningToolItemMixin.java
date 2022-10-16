package io.github.fourmisain.axesareweapons.forge.mixin;

import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MiningToolItem.class, priority = 1010)
public abstract class MiningToolItemMixin extends ToolItem implements Vanishable {
	public MiningToolItemMixin(ToolMaterial toolMaterial, Settings settings) {
		super(toolMaterial, settings);
	}

//	@Inject(method = "isCorrectToolForDrops", at = @At("HEAD"), cancellable = true)
//	public void cobWebsAreSuitable(ItemStack stack, BlockState state, CallbackInfoReturnable<Boolean> cir) {
//		overrideCobWebSuitableness(this, state, cir);
//	}
}
