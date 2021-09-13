package com.parzivail.datagen.tarkin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ModelFile
{
	private final Identifier filename;
	private final Identifier parent;
	private final HashMap<String, Identifier> textures;

	private ModelFile(Identifier filename)
	{
		this(filename, null);
	}

	private ModelFile(Identifier filename, Identifier parent)
	{
		this.filename = filename;
		this.parent = parent;
		this.textures = new HashMap<>();
	}

	public Identifier getId()
	{
		return filename;
	}

	public static ModelFile ofBlock(Block block)
	{
		return new ModelFile(AssetGenerator.getRegistryName(block), AssetGenerator.getTextureName(block));
	}

	public static ModelFile ofBlockDifferentParent(Block block, Identifier parent)
	{
		return new ModelFile(AssetGenerator.getRegistryName(block), parent);
	}

	public static ModelFile noParent(Identifier filename)
	{
		return new ModelFile(filename);
	}

	public static ModelFile ofModel(Identifier filename, Identifier source)
	{
		return new ModelFile(filename, source);
	}

	public static ModelFile cube(Block block)
	{
		return ModelFile
				.ofModel(AssetGenerator.getRegistryName(block), new Identifier("block/cube_all"))
				.texture("all", AssetGenerator.getTextureName(block));
	}

	public static ModelFile item(Block block)
	{
		return ModelFile
				.ofModel(AssetGenerator.getRegistryName(block), new Identifier("item/generated"))
				.texture("layer0", AssetGenerator.getTextureName(block));
	}

	public static ModelFile item(Block block, Identifier textureName)
	{
		return ModelFile
				.ofModel(AssetGenerator.getRegistryName(block), new Identifier("item/generated"))
				.texture("layer0", textureName);
	}

	public static ModelFile particle(Block block, Identifier particle)
	{
		return ModelFile
				.noParent(AssetGenerator.getRegistryName(block))
				.texture("particle", particle);
	}

	public static ModelFile item(Item item)
	{
		return ModelFile
				.ofModel(AssetGenerator.getRegistryName(item), new Identifier("item/generated"))
				.texture("layer0", AssetGenerator.getTextureName(item));
	}

	public static ModelFile handheld_item(Item item)
	{
		return ModelFile
				.ofModel(AssetGenerator.getRegistryName(item), new Identifier("item/handheld"))
				.texture("layer0", AssetGenerator.getTextureName(item));
	}

	public static ModelFile spawn_egg(Item item)
	{
		return ModelFile
				.ofModel(AssetGenerator.getRegistryName(item), new Identifier("item/template_spawn_egg"));
	}

	public static ModelFile empty(Item item)
	{
		return ModelFile
				.ofModel(AssetGenerator.getRegistryName(item), new Identifier("builtin/generated"));
	}

	public ModelFile texture(String key, Identifier value)
	{
		textures.put(key, value);
		return this;
	}

	public JsonElement build()
	{
		JsonObject rootElement = new JsonObject();

		if (parent != null)
			rootElement.addProperty("parent", parent.toString());

		if (!textures.isEmpty())
		{
			JsonObject textureElement = new JsonObject();

			for (Map.Entry<String, Identifier> entry : textures.entrySet())
				textureElement.addProperty(entry.getKey(), entry.getValue().toString());

			rootElement.add("textures", textureElement);
		}

		return rootElement;
	}
}
