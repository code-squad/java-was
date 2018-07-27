package webserver;

public interface BeanResolver {
    Class<?> resolve(Request request);
}
