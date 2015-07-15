package api.filter.event;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public interface IEventListener {
    public void invoke(Event event);
}
