
package cn.coderule.common.lang.concurrent.thread.pool;

import cn.coderule.common.lang.concurrent.thread.monitor.ThreadPoolMonitor;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThreadPoolWrapper {
    private String name;
    private ThreadPoolExecutor executor;
    private List<ThreadPoolMonitor> monitors;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ThreadPoolWrapper wrapper = (ThreadPoolWrapper) o;
        return Objects.equal(name, wrapper.name) && Objects.equal(executor, wrapper.executor) && Objects.equal(monitors, wrapper.monitors);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, executor, monitors);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("name", name)
            .add("threadPoolExecutor", executor)
            .add("statusPrinters", monitors)
            .toString();
    }
}
