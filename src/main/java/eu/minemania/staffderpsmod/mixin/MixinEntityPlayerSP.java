package eu.minemania.staffderpsmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.brigadier.StringReader;
import eu.minemania.staffderpsmod.command.ClientCommandManager;
import net.minecraft.client.entity.EntityPlayerSP;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP
{
    @Inject(method = "sendChatMessage(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessageSDP(String message, CallbackInfo ci)
    {
        if (message.startsWith("/"))
        {
            StringReader reader = new StringReader(message);
            reader.skip();
            int cursor = reader.getCursor();
            String commandName = reader.canRead() ? reader.readUnquotedString() : "";
            reader.setCursor(cursor);
            if (ClientCommandManager.isClientSideCommand(commandName))
            {
                ClientCommandManager.executeCommand(reader, message);
                ci.cancel();
            }
        }
    }
}