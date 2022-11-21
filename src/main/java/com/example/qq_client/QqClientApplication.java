package com.example.qq_client;

import com.alibaba.fastjson2.JSON;
import com.example.qq_client.client.ClientTCPReceiver;
import com.example.qq_client.model.Message;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

@SpringBootApplication
public class QqClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(QqClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入服务器IP地址：");
        String ip = scanner.next();
        System.out.println("请输入监听的端口号：");
        int port = scanner.nextInt();
        System.out.println("请输入您的昵称：");
        String nickName = scanner.next();

        try {
            Socket socket = new Socket(ip,port);
            ClientTCPReceiver clientTCPReceiver = new ClientTCPReceiver(socket);
            new Thread(clientTCPReceiver).start();
            Message message = new Message(nickName,"SYSTEM","LOGIN");
            byte[] messageBytes = JSON.toJSONString(message).getBytes();
            socket.getOutputStream().write(messageBytes);
            while(true){
                System.out.println("请输入需要接收消息的昵称：");
                String receiver = scanner.next();
                System.out.println("请输入需要发送的信息:");
                String message1 = scanner.next();
                Message message2 = new Message(nickName,receiver,message1);
                byte[] message2Bytes = JSON.toJSONString(message2).getBytes();
                socket.getOutputStream().write(message2Bytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
