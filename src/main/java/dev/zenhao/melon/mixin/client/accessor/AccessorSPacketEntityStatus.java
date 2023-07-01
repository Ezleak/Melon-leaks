package dev.zenhao.melon.mixin.client.accessor;

import net.minecraft.network.play.server.SPacketEntityStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPacketEntityStatus.class)
public interface AccessorSPacketEntityStatus {
    @Accessor("entityId")
    int melonGetEntityID();
}