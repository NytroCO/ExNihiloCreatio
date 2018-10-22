package exnihilocreatio.compatibility.jei.sieve;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.types.Siftable;
import exnihilocreatio.util.StackInfo;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class SieveRecipeCategory implements IRecipeCategory<SieveRecipe> {

    public static final String UID = "exnihilocreatio:sieve";
    private static final ResourceLocation texture = new ResourceLocation(ExNihiloCreatio.MODID, "textures/gui/jei_sieve.png");

    private final IDrawableStatic background;
    private final IDrawableStatic slotHighlight;
    private boolean hasHighlight;
    private int highlightX;
    private int highlightY;

    public SieveRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(texture, 0, 0, 166, 128);
        this.slotHighlight = helper.createDrawable(texture, 166, 0, 18, 18);
    }

    @Override
    @Nonnull
    public String getUid() {
        return UID;
    }

    @Override
    @Nonnull
    public String getTitle() {
        return "Sieve";
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
        if (hasHighlight) {
            slotHighlight.draw(minecraft, highlightX, highlightY);
        }
    }

    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull SieveRecipe recipeWrapper, @Nonnull IIngredients ingredients) {
        //Mesh
        recipeLayout.getItemStacks().init(0, true, 61, 9);
        recipeLayout.getItemStacks().set(0, recipeWrapper.getMesh());

        //Input
        recipeLayout.getItemStacks().init(1, true, 87, 9);
        recipeLayout.getItemStacks().set(1, ingredients.getInputs(ItemStack.class).get(0));

        IFocus<?> focus = recipeLayout.getFocus();


        if (focus != null) {
            hasHighlight = focus.getMode() == IFocus.Mode.OUTPUT;
        }


        int slotIndex = 2;
        for (int i = 0; i < recipeWrapper.getOutputs().size(); i++) {
            final int slotX = 2 + (i % 9 * 18);
            final int slotY = 36 + (i / 9 * 18);
            ItemStack outputStack = (ItemStack) recipeWrapper.getOutputs().get(i);
            recipeLayout.getItemStacks().init(slotIndex + i, false, slotX, slotY);
            recipeLayout.getItemStacks().set(slotIndex + i, outputStack);

            if (focus != null) {
                ItemStack focusStack = (ItemStack) focus.getValue();
                if (focus.getMode() == IFocus.Mode.OUTPUT && !focusStack.isEmpty()
                        && focusStack.getItem() == outputStack.getItem()
                        && focusStack.getItemDamage() == outputStack.getItemDamage()) {
                    highlightX = slotX;
                    highlightY = slotY;
                }
            }
        }

        recipeLayout.getItemStacks().addTooltipCallback(new ITooltipCallback<ItemStack>() {
            @Override
            @SideOnly(Side.CLIENT)
            public void onTooltip(int slotIndex, boolean input, @Nonnull ItemStack ingredient, @Nonnull List<String> tooltip) {
                if (!input) {
                    ItemStack mesh = recipeWrapper.getMesh();
                    Multiset<String> condensedTooltips = HashMultiset.create();
                    for (Siftable siftable : ExNihiloRegistryManager.SIEVE_REGISTRY.getDrops((ItemStack) recipeWrapper.getInputs().get(0))) {
                        if (siftable.getMeshLevel() != mesh.getItemDamage())
                            continue;
                        StackInfo info = siftable.getDrop();
                        if (info.getItemStack().getItem() != ingredient.getItem() || info.getItemStack().getItemDamage() != ingredient.getItemDamage())
                            continue;

                        String s;
                        int iChance = (int) (siftable.getChance() * 100f);
                        if (iChance > 0) {
                            s = String.format("%3d%%", (int) (siftable.getChance() * 100f));
                        } else {
                            s = String.format("%1.1f%%", siftable.getChance() * 100f);
                        }
                        condensedTooltips.add(s);
                    }
                    tooltip.add(I18n.format("jei.sieve.dropChance"));
                    for (String line : condensedTooltips.elementSet()) {
                        tooltip.add(" * " + condensedTooltips.count(line) + "x " + line);
                    }
                }
            }
        });

    }
}
