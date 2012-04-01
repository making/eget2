package am.ik.eget2.hazelcast;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastInstanceFactory implements
        FactoryBean<HazelcastInstance>, InitializingBean {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(HazelcastInstanceFactory.class);

    private HazelcastInstance hazelcastInstance;

    private List<String> addresses;

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(addresses);
        ClientConfig clientConfig = new ClientConfig();
        for (String address : addresses) {
            clientConfig.addAddress(address);
        }
        LOGGER.trace("create hazelcast client");
        hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
        LOGGER.trace("created {}", hazelcastInstance);
    }

    @Override
    public HazelcastInstance getObject() throws Exception {
        return hazelcastInstance;
    }

    @Override
    public Class<?> getObjectType() {
        return HazelcastInstance.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
