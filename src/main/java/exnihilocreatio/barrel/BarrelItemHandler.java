package exnihilocreatio.barrel;

import exnihilocreatio.networking.MessageBarrelModeUpdate;
import exnihilocreatio.networking.PacketHandler;
import exnihilocreatio.registries.registries.BarrelModeRegistry;
import exnihilocreatio.registries.registries.BarrelModeRegistry.TriggerType;
import exnihilocreatio.tiles.TileBarrel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BarrelItemHandler extends ItemStackHandler {

    private final TileBarrel barrel;

    public BarrelItemHandler(TileBarrel barrel) {
        super(1);
        this.barrel = barrel;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (barrel.getMode() != null && barrel.getMode().isTriggerItemStack(stack)) {
            barrel.getMode().addItem(stack, barrel);
            barrel.markDirty();

            IBlockState state = barrel.getWorld().getBlockState(barrel.getPos());
            barrel.getWorld().setBlockState(barrel.getPos(), state);
        } else if (barrel.getMode() != null && barrel.getMode().getHandler(barrel) != null) {
            barrel.getMode().getHandler(barrel).setStackInSlot(slot, stack);
        } else if (barrel.getMode() == null) {
            List<IBarrelMode> modes = BarrelModeRegistry.getModes(TriggerType.ITEM);

            if (modes != null) {
                for (IBarrelMode possibleMode : modes) {
                    if (possibleMode.isTriggerItemStack(stack)) {
                        simulate(possibleMode, stack);
                    }
                }
            }
        }
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        if (barrel.getMode() != null && barrel.getMode().getHandler(barrel) != null) {
            return barrel.getMode().getHandler(barrel).getStackInSlot(slot);
        }

        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (barrel.getMode() == null) {
            ArrayList<IBarrelMode> modes = BarrelModeRegistry.getModes(TriggerType.ITEM);

            if (modes == null) {
                return stack;
            }

            for (IBarrelMode possibleMode : modes) {
                if (possibleMode.isTriggerItemStack(stack)) {
                    if (!simulate) {
                        simulate(possibleMode, stack);
                    }

                    ItemStack ret = stack.copy();
                    ret.shrink(1);
                    return ret;
                }
            }

            return stack;
        } else {
            if (barrel.getMode().getHandler(barrel) != null)
                return barrel.getMode().getHandler(barrel).insertItem(slot, stack, simulate);
        }

        return stack;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (barrel.getMode() != null && barrel.getMode().getHandler(barrel) != null) {
            return barrel.getMode().getHandler(barrel).extractItem(slot, amount, simulate);
        }

        return ItemStack.EMPTY;
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return 1;
    }

    private void simulate(IBarrelMode possibleMode, ItemStack stack) {
        barrel.setMode(possibleMode.getName());
        PacketHandler.sendToAllAround(new MessageBarrelModeUpdate(barrel.getMode().getName(), barrel.getPos()), barrel);
        barrel.getMode().addItem(stack, barrel);
        barrel.markDirty();
        IBlockState state = barrel.getWorld().getBlockState(barrel.getPos());
        barrel.getWorld().setBlockState(barrel.getPos(), state);
    }

}
