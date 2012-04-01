package am.ik.eget2.task.specificpage;

import java.io.Serializable;

public class SpecificPage implements Serializable {
    private static final long serialVersionUID = 3279458354754104620L;

    private String url;

    public SpecificPage() {
    }

    public SpecificPage(String url) {
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
        return "SpecificPage [url=" + url + "]";
    }
}
