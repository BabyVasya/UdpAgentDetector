package org.example;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RawUdpSocketClient {
    private int port;
    private DatagramSocket client;

    private Thread getDThread;
    private byte[] packetData = new byte[500];
    @Getter
    private List<AgentInfoDto> actualListAgents = new ArrayList<>();
    private PcapHandle pcapHandle;

    @SneakyThrows
    public void initialize(int port ) {
        List<PcapNetworkInterface> allDevs = Pcaps.findAllDevs();
        PcapNetworkInterface networkInterface = null;
        for (PcapNetworkInterface allDev : allDevs) {
            if (allDev.getName().equals("\\Device\\NPF_Loopback")){
                networkInterface = allDev;
                break;
            }
        }
        pcapHandle = networkInterface.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 50);

    }

    @SneakyThrows
    public void send(String agentData) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeUTF(agentData);
        byte[] data = byteArrayOutputStream.toByteArray();
        DatagramPacket dp = new DatagramPacket(
                data,
                data.length,
                InetAddress.getLoopbackAddress(),
                1420
        );
        this.client.send(dp);
        log.info("Client send : " + new String(dp.getData()) +" data length " + data.length);
    }

    public void getDatagramPacket() {
        getDThread = new Thread(() -> {
            log.info("Получение клиентом пакетов");
            DatagramPacket packet = new DatagramPacket(packetData, packetData.length, InetAddress.getLoopbackAddress(), 1420);
            while (true) {
                try {
                    client.receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                byte[] data = packet.getData();
                String s = new String(data).replace("\000", "");
                actualListAgents.addAll(gson.fromJson(s, AgentInfoDto.class).getListAgents());
                log.info("Client recevied: " + gson.fromJson(s, AgentInfoDto.class).getListAgents().get(0).getAgentName());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getDThread.start();
    }
}
