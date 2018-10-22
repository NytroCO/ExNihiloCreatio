package exnihilocreatio.compatibility.jei.barrel.fluidblocktransform;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.ModBlocks;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class FluidBlockTransformRecipeCategory implements IRecipeCategory<FluidBlockTransformRecipe> {
    public static final String UID = "exnihilocreatio:fluid_block_transform";
    private static final ResourceLocation texture = new ResourceLocation(ExNihiloCreatio.MODID, "textures/gui/jei_fluid_block_transform.png");

    private final IDrawableStatic background;

    public FluidBlockTransformRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(texture, 0, 0, 166, 63);
    }

    @Override
    @Nonnull
    public String getUid() {
        return UID;
    }

    @Override
    @Nonnull
    public String getTitle() {
        return "Fluid Block Transform";
    }

    @Override
    @Nonnull
    public String getModName() {
        return ExNihiloCreatio.MODID;
    }

    @Override
    @Nonnull
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull FluidBlockTransformRecipe recipeWrapper, @Nonnull IIngredients ingredients) {
        // I learn from the best
        setRecipe(recipeLayout, recipeWrapper);
    }

    @SuppressWarnings("Duplicates")
    private void setRecipe(IRecipeLayout recipeLayout, FluidBlockTransformRecipe recipeWrapper) {
        recipeLayout.getItemStacks().init(0, true, 74, 36);
        recipeLayout.getItemStacks().init(1, true, 47, 36);
        recipeLayout.getItemStacks().init(2, true, 74, 9);
        recipeLayout.getItemStacks().init(3, false, 101, 36);

        recipeLayout.getItemStacks().set(0, new ItemStack(ModBlocks.barrelStone, 1, 0));
        recipeLayout.getItemStacks().set(1, recipeWrapper.getInputs().get(0));
        recipeLayout.getItemStacks().set(2, recipeWrapper.getInputs().get(1));
        recipeLayout.getItemStacks().set(3, recipeWrapper.getOutput());
    }
}
