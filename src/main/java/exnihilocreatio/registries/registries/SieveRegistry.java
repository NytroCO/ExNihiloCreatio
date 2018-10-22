package exnihilocreatio.registries.registries;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.api.registries.ISieveRegistry;
import exnihilocreatio.blocks.BlockSieve;
import exnihilocreatio.compatibility.jei.sieve.SieveRecipe;
import exnihilocreatio.json.CustomIngredientJson;
import exnihilocreatio.json.CustomItemInfoJson;
import exnihilocreatio.registries.ingredient.IngredientUtil;
import exnihilocreatio.registries.ingredient.OreIngredientStoring;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.registries.types.Siftable;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import exnihilocreatio.util.StackInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Stream;

import static exnihilocreatio.registries.SieveRegistry.addToDrops;

public class SieveRegistry extends BaseRegistryMap<Ingredient, List<Siftable>> implements ISieveRegistry {

    public SieveRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(Ingredient.class, CustomIngredientJson.INSTANCE)
                        .registerTypeAdapter(OreIngredientStoring.class, CustomIngredientJson.INSTANCE)
                        .registerTypeAdapter(ItemInfo.class, CustomItemInfoJson.INSTANCE)
                        .enableComplexMapKeySerialization()
                        .create(),
                new com.google.gson.reflect.TypeToken<Map<Ingredient, List<Siftable>>>() {
                }.getType(),
                ExNihiloRegistryManager.SIEVE_DEFAULT_REGISTRY_PROVIDERS
        );
    }


    public void register(ItemStack itemStack, StackInfo drop, float chance, int meshLevel) {
        if (itemStack.isEmpty()) {
            return;
        }
        if (drop instanceof ItemInfo)
            register(CraftingHelper.getIngredient(itemStack), new Siftable((ItemInfo) drop, chance, meshLevel));
        else
            register(CraftingHelper.getIngredient(itemStack), new Siftable(new ItemInfo(drop.getItemStack()), chance, meshLevel));
    }

    public void register(Item item, int meta, StackInfo drop, float chance, int meshLevel) {
        register(new ItemStack(item, 1, meta), drop, chance, meshLevel);
    }

    public void register(StackInfo item, StackInfo drop, float chance, int meshLevel) {
        register(item.getItemStack(), drop, chance, meshLevel);
    }

    public void register(Block block, int meta, StackInfo drop, float chance, int meshLevel) {
        register(new ItemStack(block, 1, meta), drop, chance, meshLevel);
    }

    public void register(IBlockState state, StackInfo drop, float chance, int meshLevel) {
        register(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)), drop, chance, meshLevel);
    }

    public void register(ResourceLocation location, int meta, StackInfo drop, float chance, int meshLevel) {
        register(new ItemStack(ForgeRegistries.ITEMS.getValue(location), 1, meta), drop, chance, meshLevel);
    }

    public void register(String name, StackInfo drop, float chance, int meshLevel) {
        if (drop instanceof ItemInfo)
            register(new OreIngredientStoring(name), new Siftable((ItemInfo) drop, chance, meshLevel));
        else
            register(new OreIngredientStoring(name), new Siftable(new ItemInfo(drop.getItemStack()), chance, meshLevel));
    }

    public void register(Ingredient ingredient, Siftable drop) {
        if (ingredient == null)
            return;

        Ingredient search = registry.keySet().stream().filter(entry -> IngredientUtil.ingredientEquals(entry, ingredient)).findAny().orElse(null);
        if (search != null) {
            registry.get(search).add(drop);
        } else {
            NonNullList<Siftable> drops = NonNullList.create();
            drops.add(drop);
            super.register(ingredient, drops);
        }
    }

    /**
     * Gets *all* possible drops from the sieve. It is up to the dropper to
     * check whether or not the drops should be dropped!
     *
     * @param stack The block to get the sieve drops for
     * @return ArrayList of {@linkplain Siftable}
     * that could *potentially* be dropped.
     */
    public List<Siftable> getDrops(StackInfo stack) {
        return getDrops(stack.getItemStack());
    }

    /**
     * Gets *all* possible drops from the sieve. It is up to the dropper to
     * check whether or not the drops should be dropped!
     *
     * @param stack The ItemStack to get the sieve drops for
     * @return ArrayList of {@linkplain Siftable}
     * that could *potentially* be dropped.
     */
    public List<Siftable> getDrops(ItemStack stack) {
        List<Siftable> drops = new ArrayList<>();
        if (!stack.isEmpty())
            registry.entrySet().stream().filter(entry -> entry.getKey().test(stack)).forEach(entry -> drops.addAll(entry.getValue()));
        return drops;
    }

    public List<Siftable> getDrops(Ingredient ingredient) {
        List<Siftable> drops = new ArrayList<>();
        registry.entrySet().stream().filter(entry -> entry.getKey() == ingredient).forEach(entry -> drops.addAll(entry.getValue()));
        return drops;
    }

    public List<ItemStack> getRewardDrops(Random random, IBlockState block, int meshLevel, int fortuneLevel) {
        if (block == null) {
            return null;
        }

        List<ItemStack> drops = new ArrayList<>();

        getDrops(new BlockInfo(block)).forEach(siftable -> addToDrops(random, meshLevel, fortuneLevel, drops, siftable));

        return drops;
    }

    public boolean canBeSifted(ItemStack stack) {
        return !stack.isEmpty() && registry.keySet().stream().anyMatch(entry -> entry.test(stack));
    }

    @Override
    public void registerEntriesFromJSON(FileReader fr) {
        HashMap<Ingredient, ArrayList<Siftable>> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<Ingredient, ArrayList<Siftable>>>() {
        }.getType());

        for (Map.Entry<Ingredient, ArrayList<Siftable>> input : gsonInput.entrySet()) {
            Ingredient key = input.getKey();

            if (key != null && key != Ingredient.EMPTY) {
                for (Siftable siftable : input.getValue()) {
                    if (siftable.getDrop().isValid()) {
                        register(key, siftable);
                    }
                }
            }
        }
    }

    @Override
    public List<SieveRecipe> getRecipeList() {
        List<SieveRecipe> sieveRecipes = new ArrayList<>();

        getRegistry().keySet().forEach(ingredient -> Stream.of(BlockSieve.MeshType.values()).forEach(meshType -> {
            if (meshType.getID() != 0 && ingredient != null) {
                SieveRecipe recipe = new SieveRecipe(ingredient, meshType);
                if (recipe.isValid() && !sieveRecipes.contains(recipe))
                    sieveRecipes.add(recipe);
            }
        }));

        return sieveRecipes;
    }
}
