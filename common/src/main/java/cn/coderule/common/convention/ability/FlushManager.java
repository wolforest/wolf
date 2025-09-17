package cn.coderule.common.convention.ability;

import java.util.ArrayList;
import java.util.List;

public class FlushManager implements Flushable {
    private final List<Flushable> components = new ArrayList<>();

    public void register(Flushable component) {
        components.add(component);
    }

    @Override
    public void flush() throws Exception {
        for (Flushable component : components) {
            component.flush();
        }
    }
}
