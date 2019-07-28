package ru.cover.test;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.cover.test.gui.DamageIndicatorGUI;
import ru.cover.test.init.ModBlocks;
import ru.cover.test.proxy.CommonProxy;
import ru.cover.test.util.Reference;
import ru.cover.test.util.handlers.ModGuiHandler;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)

public class Main {
	
	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static final CreativeTabs testtab = new CreativeTabs("testtab")
	{
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem()
		{
			return new  ItemStack(ModBlocks.CONNECTOR_TABLE);
		}
	};
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		OBJLoader.INSTANCE.addDomain(Reference.MOD_ID);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new ModGuiHandler());
		MinecraftForge.EVENT_BUS.register(new DamageIndicatorGUI());
	}
}
