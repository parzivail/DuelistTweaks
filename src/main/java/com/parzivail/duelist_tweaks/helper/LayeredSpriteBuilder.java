package com.parzivail.pswg.client.sprite;

import com.parzivail.duelist_tweaks.DTweaks;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.io.IOException;
import java.util.function.Function;

public class LayeredSpriteBuilder
{
	public static NativeImage build(NativeImage nativeImage, Identifier identifier, ResourceManager resourceManager, Function<Identifier, Identifier> texturePathResolver) throws IOException
	{
		String[] parts = identifier.getPath().replace("textures/block/compressed", "").split("_", 3);
		int layers = Integer.parseInt(parts[0]) + 1;
		NativeImage baseLayerImage = NativeImage.read(resourceManager.getResource(identifier).getInputStream());

		Identifier overlayLayerIdentifier = texturePathResolver.apply(DTweaks.id("block/compression_overlay"));
		NativeImage overlayLayerImage = NativeImage.read(resourceManager.getResource(overlayLayerIdentifier).getInputStream());

		int width = nativeImage.getWidth();
		int height = nativeImage.getHeight();

		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				for (int layer = 0; layer < layers; layer++)
					baseLayerImage.setPixelColor(x, y, blendColorsOnSrcAlpha(baseLayerImage.getPixelColor(x, y), overlayLayerImage.getPixelColor(x, y)));

		return baseLayerImage;
	}

	public static int blendColorsOnSrcAlpha(int dest, int src)
	{
		float destA = NativeImage.getAlpha(dest) / 255f;
		int destR = NativeImage.getRed(dest);
		int destG = NativeImage.getGreen(dest);
		int destB = NativeImage.getBlue(dest);

		float srcA = NativeImage.getAlpha(src) / 255f;
		int srcR = NativeImage.getRed(src);
		int srcG = NativeImage.getGreen(src);
		int srcB = NativeImage.getBlue(src);

		int a = MathHelper.clamp((int)((destA + srcA) * 255f), 0, 255);
		int r = MathHelper.clamp((int)((1 - srcA) * destR + srcA * srcR), 0, 255);
		int g = MathHelper.clamp((int)((1 - srcA) * destG + srcA * srcG), 0, 255);
		int b = MathHelper.clamp((int)((1 - srcA) * destB + srcA * srcB), 0, 255);

		return NativeImage.getAbgrColor(a, b, g, r);
	}
}
