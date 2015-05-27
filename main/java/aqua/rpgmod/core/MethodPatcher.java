package aqua.rpgmod.core;

import java.util.ArrayList;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public class MethodPatcher
{
	protected final ArrayList<InstructionPatcher> components = new ArrayList<InstructionPatcher>();

	public final String name;
	public final String desc;
	
	public MethodPatcher(ClassPatcher owner, String name, String desc)
	{
		this.name = name;
		this.desc = desc;
		
		owner.add(this);
	}
	
	public void add(InstructionPatcher patcher)
	{
		this.components.add(patcher);
	}

	public void remove(InstructionPatcher patcher)
	{
		this.components.remove(patcher);
	}

	public boolean transform(ClassPatcher owner, String name, String desc, InsnList instructions)
	{
		if(this.name.equals(name) && (this.desc == null || this.desc.equals(desc)))
		{
			AquaCoreMod.log("Method '" + name + "' has been found!");
			
			AbstractInsnNode insn = instructions.getFirst();
			
			while(insn != null)
			{
				for(InstructionPatcher component : this.components)
				{
					AbstractInsnNode lastInsn = insn;
					insn = component.patch(this, insn, instructions);
					
					if(insn != lastInsn)
						break;
				}
				
				if(insn == null)
					break;
				
				insn = insn.getNext();
				
				if(this.components.isEmpty())
					break;
			}

			if(!this.components.isEmpty())
				AquaCoreMod.log("Non-used instruction patchers: " + this.components.size());
			
			owner.remove(this);
			return true;
		}
		
		return false;
	}
}
