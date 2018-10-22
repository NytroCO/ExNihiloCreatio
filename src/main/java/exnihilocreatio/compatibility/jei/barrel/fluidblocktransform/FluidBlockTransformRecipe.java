package exnihilocreatio.compatibility.jei.barrel.fluidblocktransform;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import exnihilocreatio.registries.types.FluidBlockTransformer;
import exnihilocreatio.util.Util;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FluidBlockTransformRecipe implements IRecipeWrapper {

    @Nonnull
    private final FluidStack inputFluid;
    @Nonnull
    private final ItemStack inputBucket;
    @Nonnull
    private final List<ItemStack> inputStacks;
    @Nonnull
    private final ItemStack outputStack;

    public FluidBlockTransformRecipe(FluidBlockTransformer recipe) {
        inputFluid = new FluidStack(FluidRegistry.getFluid(recipe.getFluidName()), 1000);

        inputBucket = Util.getBucketStack(inputFluid.getFluid());

        inputStacks = Arrays.asList(recipe.getInput().getMatchingStacks());
        outputStack = recipe.getOutput().getItemStack();
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, getInputs());
        ingredients.setInputs(FluidStack.class, getFluidInputs());

        ingredients.setOutput(ItemStack.class, outputStack);
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

    @Override
    @Nonnull
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return Lists.newArrayList();
    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    public List<List<ItemStack>> getInputs() {
        return ImmutableList.of(Collections.singletonList(inputBucket), inputStacks);
    }

    public ItemStack getOutput() {
        return outputStack;
    }

    public List<FluidStack> getFluidInputs() {
        return ImmutableList.of(inputFluid);
    }

    public boolean isValid() {
        return !inputBucket.isEmpty() && !inputStacks.isEmpty() && !outputStack.isEmpty();
    }
}
