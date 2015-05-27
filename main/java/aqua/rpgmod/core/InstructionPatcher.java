package aqua.rpgmod.core;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

abstract public class InstructionPatcher
{
	public InstructionPatcher() {}
	
	public InstructionPatcher(MethodPatcher owner)
	{
		owner.add(this);
	}
	
	abstract public AbstractInsnNode patch(MethodPatcher owner, AbstractInsnNode instruction, InsnList list);
}
