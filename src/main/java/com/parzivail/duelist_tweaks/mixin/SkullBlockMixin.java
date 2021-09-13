package com.parzivail.duelist_tweaks.mixin;

import com.parzivail.duelist_tweaks.helper.SkullBlockHelper;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SkullBlock.class)
public class SkullBlockMixin extends AbstractSkullBlock
{
	public SkullBlockMixin(SkullBlock.SkullType type, Settings settings)
	{
		super(type, settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		return SkullBlockHelper.onUse(state, world, pos, player, hand, hit);
	}
}
