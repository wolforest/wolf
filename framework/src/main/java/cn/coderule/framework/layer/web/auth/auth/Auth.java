package cn.coderule.framework.layer.web.auth.auth;

import cn.coderule.common.lang.exception.api.NoPermissionException;
import cn.coderule.common.lang.exception.api.NotLoggedInException;
import cn.coderule.common.util.lang.collection.CollectionUtil;
import cn.coderule.common.util.net.AntPathUtil;
import cn.coderule.framework.layer.web.auth.model.Space;
import cn.coderule.framework.layer.web.auth.session.Session;
import cn.coderule.framework.layer.web.auth.model.User;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;

public class Auth {
    private final Session session;
    private final AuthConfig config;

    public Auth(Session session, AuthConfig config) {
        this.session = session;
        this.config = config;
    }

    public Long getAccountId() {
        if (!isLogin()) {
            throw new NotLoggedInException();
        }

        return session.get(config.getAccountIdKey(), Long.class);
    }

    public User getUser() {
        if (!isLogin()) {
            throw new NotLoggedInException();
        }

        return session.get(config.getAccountKey(), User.class);
    }

    public Space getSpace() {
        if (!isLogin()) {
            throw new NoPermissionException();
        }

        return session.get(config.getSpaceKey(), Space.class);
    }

    public Long getSpaceId() {
        if (!isLogin()) {
            throw new NoPermissionException();
        }

        return session.get(config.getSpaceIdKey(), Long.class);
    }

    public boolean isLogin() {
        Long accountId = session.get(config.getAccountIdKey(), Long.class);
        return null != accountId && accountId > 0;
    }

    public boolean isLogin(Long spaceId) {
        if (!isLogin()) {
            return false;
        }
        Long sessionSpaceId = session.get(config.getSpaceIdKey(), Long.class);
        return spaceId != null && spaceId.equals(sessionSpaceId);
    }

    public void login(@NonNull User user) {
        session.set(config.getAccountIdKey(), user.getAccountId());
        session.set(config.getAccountKey(), user);
        session.save();
    }

    public void login(@NonNull Space space) {
        session.set(config.getSpaceIdKey(), space.getSpaceId());
        session.set(config.getSpaceKey(), space);
        session.save();
    }

    public void logout() {
        session.set(config.getAccountIdKey(), null);
        session.set(config.getSpaceIdKey(), null);
        session.save();
    }

    public void logout(Long spaceId) {
        if (null == spaceId) {
            return;
        }

        Long sessionSpaceId = session.get(config.getSpaceIdKey(), Long.class);
        if (!spaceId.equals(sessionSpaceId)) {
            return;
        }

        session.set(config.getSpaceIdKey(), null);
        session.set(config.getSpaceKey(), null);
        session.save();
    }

    public boolean isNeedAuth(String path) {
        if (isExcludePath(path)) {
            return false;
        }

        return isAuthPath(path);
    }

    public boolean isAuthPath(String path) {
        if (CollectionUtil.isEmpty(config.getAuthPath())) {
            return true;
        }

        for (String pattern : config.getAuthPath()) {
            boolean isMatch = AntPathUtil.match(pattern, path);
            if (isMatch) {
                return true;
            }
        }

        return false;
    }

    public boolean isExcludePath(String path) {
        for (String pattern : config.getExcludePath()) {
            boolean isMatch = AntPathUtil.match(pattern, path);
            if (isMatch) {
                return true;
            }
        }

        return false;
    }

    public void accessDeny(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");

        String data = """
            {
                "success": true,
                "codeType": 30,
                "code": "%s",
                "message": "%s",
                "data": ""
            }
            """.formatted(config.getDenyCode(), config.getDenyMessage());

        response.getWriter().println(data);
    }


}
