package exnihilocreatio.networking;

import exnihilocreatio.tiles.TileBarrel;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageFluidUpdate implements IMessage {

    private String fluidName;
    private int fillAmount;
    private int x, y, z;

    public MessageFluidUpdate() {
    }

    public MessageFluidUpdate(FluidStack fluid, BlockPos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        if (fluid == null) {
            this.fillAmount = 0;
            this.fluidName = "null";
        } else {
            this.fillAmount = fluid.amount;
            this.fluidName = fluid.getFluid().getName();
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.fillAmount = buf.readInt();
        this.fluidName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(fillAmount);
        ByteBufUtils.writeUTF8String(buf, fluidName);
    }

    public static class MessageFluidUpdateHandler implements IMessageHandler<MessageFluidUpdate, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(final MessageFluidUpdate msg, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                @SideOnly(Side.CLIENT)
                public void run() {
                    TileEntity entity = Minecraft.getMinecraft().player.getEntityWorld().getTileEntity(new BlockPos(msg.x, msg.y, msg.z));
                    if (entity instanceof TileBarrel) {
                        TileBarrel te = (TileBarrel) entity;
                        Fluid fluid = FluidRegistry.getFluid(msg.fluidName);
                        FluidStack f = fluid == null ? null : new FluidStack(fluid, msg.fillAmount);
                        te.getTank().setFluid(f);
                    }
                }
            });
            return null;
        }
    }
}
