package org.example;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.Agent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class AgentDetector extends Agent implements AgentDetectorInterface {
    private List<AgentInfoDto> agentInfoDtoList = new ArrayList<>();
    protected void setup() {
        addBehaviour(new DetectorBehavior(this, 2000));
    }

    @SneakyThrows
    @Override
    public void startPublishing(AID aid, int port) {
        Gson gson = new Gson();
        AgentInfoDto agentInfoDto = new AgentInfoDto();
        agentInfoDto.setAgentName(aid.getName());
        var agentPublisher = new RawUdpSocketClient();
        agentPublisher.initialize(port);

            log.info(gson.toJson(agentInfoDto));
            agentPublisher.send(gson.toJson(agentInfoDto));
            agentPublisher.getDatagramPacket();
            if (!agentPublisher.getActualListAgents().isEmpty()) {
                agentInfoDtoList = agentPublisher.getActualListAgents();
                log.info("Метка получения " + String.valueOf(agentInfoDtoList.get(0).getAgentName()));
            }
            Thread.sleep(2000);

    }

    @SneakyThrows
    @Override
    public void startDiscovering(int port) {
        var server = new UdpSocketServer();
        server.start(port);
        server.sendAgentList();
    }

    @Override
    public List<AID> getActiveAgents() {
        return null;
    }
}
