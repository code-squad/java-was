package codesquad.util.responses;

import codesquad.model.Request;
import codesquad.model.Url;

import java.io.DataOutputStream;

public interface Response {

    void header(DataOutputStream dos, Request request);

    void body(DataOutputStream dos);

}
