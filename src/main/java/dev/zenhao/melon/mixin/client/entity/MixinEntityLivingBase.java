package dev.zenhao.melon.mixin.client.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = {EntityLivingBase.class}, priority = Integer.MAX_VALUE)
public class MixinEntityLivingBase {
}
