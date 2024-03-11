package net.zombii.minecraft.gui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.zombii.minecraft.init.BlockInit;
import net.zombii.minecraft.init.MenuTypeInit;
import net.zombii.minecraft.init.RecipeTypeInit;
import net.zombii.minecraft.recipies.UpgradeRecipe;

import javax.annotation.Nullable;
import java.util.List;

public class OldSmithingMenu extends ItemCombinerMenu {
    private final Level level;
    @Nullable
    private UpgradeRecipe selectedRecipe;
    private final List<UpgradeRecipe> recipes;

    public OldSmithingMenu(int p_40245_, Inventory p_40246_) {
        this(p_40245_, p_40246_, ContainerLevelAccess.NULL);
    }

    public OldSmithingMenu(int p_40248_, Inventory p_40249_, ContainerLevelAccess p_40250_) {
        super(MenuTypeInit.OLD_SMITHING.get(), p_40248_, p_40249_, p_40250_);
        this.level = p_40249_.player.level();
        this.recipes = this.level.getRecipeManager().getAllRecipesFor(RecipeTypeInit.SMITHING.get());
    }

    protected boolean isValidBlock(BlockState p_40266_) {
        return p_40266_.is(BlockInit.OLD_SMITHING_TABLE.get());
    }

    protected boolean mayPickup(Player p_40268_, boolean p_40269_) {
        return this.selectedRecipe != null && this.selectedRecipe.matches(this.inputSlots, this.level);
    }

    protected void onTake(Player p_150663_, ItemStack p_150664_) {
        p_150664_.onCraftedBy(p_150663_.level(), p_150663_, p_150664_.getCount());
        this.resultSlots.awardUsedRecipes(p_150663_, this.getRelevantItems());
        this.shrinkStackInSlot(0);
        this.shrinkStackInSlot(1);
        this.access.execute((p_40263_, p_40264_) -> p_40263_.levelEvent(1044, p_40264_, 0));
    }

    private List<ItemStack> getRelevantItems() {
        return List.of(this.inputSlots.getItem(0), this.inputSlots.getItem(1));
    }

    private void shrinkStackInSlot(int p_40271_) {
        ItemStack stack = this.inputSlots.getItem(p_40271_);
        stack.shrink(1);
        this.inputSlots.setItem(p_40271_, stack);
    }

    public void createResult() {
        for (UpgradeRecipe recipe : this.recipes) {
            ItemStack result = recipe.assemble(this.inputSlots, this.level.registryAccess());
            if (result.isItemEnabled(this.level.enabledFeatures())) {
                this.selectedRecipe = recipe;
                this.resultSlots.setRecipeUsed(recipe);
                this.resultSlots.setItem(0, result);
                return;
            }
        }
        this.resultSlots.setItem(0, ItemStack.EMPTY);
    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(0, 27, 47, this::checkBaseIngredient)
                .withSlot(1, 76, 47, this::checkAdditionIngredient)
                .withResultSlot(2, 134, 47)
                .build();
    }

    private boolean checkBaseIngredient(ItemStack stack) {
        return this.recipes.stream().anyMatch(recipe -> recipe.isBaseIngredient(stack));
    }

    private boolean checkAdditionIngredient(ItemStack stack) {
        return this.recipes.stream().anyMatch(recipe -> recipe.isAdditionIngredient(stack));
    }

    protected boolean shouldQuickMoveToAdditionalSlot(ItemStack stack) {
        return this.recipes.stream().anyMatch(recipe -> recipe.isAdditionIngredient(stack));
    }

    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }
}
