package org.example;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class AnyAgents extends Agent {

    public static final List<AID> aids = new ArrayList<>();
    @Override
    protected void setup() {
        aids.add(getAID());
        log.info("AIds " + aids);
    }
}
