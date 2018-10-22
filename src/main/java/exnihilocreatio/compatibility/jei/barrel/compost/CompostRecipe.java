package exnihilocreatio.compatibility.jei.barrel.compost;

import com.google.common.collect.Lists;
import exnihilocreatio.util.BlockInfo;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class CompostRecipe implements IRecipeWrapper {
    private final List<List<ItemStack>> inputs;
    private final List<ItemStack> output;

    public CompostRecipe(BlockInfo output, List<List<ItemStack>> inputs) {
        this.inputs = inputs;
        this.output = Collections.singletonList(output.getItemStack());
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, output);
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
        return inputs;
    }

    public List<ItemStack> getOutputs() {
        return output;
    }

    /**
     * @return Returns full if the input has any space free
     */
    public boolean isNonFull() {
        return inputs.size() < 45;
    }

    public boolean outputMatch(ItemStack stack) {
        return output.get(0).isItemEqual(stack);
    }

}
