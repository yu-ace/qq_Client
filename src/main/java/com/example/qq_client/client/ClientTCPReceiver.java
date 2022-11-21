package com.example.qq_client.client;

import com.alibaba.fastjson2.JSON;
import com.example.qq_client.model.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientTCPReceiver implements Runnable{
    Socket socket;
    public ClientTCPReceiver(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        while(true){
            try {
                byte[] buffer = new byte[1024];
                InputStream in = socket.getInputStream();
                in.read(buffer);
                String message = new String(buffer);
                Message message1 = JSON.parseObject(message.trim(),Message.class);
                if("SYSTEM".equals(message1.getNickName())){
                    if("BYE".equals(message1.getMessage())){
                        System.out.printf("服务器已关闭连接：%s再见",message1.getReceiverName());
                        System.exit(0);
                    }else{
                        System.out.printf("来自系统的消息：%s\n",message1.getMessage());
                    }
                }else{
                    System.out.printf("来自%s的信息：%s\n",message1.getNickName(),message1.getMessage());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
