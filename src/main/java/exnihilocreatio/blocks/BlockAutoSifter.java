package exnihilocreatio.blocks;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.tiles.TileAutoSifter;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockAutoSifter extends BlockBase implements ITileEntityProvider {
    public static final PropertyEnum<EnumAutoSifterParts> PART_TYPE = PropertyEnum.create("part_type", EnumAutoSifterParts.class);


    public BlockAutoSifter() {
        super(Material.WOOD, "block_auto_sifter");
        setDefaultState(this.blockState.getBaseState().withProperty(PART_TYPE, EnumAutoSifterParts.EMPTY));

        setHardness(2);
        setHarvestLevel("axe", 0);

        setCreativeTab(ExNihiloCreatio.tabExNihilo);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileAutoSifter();
    }

    private TileAutoSifter getTe(World world, BlockPos pos) {
        return (TileAutoSifter) world.getTileEntity(pos);
    }

    //region >>>> RENDERING OPTIONS
    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileAutoSifter te = getTe(worldIn, pos);
        te.facing = placer.getHorizontalFacing();
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PART_TYPE);
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    //endregion
}
