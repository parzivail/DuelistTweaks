package com.parzivail.duelist_tweaks;

import com.parzivail.duelist_tweaks.compression.CompressedBlockData;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class DTweaks implements ModInitializer
{
	public static final String MODID = "duelist_tweaks";

	public static final ItemGroup GROUP = FabricItemGroupBuilder.create(id("group"))
	                                                            .icon(() -> new ItemStack(Items.ENDER_EYE))
	                                                            .build();

	public static HashMap<Block, CompressedBlockData> COMPRESSION_MAP = new HashMap<>();

	@Override
	public void onInitialize()
	{
		registerCompressible(Blocks.COBBLESTONE, 9);
		registerCompressible(Blocks.DIRT, 9);
		registerCompressible(Blocks.GRANITE, 9);
		registerCompressible(Blocks.ANDESITE, 9);
		registerCompressible(Blocks.DIORITE, 9);
		registerCompressible(Blocks.NETHERRACK, 9);
		registerCompressible(Blocks.SOUL_SAND, 9);
		registerCompressible(Blocks.SAND, 9);
		registerCompressible(Blocks.GRAVEL, 9);

		for (CompressedBlockData data : COMPRESSION_MAP.values())
			data.registerCompressionLevels();

		//		try
		//		{
		//			Tarkin.main("");
		//		}
		//		catch (Exception e)
		//		{
		//			e.printStackTrace();
		//		}
	}

	private void registerCompressible(Block block, int levels)
	{
		COMPRESSION_MAP.put(block, new CompressedBlockData(block, levels));
	}

	public static Identifier id(String value)
	{
		return new Identifier(MODID, value);
	}
}
