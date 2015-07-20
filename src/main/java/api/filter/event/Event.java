package api.filter.event;

import com.google.common.base.Preconditions;
import com.technostar98.tcbot.command.Cancelable;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Bret 'Horfius' Dusseault & MCF Development Team
 * @version 1.0
 */
public class Event {

    public enum Result
    {
        DENY,
        DEFAULT,
        ALLOW
    }

    private boolean isCanceled = false;
    private final boolean isCancelable;
    private EventPriority phase = null;
    public final EventContext context;

    private static final Map<Class<?>, Map<Class<?>, Boolean>> annotationMap = new ConcurrentHashMap<Class<?>, Map<Class<?>, Boolean>>();

    public Event(EventContext context)
    {
        isCancelable = hasAnnotation(Cancelable.class);
        this.context = context;
    }

    private boolean hasAnnotation(Class<? extends Annotation> annotation)
    {
        Class<?> me = this.getClass();
        Map<Class<?>, Boolean> list = annotationMap.get(me);
        if (list == null)
        {
            list = new ConcurrentHashMap<Class<?>, Boolean>();
            annotationMap.put(me, list);
        }

        Boolean cached = list.get(annotation);
        if (cached != null)
        {
            return cached;
        }

        Class<?> cls = me;
        while (cls != Event.class)
        {
            if (cls.isAnnotationPresent(annotation))
            {
                list.put(annotation, true);
                return true;
            }
            cls = cls.getSuperclass();
        }

        list.put(annotation, false);
        return false;
    }

    /**
     * Determine if this function is cancelable at all.
     * @return If access to setCanceled should be allowed
     */
    public boolean isCancelable()
    {
        return isCancelable;
    }

    /**
     * Determine if this event is canceled and should stop executing.
     * @return The current canceled state
     */
    public boolean isCanceled()
    {
        return isCanceled;
    }

    /**
     * Sets the state of this event, not all events are cancelable, and any attempt to
     * cancel a event that can't be will result in a IllegalArgumentException.
     *
     * The functionality of setting the canceled state is defined on a per-event bases.
     *
     * @param cancel The new canceled value
     */
    public void setCanceled(boolean cancel)
    {
        if (!isCancelable())
        {
            throw new IllegalArgumentException("Attempted to cancel a uncancelable event");
        }
        isCanceled = cancel;
    }

    /**
     * Called by the base constructor, this is used by ASM generated
     * event classes to setup various functionality such as the listenerlist.
     */
    protected void setup()
    {
    }

    public EventPriority getPhase()
    {
        return this.phase;
    }

    public void setPhase(EventPriority value)
    {
        Preconditions.checkArgument(value != null, "setPhase argument must not be null");
        int prev = phase == null ? -1 : phase.ordinal();
        Preconditions.checkArgument(prev < value.ordinal(), "Attempted to set event phase to %s when already %s", value, phase);
        phase = value;
    }
}
