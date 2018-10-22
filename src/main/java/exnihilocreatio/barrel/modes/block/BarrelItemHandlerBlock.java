package exnihilocreatio.barrel.modes.block;

import exnihilocreatio.networking.MessageBarrelModeUpdate;
import exnihilocreatio.networking.PacketHandler;
import exnihilocreatio.tiles.TileBarrel;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class BarrelItemHandlerBlock extends ItemStackHandler {
    @Setter
    private TileBarrel barrel;

    public BarrelItemHandlerBlock(TileBarrel barrel) {
        super(1);
        this.barrel = barrel;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        super.setStackInSlot(slot, stack);

        checkEmpty();
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        ItemStack returned = super.insertItem(slot, stack, simulate);

        if (!simulate) {
            PacketHandler.sendNBTUpdate(barrel);
        }

        return returned;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack ret = super.extractItem(slot, amount, simulate);

        checkEmpty();

        return ret;
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return stack.getMaxStackSize();
    }

    private void checkEmpty() {
        if (getStackInSlot(0).isEmpty() && barrel != null) {
            barrel.setMode("null");
            PacketHandler.sendToAllAround(new MessageBarrelModeUpdate("null", barrel.getPos()), barrel);
        }
    }
}
