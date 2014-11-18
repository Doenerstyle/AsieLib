package pl.asie.lib;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.asie.lib.api.AsieLibAPI;
import pl.asie.lib.api.chat.INicknameHandler;
import pl.asie.lib.api.chat.INicknameRepository;
import pl.asie.lib.chat.ChatHandler;
import pl.asie.lib.chat.NicknameNetworkHandler;
import pl.asie.lib.chat.NicknameRepository;
import pl.asie.lib.client.BlockBaseRender;
import pl.asie.lib.integration.Integration;
import pl.asie.lib.network.PacketHandler;
import pl.asie.lib.shinonome.EventKeyClient;
import pl.asie.lib.shinonome.EventKeyServer;
import pl.asie.lib.shinonome.ItemKey;
import pl.asie.lib.tweak.enchantment.EnchantmentTweak;

import java.util.Random;

@Mod(modid = "asielib", name = "AsieLib", version = "0.3.3", useMetadata = true)
public class AsieLibMod extends AsieLibAPI {
	public Configuration config;
	public static Integration integration;
	public static Random rand = new Random();
	public static Logger log;
	public static ChatHandler chat;
	public static NicknameRepository nick;
	public static ItemKey itemKey;
	public static PacketHandler packet;
	public static EventKeyClient keyClient = new EventKeyClient();
	public static EventKeyServer keyServer = new EventKeyServer();

	public static boolean ENABLE_DYNAMIC_ENERGY_CALCULATION;

	@Instance(value = "asielib")
	public static AsieLibMod instance;

	@SidedProxy(clientSide = "pl.asie.lib.ClientProxy", serverSide = "pl.asie.lib.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		AsieLibAPI.instance = this;
		integration = new Integration();
		log = LogManager.getLogger("asielib");

		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		chat = new ChatHandler(config);

		if(chat.enableChatFeatures)
			MinecraftForge.EVENT_BUS.register(chat);
		MinecraftForge.EVENT_BUS.register(new AsieLibEvents());

		ENABLE_DYNAMIC_ENERGY_CALCULATION =
			config.getBoolean("enableDynamicEnergyUsageCalculation", "general", true, "If you want to disable dynamic generation of current/peak energy usage, use this.");

		if(System.getProperty("user.dir").contains(".asielauncher")) {
			log.info("Hey, you! Yes, you! Thanks for using AsieLauncher! ~asie");
		}

		itemKey = new ItemKey();
		//GameRegistry.registerItem(itemKey, "item.asietweaks.key");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		if(proxy.isClient()) {
			MinecraftForge.EVENT_BUS.register(keyClient);
			FMLCommonHandler.instance().bus().register(keyClient);

			new BlockBaseRender();
		}
		MinecraftForge.EVENT_BUS.register(keyServer);

		packet = new PacketHandler("asielib", new NetworkHandlerClient(), null);

		if(config.get("enchantments", "usefulBaneOfArthropods", false,
			"Might make Bane Of Arthropods actually useful (Experimental)").getBoolean(false)) {
			EnchantmentTweak.registerBaneEnchantment(config.getInt("baneEnchantmentID", "enchantments", 244, 0, 255,
				"The enchantment ID for the better Bane Of Arthropods"));
			EnchantmentTweak tweak = new EnchantmentTweak();
			MinecraftForge.EVENT_BUS.register(tweak);
			FMLCommonHandler.instance().bus().register(tweak);
		}

		nick = new NicknameRepository();
		nick.loadNicknames();
		MinecraftForge.EVENT_BUS.register(nick);

		FMLCommonHandler.instance().bus().register(new NicknameNetworkHandler());

		if(config.get("tweaks", "dyeItemNamesInAnvil", true).getBoolean(true)) {
			MinecraftForge.EVENT_BUS.register(new AnvilDyeTweak());
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		config.save();
	}

	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		chat.registerCommands(event);
	}

	@EventHandler
	public void onServerStop(FMLServerStoppingEvent event) {
		nick.saveNicknames();
	}

	public void registerNicknameHandler(INicknameHandler handler) {
		if(nick != null)
			nick.addHandler(handler);
	}

	public INicknameRepository getNicknameRepository() {
		return nick;
	}
}
