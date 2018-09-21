package exnihilocreatio.compatibility.forestry;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Random;

public class BlockHive extends Block implements IHasModel {
    public static final PropertyEnum<BlockHive.EnumType> VARIANT = PropertyEnum.create("variant", BlockHive.EnumType.class);

    public BlockHive(Material mat, String name) {
        super(mat);
        this.setRegistryName(name);
        this.setTranslationKey(ExNihiloCreatio.MODID + "." + name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.ARTIFICIAL));
        this.setCreativeTab(ExNihiloCreatio.tabExNihilo);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {VARIANT});
    }

    @Override
    public int damageDropped(IBlockState state) {
        return (state.getValue(VARIANT)).getMetadata();
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (BlockHive.EnumType enumtype : BlockHive.EnumType.values()) {
            items.add(new ItemStack(this, 1, enumtype.getMetadata()));
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, BlockHive.EnumType.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @SuppressWarnings("deprecation")
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return (state.getValue(VARIANT)).getMapColor();
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        //TODO Transformation logic
    }

    public static enum EnumType implements IStringSerializable {
        ARTIFICIAL(0, "artificial_hive", MapColor.YELLOW),
        SCENTED(1, "scented_hive", MapColor.BROWN);

        private final int metadata;
        private final String name;
        private final String translationKey;
        private final MapColor mapColor;

        EnumType(int meta, String name, MapColor color){
            this.metadata=meta;
            this.name=name;
            this.translationKey="tile.exnihilocreatio.hive."+name;
            this.mapColor = color;
        }

        public int getMetadata()
        {
            return this.metadata;
        }
        public MapColor getMapColor(){
            return this.mapColor;
        }
        public String toString()
        {
            return this.name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initModel(ModelRegistryEvent e){
        for (int i = 0; i < EnumType.values().length; i++) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock((Block) this), i,
                    new ModelResourceLocation(((IForgeRegistryEntry<?>) this).getRegistryName(), "variant=" + BlockHive.EnumType.values()[i].getName()));
        }
    }
}
