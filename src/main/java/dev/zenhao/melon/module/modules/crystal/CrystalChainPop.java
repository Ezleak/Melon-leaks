package dev.zenhao.melon.module.modules.crystal;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;

public class CrystalChainPop {
    private final EntityLivingBase target;
    private final BlockPos targetPos;
    private final double dmg;

    public CrystalChainPop(EntityLivingBase target, BlockPos renderPos, double dmg) {
        this.target = target;
        this.targetPos = renderPos;
        this.dmg = dmg;
    }

    public EntityLivingBase getTarget() {
        return target;
    }

    public BlockPos getTargetPos() {
        return targetPos;
    }

    public double getDmg() {
        return dmg;
    }

}
