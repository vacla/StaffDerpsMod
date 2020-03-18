package eu.minemania.staffderpsmod.subfunctions;

import eu.minemania.staffderpsmod.config.Configs;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CompassMath
{
    private MinecraftClient minecraft;

    public CompassMath(MinecraftClient minecraft) 
    {
        this.minecraft = minecraft;
    }

    public void passThrough()
    {
        Vec3d vector = minecraft.player.getRotationVector();
        double prevX = minecraft.player.getX();
        double prevY = minecraft.player.getY() + 1.62;
        double prevZ = minecraft.player.getZ();

        boolean doesWallExist = false;
        Style style = new Style();
        LiteralText message;

        for (int i = 0; i < 512; i++)
        {
            prevX += vector.x/8;
            prevY += vector.y/8;
            prevZ += vector.z/8;

            int x = (int) Math.floor(prevX);
            int y = (int) Math.floor(prevY);
            int z = (int) Math.floor(prevZ);

            if (canCollide(x, y, z))
            {
                doesWallExist = true;
            }

            if (doesWallExist && y > 0)
            {
                if (!canCollide(x, y + 1, z) && !canCollide(x, y, z))
                {
                    minecraft.player.sendChatMessage(Configs.Generic.TP_COMMAND.getStringValue() + " " + x + " " + y + " " + z);
                    return;
                }
                else if (!canCollide(x, y, z) && !canCollide(x, y - 1, z))
                {
                    minecraft.player.sendChatMessage(Configs.Generic.TP_COMMAND.getStringValue() + " " + x + " " + (y - 1) + " " + z);
                    return;
                }
            }
        }

        if (doesWallExist)
        {
            message = new LiteralText("Too much wall. You shall not pass!");
        }
        else
        {
            message = new LiteralText("Nothing to pass through!");
        }

        style.setColor(Formatting.DARK_RED);
        message.setStyle(style);
        minecraft.player.sendMessage(message);
    }

    public void jumpTo()
    {
        Vec3d vector = minecraft.player.getRotationVector();
        double prevX = minecraft.player.getX();
        double prevY = minecraft.player.getY() + 1.62;
        double prevZ = minecraft.player.getZ();
        Style style = new Style();
        LiteralText message;

        for (int i = 0; i < 512; i++)
        {
            prevX += vector.x/8;
            prevY += vector.y/8;
            prevZ += vector.z/8;

            int x = (int) Math.floor(prevX);
            int y = (int) Math.floor(prevY);
            int z = (int) Math.floor(prevZ);

            if (canCollide(x, y, z) && (minecraft.player.getPos().getX() != x && minecraft.player.getPos().getZ() != z))
            {
                for (int j = y; j < 256; j++)
                {
                    if (!canCollide(x, j + 1, z) && !canCollide(x, j + 2, z))
                    {
                        minecraft.player.sendChatMessage(Configs.Generic.TP_COMMAND.getStringValue() + " " + x + " " + (j + 1) + " " + z);
                        return;
                    }
                }
            }
        }

        message = new LiteralText("No block in sight (or too far)!");
        style.setColor(Formatting.DARK_RED);
        message.setStyle(style);
        minecraft.player.sendMessage(message);
    }

    private boolean canCollide(int x, int y, int z)
    {
        BlockPos pos = new BlockPos.Mutable(x, y, z);
        BlockState state = minecraft.world.getBlockState(pos);
        if (!state.getCollisionShape(minecraft.world, pos).isEmpty())
        {
            return true;
        }
        return false;
    }
}