package org.example;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
@Slf4j
public class UdpSocketClient {
    private int port;
    private DatagramSocket client;

    private Thread getDThread;
    private byte[] packetData = new byte[500];

    @SneakyThrows
    public void initialize(int port ) {
        this.port = port;
        client = new DatagramSocket(port);
    }

    @SneakyThrows
    public void sendDatagramPacket(String data) {
        DatagramPacket dp = new DatagramPacket(
                data.getBytes(),
                data.getBytes().length,
                InetAddress.getLoopbackAddress(),
                1420
        );
        this.client.send(dp);
        log.info("Datagram sended " + dp.getAddress() + ":"+ dp.getPort());
    }

    public void getDatagramPacket(int port) {
        getDThread = new Thread(() -> {
            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket(port);
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }
            DatagramPacket packet = new DatagramPacket(packetData, packetData.length);
            while (true) {
                try {
                    socket.receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] data = packet.getData();
                String s = new String(data).replace("\000", "");
                System.out.println("Server recevied: " + packet);
            }
        });
        getDThread.start();
    }
}
