package aqua.rpgmod.core;

import java.io.IOException;

public class AccessTransformer extends cpw.mods.fml.common.asm.transformers.AccessTransformer
{
	public AccessTransformer() throws IOException
	{
		super("totalrpg_at.cfg");
	}

}
