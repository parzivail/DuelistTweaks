package com.parzivail.duelist_tweaks.helper;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.parzivail.duelist_tweaks.DTweaks;
import com.parzivail.duelist_tweaks.compression.CompressedBlockData;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

public class RecipeHelper
{
	public static void apply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci, Map<RecipeType<?>, ImmutableMap.Builder<Identifier, Recipe<?>>> recipes)
	{
		for (CompressedBlockData data : DTweaks.COMPRESSION_MAP.values())
			data.registerRecipes(recipes);
	}
}
