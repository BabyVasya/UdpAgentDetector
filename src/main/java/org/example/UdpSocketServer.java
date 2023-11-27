package org.example;

import jade.core.AID;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Slf4j
public class UdpSocketServer {
    private int agentDetectorPort;
    private List<String> agentsList = new ArrayList<>();
    private byte[] packetData = new byte[6];
    private Thread recThread;
    private Thread answThread;

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
                agentsList.add(new String(data));
                String s = new String(data).replace("\000", "");
                log.info("Server recevied: " + agentsList);
            }
        });
        recThread.start();
    }


    @SneakyThrows
    public void sendAgentList() throws IOException{
            log.info("Начали отправку назад ");
                DatagramPacket packet = new DatagramPacket(
                        String.valueOf(agentsList).getBytes(),
                        String.valueOf(agentsList).getBytes().length,
                        InetAddress.getLoopbackAddress(),
                        this.agentDetectorPort
                );
                try {
                    DatagramSocket ds = new DatagramSocket(2500);
                    ds.send(packet);
                    log.info("Server sended answer back to client " + new String(packet.getData()));
                } catch (SocketException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    try {
                        throw new IOException(e);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                log.info(new String(packet.getData()) + "");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }
}