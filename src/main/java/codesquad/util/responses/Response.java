package codesquad.util.responses;

import codesquad.model.Url;

import java.io.DataOutputStream;

public interface Response {

    void header(DataOutputStream dos, Url url);

    void body(DataOutputStream dos);

}
