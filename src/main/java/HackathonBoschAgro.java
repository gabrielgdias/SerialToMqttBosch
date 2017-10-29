import jssc.SerialPort;
import jssc.SerialPortException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;

public class HackathonBoschAgro {
    static    String topic        = "Asper MQTT example";
    static int qos             = 2;
//    static String content      = "Message from MqttPublishSample";
    static String broker       = "tcp://localhost:1883";
    static    String clientId     = "AsperDemo";
    static MemoryPersistence persistence = new MemoryPersistence();


    public static void main(String[] args) throws SerialPortException, MqttException {
        MqttClient sampleClient = new HackathonBoschAgro().buildClient();

        SerialPort serialPort = new SerialPort("/dev/ttyACM0");
        serialPort.openPort();//Open serial port
        serialPort.setParams(115200, 8, 1, 0);//Set params.
        while(true) {
            byte[] buffer = serialPort.readBytes();
////            System.out.println(serialPort.readBytes());
            if(buffer!=null) {
////                for(byte b:buffer) {
//                    String str = new String(buffer, StandardCharsets.UTF_8);

                MqttMessage message = new MqttMessage(buffer);
                message.setQos(qos);

                sampleClient.publish(topic, message);

//                System.out.println(str);
////                    System.out.println(b);
                }
            }

    }

    private MqttClient buildClient() {
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            sampleClient.connect(connOpts);
            return sampleClient;
        } catch(MqttException me) {
            me.printStackTrace();
        }
        return null;
    }
    }
//}
