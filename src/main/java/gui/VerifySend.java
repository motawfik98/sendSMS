package gui;

import main.Employee;
import restapi.ApacheHttpPost;
import sendsms.SendTwilioSMS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VerifySend extends ShowTableFrame {
    private JButton btnSend = new JButton("Send");
    private Container container;
    private String[] salaries;
    private Employee[] IDNamesPhones;

    public VerifySend(Object[][] data, String[] columns, Employee[] employees, String[] salaries) {
        super(data, columns, 0);

        this.IDNamesPhones = employees;
        this.salaries = salaries;

        container = getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(btnSend);
//        btnSend.setSize(200, 200);
        btnSend.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
//                SendMessagesViaTwilio();
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to send ?", "Question", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.NO_OPTION)
                    return;
                sendMessagesViaSMSBulko();
            }
        });
    }

    private void SendMessagesViaTwilio() {
        SendTwilioSMS sendTwilioSMS = new SendTwilioSMS(VerifySend.this.IDNamesPhones, VerifySend.this.salaries);
        int success = sendTwilioSMS.sendMessage();
        int total = VerifySend.this.IDNamesPhones.length;
        if (total == success) {
            JOptionPane.showConfirmDialog(null,
                    "All " + total + " messages were send", "Success", JOptionPane.DEFAULT_OPTION);
        } else {
            JOptionPane.showConfirmDialog(null,
                    "Only " + success + " out of " + total + " were send", "Error", JOptionPane.DEFAULT_OPTION);
        }
    }

    private void sendMessagesViaSMSBulko() {
        ApacheHttpPost apacheHttpPost = new ApacheHttpPost(VerifySend.this.IDNamesPhones, VerifySend.this.salaries);
        apacheHttpPost.sendMessages();
        int total = VerifySend.this.IDNamesPhones.length;
        int success = apacheHttpPost.getSuccessLength();
        String outputs = apacheHttpPost.getOutput();

        if (total == success) {
            JOptionPane.showConfirmDialog(null,
                    outputs, "Success", JOptionPane.DEFAULT_OPTION);
        } else {
            JOptionPane.showConfirmDialog(null,
                    outputs, "Error", JOptionPane.DEFAULT_OPTION);

        }
    }


}
