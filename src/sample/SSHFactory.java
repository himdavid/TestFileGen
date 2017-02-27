package sample;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SSHFactory {
    public SSHFactory() {
    }

    public Session connectToSSH(String host, int port) {
        Session session = null;

        try {
            JSch e = new JSch();
            session = e.getSession("dhim", host, port);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword("2E&d47Hqm");
            session.connect(30000);
        } catch (Exception var6) {
            System.out.println(var6);
        }

        return session;
    }

    public String sendCommand(Session session, String command) {
        StringBuilder outputBuffer = new StringBuilder();
        String terminalOutput = null;

        try {
            Channel e = session.openChannel("exec");
            ((ChannelExec)e).setCommand(command);
            InputStream commandOutput = e.getInputStream();
            e.connect();

            for(int readByte = commandOutput.read(); readByte != -1; readByte = commandOutput.read()) {
                outputBuffer.append((char)readByte);
            }

            e.disconnect();
            session.disconnect();
            terminalOutput = outputBuffer.toString();
            System.out.println(terminalOutput);
        } catch (JSchException var8) {
            var8.printStackTrace();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

        return terminalOutput;
    }
}
