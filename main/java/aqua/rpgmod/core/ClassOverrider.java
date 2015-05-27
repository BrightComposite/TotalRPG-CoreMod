package aqua.rpgmod.core;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ClassOverrider extends ClassTransformer
{
	public ClassOverrider(Transformer owner, String className)
	{
		super(owner, className);
	}

	@Override
	public byte[] transform(String className, byte[] bytes)
	{
		if(!this.className.equals(className))
			return bytes;
			
		File location = AquaCoreMod.mod.location;

		try
		{
			ZipFile zip = new ZipFile(location);
			ZipEntry entry = zip.getEntry(className.replace('.', '/') + ".class");
	
			if(entry != null)
			{
				InputStream zin = zip.getInputStream(entry);
				bytes = new byte[(int) entry.getSize()];
				zin.read(bytes);
				zin.close();
				AquaCoreMod.log("Class " + className + " patched!");
			}
			else
				AquaCoreMod.log(className + " not found in " + location.getName());
			
			zip.close();
		} 
		catch (Exception e) 
		{
			throw new RuntimeException("Error overriding " + className + " from " + location.getName(), e);
		}

		this.owner.components.remove(this);
		return bytes;
	}
}
