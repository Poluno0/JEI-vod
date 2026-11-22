package dev.tanjei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Method;

@SuppressWarnings({"rawtypes", "unchecked"})
public class WaterPurifierCategory implements IRecipeCategory<Recipe> {
    static final mezz.jei.api.recipe.RecipeType<Recipe> TYPE =
            mezz.jei.api.recipe.RecipeType.create("tanjeicompat", "water_purifying", Recipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;

    public WaterPurifierCategory(IGuiHelper gui) {
        this.background = gui.createBlankDrawable(110, 36);
        Block block = ForgeRegistries.BLOCKS.getValue(TanJeiPlugin.BLOCK_ID);
        ItemStack iconStack = block != null ? new ItemStack(block) : ItemStack.EMPTY;
        this.icon = gui.createDrawableIngredient(VanillaTypes.ITEM_STACK, iconStack);
        this.arrow = gui.createDrawable(new ResourceLocation("jei", "textures/jei/gui/gui_vanilla.png"), 82, 128, 24, 17);
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<Recipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Водоочиститель");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup focuses) {
        for (Object o : recipe.getIngredients()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 4, 10).addIngredients((Ingredient) o);
            break;
        }
        ItemStack out = recipe.getResultItem(Minecraft.getInstance().level.registryAccess());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 88, 10).addItemStack(out);
    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView view, GuiGraphics g, double mx, double my) {
        arrow.draw(g, 40, 10);
        int ticks = purifyTime(recipe);
        if (ticks > 0) {
            String s = (ticks / 20.0) + " c";
            g.drawString(Minecraft.getInstance().font, s, 40, 28, 0xFF808080, false);
        }
    }

    private static int purifyTime(Recipe r) {
        try {
            Method m = r.getClass().getMethod("getPurifyTime");
            return (Integer) m.invoke(r);
        } catch (Throwable t) {
            return -1;
        }
    }
}
