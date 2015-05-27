package aqua.rpgmod.block;

import aqua.rpgmod.AquaMod;
import aqua.rpgmod.inventory.AQWorkbenchContainer;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class AQBlockWorkbench extends BlockWorkbench
{
    public AQBlockWorkbench()
    {
        super();
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
    	if(world.isRemote)
    		return true;
    	
        player.openGui(AquaMod.mod, AQWorkbenchContainer.ID, world, x, y, z);
        return true;
    }
}