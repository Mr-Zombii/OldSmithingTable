package net.zombii.minecraft.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zombii.minecraft.Mod;
import net.zombii.minecraft.gui.OldSmithingMenu;

public class MenuTypeInit {


    public static DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Mod.MODID);

    public static final RegistryObject<MenuType<OldSmithingMenu>> OLD_SMITHING = REGISTRY.register(
            "old_smithing_menu",
            () -> new MenuType<>(OldSmithingMenu::new, FeatureFlags.VANILLA_SET)
    );

}
