package am.ik.eget2.task.anypage;

import java.io.Serializable;

public class AnyPage implements Serializable {
    private static final long serialVersionUID = 571169050195272196L;
    private String url;

    public AnyPage() {
    }

    public AnyPage(String url) {
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "AnyPage [url=" + url + "]";
    }
}
