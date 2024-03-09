package net.zombii.minecraft;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zombii.minecraft.init.BlockInit;
import net.zombii.minecraft.init.MenuScreensInit;
import net.zombii.minecraft.init.MenuTypeInit;
import net.zombii.minecraft.init.RecipeTypeInit;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@net.minecraftforge.fml.common.Mod(Mod.FAKER_ID)
public class Mod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "minecraft";
    public static final String FAKER_ID = "smithing_table_adapter";

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    static DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public Mod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading

        BlockInit.REGISTRY.register(modEventBus);
        MenuTypeInit.REGISTRY.register(modEventBus);
        RecipeTypeInit.REGISTRY.register(modEventBus);
        RecipeTypeInit.REGISTRY_S.register(modEventBus);


        for (RegistryObject<? extends Block> block : BlockInit.REGISTRY.getEntries()) {
            REGISTRY.register(block.getId().getPath(), () -> new BlockItem(
                    block.get(),
                    new Item.Properties()
            ));
        }

        REGISTRY.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS)
            for (RegistryObject<Item> i : REGISTRY.getEntries()) {
                event.accept(i.get());
            }
    }

    @net.minecraftforge.fml.common.Mod.EventBusSubscriber(modid = FAKER_ID, bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            MenuScreensInit.Init();
        }
    }
}
