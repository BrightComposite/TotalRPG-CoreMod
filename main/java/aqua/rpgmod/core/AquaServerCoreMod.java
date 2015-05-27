package aqua.rpgmod.core;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.DependsOn;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;

@Name("TotalRPGcore")
@MCVersion("1.7.10")
@DependsOn("forge")
@SortingIndex(2001)
public class AquaServerCoreMod extends AquaCoreMod
{
	public AquaServerCoreMod()
	{
		super();
	}
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[]
		{
			ServerTransformer.class.getName()
		};
	}

	@Override
	public String getModContainerClass()
	{
		return AquaServerCoreMod.class.getName();
	}
}
