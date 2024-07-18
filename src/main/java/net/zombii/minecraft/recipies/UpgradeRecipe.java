package net.zombii.minecraft.recipies;

import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
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

public class UpgradeRecipe implements Recipe<OldSmithingRecipeInput>  {

    final Ingredient base;
    final Ingredient addition;
    final ItemStack result;

    public UpgradeRecipe(Ingredient p_266787_, Ingredient p_267292_, ItemStack p_267031_) {
        this.base = p_266787_;
        this.addition = p_267292_;
        this.result = p_267031_;
    }

    public RecipeType<?> getType() {
        return RecipeTypeInit.SMITHING.get();
    }

    @Override
    public boolean matches(OldSmithingRecipeInput recipeInput, Level level) {
        return this.base.test(recipeInput.getItem(0)) && this.addition.test(recipeInput.getItem(1));
    }

    @Override
    public ItemStack assemble(OldSmithingRecipeInput recipeInput, HolderLookup.Provider provider) {
        ItemStack itemstack = recipeInput.base().transmuteCopy(this.result.getItem(), this.result.getCount());
        itemstack.applyComponents(this.result.getComponentsPatch());
        return itemstack;
    }

    public boolean canCraftInDimensions(int p_266835_, int p_266829_) {
        return p_266835_ >= 3 && p_266829_ >= 1;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result;
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(BlockInit.OLD_SMITHING_TABLE.get());
    }

    public boolean isBaseIngredient(ItemStack p_267276_) {
        return this.base.test(p_267276_);
    }

    public boolean isAdditionIngredient(ItemStack p_267260_) {
        return this.addition.test(p_267260_);
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SMITHING_TRANSFORM;
    }

    public boolean isIncomplete() {
        return Stream.of(this.base, this.addition).anyMatch(ForgeHooks::hasNoElements);
    }

    public static class Serializer implements RecipeSerializer<UpgradeRecipe> {
        private static final MapCodec<UpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec((p_327220_) -> {
            return p_327220_.group(Ingredient.CODEC.fieldOf("base").forGetter((p_298250_) -> {
                return p_298250_.base;
            }), Ingredient.CODEC.fieldOf("addition").forGetter((p_299654_) -> {
                return p_299654_.addition;
            }), ItemStack.STRICT_CODEC.fieldOf("result").forGetter((p_297480_) -> {
                return p_297480_.result;
            })).apply(p_327220_, UpgradeRecipe::new);
        });
        public static final StreamCodec<RegistryFriendlyByteBuf, UpgradeRecipe> STREAM_CODEC = StreamCodec.of(UpgradeRecipe.Serializer::toNetwork, UpgradeRecipe.Serializer::fromNetwork);

        public Serializer() {
        }

        private static UpgradeRecipe fromNetwork(RegistryFriendlyByteBuf p_333917_) {
            Ingredient ingredient1 = Ingredient.CONTENTS_STREAM_CODEC.decode(p_333917_);
            Ingredient ingredient2 = Ingredient.CONTENTS_STREAM_CODEC.decode(p_333917_);
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(p_333917_);
            return new UpgradeRecipe(ingredient1, ingredient2, itemstack);
        }

        private static void toNetwork(RegistryFriendlyByteBuf p_329920_, UpgradeRecipe p_266927_) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(p_329920_, p_266927_.base);
            Ingredient.CONTENTS_STREAM_CODEC.encode(p_329920_, p_266927_.addition);
            ItemStack.STREAM_CODEC.encode(p_329920_, p_266927_.result);
        }

        @Override
        public MapCodec<UpgradeRecipe> codec() {
            return null;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, UpgradeRecipe> streamCodec() {
            return null;
        }
    }
}
