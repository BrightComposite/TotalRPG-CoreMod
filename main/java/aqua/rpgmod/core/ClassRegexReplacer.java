package aqua.rpgmod.core;

public class ClassRegexReplacer extends ClassTransformer
{
	public final String oldName;
	public final String newName;
	
	public ClassRegexReplacer(Transformer owner, String className, String oldName, String newName)
	{
		super(owner, className);
		this.oldName = oldName;
		this.newName = newName;
	}
	
	@Override
	public byte[] transform(String className, byte[] bytes)
	{
		if(!this.className.equals(className))
			return bytes;
		
		try
		{
			String s = new String(bytes, "cp1251");
			s = s.replaceAll(this.oldName, this.newName);
			bytes = s.getBytes("cp1251");
			AquaCoreMod.log("Class " + className + " patched, " + this.oldName + " has been replaced by " + this.newName + "!");
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		this.owner.components.remove(this);
		return bytes;
	}

}
