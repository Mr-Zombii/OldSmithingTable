package net.zombii.minecraft.recipies;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record OldSmithingRecipeInput(ItemStack base, ItemStack addition) implements RecipeInput {

    public ItemStack getItem(int p_343148_) {
        ItemStack var10000;
        switch (p_343148_) {
            case 0 -> var10000 = this.base;
            case 1 -> var10000 = this.addition;
            default -> throw new IllegalArgumentException("Recipe does not contain slot " + p_343148_);
        }

        return var10000;
    }

    public int size() {
        return 3;
    }

    public boolean isEmpty() {
        return this.base.isEmpty() && this.addition.isEmpty();
    }

    public ItemStack base() {
        return this.base;
    }

    public ItemStack addition() {
        return this.addition;
    }
}
