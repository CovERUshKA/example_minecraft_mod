package ru.cover.test.util.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import ru.cover.test.containers.ContainerConnector;
import ru.cover.test.gui.containers.GuiContainerConnector;

public class ModGuiHandler implements IGuiHandler
{
	/**
	 * Each gui needs an ID
	 */
	public static final int CONNECTOR_BLOCK = 0;

	/**
	 * Should return the container for that gui. This is called server side
	 * because servers handle items in guis
	 */
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		switch (ID) {
		case CONNECTOR_BLOCK:
			return new ContainerConnector(player.inventory, world, pos);
		}
		return null;
	}

	/**
	 * Should return the actual gui. This is called client side as thats where
	 * guis are rendered
	 */
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		switch (ID) {
		case CONNECTOR_BLOCK:
			return new GuiContainerConnector(player.inventory, world);
		}
		return null;
	}

}
