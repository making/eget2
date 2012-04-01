package am.ik.eget2.hazelcast;

import org.springframework.beans.factory.InitializingBean;

abstract public class AbstractHazelcastCollection implements InitializingBean {
    private boolean clearOnInit = false;

    public void setClearOnInit(boolean clearOnInit) {
        this.clearOnInit = clearOnInit;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (clearOnInit) {
            clearCollection();
        }
    }

    abstract protected void clearCollection();

}
