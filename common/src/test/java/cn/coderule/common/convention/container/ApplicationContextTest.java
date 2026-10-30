package cn.coderule.common.convention.container;

import cn.coderule.common.lang.exception.lang.ClassNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ApplicationContextTest {

    static class Foo {
    }

    static class Bar {
    }

    interface Service {
    }

    static class ServiceImpl implements Service {
    }

    @Test
    public void registerAndGetInDefaultNamespace() {
        ApplicationContext context = new ApplicationContext();
        Foo foo = new Foo();

        context.register(foo);

        assertSame(foo, context.getBean(Foo.class));
    }

    @Test
    public void registerAndGetInNamespace() {
        ApplicationContext context = new ApplicationContext();
        Foo foo = new Foo();

        context.register(foo, "tenant");

        assertSame(foo, context.getBean(Foo.class, "tenant"));
    }

    @Test
    public void registerWithSuperTypeInNamespace() {
        ApplicationContext context = new ApplicationContext();
        ServiceImpl impl = new ServiceImpl();

        context.register(impl, Service.class, "tenant");

        assertSame(impl, context.getBean(Service.class, "tenant"));
    }

    @Test
    public void isolatedBetweenNamespaces() {
        ApplicationContext context = new ApplicationContext();
        context.register(new Foo(), "a");

        assertThrows(ClassNotFoundException.class, () -> context.getBean(Foo.class, "b"));
    }

    @Test
    public void isolatedFromDefaultNamespace() {
        ApplicationContext context = new ApplicationContext();
        context.register(new Foo(), "a");

        assertThrows(ClassNotFoundException.class, () -> context.getBean(Foo.class));
    }

    @Test
    public void returnNullWhenNotFoundIsDisabled() {
        ApplicationContext context = new ApplicationContext().throwOnNotFound(false);

        assertNull(context.getBean(Foo.class));
        assertNull(context.getBean(Foo.class, "tenant"));
    }

    @Test
    public void mergeMergesWithoutOverwrite() {
        ApplicationContext parent = new ApplicationContext();
        Foo parentFoo = new Foo();
        parent.register(parentFoo);

        ApplicationContext sub = new ApplicationContext();
        Foo subFoo = new Foo();
        sub.register(subFoo);

        parent.merge(sub);

        Set<Object> beans = parent.getBeanMap().get(Foo.class.getName());
        assertEquals(2, beans.size());
        assertTrue(beans.contains(parentFoo));
        assertTrue(beans.contains(subFoo));
    }

    @Test
    public void mergeIntoNamespace() {
        ApplicationContext sub = new ApplicationContext();
        Foo subFoo = new Foo();
        sub.register(subFoo);

        ApplicationContext parent = new ApplicationContext();
        parent.merge(sub, "tenant");

        assertSame(subFoo, parent.getBean(Foo.class, "tenant"));
        assertThrows(ClassNotFoundException.class, () -> parent.getBean(Foo.class));
    }

    @Test
    public void registerWithSuperTypeInDefaultNamespace() {
        ApplicationContext context = new ApplicationContext();
        ServiceImpl impl = new ServiceImpl();

        context.register(impl, Service.class);

        assertSame(impl, context.getBean(Service.class));
    }

    @Test
    public void throwOnNotFoundByDefault() {
        ApplicationContext context = new ApplicationContext();

        assertTrue(context.isThrowOnNotFound());
        assertThrows(ClassNotFoundException.class, () -> context.getBean(Foo.class));
    }

    @Test
    public void wrongTypeBeanThrowsClassNotFound() {
        ApplicationContext context = new ApplicationContext();
        context.register("not a foo", Foo.class);

        assertThrows(ClassNotFoundException.class, () -> context.getBean(Foo.class));
    }

    @Test
    public void wrongTypeBeanReturnsNullWhenNotFoundIsDisabled() {
        ApplicationContext context = new ApplicationContext().throwOnNotFound(false);
        context.register("not a foo", Foo.class);

        assertNull(context.getBean(Foo.class));
    }

    @Test
    public void registerAllIntoDefaultNamespace() {
        ApplicationContext context = new ApplicationContext();
        Foo foo = new Foo();

        context.registerAll(beanMap(Foo.class, foo));

        assertSame(foo, context.getBean(Foo.class));
    }

    @Test
    public void registerAllIntoNamespace() {
        ApplicationContext context = new ApplicationContext();
        Foo foo = new Foo();

        context.registerAll(beanMap(Foo.class, foo), "tenant");

        assertSame(foo, context.getBean(Foo.class, "tenant"));
        assertThrows(ClassNotFoundException.class, () -> context.getBean(Foo.class));
    }

    @Test
    public void registerAllReplacesBeansOfSameClass() {
        ApplicationContext context = new ApplicationContext();
        context.register(new Foo());

        Foo replaced = new Foo();
        context.registerAll(beanMap(Foo.class, replaced));

        Set<Object> beans = context.getBeanMap().get(Foo.class.getName());
        assertEquals(1, beans.size());
        assertTrue(beans.contains(replaced));
    }

    @Test
    public void mergeEmptySubContextIsNoop() {
        ApplicationContext parent = new ApplicationContext();
        Foo foo = new Foo();
        parent.register(foo);

        parent.merge(new ApplicationContext());

        assertSame(foo, parent.getBean(Foo.class));
    }

    @Test
    public void blankNamespaceIsRejected() {
        ApplicationContext context = new ApplicationContext();
        Foo foo = new Foo();

        assertThrows(IllegalArgumentException.class, () -> context.register(foo, (String) null));
        assertThrows(IllegalArgumentException.class, () -> context.register(foo, ""));
        assertThrows(IllegalArgumentException.class, () -> context.register(foo, "   "));
        assertThrows(IllegalArgumentException.class, () -> context.getBean(Foo.class, (String) null));
        assertThrows(IllegalArgumentException.class, () -> context.getBean(Foo.class, "   "));
    }

    @Test
    public void namespaceIsTrimmed() {
        ApplicationContext context = new ApplicationContext();
        Foo foo = new Foo();

        context.register(foo, "  tenant  ");

        assertSame(foo, context.getBean(Foo.class, "tenant"));
    }

    private static Map<String, Set<Object>> beanMap(Class<?> clazz, Object... beans) {
        Map<String, Set<Object>> map = new HashMap<>();
        map.put(clazz.getName(), new HashSet<>(Arrays.asList(beans)));
        return map;
    }

}
