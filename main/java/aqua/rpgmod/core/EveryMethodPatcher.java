package aqua.rpgmod.core;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public class EveryMethodPatcher extends MethodPatcher
{
	public EveryMethodPatcher(ClassPatcher owner, String desc)
	{
		super(owner, "", desc);
	}
	
	@Override
	public boolean transform(ClassPatcher owner, String name, String desc, InsnList instructions)
	{
		AquaCoreMod.log("Trying method '" + name + "'...");
		
		AbstractInsnNode insn = instructions.getFirst();
		
		while(insn != null)
		{
			for(InstructionPatcher component : this.components)
			{
				AbstractInsnNode lastInsn = insn;
				insn = component.patch(this, lastInsn, instructions);
				
				if(insn != lastInsn)
					break;
			}
			
			if(insn == null)
				break;
			
			insn = insn.getNext();
			
			if(this.components.isEmpty())
			{
				owner.remove(this);
				return true;
			}
		}

		return false;
	}
}
