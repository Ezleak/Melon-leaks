package dev.zenhao.melon.mixin.client.render;

import dev.zenhao.melon.event.events.render.RenderEnderChestEvent;
import dev.zenhao.melon.module.modules.render.NoRender;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityEnderChestRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {TileEntityEnderChestRenderer.class})
public class MixinTileEntityEnderChestRenderer {

    @Inject(method = {"render*"}, at = @At(value = "INVOKE"))
    private void renderEnchantingTableBook(TileEntityEnderChest te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, CallbackInfo ci) {
        RenderEnderChestEvent event = new RenderEnderChestEvent();
        IBlockState blockState = Blocks.SNOW_LAYER.defaultBlockState.withProperty(BlockSnow.LAYERS, 8);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            Minecraft.getMinecraft().world.setBlockState(te.getPos(), blockState);
            Minecraft.getMinecraft().world.markTileEntityForRemoval(te);
        }
    }

    @Inject(method = "render*", at = @At("INVOKE"), cancellable = true)
    public void onRenderTileEntityPre(TileEntityEnderChest te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, CallbackInfo ci) {
        if (NoRender.INSTANCE.isEnabled()) {
            if (NoRender.INSTANCE.tryReplaceEnderChest(te)) {
                ci.cancel();
            }
        }
    }

}
