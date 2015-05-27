package aqua.rpgmod.core;

abstract public class ClassTransformer
{
	public final Transformer owner;
	public final String className;
	
	public ClassTransformer(Transformer owner, String className)
	{
		this.owner = owner;
		this.className = className;
		
		owner.components.add(this);
	}
	
	public abstract byte[] transform(String className, byte[] bytes);
}
