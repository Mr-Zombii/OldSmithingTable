package net.zombii.minecraft.recipies;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.zombii.minecraft.init.BlockInit;
import net.zombii.minecraft.init.RecipeTypeInit;

import java.util.stream.Stream;

public class UpgradeRecipe implements Recipe<Container>  {

    private final ResourceLocation id;
    final Ingredient base;
    final Ingredient addition;
    final ItemStack result;

    public UpgradeRecipe(ResourceLocation p_267143_, Ingredient p_266787_, Ingredient p_267292_, ItemStack p_267031_) {
        this.id = p_267143_;
        this.base = p_266787_;
        this.addition = p_267292_;
        this.result = p_267031_;
    }

    public RecipeType<?> getType() {
        return RecipeTypeInit.SMITHING.get();
    }

    public boolean canCraftInDimensions(int p_266835_, int p_266829_) {
        return p_266835_ >= 3 && p_266829_ >= 1;
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(BlockInit.OLD_SMITHING_TABLE.get());
    }

    public boolean matches(Container p_266855_, Level p_266781_) {
        return this.base.test(p_266855_.getItem(0)) && this.addition.test(p_266855_.getItem(1));
    }

    public ItemStack assemble(Container p_267036_, RegistryAccess p_266699_) {
        ItemStack itemstack = this.result.copy();
        CompoundTag compoundtag = p_267036_.getItem(1).getTag();
        if (compoundtag != null) {
            itemstack.setTag(compoundtag.copy());
        }

        return itemstack;
    }

    public ItemStack getResultItem(RegistryAccess p_267209_) {
        return this.result;
    }

    public boolean isBaseIngredient(ItemStack p_267276_) {
        return this.base.test(p_267276_);
    }

    public boolean isAdditionIngredient(ItemStack p_267260_) {
        return this.addition.test(p_267260_);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SMITHING_TRANSFORM;
    }

    public boolean isIncomplete() {
        return Stream.of(this.base, this.addition).anyMatch(ForgeHooks::hasNoElements);
    }

    public static class Serializer implements RecipeSerializer<UpgradeRecipe> {
        public Serializer() {
        }

        public UpgradeRecipe fromJson(ResourceLocation p_266953_, JsonObject p_266720_) {
            Ingredient ingredient1 = Ingredient.fromJson(GsonHelper.getNonNull(p_266720_, "base"));
            Ingredient ingredient2 = Ingredient.fromJson(GsonHelper.getNonNull(p_266720_, "addition"));
            ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(p_266720_, "result"));
            return new UpgradeRecipe(p_266953_, ingredient1, ingredient2, itemstack);
        }

        public UpgradeRecipe fromNetwork(ResourceLocation p_267117_, FriendlyByteBuf p_267316_) {
            Ingredient ingredient1 = Ingredient.fromNetwork(p_267316_);
            Ingredient ingredient2 = Ingredient.fromNetwork(p_267316_);
            ItemStack itemstack = p_267316_.readItem();
            return new UpgradeRecipe(p_267117_, ingredient1, ingredient2, itemstack);
        }

        public void toNetwork(FriendlyByteBuf p_266746_, UpgradeRecipe p_266927_) {
            p_266927_.base.toNetwork(p_266746_);
            p_266927_.addition.toNetwork(p_266746_);
            p_266746_.writeItem(p_266927_.result);
        }
    }
}
