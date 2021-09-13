package com.parzivail.duelist_tweaks.compression;

import com.google.common.collect.ImmutableMap;
import com.parzivail.duelist_tweaks.DTweaks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.FallingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class CompressedBlockData
{
	private final Block source;
	private final Block[] compressedVariants;

	public CompressedBlockData(Block source, int levels)
	{
		this.source = source;
		this.compressedVariants = new Block[levels];

		for (int i = 0; i < compressedVariants.length; i++)
		{
			FabricBlockSettings s = FabricBlockSettings.copyOf(source);
			AbstractBlockSettingsAccessor sA = (AbstractBlockSettingsAccessor)s;
			s = s.hardness(sA.getHardness() + 5 * (i + 1));
			s = s.resistance(sA.getResistance() + 75 * (i + 1));

			if (source instanceof FallingBlock)
				compressedVariants[i] = new CompressedFallingBlock(s, i);
			else
				compressedVariants[i] = new CompressedBlock(s, i);
		}
	}

	public Block getCompressionLevel(int level)
	{
		if (level == -1)
			return source;

		if (level >= compressedVariants.length || level < 0)
			return null;

		return compressedVariants[level];
	}

	public void registerCompressionLevels()
	{
		Identifier sourceId = Registry.BLOCK.getId(source);

		for (int i = 0; i < compressedVariants.length; i++)
		{
			Identifier id = DTweaks.id(String.format("compressed%s_%s_%s", i, sourceId.getNamespace(), sourceId.getPath()));

			Registry.register(Registry.BLOCK, id, compressedVariants[i]);
			Registry.register(Registry.ITEM, id, new BlockItem(compressedVariants[i], new FabricItemSettings().group(DTweaks.GROUP)));
		}
	}

	public void registerRecipes(Map<RecipeType<?>, ImmutableMap.Builder<Identifier, Recipe<?>>> recipes)
	{
		for (int i = 0; i < compressedVariants.length; i++)
		{
			Block previousBlock = getCompressionLevel(i - 1);
			Identifier previousBlockId = Registry.BLOCK.getId(previousBlock);
			Block thisBlock = getCompressionLevel(i);

			DefaultedList<Ingredient> ingredients = DefaultedList.copyOf(
					Ingredient.EMPTY,
					Ingredient.ofItems(previousBlock), Ingredient.ofItems(previousBlock), Ingredient.ofItems(previousBlock),
					Ingredient.ofItems(previousBlock), Ingredient.ofItems(previousBlock), Ingredient.ofItems(previousBlock),
					Ingredient.ofItems(previousBlock), Ingredient.ofItems(previousBlock), Ingredient.ofItems(previousBlock)
			);
			Recipe<?> recipe = new ShapedRecipe(DTweaks.id(String.format("compress_%s_%s", previousBlockId.getNamespace(), previousBlockId.getPath())), "", 3, 3, ingredients, new ItemStack(thisBlock));

			recipes.computeIfAbsent(recipe.getType(), (recipeType) -> ImmutableMap.builder())
			       .put(recipe.getId(), recipe);
		}
	}

	public Block[] getBlocks()
	{
		return compressedVariants;
	}

	public Block getSource()
	{
		return source;
	}
}
