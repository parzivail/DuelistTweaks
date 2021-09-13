package com.parzivail.datagen.tarkin;

import net.minecraft.block.Block;
import net.minecraft.data.client.model.BlockStateSupplier;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class BlockGenerator
{
	public static BlockGenerator basic(Block block)
	{
		return block(block)
				.lootTable(LootTableFile::singleSelf);
	}

	public static BlockGenerator blockNoModelLangEntry(Block block)
	{
		return new BlockGenerator(block)
				.lang(LanguageProvider::ofBlock);
	}

	public static BlockGenerator block(Block block)
	{
		return blockNoModelLangEntry(block)
				.state(BlockStateGenerator::basic)
				.model(ModelFile::cube)
				.itemModel(ModelFile::ofBlock);
	}

	@FunctionalInterface
	public interface BlockStateSupplierFunc
	{
		BlockStateSupplier apply(Block block, Identifier modelId);
	}

	private final Block block;

	private BlockStateSupplier stateSupplier;
	private ModelFile itemModel;
	private LanguageProvider languageProvider;

	private final Collection<ModelFile> blockModels;
	private final Collection<LootTableFile> lootTables;

	BlockGenerator(Block block)
	{
		this.block = block;

		this.blockModels = new ArrayList<>();
		this.lootTables = new ArrayList<>();
	}

	public Block getBlock()
	{
		return block;
	}

	private Identifier getRegistryName()
	{
		return Registry.BLOCK.getId(block);
	}

	public void build(List<BuiltAsset> assets)
	{
		// blockstate
		if (stateSupplier != null)
			assets.add(BuiltAsset.blockstate(getRegistryName(), stateSupplier.get()));

		if (languageProvider != null)
			assets.add(languageProvider.build());

		// models
		blockModels.forEach(modelFile -> assets.add(BuiltAsset.blockModel(modelFile.getId(), modelFile.build())));

		if (itemModel != null)
			assets.add(BuiltAsset.itemModel(itemModel.getId(), itemModel.build()));

		// loot tables
		lootTables.forEach(lootTableFile -> assets.add(BuiltAsset.lootTable(lootTableFile.filename, lootTableFile.build())));
	}

	public BlockGenerator lang(Function<Block, LanguageProvider> languageFunc)
	{
		languageProvider = languageFunc.apply(block);
		return this;
	}

	public BlockGenerator state(BlockStateSupplierFunc stateSupplierFunc, Identifier modelId)
	{
		this.stateSupplier = stateSupplierFunc.apply(block, modelId);
		return this;
	}

	public BlockGenerator state(BlockStateSupplierFunc stateSupplierFunc)
	{
		return state(stateSupplierFunc, AssetGenerator.getTextureName(block));
	}

	public BlockGenerator models(Function<Block, Collection<ModelFile>> modelFunc)
	{
		this.blockModels.addAll(modelFunc.apply(block));
		return this;
	}

	public BlockGenerator model(Function<Block, ModelFile> modelFunc)
	{
		this.blockModels.add(modelFunc.apply(block));
		return this;
	}

	public BlockGenerator itemModel(Function<Block, ModelFile> modelFunc)
	{
		this.itemModel = modelFunc.apply(block);
		return this;
	}

	public BlockGenerator lootTable(Function<Block, LootTableFile> lootTableFunc)
	{
		this.lootTables.add(lootTableFunc.apply(block));
		return this;
	}
}
