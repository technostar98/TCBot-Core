package api.filter.event;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public enum EventPriority implements IEventListener{
    HIGHEST, HIGH, NORMAL, LOW, LOWEST;

    @Override
    public void invoke(Event event) {

    }


}
