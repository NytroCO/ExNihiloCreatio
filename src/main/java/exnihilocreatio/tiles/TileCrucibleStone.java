package exnihilocreatio.tiles;

import exnihilocreatio.capabilities.CapabilityHeatManager;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.util.BlockInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileCrucibleStone extends TileCrucibleBase {

    public TileCrucibleStone() {
        super(ExNihiloRegistryManager.CRUCIBLE_STONE_REGISTRY);
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

    public int getHeatRate() {
        BlockPos posBelow = pos.add(0, -1, 0);
        IBlockState stateBelow = getWorld().getBlockState(posBelow);

        if (stateBelow == Blocks.AIR.getDefaultState()) {
            return 0;
        }

        // Try to match metadata
        int heat = ExNihiloRegistryManager.HEAT_REGISTRY.getHeatAmount(new BlockInfo(stateBelow));

        // Try to match without metadata
        if (heat == 0)
            heat = ExNihiloRegistryManager.HEAT_REGISTRY.getHeatAmount(new BlockInfo(stateBelow.getBlock()));

        if (heat != 0)
            return heat;

        TileEntity tile = getWorld().getTileEntity(posBelow);

        if (tile != null && tile.hasCapability(CapabilityHeatManager.HEAT_CAPABILITY, EnumFacing.UP)) {
            return tile.getCapability(CapabilityHeatManager.HEAT_CAPABILITY, EnumFacing.UP).getHeatRate();
        }

        return 0;
    }
}
