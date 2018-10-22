package exnihilocreatio.networking;

import exnihilocreatio.tiles.TileBarrel;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageBarrelModeUpdate implements IMessage {

    private String modeName;
    private int x, y, z;

    public MessageBarrelModeUpdate() {
    }

    public MessageBarrelModeUpdate(String modeName, BlockPos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.modeName = modeName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.modeName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        ByteBufUtils.writeUTF8String(buf, modeName);
    }

    public static class MessageBarrelModeUpdateHandler implements IMessageHandler<MessageBarrelModeUpdate, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(final MessageBarrelModeUpdate msg, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                @SideOnly(Side.CLIENT)
                public void run() {
                    TileEntity entity = Minecraft.getMinecraft().player.getEntityWorld().getTileEntity(new BlockPos(msg.x, msg.y, msg.z));
                    if (entity instanceof TileBarrel) {
                        TileBarrel te = (TileBarrel) entity;
                        te.setMode(msg.modeName);

                        // Minecraft.getMinecraft().world.notifyBlockUpdate(new BlockPos(msg.x, msg.y, msg.z), entity.getBlockType().getDefaultState(), entity.getBlockType().getDefaultState(), 3);
                    }
                }
            });
            return null;
        }
    }
}
