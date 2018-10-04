package domain;

import java.util.ArrayList;
import java.util.List;

import static util.HttpRequestUtils.Pair;

public class Headers {
    private List<Pair> pairs = new ArrayList<>();

    public String getValue(String key) {
        // TODO 빈 객체를 리턴하던가, Optional객체를 리턴하던가
        // TODO Optional 덕분에 NullObjectPattern
//        if(pairs.stream()
//                .anyMatch(pair -> pair.getKey().equals(key))) {
//
//        }
        return pairs.stream().filter(pair -> pair.getKey().equals(key)).findAny().map(Pair::getValue).orElse("");
    }

    public void add(Pair pair) {
        pairs.add(pair);
    }

    @Override
    public String toString() {
        return "Headers{" +
                "pairs=" + pairs +
                '}';
    }
}
