package cn.coderule.common.convention.container;

import junit.framework.TestCase;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventBusTest extends TestCase {

    public void testEmit() {
        EventBus eventBus = new EventBus();
        TestListener  listener = new TestListener();
        TestEvent event = new TestEvent();
        TestArgument arg = new TestArgument();

        eventBus.on(event, listener::onEvent);
        eventBus.emit(event, arg);

        assertTrue("EventBus emit failed", listener.isCalled());
    }

    public void testEmitWithWrongType() {
        EventBus eventBus = new EventBus();
        TestListener  listener = new TestListener();
        TestEvent event = new TestEvent();

        eventBus.on(event, listener::onEvent);
        eventBus.emit(event, event);

        assertFalse("EventBus emit failed", listener.isCalled());
    }

    static class TestArgument {
    }

    static class TestEvent {
    }

    @Getter
    static class TestListener {
        private boolean  called = false;
        public void onEvent(Object arg) {
            if (!(arg instanceof TestArgument event)) {
                log.error("wrong event type: {}", arg.getClass().getSimpleName());
                return;
            }

            called = true;
            log.info("TestListener.onEvent: {}", event.getClass().getSimpleName());
        }
    }
}
