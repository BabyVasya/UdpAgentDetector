package org.example;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class JadeStarter {
    @Getter
    private static AgentContainer container;

    @SneakyThrows
    public static void main(String[] args) throws InterruptedException {
        Map<String, Class<?>> agents = Map.of(
                "Agent1", AgentDetector.class,
                "AgentDetector", AgentDetector.class

        );

        Properties props = new ExtendedProperties();
        props.setProperty("gui", "true");
        props.setProperty("agents", addAgents(agents));
        props.setProperty("services", addServices(List.of("jade.core.messaging.TopicManagementService")));
        ProfileImpl p = new ProfileImpl(props);

        Runtime.instance().setCloseVM(true);
        container = Runtime.instance().createMainContainer(p);
    }

    private static String addAgents(Map<String, Class<?>> createAgents){
        String outString = "";
        for (Map.Entry<String, Class<?>> entry : createAgents.entrySet()) {
            outString += entry.getKey()+":"+entry.getValue().getName()+";";
        }
        System.out.println(outString);
        return outString;
    }

    private static String addServices(List<String> services) {
        String outString ="";
        for (String service : services) {
            outString+=service+";";
        }
        return outString;
    }

}
