package ru.cover.test.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.cover.test.Main;
import ru.cover.test.init.ModBlocks;
import ru.cover.test.init.ModItems;
import ru.cover.test.util.IHasModel;
import ru.cover.test.util.handlers.ModGuiHandler;

public class Connector extends Block implements IHasModel {
	
	public Connector() {
		super(Material.IRON);
		this.setHardness(5.0f);
		this.setSoundType(SoundType.METAL);
		this.setHarvestLevel("pickaxe", 2);
		this.setRegistryName("connector_table");
		this.setUnlocalizedName("connector_table");
		this.setCreativeTab(Main.testtab);
		
		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            playerIn.openGui(Main.instance, ModGuiHandler.CONNECTOR_BLOCK, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }
	
	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
	
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

}
