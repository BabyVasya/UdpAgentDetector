package org.example;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class DetectorBehavior extends TickerBehaviour {
    private List<AID> aids = new ArrayList<>();

    public DetectorBehavior(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        aids.add(myAgent.getAID());
        for (AID agent : aids) {
            log.info("Existing Agent: " + agent);
        }
    }
}
