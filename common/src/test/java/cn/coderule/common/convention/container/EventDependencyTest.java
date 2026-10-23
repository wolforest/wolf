package cn.coderule.common.convention.container;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventDependencyTest {

    @Test
    public void of() {
        EventDependency dependency = EventDependency.of(
            "E",
            "arg",
            "A", "B"
        );

        assertEquals("E", dependency.getEvent());
        assertEquals("arg", dependency.getArg());
        assertFalse(dependency.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void ofEmptySourceEvents() {
        EventDependency.of("E", "arg");
    }

    @Test
    public void markFiredAllDone() {
        EventDependency dependency = EventDependency.of(
            "E",
            "arg",
            "A", "B", "C"
        );

        assertFalse(dependency.markFired("A"));
        assertFalse(dependency.markFired("B"));
        assertTrue(dependency.markFired("C"));
    }

    @Test
    public void markFiredUnknownEvent() {
        EventDependency dependency = EventDependency.of(
            "E",
            "arg",
            "A", "B"
        );

        assertFalse(dependency.markFired("C"));
    }

    @Test
    public void markFiredDuplicate() {
        EventDependency dependency = EventDependency.of(
            "E",
            "arg",
            "A", "B"
        );

        assertFalse(dependency.markFired("A"));
        assertFalse(dependency.markFired("A"));
    }

    @Test
    public void reset() {
        EventDependency dependency = EventDependency.of(
            "E",
            "arg",
            "A", "B"
        );

        dependency.markFired("A");
        assertTrue(dependency.markFired("B"));

        dependency.reset();

        assertFalse(dependency.markFired("A"));
    }

    @Test
    public void addSource() {
        EventDependency dependency = EventDependency.of(
            "E",
            "arg",
            "A"
        );

        dependency.addSource("B");

        assertFalse(dependency.markFired("A"));
        assertTrue(dependency.markFired("B"));
    }

    @Test
    public void addSourceDuplicate() {
        EventDependency dependency = EventDependency.of(
            "E",
            "arg",
            "A"
        );

        dependency.addSource("A");
        assertTrue(dependency.markFired("A"));
    }
}
