package ru.cover.test.items;

import net.minecraft.item.Item;
import ru.cover.test.Main;
import ru.cover.test.init.ModItems;
import ru.cover.test.proxy.CommonProxy;
import ru.cover.test.util.IHasModel;

public class ItemTestBlock extends Item implements IHasModel {
	
	public ItemTestBlock()
	{
		this.setUnlocalizedName("testblock");
		this.setRegistryName("itemtestblock");
		this.setCreativeTab(Main.testtab);
		
		ModItems.ITEMS.add(this);
	}
	
	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
	
}
