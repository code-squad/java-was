package requestmapping;

import java.util.Map;

import util.HttpRequestUtils;
import util.StringUtils;
import util.HttpRequestUtils.RequestTypes;

public class RequestLine {
    
    String path;
    RequestTypes method;
    Map<String, String> params;
    
    public RequestLine(String line) {
        this.path = StringUtils.directoryFromRequestHeader(line);
        this.method = HttpRequestUtils.parseRequestType(line);
        this.params = HttpRequestUtils.parseQueryString(line);
    }
    
    public RequestLine(String path, RequestTypes type) {
        this.path = path;
        this.method = type;
    }

    public String getPath() {
        return path;
    }

    public RequestTypes getMethod() {
        return method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RequestLine other = (RequestLine) obj;
        if (method != other.method)
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RequestLine [path=" + path + ", method=" + method + ", params=" + params.values() + "]";
    }
    
    
    
    

}
