package cn.coderule.ddd.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Entity implements Serializable {
    protected List<DomainEvent> eventList = new ArrayList<>();

    public void addEvent(DomainEvent event) {
        eventList.add(event);
    }

    public void clearEvent() {
        eventList.clear();
    }
}
