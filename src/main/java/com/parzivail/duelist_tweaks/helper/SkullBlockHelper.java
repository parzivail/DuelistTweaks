package com.parzivail.duelist_tweaks.helper;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SkullBlockHelper
{
	public static ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		ItemStack itemStack = player.getStackInHand(hand);
		if (itemStack.getItem() == Items.NAME_TAG)
		{
			if (!world.isClient)
			{
				BlockEntity be = world.getBlockEntity(pos);
				if (be instanceof SkullBlockEntity)
				{
					SkullBlockEntity sbe = (SkullBlockEntity)be;

					CompoundTag nbt = new CompoundTag();
					nbt.putString("Name", itemStack.getName().asString());

					sbe.setOwnerAndType(NbtHelper.toGameProfile(nbt));
					world.updateListeners(pos, state, state, 4);

					if (!player.abilities.creativeMode)
					{
						itemStack.decrement(1);
					}
				}
			}

			return ActionResult.success(world.isClient);
		}
		else
		{
			return ActionResult.PASS;
		}
	}
}
