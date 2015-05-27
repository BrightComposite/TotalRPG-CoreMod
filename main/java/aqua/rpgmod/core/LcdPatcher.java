package aqua.rpgmod.core;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;

public class LcdPatcher extends InstructionPatcher
{
	Object oldCst;
	Object newCst;
	
	public LcdPatcher(MethodPatcher owner, Object oldCst, Object newCst)
	{
		super(owner);
		
		this.oldCst = oldCst;
		this.newCst = newCst;
	}

	@Override
	public AbstractInsnNode patch(MethodPatcher owner, AbstractInsnNode instruction, InsnList list)
	{
		if(instruction instanceof LdcInsnNode)
		{
			LdcInsnNode ldcInsn = (LdcInsnNode)instruction;
			
			if(!ldcInsn.cst.equals(this.oldCst))
				return instruction;
			
			ldcInsn.cst = this.newCst;
			AquaCoreMod.log("Constant " + this.oldCst + " has been patched!");
			owner.remove(this);
			
			return instruction.getNext();
		}
		
		return instruction;
	}

}
