package net.zombii.minecraft.init;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.zombii.minecraft.gui.OldSmithingScreen;

@OnlyIn(Dist.CLIENT)
public class MenuScreensInit {

    public static void Init() {
        MenuScreens.register(MenuTypeInit.OLD_SMITHING.get(), OldSmithingScreen::new);
    }

}
