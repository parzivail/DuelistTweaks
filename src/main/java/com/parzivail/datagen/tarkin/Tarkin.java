package com.parzivail.datagen.tarkin;

import com.parzivail.duelist_tweaks.DTweaks;
import com.parzivail.duelist_tweaks.compression.CompressedBlockData;
import net.minecraft.block.Block;
import net.minecraft.client.resource.language.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * T.A.R.K.I.N. - Text Asset Record-Keeping, Integration, and Normalization
 */
public class Tarkin
{
	public static void main(String arg) throws Exception
	{
		List<BuiltAsset> assets = new ArrayList<>();

		generateBlocks(assets);
		generateItems(assets);

		generateLangEntries(assets);

		BuiltAsset.nukeRecipeDir();
		BuiltAsset.nukeBlockstateDir();
		BuiltAsset.nukeBlockModelJsons();
		BuiltAsset.nukeItemModelJsons();
		BuiltAsset.nukeBlockLootTables();

		for (BuiltAsset asset : assets)
		{
			asset.write();
		}

		// Synchronize the keys of the en_us locale
		BuiltAsset.mergeLanguageKeys(DTweaks.id("en_us_temp"), DTweaks.id("en_us"));
	}

	private static void generateLangEntries(List<BuiltAsset> assets)
	{
		LanguageBuilder lang = new LanguageBuilder(DTweaks.id(LanguageProvider.OUTPUT_LOCALE));

		lang.cloneWithRoot("tooltip").modid().dot("compressed_value").build(assets);

		lang.build(assets);
	}

	private static void generateItems(List<BuiltAsset> assets)
	{
		//		ItemGenerator.tool(SwgItems.Axe.Durasteel).build(assets);
	}

	private static void generateBlocks(List<BuiltAsset> assets)
	{
		for (CompressedBlockData data : DTweaks.COMPRESSION_MAP.values())
		{
			Block[] blocks = data.getBlocks();
			for (int i = 0; i < blocks.length; i++)
			{
				Block value = blocks[i];
				int finalI = i;
				BlockGenerator.basic(value).lang(block -> LanguageProvider.ofBlock(block, getEnglishName(data.getSource(), finalI))).build(assets);
			}
		}
	}

	private static final String[] TUPLES = new String[] {
			"Double",
			"Triple",
			"Quadruple",
			"Quintuple",
			"Sextuple",
			"Septuple",
			"Octuple",
			"Nonuple",
			"Decuple",
			"Undecuple",
			"Duodecuple",
			"Tredecuple",
			"Quattuordecuple",
			"Quindecuple",
			"Sexdecuple",
			"Septendecuple",
			"Octodecuple",
			"Novemdecuple",
			"Vigintuple"
	};

	private static String getEnglishName(Block block, int i)
	{
		return String.format("%s-Compressed %s", TUPLES[i], I18n.translate(block.getTranslationKey()));
	}
}
