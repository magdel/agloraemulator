package ru.magdel.agloraemulator;

import ru.magdel.agloraemulator.emulator.DeviceTrack;
import ru.magdel.agloraemulator.emulator.AgloraClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeviceForm extends JFrame{


    private JPanel basePanel;
    private JButton buttonLocal;

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new DeviceForm().setVisible(true));
    }

    private JTextField textFieldId;
    private JTextField textFieldPort;
    private JLabel jLabel;
    private DeviceTrack devTrack;
    private AgloraClient client;
    public DeviceForm() {
        setTitle("AGLoRa Emulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(basePanel);
        pack();

        devTrack = new DeviceTrack(getClass().getResourceAsStream("/data/round1.txt"));
        devTrack.start();

        buttonLocal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client != null) {
                    devTrack.unregisterListener(client);
                    client.stop();
                    buttonLocal.setText("Start");
                    client = null;
                } else {
                    client = new AgloraClient(textFieldPort.getText(), textFieldId.getText(), jLabel);
                    devTrack.registerListener(client);
                    client.start();
                    buttonLocal.setText("Stop");
                }
            }
        });

    }

}
