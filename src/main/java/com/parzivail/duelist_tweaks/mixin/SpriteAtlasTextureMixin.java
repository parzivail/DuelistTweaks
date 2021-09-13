package com.parzivail.duelist_tweaks.mixin;

import com.parzivail.duelist_tweaks.DTweaks;
import com.parzivail.pswg.client.sprite.LayeredSpriteBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.io.IOException;

@Mixin(SpriteAtlasTexture.class)
@Environment(EnvType.CLIENT)
public abstract class SpriteAtlasTextureMixin
{
	@Shadow
	protected abstract Identifier getTexturePath(Identifier identifier);

	@ModifyVariable(method = "loadSprite", at = @At(value = "NEW", target = "net/minecraft/client/texture/Sprite"), ordinal = 0)
	public NativeImage spriteAddBaseLayer(NativeImage nativeImage, ResourceManager container, Sprite.Info info, int atlasWidth, int atlasHeight, int maxLevel, int x, int y) throws IOException
	{
		Identifier identifier = getTexturePath(info.getId());

		if (identifier.getNamespace().equals(DTweaks.MODID) && identifier.getPath().startsWith("textures/block/compressed"))
			return LayeredSpriteBuilder.build(nativeImage, identifier, container, this::getTexturePath);

		return nativeImage;
	}
}
