package aqua.rpgmod.core;

import java.util.ArrayList;
import java.util.Iterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public class Initializer
{
	public final String signature;
	public final InstructionClassPatcher owner;
	protected final ArrayList<InitializerPart> parts = new ArrayList<InitializerPart>();
	
	public Initializer(InstructionClassPatcher owner, String signature)
	{
		this.owner = owner;
		this.signature = signature;
	}
	
	public void add(InitializerPart part)
	{
		this.parts.add(part);
		part.owner = this;
	}
	
	public AbstractInsnNode insertCode(AbstractInsnNode start, AbstractInsnNode instruction, InsnList list)
	{
		for(Iterator<InitializerPart> i = this.parts.iterator(); i.hasNext();)
			start = i.next().load(start, instruction, list);
		
		return start;
	}
}
