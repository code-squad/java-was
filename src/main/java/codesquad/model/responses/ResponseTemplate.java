package codesquad.model.responses;

import java.io.DataOutputStream;

public interface ResponseTemplate {

    void header(DataOutputStream dos, HttpResponse httpResponse);

    void body(DataOutputStream dos, HttpResponse httpResponse);

}
