package aqua.rpgmod.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockSandStone;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class AQBlockSandStone extends BlockSandStone
{
    public static final String[] types = new String[] {"normal", "carved", "smooth"};
    @SideOnly(Side.CLIENT)
    public IIcon[] side;
    @SideOnly(Side.CLIENT)
    public IIcon[] top;
    @SideOnly(Side.CLIENT)
    public IIcon[] bottom;
    
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        if (p_149691_1_ != 1 && (p_149691_1_ != 0 || p_149691_2_ != 1 && p_149691_2_ != 2))
        {
            if (p_149691_1_ == 0)
            {
                return this.bottom[p_149691_2_];
            }
            
            if (p_149691_2_ < 0 || p_149691_2_ >= types.length)
            {
                p_149691_2_ = 0;
            }

            return this.side[p_149691_2_];
        }
        
        return this.top[p_149691_2_];
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.side = new IIcon[types.length];
        this.top = new IIcon[types.length];
        this.bottom = new IIcon[types.length];

        for (int i = 0; i < types.length; ++i)
        {
            this.side[i] = p_149651_1_.registerIcon(this.getTextureName() + "_" + types[i]);
            this.top[i] = p_149651_1_.registerIcon(this.getTextureName() + "_top_" + types[i]);
            this.bottom[i] = p_149651_1_.registerIcon(this.getTextureName() + "_bottom_" + types[i]);
        }
    }
}
