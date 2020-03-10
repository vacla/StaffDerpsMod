package eu.minemania.staffderpsmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import eu.minemania.staffderpsmod.command.ClientCommandManager;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.command.ISuggestionProvider;

@Mixin(GuiChat.class)
public class MixinGuiChat {
	@Shadow
	protected GuiTextField inputField;
	@Shadow 
	private ParseResults<ISuggestionProvider> currentParse;

	@Inject(method = "updateSuggestion", at = @At("RETURN"))
	public void onUpdateCommandSDM(CallbackInfo ci)
	{
		boolean isClientCommand;
		if (currentParse == null)
		{
			isClientCommand = false;
		}
		else
		{
			StringReader reader = new StringReader(currentParse.getReader().getString());
			reader.skip(); // /
			String command = reader.canRead() ? reader.readUnquotedString() : "";
			isClientCommand = ClientCommandManager.isClientSideCommand(command);
		}

		inputField.setMaxStringLength(isClientCommand ? 32500 : 256);
	}
}