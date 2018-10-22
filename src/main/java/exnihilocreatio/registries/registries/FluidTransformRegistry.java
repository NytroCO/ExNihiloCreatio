package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihilocreatio.api.registries.IFluidTransformRegistry;
import exnihilocreatio.compatibility.jei.barrel.fluidtransform.FluidTransformRecipe;
import exnihilocreatio.json.CustomBlockInfoJson;
import exnihilocreatio.json.CustomItemInfoJson;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import exnihilocreatio.registries.types.FluidTransformer;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import net.minecraftforge.fluids.FluidRegistry;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FluidTransformRegistry extends BaseRegistryMap<String, List<FluidTransformer>> implements IFluidTransformRegistry {
    public FluidTransformRegistry() {
        super(new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemInfo.class, CustomItemInfoJson.INSTANCE)
                        .registerTypeAdapter(BlockInfo.class, CustomBlockInfoJson.INSTANCE)
                        .create(),
                new com.google.gson.reflect.TypeToken<Map<String, List<FluidTransformer>>>() {
                }.getType(),
                ExNihiloRegistryManager.FLUID_TRANSFORM_DEFAULT_REGISTRY_PROVIDERS);
    }

    public void register(String inputFluid, String outputFluid, int duration, BlockInfo[] transformingBlocks, BlockInfo[] blocksToSpawn) {
        register(new FluidTransformer(inputFluid, outputFluid, duration, transformingBlocks, blocksToSpawn));
    }

    public void register(FluidTransformer transformer) {
        List<FluidTransformer> list = registry.get(transformer.getInputFluid());

        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(transformer);
        registry.put(transformer.getInputFluid(), list);
    }

    public boolean containsKey(String inputFluid) {
        return registry.containsKey(inputFluid);
    }

    public FluidTransformer getFluidTransformer(String inputFluid, String outputFluid) {
        if (registry.containsKey(inputFluid)) {
            for (FluidTransformer transformer : registry.get(inputFluid)) {
                if (transformer.getInputFluid().equals(inputFluid) && transformer.getOutputFluid().equals(outputFluid))
                    return transformer;
            }
        }
        return null;
    }

    public List<FluidTransformer> getFluidTransformers(String inputFluid) {
        return registry.get(inputFluid);
    }

    /**
     * Overridden as I don't want the registry to get saved directly,
     * rather a List that equals the contents of the registry
     */
    @Override
    public void saveJson(File file) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);

            gson.toJson(getFluidTransformers(), fw);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fw);
        }
    }

    @Override
    protected void registerEntriesFromJSON(FileReader fr) {
        List<FluidTransformer> gsonInput = gson.fromJson(fr, new TypeToken<List<FluidTransformer>>() {
        }.getType());

        for (FluidTransformer transformer : gsonInput) {
            register(transformer);
        }
    }

    @Override
    public List<FluidTransformRecipe> getRecipeList() {
        List<FluidTransformRecipe> fluidTransformRecipes = Lists.newLinkedList();
        getFluidTransformers().forEach(transformer -> {
            // Make sure both fluids are registered
            if (FluidRegistry.isFluidRegistered(transformer.getInputFluid()) && FluidRegistry.isFluidRegistered(transformer.getOutputFluid())) {
                FluidTransformRecipe recipe = new FluidTransformRecipe(transformer);
                if (recipe.isValid()) {
                    fluidTransformRecipes.add(new FluidTransformRecipe(transformer));
                }
            }
        });
        return fluidTransformRecipes;
    }

    public List<FluidTransformer> getFluidTransformers() {
        List<FluidTransformer> fluidTransformers = new ArrayList<>();
        for (List<FluidTransformer> transformers : registry.values()) {
            fluidTransformers.addAll(transformers);
        }
        return fluidTransformers;
    }
}
