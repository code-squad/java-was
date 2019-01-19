package codesquad.util.responses;

import codesquad.model.Header;

import java.io.DataOutputStream;

public interface Response {

    void header(DataOutputStream dos, Header header);

    void body(DataOutputStream dos);

}
