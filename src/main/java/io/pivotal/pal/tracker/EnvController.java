package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {

    private String m_port;
    private String m_memoryLimit;
    private String m_instanceIndex;
    private String m_instanceAddress;



    public EnvController( @Value("${PORT:NOT SET}") String port, @Value("${MEMORY_LIMIT:NOT SET}") String memoryLimit, @Value("${CF_INSTANCE_INDEX:NOT SET}") String instanceIndex, @Value("${CF_INSTANCE_ADDR:NOT SET}") String instanceAddress ){

        m_port = port;
        m_memoryLimit = memoryLimit;
        m_instanceAddress = instanceAddress;
        m_instanceIndex = instanceIndex;

    }

    @GetMapping("/env")
    public Map<String, String> getEnv(){

        Map<String, String> theEnvMap = new HashMap<String, String>();
        theEnvMap.put("PORT", m_port);
        theEnvMap.put("MEMORY_LIMIT", m_memoryLimit);
        theEnvMap.put("CF_INSTANCE_INDEX", m_instanceIndex);
        theEnvMap.put("CF_INSTANCE_ADDR", m_instanceAddress);

        return theEnvMap;
    }

}
