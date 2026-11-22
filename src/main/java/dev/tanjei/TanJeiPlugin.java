package dev.tanjei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
@SuppressWarnings({"rawtypes", "unchecked"})
public class TanJeiPlugin implements IModPlugin {
    static final ResourceLocation UID = new ResourceLocation("tanjeicompat", "jei_plugin");
    static final ResourceLocation TYPE_ID = new ResourceLocation("toughasnails", "water_purifying");
    static final ResourceLocation BLOCK_ID = new ResourceLocation("toughasnails", "water_purifier");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        reg.addRecipeCategories(new WaterPurifierCategory(reg.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg) {
        RecipeType<?> type = ForgeRegistries.RECIPE_TYPES.getValue(TYPE_ID);
        Minecraft mc = Minecraft.getInstance();
        if (type == null || mc.level == null) {
            return;
        }
        List<Recipe> found = new ArrayList<>();
        for (Recipe r : mc.level.getRecipeManager().getRecipes()) {
            if (r.getType() == type) {
                found.add(r);
            }
        }
        reg.addRecipes(WaterPurifierCategory.TYPE, found);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
        Block block = ForgeRegistries.BLOCKS.getValue(BLOCK_ID);
        if (block != null) {
            reg.addRecipeCatalyst(new ItemStack(block), WaterPurifierCategory.TYPE);
        }
    }
}
