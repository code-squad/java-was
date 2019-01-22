package codesquad.model.responses;

import java.io.DataOutputStream;

public interface ResponseTemplate {

    void header(DataOutputStream dos, Response response);

    void body(DataOutputStream dos, Response response);

}
