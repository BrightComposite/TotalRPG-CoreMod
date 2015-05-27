package aqua.rpgmod.core;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

abstract public class InitializerPart
{
	public Initializer owner;

	public InitializerPart()
	{
		this.owner = null;
	}
	
	public InitializerPart(Initializer owner)
	{
		owner.add(this);
	}

	@SuppressWarnings({ "static-method", "unused" })
	public AbstractInsnNode load(AbstractInsnNode start, AbstractInsnNode instruction, InsnList list)
	{
		return start;
	}
}
