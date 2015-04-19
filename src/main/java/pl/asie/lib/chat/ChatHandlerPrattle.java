package pl.asie.lib.chat;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mcp.mobius.talkative.api.PrattleChatEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import pl.asie.lib.AsieLibMod;
import pl.asie.lib.util.ChatUtils;

import java.util.Calendar;

import static pl.asie.lib.AsieLibMod.chat;

/**
 * @author Vexatos
 */
public class ChatHandlerPrattle {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void chatEvent(PrattleChatEvent.ServerSendChatEvent event) {

		ChatComponentText chatmessage;
		String username = ChatUtils.color(AsieLibMod.nick.getNickname(event.sender)) + EnumChatFormatting.RESET;
		final String messageVar = "%MESSAGE%";
		EntityPlayerMP player;
		try {
			player = FMLCommonHandler.instance().getSidedDelegate().getServer().getConfigurationManager().func_152612_a(event.sender);
		} catch(Exception e) {
			player = null;
		}
		String message = event.displayMsg.getUnformattedText()
			.replaceAll(
				StatCollector.translateToLocalFormatted("chat.type.text",
					player != null ? player.getDisplayName() : event.sender, messageVar)
					.replaceAll("(.*)(" + messageVar + ")(.*)", "\\\\Q$1\\\\E(.*)\\\\Q$3\\\\E")
				, "$1");
		final String originalMessage = message;

		//The /me comamnd
		/*if(message.startsWith("%MECOMMAND%")) {
			event.displayMsg = new ChatComponentText(message.substring("%MECOMMAND%".length()));
			return;
		}*/

		if(message.startsWith("!") && chat.enableShout) {
			message = message.substring(1);
		}

		if(chat.enableGreentext && message.startsWith(">")) {
			message = EnumChatFormatting.GREEN + message;
		}

		Calendar now = Calendar.getInstance();
		String formattedMessage = EnumChatFormatting.RESET + chat.messageFormat;
		try {
			formattedMessage = formattedMessage.replaceAll("%u", username)
				.replaceAll("%m", message)
				.replaceAll("%H", ChatHandler.pad(now.get(Calendar.HOUR_OF_DAY)))
				.replaceAll("%M", ChatHandler.pad(now.get(Calendar.MINUTE)))
				.replaceAll("%S", ChatHandler.pad(now.get(Calendar.SECOND)));
			if(player != null) {
				formattedMessage = formattedMessage.replaceAll("%w", player.worldObj.provider.getDimensionName());
			}
		} catch(Exception e) {
			e.printStackTrace();
			formattedMessage = EnumChatFormatting.RESET + "<" + username + "" + EnumChatFormatting.RESET + "> " + message;
		}

		if(chat.enableColor) {
			try {
				formattedMessage = ChatUtils.color(formattedMessage);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		if(originalMessage.startsWith("!") && chat.enableShout) {
			chatmessage = new ChatComponentText(EnumChatFormatting.YELLOW + chat.shoutPrefix + " " + formattedMessage);
		} else {
			chatmessage = new ChatComponentText(formattedMessage);
		}

		event.displayMsg = chatmessage;
	}
}