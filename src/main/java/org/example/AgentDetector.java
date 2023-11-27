package org.example;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class AgentDetector extends Agent implements AgentDetectorInterface {

    protected void setup() {
        // Регистрация для получения уведомлений о добавлении агентов
        addBehaviour(new DetectorBehavior(this, 2000));
    }

    @SneakyThrows
    @Override
    public void startPublishing(int port) {
        var agentPublisher = new UdpSocketClient();
        var server = new UdpSocketServer();
        agentPublisher.initialize(port);
        log.info("Initialized");
        server.start(1420);
        String agent;
        int i =0;
        while (true) {
        }
    }

    @Override
    public void startDiscovering(int port) {

    }

    @Override
    public List<AID> getActiveAgents() {
        return null;
    }
}
