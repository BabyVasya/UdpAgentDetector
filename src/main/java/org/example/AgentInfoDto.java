package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AgentInfoDto {
    @Getter
    @Setter
    private String agentName;

    @Getter
    @Setter
    private List<AgentInfoDto> listAgents;
}
