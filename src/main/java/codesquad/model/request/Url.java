package codesquad.model.request;

import org.slf4j.Logger;

import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class Url {
    private static final Logger log = getLogger(Url.class);

    private HttpMethod httpMethod;

    private String accessPath;

    public Url(HttpMethod httpMethod, String accessPath) {
        this.httpMethod = httpMethod;
        this.accessPath = accessPath;
    }

    public String getAccessPath() {
        return this.accessPath;
    }

    public void renewAccessPath(String newAccessPath) {
        this.accessPath = newAccessPath;
    }

    @Override
    public String toString() {
        return "Url{" +
                "httpMethod=" + httpMethod +
                ", accessPath='" + accessPath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Url url = (Url) o;
        return httpMethod == url.httpMethod &&
                accessPath.equals(url.accessPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpMethod, accessPath);
    }
}
