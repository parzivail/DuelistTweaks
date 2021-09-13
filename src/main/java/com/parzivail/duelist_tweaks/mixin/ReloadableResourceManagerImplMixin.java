package com.parzivail.duelist_tweaks.mixin;

import com.parzivail.duelist_tweaks.DTweaks;
import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

@Mixin(ReloadableResourceManagerImpl.class)
public class ReloadableResourceManagerImplMixin
{
	@Shadow
	@Final
	private Map<String, NamespaceResourceManager> namespaceManagers;

	@Inject(method = "getResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;", at = @At("HEAD"), cancellable = true)
	private void getResource(Identifier id, CallbackInfoReturnable<Resource> cir) throws IOException
	{
		if (!id.getNamespace().equals(DTweaks.MODID) || !id.getPath().startsWith("textures/block/compressed"))
			return;

		String[] parts = id.getPath().replace("textures/block/compressed", "").split("_", 3);
		id = new Identifier(parts[1], String.format("textures/block/%s", parts[2]));

		ResourceManager resourceManager = namespaceManagers.get(id.getNamespace());
		if (resourceManager != null)
		{
			cir.setReturnValue(resourceManager.getResource(id));
		}
		else
		{
			throw new FileNotFoundException(id.toString());
		}
	}
}
