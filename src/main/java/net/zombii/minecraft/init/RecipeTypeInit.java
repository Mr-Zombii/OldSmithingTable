package net.zombii.minecraft.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zombii.minecraft.Mod;
import net.zombii.minecraft.recipies.UpgradeRecipe;

public class RecipeTypeInit {

    public static DeferredRegister<RecipeType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Mod.MODID);
    public static DeferredRegister<RecipeSerializer<?>> REGISTRY_S = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Mod.MODID);

    public static RegistryObject<RecipeType<UpgradeRecipe>> SMITHING = register("smithing");
    public static RegistryObject<RecipeSerializer<UpgradeRecipe>> SMITHING_RECIPE_SERIALIZER = REGISTRY_S.register(
            "smithing",
            UpgradeRecipe.Serializer::new
    );

    static <T extends Recipe<?>> RegistryObject<RecipeType<T>> register(final String name) {
        return REGISTRY.register(name, ()-> new RecipeType<T>() {
            @Override
            public String toString() {
                return name;
            }
        });
    }

}
