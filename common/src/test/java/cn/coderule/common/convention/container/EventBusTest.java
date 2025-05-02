package cn.coderule.common.convention.container;

import junit.framework.TestCase;
import lombok.Getter;

public class EventBusTest extends TestCase {

    public void testEmit() {
        EventBus eventBus = new EventBus();
        TestListener  listener = new TestListener();
        TestEvent event = new TestEvent();

        eventBus.on(event, listener::onEvent);
        eventBus.emit(event);

        assertTrue("EventBus emit failed", listener.isCalled());
    }

    static class TestEvent {
    }

    @Getter
    static class TestListener {
        private boolean  called = false;
        public void onEvent(Object arg) {
            if (!(arg instanceof TestEvent event)) {
                return;
            }

            called = true;
            System.out.println("TestListener.onEvent:" + event.getClass().getSimpleName());
        }
    }
}
