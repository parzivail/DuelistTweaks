package com.parzivail.duelist_tweaks.compression;

import com.parzivail.duelist_tweaks.DTweaks;
import net.minecraft.block.FallingBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CompressedFallingBlock extends FallingBlock
{
	private final int level;

	public CompressedFallingBlock(Settings blockSettings, int level)
	{
		super(blockSettings);
		this.level = level;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options)
	{
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText(String.format("tooltip.%s.compressed_value", DTweaks.MODID), String.format("%,d", (int)Math.pow(9, level + 1))));
	}
}
