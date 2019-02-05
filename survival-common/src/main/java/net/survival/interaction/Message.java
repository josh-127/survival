package net.survival.interaction;

public abstract class Message implements Comparable<Message>
{
    public abstract void accept(MessageVisitor visitor, InteractionContext ic);

    public abstract int getPriority();

    @Override
    public int compareTo(Message o) {
        var priority = getPriority();
        var otherPriority = o.getPriority();
        if (priority < otherPriority)
            return -1;
        if (priority > otherPriority)
            return 1;
        return 0;
    }
}