package eu.minemania.staffderpsmod.compat.modmenu;

import java.util.function.Function;
import eu.minemania.staffderpsmod.Reference;
import eu.minemania.staffderpsmod.gui.GuiConfigs;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;

public class ModMenuImpl implements ModMenuApi
{
	@Override
	public String getModId()
	{
		return Reference.MOD_ID;
	}

	@Override
	public Function<Screen, ? extends Screen> getConfigScreenFactory()
	{
		return (screen) -> {
			GuiConfigs gui = new GuiConfigs();
			gui.setParent(screen);
			return gui;
		};
	}
}
