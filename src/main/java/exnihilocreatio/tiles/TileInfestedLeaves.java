package exnihilocreatio.tiles;

import exnihilocreatio.blocks.BlockInfestingLeaves;
import exnihilocreatio.networking.PacketHandler;
import exnihilocreatio.util.BlockInfo;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;

public class TileInfestedLeaves extends BaseTileEntity implements ITileLeafBlock {

    @Getter
    private IBlockState leafBlock = Blocks.LEAVES.getDefaultState();

    public void setLeafBlock(IBlockState block) {
        leafBlock = block;
        PacketHandler.sendNBTUpdate(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        leafBlock = TileInfestingLeaves.getLeafFromBlock(tag);
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        tag.setString("leafBlock", leafBlock.getBlock().getRegistryName() == null ? "" : leafBlock.getBlock().getRegistryName().toString());
        tag.setInteger("leafBlockMeta", leafBlock.getBlock().getMetaFromState(leafBlock));
        return tag;
    }

    @Override
    @Nonnull
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public boolean hasFastRenderer() {
        return false;
    }
}
