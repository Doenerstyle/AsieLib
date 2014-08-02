package pl.asie.lib;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import pl.asie.lib.network.MessageHandlerBase;
import pl.asie.lib.network.Packet;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class NetworkHandlerClient extends MessageHandlerBase {
	@Override
	public void onMessage(Packet packet, INetHandler handler, EntityPlayer player, int command)
			throws IOException {
		switch(command) {
			case Packets.NICKNAME_CHANGE:
				String username = packet.readString();
				String nickname = packet.readString();
				AsieLibMod.nick.setNickname(username, nickname);
				break;
			case Packets.NANO_NANO:
				AsieLibMod.keyClient.scheduleSpin();
				break;
			case Packets.SPAWN_PARTICLE: {
		        double x = packet.readFloat();
		        double y = packet.readFloat();
		        double z = packet.readFloat();
		        double vx = packet.readFloat();
		        double vy = packet.readFloat();
		        double vz = packet.readFloat();
		        String name = packet.readString();
		        Minecraft.getMinecraft().thePlayer.getEntityWorld().spawnParticle(name, x, y, z, vx, vy, vz);
			} break;
		}
	}
}
