package cn.coderule.common.convention.container;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

public class EventBusBindTest {

    @Test
    public void bindSingleSource() {
        EventBus bus = new EventBus();
        AtomicReference<Object> received = new AtomicReference<>();

        bus.on("E", received::set);
        bus.bind("E", "targetArg", "A");

        bus.emit("A", "sourceArg");

        assertEquals("targetArg", received.get());
    }

    @Test
    public void bindNotFiredUntilAllSources() {
        EventBus bus = new EventBus();
        AtomicInteger count = new AtomicInteger(0);

        bus.on("E", ignored -> count.incrementAndGet());
        bus.bind("E", "targetArg", "A", "B", "C");

        bus.emit("A", null);
        bus.emit("B", null);

        assertEquals(0, count.get());
    }

    @Test
    public void bindMultipleSources() {
        EventBus bus = new EventBus();
        AtomicInteger count = new AtomicInteger(0);

        bus.on("E", ignored -> count.incrementAndGet());
        bus.bind("E", "targetArg", "A", "B", "C");

        bus.emit("A", null);
        bus.emit("B", null);
        bus.emit("C", null);

        assertEquals(1, count.get());
    }

    @Test
    public void bindOrderIndependent() {
        EventBus bus = new EventBus();
        AtomicInteger count = new AtomicInteger(0);

        bus.on("E", ignored -> count.incrementAndGet());
        bus.bind("E", "targetArg", "A", "B", "C");

        bus.emit("C", null);
        bus.emit("A", null);
        assertEquals(0, count.get());

        bus.emit("B", null);
        assertEquals(1, count.get());
    }

    @Test
    public void bindResetsAfterPropagate() {
        EventBus bus = new EventBus();
        AtomicInteger count = new AtomicInteger(0);

        bus.on("E", ignored -> count.incrementAndGet());
        bus.bind("E", "targetArg", "A", "B");

        bus.emit("A", null);
        bus.emit("B", null);
        assertEquals(1, count.get());

        bus.emit("A", null);
        bus.emit("B", null);
        assertEquals(2, count.get());
    }

    @Test
    public void bindTargetArg() {
        EventBus bus = new EventBus();
        AtomicReference<Object> received = new AtomicReference<>();

        bus.on("E", received::set);
        bus.bind("E", "theTargetArg", "A", "B");

        bus.emit("A", "unused");
        bus.emit("B", "unused");

        assertEquals("theTargetArg", received.get());
    }
}
