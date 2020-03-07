package eu.minemania.staffderpsmod.subfunctions;

import java.text.DecimalFormat;
import eu.minemania.staffderpsmod.config.Configs;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class MobSummoner 
{
    private String command;
    private DecimalFormat df;


    public MobSummoner()
    {
        this.command = Configs.Generic.SUMMON_COMMAND.getStringValue();
        this.df = new DecimalFormat("0.0");
    }

    public void summon()
    {
        EntityPlayer player = Minecraft.getInstance().player;

        double x = player.getLookVec().x * Configs.Generic.SCALAR.getDoubleValue();
        double y = player.getLookVec().y * Configs.Generic.SCALAR.getDoubleValue();
        double z = player.getLookVec().z * Configs.Generic.SCALAR.getDoubleValue();

        String result = "Motion:[" + df.format(x).replace(',', '.') + "," + df.format(y).replace(',', '.') + "," + df.format(z).replace(',', '.') + "]";
        if (this.command.contains(":"))
        {
            result = "," + result;
        }
        result = this.command.substring(0, this.command.length() - 1) + result + "}";
        Minecraft.getInstance().player.sendChatMessage(result);
    }
}