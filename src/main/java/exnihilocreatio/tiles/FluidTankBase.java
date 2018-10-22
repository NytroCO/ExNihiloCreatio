package exnihilocreatio.tiles;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;

public class FluidTankBase extends FluidTank {

    private final BaseTileEntity tileEntity;

    public FluidTankBase(int capacity, BaseTileEntity tileEntity) {
        super(capacity);
        this.tileEntity = tileEntity;
    }

    public FluidTankBase(@Nullable FluidStack fluidStack, int capacity, BaseTileEntity tileEntity) {
        super(fluidStack, capacity);
        this.tileEntity = tileEntity;
    }

    public FluidTankBase(Fluid fluid, int amount, int capacity, BaseTileEntity tileEntity) {
        super(fluid, amount, capacity);
        this.tileEntity = tileEntity;
    }

    @Override
    public int fillInternal(FluidStack resource, boolean doFill) {
        return super.fillInternal(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drainInternal(int maxDrain, boolean doDrain) {
        return super.drainInternal(maxDrain, doDrain);
    }

    @Override
    protected void onContentsChanged() {
        tileEntity.markDirtyClient();
    }
}
