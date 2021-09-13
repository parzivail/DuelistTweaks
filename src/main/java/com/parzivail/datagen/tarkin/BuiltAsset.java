package com.parzivail.datagen.tarkin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.parzivail.duelist_tweaks.DTweaks;
import net.minecraft.util.Identifier;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class BuiltAsset
{
	static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

	protected final Path file;
	protected final JsonElement contents;

	protected BuiltAsset(Path file, JsonElement contents)
	{
		this.file = file;
		this.contents = contents;
	}

	private static Path getResourcePath(String slug, Identifier identifier)
	{
		return AssetGenerator.getRootDir().resolve(Paths.get(slug, identifier.getNamespace(), identifier.getPath()));
	}

	private static Path getAssetPath(Identifier identifier)
	{
		return getResourcePath("assets", identifier);
	}

	private static Path getDataPath(Identifier identifier)
	{
		return getResourcePath("data", identifier);
	}

	private static Path getBlockstatePath(Identifier identifier)
	{
		return getAssetPath(IdentifierUtil.concat("blockstates/", identifier, ".json"));
	}

	private static Path getBlockModelPath(Identifier identifier)
	{
		return getAssetPath(IdentifierUtil.concat("models/block/", identifier, ".json"));
	}

	private static Path getItemModelPath(Identifier identifier)
	{
		return getAssetPath(IdentifierUtil.concat("models/item/", identifier, ".json"));
	}

	private static Path getLangPath(Identifier identifier)
	{
		// /assets/minecraft/lang/en_us.json
		return getAssetPath(IdentifierUtil.concat("lang/", identifier, ".json"));
	}

	private static Path getLootTablePath(Identifier identifier)
	{
		return getDataPath(IdentifierUtil.concat("loot_tables/", identifier, ".json"));
	}

	private static Path getRecipePath(Identifier identifier)
	{
		return getDataPath(IdentifierUtil.concat("recipes/tarkin/", identifier, ".json"));
	}

	public static BuiltAsset blockstate(Identifier identifier, JsonElement contents)
	{
		return new BuiltAsset(getBlockstatePath(identifier), contents);
	}

	public static BuiltAsset blockModel(Identifier identifier, JsonElement contents)
	{
		return new BuiltAsset(getBlockModelPath(identifier), contents);
	}

	public static BuiltAsset itemModel(Identifier identifier, JsonElement contents)
	{
		return new BuiltAsset(getItemModelPath(identifier), contents);
	}

	public static BuiltAsset lang(Identifier identifier, JsonElement contents)
	{
		return new JsonObjKeyInsBuiltAsset(getLangPath(identifier), contents);
	}

	public static BuiltAsset lootTable(Identifier identifier, JsonElement contents)
	{
		return new BuiltAsset(getLootTablePath(identifier), contents);
	}

	public static BuiltAsset recipe(Identifier identifier, JsonElement contents)
	{
		return new BuiltAsset(getRecipePath(identifier), contents);
	}

	public static void nukeRecipeDir() throws IOException
	{
		Path dummyAsset = getRecipePath(DTweaks.id("dummy"));

		Path parentDir = dummyAsset.getParent();

		if (!Files.exists(parentDir))
			return;

		FileUtils.cleanDirectory(parentDir.toFile());
	}

	public static void nukeBlockModelJsons() throws IOException
	{
		Path dummyAsset = getBlockModelPath(DTweaks.id("dummy"));

		Path parentDir = dummyAsset.getParent();

		if (!Files.exists(parentDir))
			return;

		cleanDirectoryOf(parentDir.toFile(), "json");
	}

	public static void nukeItemModelJsons() throws IOException
	{
		Path dummyAsset = getItemModelPath(DTweaks.id("dummy"));

		Path parentDir = dummyAsset.getParent();

		if (!Files.exists(parentDir))
			return;

		cleanDirectoryOf(parentDir.toFile(), "json");
	}

	public static void nukeBlockLootTables() throws IOException
	{
		Path dummyAsset = getLootTablePath(DTweaks.id("blocks/dummy"));

		Path parentDir = dummyAsset.getParent();

		if (!Files.exists(parentDir))
			return;

		cleanDirectoryOf(parentDir.toFile(), "json");
	}

	public static void nukeBlockstateDir() throws IOException
	{
		Path dummyAsset = getBlockstatePath(DTweaks.id("dummy"));

		Path parentDir = dummyAsset.getParent();

		if (!Files.exists(parentDir))
			return;

		FileUtils.cleanDirectory(parentDir.toFile());
	}

	public static void cleanDirectoryOf(final File directory, String extension) throws IOException
	{
		final File[] files = directory.listFiles();

		IOException exception = null;
		for (final File file : files)
		{
			if (FilenameUtils.isExtension(file.getName(), extension))
				FileUtils.forceDelete(file);
		}
	}

	public static void mergeLanguageKeys(Identifier localeKeySource, Identifier locateDestination) throws IOException
	{
		updateLanguageKeys(localeKeySource, locateDestination);

		Path pathKeySource = getLangPath(localeKeySource);
		Files.delete(pathKeySource);
	}

	public static void updateLanguageKeys(Identifier localeKeySource, Identifier locateDestination) throws IOException
	{
		Path pathKeySource = getLangPath(localeKeySource);
		Path pathDest = getLangPath(locateDestination);

		JsonObject keySource;
		try (Reader reader = Files.newBufferedReader(pathKeySource, StandardCharsets.UTF_8))
		{
			keySource = GSON.fromJson(reader, JsonObject.class);
		}

		JsonObject valueSource;
		try (Reader reader = Files.newBufferedReader(pathDest, StandardCharsets.UTF_8))
		{
			valueSource = GSON.fromJson(reader, JsonObject.class);
		}

		for (Map.Entry<String, JsonElement> entry : valueSource.entrySet())
		{
			JsonElement newMember = keySource.get(entry.getKey());
			if (newMember != null)
			{
				String oldValue = entry.getValue().getAsString();
				keySource.addProperty(entry.getKey(), oldValue);
			}
		}

		try (
				Writer writer = Files.newBufferedWriter(pathDest, StandardCharsets.UTF_8);
				JsonWriter jsonWriter = new JsonWriter(writer)
		)
		{
			jsonWriter.setIndent("\t");
			GSON.toJson(keySource, jsonWriter);
		}
	}

	public void write()
	{
		try
		{
			Files.createDirectories(file.getParent());

			try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8))
			{
				GSON.toJson(contents, writer);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public String getFilename()
	{
		return file.toAbsolutePath().toString();
	}
}
