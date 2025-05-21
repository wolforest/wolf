package cn.coderule.common.util.lang.bean;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public class ExceptionUtil {

    public static String getName(Throwable e) {
        String className = e.getClass().getSimpleName();
        return className.replace("Exception", "");
    }

    public static Throwable getRealException(Throwable throwable) {
        if (throwable instanceof CompletionException || throwable instanceof ExecutionException) {
            if (throwable.getCause() != null) {
                throwable = throwable.getCause();
            }
        }
        return throwable;
    }

    public static String getErrorDetailMessage(Throwable t) {
        if (t == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(t.getMessage()).append(". ").append(t.getClass().getSimpleName());

        if (t.getStackTrace().length > 0) {
            sb.append(". ").append(t.getStackTrace()[0]);
        }
        return sb.toString();
    }
}
