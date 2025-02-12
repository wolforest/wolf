package cn.coderule.framework.layer.web.auth.session;

import jakarta.servlet.ServletRequestEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextListener;

@Slf4j
public class SessionSaver extends RequestContextListener {
    private final Session session;

    public SessionSaver(Session session) {
        this.session = session;
    }

    @Override
    public void requestDestroyed(ServletRequestEvent requestEvent) {
        super.requestDestroyed(requestEvent);
        session.save();
    }
}
