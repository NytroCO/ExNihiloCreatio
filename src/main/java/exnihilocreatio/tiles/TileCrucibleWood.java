package exnihilocreatio.tiles;

import exnihilocreatio.config.ModConfig;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.util.TankUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileCrucibleWood extends TileCrucibleBase {

    public TileCrucibleWood() {
        super(ExNihiloRegistryManager.CRUCIBLE_WOOD_REGISTRY);
    }

    @Override
    public void update() {
        if (getWorld().isRemote)
            return;

        ticksSinceLast++;

        if (ticksSinceLast >= 10) {
            ticksSinceLast = 0;
            updateCrucible(getHeatRate());
        }
    }

    @Override
    public int getHeatRate() {
        return ModConfig.crucible.woodenCrucibleSpeed;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ, IFluidHandler handler) {
        return TankUtil.drainWaterIntoBottle(this, player, tank) || super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ, handler);
    }
}
