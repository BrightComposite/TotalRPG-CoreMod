package aqua.rpgmod.core;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

public class KeyBindPatcher extends InstructionPatcher
{
	String bindName;
	int value;
	
	public KeyBindPatcher(MethodPatcher owner, String bindName, int value)
	{
		super(owner);
		
		this.bindName = bindName;
		this.value = value;
	}

	@Override
	public AbstractInsnNode patch(MethodPatcher owner, AbstractInsnNode instruction, InsnList list)
	{
		if(instruction instanceof LdcInsnNode && ((LdcInsnNode)instruction).cst.equals(this.bindName))
		{
			while(true)
			{
				instruction = instruction.getNext();
				
				if(instruction == null)
					return instruction;
				
				if(instruction instanceof IntInsnNode)
					break;
			}
			
			IntInsnNode intInsn = (IntInsnNode)instruction;
			intInsn.operand = this.value;

			AquaCoreMod.log("Key binding '" + this.bindName + "' has been patched!");
			owner.remove(this);
		}
		
		return instruction;
	}

}
