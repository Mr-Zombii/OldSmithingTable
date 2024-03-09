package net.zombii.minecraft.init;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zombii.minecraft.Mod;
import net.zombii.minecraft.blocks.OldSmithingTable;

public class BlockInit {

    public static DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Mod.MODID);

    public static final RegistryObject<OldSmithingTable> OLD_SMITHING_TABLE = REGISTRY.register("old_smithing_table",
            () -> new OldSmithingTable(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DEEPSLATE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .sound(SoundType.METAL)
                            .requiresCorrectToolForDrops()
                            .strength(30.0F, 1200.0F)
            )
    );

}
