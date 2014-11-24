package de.unixhelden;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.Properties;

/**
 * Created by sheld on 22.11.2014.
 */
public class SftpExample {

    private static Properties prop;
    private static String propFileName = "src/main/resources/sftp.properties";

    private static String user;
    private static String host;
    private static Integer port;
    private static String keyFile;

    private static JSch jSch;

    public static void main(String [] args) {
        try {
            loadProperties();
        } catch (IOException e) {
            System.out.println("Kann die Datei nicht finden " + propFileName + " bitte prüfen");
        }

        try {
            jSch = new JSch();

            // Das Key File laden.
            jSch.addIdentity(keyFile);


            Session session = jSch.getSession(user, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            System.out.println("session connected.....");


            ChannelSftp sftpChannel = (ChannelSftp) channel;

            File file = new File("src/hello.txt");
            sftpChannel.put(new FileInputStream(file), "hello.txt");
            System.out.println("shell channel connected....");
            sftpChannel.exit();
            session.disconnect();
            System.out.println("File transfered");

        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Laden der Einstellungen aus einer Properties
     *
     * @throws IOException
     */
    private static void loadProperties() throws IOException {

        // create the Properties Object.
        prop = new Properties();

        // Create InputStream and load it.
        InputStream inputStream = new FileInputStream(propFileName);
        prop.load(inputStream);
        inputStream.close();

        user = prop.getProperty("user");
        host = prop.getProperty("host");

        port = Integer.parseInt(prop.getProperty("port"));
        keyFile = prop.getProperty("keyfile");
    }

}
