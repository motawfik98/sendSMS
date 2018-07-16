package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FollowUpFrame extends JFrame {
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JButton btnOK = new JButton("OK");
    private JLabel lblError;
    private String windowName, message;

    public FollowUpFrame(String name, String message) {
        init(name, message);

    }

    public FollowUpFrame(String name, String[] message) {
        StringBuilder allMessages = new StringBuilder();
        for (String m : message)
            allMessages.append(m).append("\n");

        init(name, allMessages.toString());
    }

    private void init(String name, String message) {
        this.windowName = name;
        this.message = message;
        lblError = new JLabel(message);

        setTitle(name);
        setSize(400, 100);
        setLocation(500, 250);

        mainPanel.add(btnOK, BorderLayout.SOUTH);
        lblError.setHorizontalTextPosition(SwingConstants.CENTER);
        lblError.setVerticalTextPosition(SwingConstants.CENTER);
        mainPanel.add(lblError);
        JScrollPane scrollPane = new JScrollPane(mainPanel);
//        scrollPane.setPreferredSize(new Dimension(400, 100));
        add(scrollPane);

//        setContentPane(mainPanel);


        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeFrame();
            }
        });
    }

    private void closeFrame() {
        this.dispose();
    }
}
