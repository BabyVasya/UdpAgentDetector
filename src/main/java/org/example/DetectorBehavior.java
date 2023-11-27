package org.example;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class DetectorBehavior extends TickerBehaviour {

    public DetectorBehavior(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        List<AID> aids = AnyAgents.aids;
        log.info("DF " + aids);
        AgentDetector agentDetector = new AgentDetector();
        if(aids!=null) {
            agentDetector.startDiscovering(1420);
            int port = 1200;
            for (AID aid : aids) {
                log.info("adding AID " + aid);
                agentDetector.startPublishing(aid, port);
                port++;
            }

        }
    }
}
