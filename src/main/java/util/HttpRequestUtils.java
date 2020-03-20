package util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Http request utils.
 */
public class HttpRequestUtils {
  /**
   * @param queryString은
   *            URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
   * @return
   */
  public static Map<String, String> parseQueryString(String queryString) {
    return parseValues(queryString, "&");
  }

  public static Map<String, String> parseUriString(String uriString) {
    return parseValues(uriString, "&");
  }

  /**
   * @param 쿠키
   *            값은 name1=value1; name2=value2 형식임
   * @return
   */
  public static Map<String, String> parseCookies(String cookies) {
    return parseValues(cookies, ";");
  }

  public static Map<String, String> parseRequestBody(String requestBody) {
    return parseValues(requestBody, "&");
  }

  private static Map<String, String> parseValues(String values, String separator) {
    if (Strings.isNullOrEmpty(values)) {
      return Maps.newHashMap();
    }

    String[] tokens = values.split(separator);
    return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(p -> p != null)
        .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
  }

  static Pair getKeyValue(String keyValue, String regex) {
    if (Strings.isNullOrEmpty(keyValue)) {
      return null;
    }

    String[] tokens = keyValue.split(regex);
    if (tokens.length != 2) {
      return null;
    }

    return new Pair(tokens[0], tokens[1]);
  }

  /**
   *  Feat : requestLine 을 파싱해줍니다.
   *  Desc :
   *  Return : Pair[]
   */
  public static Pair[] parseRequestLine(String requestLine) {
    Pair[] returnPairs = new Pair[3];
    String[] parseRequestLine = requestLine.split(" ");
    returnPairs[0] = new Pair("method", parseRequestLine[0]);
    returnPairs[1] = new Pair("requestUrl", parseRequestLine[1]);
    returnPairs[2] = new Pair("protocol", parseRequestLine[2]);

    return returnPairs;
  }

  public static Pair parseHeader(String header) {
    return getKeyValue(header, ": ");
  }

  /**
   * Feat : regex 를 기준으로 Map 을 리턴 합니다.
   * Desc : Header 를 파싱하기 위해 사용합니다.
   * Return : Map<String, String>
   */
  public static Map<String, String> getKeyValueMap(String keyValue, String regex) {
    if (Strings.isNullOrEmpty(keyValue)) {
      return new HashMap<>();
    }

    String[] tokens = keyValue.split(regex);
    if (tokens.length != 2) {
      return new HashMap<>();
    }

    Map<String, String> returnMap = new HashMap<>();
    returnMap.put(tokens[0], tokens[1]);

    return returnMap;
  }

  public static class Pair {
    String key;
    String value;

    Pair(String key, String value) {
      this.key = key.trim();
      this.value = value.trim();
    }

    public String getKey() {
      return key;
    }

    public String getValue() {
      return value;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((key == null) ? 0 : key.hashCode());
      result = prime * result + ((value == null) ? 0 : value.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      Pair other = (Pair) obj;
      if (key == null) {
        if (other.key != null) {
          return false;
        }
      } else if (!key.equals(other.key)) {
        return false;
      }
      if (value == null) {
        if (other.value != null) {
          return false;
        }
      } else if (!value.equals(other.value)) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return "Pair [key=" + key + ", value=" + value + "]";
    }
  }
}
