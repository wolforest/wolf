package cn.coderule.ddd.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class DomainResult<T> {
    private static final String DEFAULT_FAILURE_MESSAGE = "操作失败";

    private boolean success = false;

    private String message;
    private T result = null;

    private List<DomainEvent> eventList = new ArrayList<>();

    public void addEvent(DomainEvent event) {
        this.eventList.add(event);
    }

    public void addEvents(List<DomainEvent> eventList) {
        this.eventList.addAll(eventList);
    }

    public static <T> DomainResult<T> success(T result) {
        DomainResult<T> domainResult = new DomainResult<>();
        domainResult.setSuccess(true);
        domainResult.setResult(result);
        return domainResult;
    }

    public static <T> DomainResult<T> failure() {
       return failure(DEFAULT_FAILURE_MESSAGE);
    }

    public static <T> DomainResult<T> failure(String message) {
        DomainResult<T> domainResult = new DomainResult<>();
        domainResult.setSuccess(false);
        domainResult.setMessage(message);
        domainResult.setResult(null);
        return domainResult;
    }
}
