//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.zombii.minecraft.gui;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.zombii.minecraft.Mod;
import net.zombii.minecraft.init.BlockInit;
import net.zombii.minecraft.init.MenuTypeInit;
import net.zombii.minecraft.init.RecipeTypeInit;
import net.zombii.minecraft.recipies.UpgradeRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
        this.access.execute((p_40263_, p_40264_) -> {
            p_40263_.levelEvent(1044, p_40264_, 0);
        });
    }

    private List<ItemStack> getRelevantItems() {
        return List.of(this.inputSlots.getItem(0), this.inputSlots.getItem(1));
    }

    private void shrinkStackInSlot(int p_40271_) {
        ItemStack $$1 = this.inputSlots.getItem(p_40271_);
        $$1.shrink(1);
        this.inputSlots.setItem(p_40271_, $$1);
    }

    public void createResult() {
        List<UpgradeRecipe> $$0 = this.level.getRecipeManager().getRecipesFor(RecipeTypeInit.SMITHING.get(), this.inputSlots, this.level);
        if ($$0.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        } else {
            UpgradeRecipe $$1 = $$0.get(0);
            ItemStack $$2 = $$1.assemble(this.inputSlots, this.level.registryAccess());
            if ($$2.isItemEnabled(this.level.enabledFeatures())) {
                this.selectedRecipe = $$1;
                this.resultSlots.setRecipeUsed($$1);
                this.resultSlots.setItem(0, $$2);
            }
        }

    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create().withSlot(0, 27, 47, (p_266643_) -> {
            return this.recipes.stream().anyMatch((p_266642_) -> {
                return p_266642_.isBaseIngredient(p_266643_);
            });
        }).withSlot(1, 76, 47, (p_286207_) -> {
            return this.recipes.stream().anyMatch((p_286204_) -> {
                return p_286204_.isAdditionIngredient(p_286207_);
            });
        }).withResultSlot(2, 134, 47).build();
    }

    protected boolean shouldQuickMoveToAdditionalSlot(ItemStack p_40255_) {
        return this.recipes.stream().anyMatch((p_40261_) -> {
            return p_40261_.isAdditionIngredient(p_40255_);
        });
    }

    public boolean canTakeItemForPickAll(ItemStack p_40257_, Slot p_40258_) {
        return p_40258_.container != this.resultSlots && super.canTakeItemForPickAll(p_40257_, p_40258_);
    }
}
