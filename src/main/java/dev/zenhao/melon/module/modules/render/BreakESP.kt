package dev.zenhao.melon.module.modules.render

import com.google.common.collect.Maps
import dev.zenhao.melon.event.events.block.BlockBreakEvent
import dev.zenhao.melon.event.events.render.RenderEvent
import dev.zenhao.melon.manager.FriendManager
import dev.zenhao.melon.module.Category
import dev.zenhao.melon.module.Module
import dev.zenhao.melon.setting.BooleanSetting
import dev.zenhao.melon.setting.Setting
import dev.zenhao.melon.utils.animations.sq
import dev.zenhao.melon.utils.block.BlockUtil
import dev.zenhao.melon.utils.block.BreakingUtil
import dev.zenhao.melon.utils.gl.MelonTessellator
import melon.system.event.safeEventListener
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import java.awt.Color
import java.text.DecimalFormat
import kotlin.math.abs

@Module.Info(name = "BreakESP", category = Category.RENDER, description = "BreakEsp")
object BreakESP : Module() {
    private val mineMap: MutableMap<Int, BreakESPExtend>? = Maps.newHashMap()
    private var renderDoublePos = bsetting("RenderDoublePos", false)
    private var doublePosColor = csetting("DoublePosColor", Color(239, 13, 136)).b(renderDoublePos)
    private var fdoublePosColor = csetting("FDoublePosColor", Color(62, 139, 13)).b(renderDoublePos)
    private var renderPercent = bsetting("RenderPercent", false)
    private var renderAir: BooleanSetting = bsetting("RenderAir", false)
    private var renderSelf = bsetting("RenderSelf", true)
    private var drawID = bsetting("DrawID", false)
    private var drawProgress = bsetting("DrawProgress", false)
    private var colors = csetting("Color", Color(11, 232, 145))
    private var friendColor = csetting("FriendColor", Color(157, 14, 192))
    private var alpha: Setting<Int> = isetting("Alpha", 100, 1, 255)
    private var range: Setting<Int> = isetting("Range", 6, 1, 20)
    private var lineWidth: Setting<Int> = isetting("LineWidth", 2, 1, 3)
    private var renderMode = isetting("RenderMode", 0, 0, 10)
    private var minePos: BlockPos? = null
    private var packetPos: BlockPos? = null
    private var secPacketPos: BlockPos? = null
    private var df = DecimalFormat("0.00")
    private var lastPos: BlockPos? = null
    private var currentState = ""

    init {
        safeEventListener<BlockBreakEvent> { event ->
            if (!renderSelf.value) {
                if (event.breakerID == mc.player.entityId) {
                    return@safeEventListener
                }
            }
            if (event.position.distanceSq(BlockPos(player)) <= range.value.sq) {
                if (BlockUtil.canBreak(event.position, renderAir.value) && mineMap != null) {
                    var destroyblockprogress = mineMap[event.breakerID]
                    if (destroyblockprogress == null
                        || destroyblockprogress.firstPos.getX() != event.position.getX()
                        || destroyblockprogress.firstPos.getY() != event.position.getY()
                        || destroyblockprogress.firstPos
                            .getZ() != event.position.getZ()
                    ) {
                        if (mc.world.getEntityByID(event.breakerID) is EntityPlayer) {
                            destroyblockprogress = BreakESPExtend(
                                event.position,
                                if (lastPos != null && lastPos != event.position) {
                                    lastPos
                                } else {
                                    null
                                },
                                BreakingUtil.calcBreakTime(
                                    event.breakerID,
                                    event.position
                                ),
                                System.currentTimeMillis(),
                                0f
                            )
                            lastPos = event.position
                            if (!mineMap.containsKey(event.breakerID)) {
                                mineMap[event.breakerID] = destroyblockprogress
                            } else {
                                mineMap.replace(event.breakerID, destroyblockprogress)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onWorldRender(event: RenderEvent) {
        val color = colors.value
        val fcolor = friendColor.value
        val dcolor = doublePosColor.value
        val dfcolor = fdoublePosColor.value
        //TODO: PacketMine Mode
        mineMap!!.forEach {
            packetPos = it.value.firstPos
            secPacketPos = if (it.value.secondPos != null) it.value.secondPos else null
            it.value.finalProgress =
                MathHelper.clamp(
                    it.value.calcMineTime - (
                            MathHelper.clamp(
                                (System.currentTimeMillis() - it.value.currentTime).toDouble(),
                                0.0,
                                it.value.currentTime.toDouble()
                            ).toFloat()), 0.0f, it.value.calcMineTime
                )
            if (mc.world.getBlockState(packetPos!!).block === Blocks.AIR && !renderAir.value) {
                return@forEach
            }
            if (packetPos != null && packetPos !== minePos && mc.world.getEntityByID(it.key) != null && mc.world.getEntityByID(
                    it.key
                ) is EntityPlayer
            ) {
                if (packetPos!!.distanceSq(BlockPos(mc.player)) <= range.value.sq) {
                    MelonTessellator.boxESP(
                        packetPos!!,
                        if (FriendManager.isFriend(mc.world.getEntityByID(it.key))) {
                            fcolor
                        } else {
                            color
                        },
                        alpha.value,
                        lineWidth.value.toFloat(),
                        abs(it.value.finalProgress / it.value.calcMineTime),
                        renderMode.value
                    )
                    if (drawProgress.value) {
                        GlStateManager.pushMatrix()
                        MelonTessellator.glBillboardDistanceScaled(
                            packetPos!!.getX().toFloat() + 0.5f,
                            packetPos!!.getY().toFloat() + 0.4f,
                            packetPos!!.getZ().toFloat() + 0.5f,
                            mc.player,
                            0.5f
                        )
                        val percent = df.format(
                            MathHelper.clamp(
                                100.0 - ((it.value.finalProgress / it.value.calcMineTime) * 100.0),
                                0.0,
                                100.0
                            )
                        )
                        GlStateManager.disableDepth()
                        currentState =
                            if ((mc.world.getEntityByID(it.key) as EntityPlayer).getDistanceSq(packetPos!!) < 36) {
                                if (!renderPercent.value) {
                                    if (!mc.world.isAirBlock(packetPos!!)) {
                                        "Breaking..."
                                    } else {
                                        "Broke"
                                    }
                                } else {
                                    percent
                                }
                            } else {
                                "Failed"
                            }
                        GlStateManager.translate(
                            -mc.fontRenderer.getStringWidth(currentState).toFloat() / 2.0f, 0f, 0f
                        )
                        val textColor = when (MathHelper.clamp(
                            abs(100.0 - ((it.value.finalProgress / it.value.calcMineTime) * 100.0)),
                            0.0,
                            100.0
                        )) {
                            in 0.0..50.00 -> {
                                Color(255, 0, 0).rgb
                            }

                            in 50.0..99.0 -> {
                                Color(255, 100, 0).rgb
                            }

                            else -> {
                                Color(0, 255, 0).rgb
                            }
                        }
                        mc.fontRenderer.drawStringWithShadow(
                            currentState,
                            0f,
                            0f,
                            if (currentState == "Failed") {
                                Color(255, 0, 0).rgb
                            } else {
                                textColor
                            }
                        )
                        GlStateManager.popMatrix()
                    }
                    if (drawID.value) {
                        if (mc.world.getEntityByID(it.key) is EntityPlayer) {
                            GlStateManager.pushMatrix()
                            MelonTessellator.glBillboardDistanceScaled(
                                packetPos!!.getX().toFloat() + 0.5f,
                                packetPos!!.getY().toFloat() + 0.65f,
                                packetPos!!.getZ().toFloat() + 0.5f,
                                mc.player,
                                0.5f
                            )
                            GlStateManager.disableDepth()
                            GlStateManager.translate(
                                -(mc.fontRenderer.getStringWidth((mc.world.getEntityByID(it.key) as EntityPlayer).name) / 2.0),
                                0.0,
                                0.0
                            )
                            mc.fontRenderer.drawStringWithShadow(
                                ((mc.world.getEntityByID(it.key) as EntityPlayer).name),
                                0f,
                                0f,
                                Color(255, 255, 255).rgb
                            )
                            GlStateManager.popMatrix()
                        }
                    }
                }
            }

            //SecondPos Render
            if (secPacketPos != null && renderDoublePos.value && mc.world.getEntityByID(it.key) != null && mc.world.getEntityByID(it.key)!!.getDistanceSq(secPacketPos!!) < 36) {
                if (!mc.world.isAirBlock(secPacketPos!!)) {
                    if (secPacketPos!!.distanceSq(BlockPos(mc.player)) <= range.value.sq) {
                        MelonTessellator.boxESP(
                            secPacketPos!!,
                            if (FriendManager.isFriend(mc.world.getEntityByID(it.key))) {
                                dfcolor
                            } else {
                                dcolor
                            },
                            alpha.value,
                            lineWidth.value.toFloat(),
                            0f,
                            renderMode.value
                        )
                        if (drawID.value) {
                            if (mc.world.getEntityByID(it.key) is EntityPlayer) {
                                GlStateManager.pushMatrix()
                                MelonTessellator.glBillboardDistanceScaled(
                                    secPacketPos!!.getX().toFloat() + 0.5f,
                                    secPacketPos!!.getY().toFloat() + 0.65f,
                                    secPacketPos!!.getZ().toFloat() + 0.5f,
                                    mc.player,
                                    0.5f
                                )
                                GlStateManager.disableDepth()
                                GlStateManager.translate(
                                    -(mc.fontRenderer.getStringWidth((mc.world.getEntityByID(it.key) as EntityPlayer).name) / 2.0),
                                    0.0,
                                    0.0
                                )
                                mc.fontRenderer.drawStringWithShadow(
                                    ((mc.world.getEntityByID(it.key) as EntityPlayer).name),
                                    0f,
                                    0f,
                                    Color(255, 255, 255).rgb
                                )
                                GlStateManager.popMatrix()
                            }
                        }
                        GlStateManager.pushMatrix()
                        MelonTessellator.glBillboardDistanceScaled(
                            secPacketPos!!.getX().toFloat() + 0.5f,
                            secPacketPos!!.getY().toFloat() + 0.4f,
                            secPacketPos!!.getZ().toFloat() + 0.5f,
                            mc.player,
                            0.5f
                        )
                        GlStateManager.disableDepth()
                        GlStateManager.translate(
                            -(mc.fontRenderer.getStringWidth("Double") / 2.0),
                            0.0,
                            0.0
                        )
                        mc.fontRenderer.drawStringWithShadow(
                            "Double",
                            0f,
                            0f,
                            Color(255, 165, 0).rgb
                        )
                        GlStateManager.popMatrix()
                    }
                }
            }
        }
    }

    class BreakESPExtend(
        val firstPos: BlockPos,
        val secondPos: BlockPos?,
        var calcMineTime: Float,
        var currentTime: Long,
        var finalProgress: Float
    )
}