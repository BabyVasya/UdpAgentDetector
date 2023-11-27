package org.example;

import com.google.gson.Gson;
import jade.core.AID;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Slf4j
public class UdpSocketServer {
    private int agentDetectorPort;
    private List<AgentInfoDto> agentsList = new ArrayList<>();
    private byte[] packetData = new byte[150];
    private Thread recThread;
    private Thread answThread;

    private DatagramSocket server = new DatagramSocket(1421);

    public UdpSocketServer() throws SocketException {
    }

    @SneakyThrows
    public void start(int port) {
        recThread = new Thread(() -> {
            log.info("Server started");
            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket(port);
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                DatagramPacket packet = null;
                packet = new DatagramPacket(packetData, packetData.length, InetAddress.getLoopbackAddress(), 1421);
                try {
                    socket.receive(packet);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                this.agentDetectorPort = packet.getPort();
                byte[] data = packet.getData();
                byte[] newData = new byte[data.length-2];
                Gson gson = new Gson();
                System.arraycopy(data, 2, newData, 0, newData.length);
                String s = new String(newData).replace("\000", "");
                log.info("Server recevied: " + gson.fromJson(s, AgentInfoDto.class).getAgentName());
                if (!agentsList.contains(gson.fromJson(s, AgentInfoDto.class).getAgentName()) ) {
                    agentsList.add(gson.fromJson(s, AgentInfoDto.class));
                    log.info("Добавили " + agentsList.get(0).getAgentName());
                }
            }
        });
        recThread.start();
    }


    @SneakyThrows
    public void sendAgentList() {
        answThread = new Thread(()-> {
            while (true) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                Gson gson = new Gson();
                if (!agentsList.isEmpty()) {
                    try {
                        AgentInfoDto agentInfoDto = new AgentInfoDto();
                        agentInfoDto.setListAgents(agentsList);
                        dataOutputStream.writeUTF(gson.toJson(agentInfoDto));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    byte[] data = byteArrayOutputStream.toByteArray();
                    byte[] newData = new byte[data.length - 2];
                    System.arraycopy(data, 2, newData, 0, newData.length);
                    String s = new String(newData).replace("\000", "");
                    DatagramPacket dp = new DatagramPacket(
                            newData,
                            newData.length,
                            InetAddress.getLoopbackAddress(),
                            1200
                    );
                    try {
                        this.server.send(dp);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    log.info("Отправка назад parsed data: " + new String(dp.getData()) + " data length " + newData.length);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        answThread.start();
    }
}