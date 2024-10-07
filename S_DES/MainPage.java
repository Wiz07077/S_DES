package S_DES;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//普通加密解密
class S_Des_Normal extends JFrame {

    private JTextField keyField;
    private JTextField inputField;
    private JTextField outputField;
    private JButton actionButton;
    private JButton backButton;
    private JButton generateKeyButton;
    private JRadioButton encryptRadio;
    private JRadioButton decryptRadio;
    private ButtonGroup modeGroup;

    public S_Des_Normal() {
        setTitle("S-DES 加密解密");
        setSize(340, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 设置背景图片
        JPanel backgroundPanel = new JPanel() {
            ImageIcon backgroundImage = new ImageIcon("bg.jpg");
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        setContentPane(backgroundPanel);

        // 采用SpringLayout布局
        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        // 输入与输出组件
        JLabel keyLabel = new JLabel("     密钥 (10bits):");
        keyField = new JTextField(15);

        JLabel inputLabel = new JLabel("     输入 ( 8bits): ");
        inputField = new JTextField(15);

        JLabel outputLabel = new JLabel("     输出结果:      ");
        outputField = new JTextField(15);
        outputField.setEditable(false);

        // 操作选择按钮
        encryptRadio = new JRadioButton("加密", true);
        decryptRadio = new JRadioButton("解密");
        modeGroup = new ButtonGroup();
        modeGroup.add(encryptRadio);
        modeGroup.add(decryptRadio);

        actionButton = new JButton("执行");
        generateKeyButton = new JButton("随机生成密钥");
        backButton = new JButton("返回");

        // 添加组件到窗口
        add(keyLabel);
        add(keyField);
        add(inputLabel);
        add(inputField);
        add(outputLabel);
        add(outputField);
        add(encryptRadio);
        add(decryptRadio);
        add(actionButton);
        add(generateKeyButton);
        add(backButton);

        // 设置SpringLayout布局
        layout.putConstraint(SpringLayout.WEST, keyLabel, 10, SpringLayout.WEST, this.getContentPane());
        layout.putConstraint(SpringLayout.NORTH, keyLabel, 10, SpringLayout.NORTH, this.getContentPane());
        layout.putConstraint(SpringLayout.WEST, keyField, 10, SpringLayout.EAST, keyLabel);
        layout.putConstraint(SpringLayout.NORTH, keyField, 0, SpringLayout.NORTH, keyLabel);

        layout.putConstraint(SpringLayout.WEST, inputLabel, 10, SpringLayout.WEST, this.getContentPane());
        layout.putConstraint(SpringLayout.NORTH, inputLabel, 20, SpringLayout.SOUTH, keyLabel);
        layout.putConstraint(SpringLayout.WEST, inputField, 10, SpringLayout.EAST, inputLabel);
        layout.putConstraint(SpringLayout.NORTH, inputField, 0, SpringLayout.NORTH, inputLabel);

        layout.putConstraint(SpringLayout.WEST, outputLabel, 10, SpringLayout.WEST, this.getContentPane());
        layout.putConstraint(SpringLayout.NORTH, outputLabel, 20, SpringLayout.SOUTH, inputLabel);
        layout.putConstraint(SpringLayout.WEST, outputField, 10, SpringLayout.EAST, outputLabel);
        layout.putConstraint(SpringLayout.NORTH, outputField, 0, SpringLayout.NORTH, outputLabel);

        layout.putConstraint(SpringLayout.WEST, encryptRadio, 85, SpringLayout.WEST, this.getContentPane());
        layout.putConstraint(SpringLayout.NORTH, encryptRadio, 20, SpringLayout.SOUTH, outputLabel);
        layout.putConstraint(SpringLayout.WEST, decryptRadio, 40, SpringLayout.EAST, encryptRadio);
        layout.putConstraint(SpringLayout.NORTH, decryptRadio, 20, SpringLayout.SOUTH, outputLabel);

        layout.putConstraint(SpringLayout.WEST, actionButton, 25, SpringLayout.WEST, this.getContentPane());
        layout.putConstraint(SpringLayout.NORTH, actionButton, 20, SpringLayout.SOUTH, encryptRadio);

        layout.putConstraint(SpringLayout.WEST, generateKeyButton, 20, SpringLayout.EAST, actionButton);
        layout.putConstraint(SpringLayout.NORTH, generateKeyButton, 20, SpringLayout.SOUTH, encryptRadio);

        layout.putConstraint(SpringLayout.WEST, backButton, 20, SpringLayout.EAST, generateKeyButton);
        layout.putConstraint(SpringLayout.NORTH, backButton, 20, SpringLayout.SOUTH, encryptRadio);

        // 执行按钮事件监听
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key = keyField.getText();
                String inputText = inputField.getText();
                if (validateInput(key, inputText, 10, 8)) {
                    String[] keys = functions.generateKeys(key);
                    String result;
                    if (encryptRadio.isSelected()) {
                        result = functions.encrypt(inputText, keys);
                    } else {
                        result = functions.decrypt(inputText, keys);
                    }
                    outputField.setText(result);
                } else {
                    JOptionPane.showMessageDialog(S_Des_Normal.this, "请检查输入格式与长度是否满足要求。", "输入错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 随机生成密钥按钮的事件监听
        generateKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String randomKey = generateRandomKey(10);
                keyField.setText(randomKey);
                JOptionPane.showMessageDialog(S_Des_Normal.this, "生成的随机密钥: " + randomKey, "随机密钥生成", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // 返回按钮的事件监听
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainPage().setVisible(true);
                dispose();
            }
        });
    }

    // 验证输入格式
    private boolean validateInput(String key, String text, int keyLength, int textLength) {
        return key.matches("[01]{" + keyLength + "}") && text.matches("[01]{" + textLength + "}");
    }

    // 生成随机的二进制密钥
    private String generateRandomKey(int length) {
        Random random = new Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < length; i++) {
            key.append(random.nextInt(2)); // 生成0或1
        }
        return key.toString();
    }


}

//暴力破解功能

class S_Des_Crack extends JFrame {
    private JButton crackButton;
    private JButton backButton;
    private JTextField plaintextField;
    private JTextField ciphertextField;
    private JTextArea resultArea;
    private JTextField timeField;  // 破解时间输出框
    private JRadioButton singleThreadButton; // 单线程选项
    private JRadioButton multiThreadButton;  // 多线程选项
    private ButtonGroup threadGroup;  // 线程选项组

    public S_Des_Crack() {
        // 设置窗口标题和大小
        setTitle("S-DES 暴力破解");
        setSize(400, 400); // 调整窗口大小以容纳更多组件
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建自定义的背景面板
        JPanel backgroundPanel = new JPanel() {
            ImageIcon backgroundImage = new ImageIcon("bg.jpg");
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 绘制背景图片
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        // 将背景面板添加到窗口
        setContentPane(backgroundPanel);

        // 设置布局
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // 添加明文标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("明文(8bits): "), gbc);

        gbc.gridx = 1;
        plaintextField = new JTextField(19);
        add(plaintextField, gbc);

        // 添加密文标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("密文(8bits): "), gbc);

        gbc.gridx = 1;
        ciphertextField = new JTextField(19);
        add(ciphertextField, gbc);

        // 添加密钥结果区域
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        resultArea = new JTextArea(5, 30); // 设置文本区域大小
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), gbc); // 使用滚动面板

        // 添加破解时间输出框
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(new JLabel("破解时间(毫秒): "), gbc);

        gbc.gridx = 1;
        timeField = new JTextField(19);
        timeField.setEditable(false);
        add(timeField, gbc);

        // 设置单线程和多线程选项
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        singleThreadButton = new JRadioButton("单线程破解");
        add(singleThreadButton, gbc);

        gbc.gridx = 1;
        multiThreadButton = new JRadioButton("多线程破解");
        add(multiThreadButton, gbc);

        // 将单选按钮添加到 ButtonGroup 使其互斥
        threadGroup = new ButtonGroup();
        threadGroup.add(singleThreadButton);
        threadGroup.add(multiThreadButton);
        singleThreadButton.setSelected(true); // 默认选择为单线程

        // 创建按钮并设置位置
        gbc.gridwidth = 1;
        gbc.gridy = 5;
        gbc.gridx = 0;
        crackButton = new JButton("破解密钥");
        add(crackButton, gbc);

        // 右下角添加返回按钮
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        backButton = new JButton("返回");
        add(backButton, gbc);

        // 破解密钥按钮事件监听
        crackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String plaintext = plaintextField.getText();
                String ciphertext = ciphertextField.getText();
                resultArea.setText(""); // 初始化结果区域
                timeField.setText(""); // 初始化时间输出框

                // 破解开始时间添加时间戳
                long startTime = System.currentTimeMillis();

                // 获取用户选择结果
                if (singleThreadButton.isSelected()) {
                    // 单线程破解
                    singleThreadCrack(plaintext, ciphertext);
                } else {
                    // 多线程破解
                    multiThreadCrack(plaintext, ciphertext);
                }

                // 破解结束时间添加时间戳
                long endTime = System.currentTimeMillis();
                // 计算破解所需的时间
                long elapsedTime = endTime - startTime;

                // 输出破解时间到timeField
                timeField.setText(String.valueOf(elapsedTime));
            }
        });

        // 返回按钮的事件监听
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainPage().setVisible(true);
                dispose();
            }
        });
    }

    // 单线程破解方法
    private void singleThreadCrack(String plaintext, String ciphertext) {
        for (String possibleKey : generateKeySpace()) {
            String decryptedText = functions.decrypt(ciphertext, functions.generateKeys(possibleKey));
            if (decryptedText.equals(plaintext)) {
                resultArea.append(possibleKey + "\n");
            }
        }
    }

    // 多线程破解方法
    private void multiThreadCrack(String plaintext, String ciphertext) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);  // 创建线程池
        List<String> keySpace = generateKeySpace();

        for (String possibleKey : keySpace) {
            executorService.submit(() -> {
                String decryptedText = functions.decrypt(ciphertext, functions.generateKeys(possibleKey));
                if (decryptedText.equals(plaintext)) {
                    synchronized (resultArea) {
                        resultArea.append(possibleKey + "\n");
                    }
                }
            });
        }
        executorService.shutdown();  // 等待所有任务完成
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);  // 确保所有线程执行完毕
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    // 生成密钥空间
    private List<String> generateKeySpace() {
        String[] keys = new String[1024]; // 遍历10 位二进制密钥
        for (int i = 0; i < 1024; i++) {
            keys[i] = String.format("%10s", Integer.toBinaryString(i)).replace(' ', '0');
        }
        // 格式转换
        return Arrays.asList(keys);
    }
}

//ASCII码加密解密
class S_Des_ASCII extends JFrame {

    // GUI组件
    private JTextField keyField;
    private JTextField inputField;
    private JTextField outputField;
    private JButton actionButton;
    private JButton backButton;
    private JButton generateKeyButton;
    private JRadioButton encryptRadio;
    private JRadioButton decryptRadio;
    private ButtonGroup modeGroup;

    public S_Des_ASCII() {
        // 设置窗口标题
        setTitle("S-DES 加密解密（ASCII）");
        setSize(340, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 设置背景面板
        JPanel backgroundPanel = new JPanel() {
            ImageIcon backgroundImage = new ImageIcon("bg.jpg");
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 绘制背景图片
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        // 将背景面板添加到窗口
        setContentPane(backgroundPanel);

        // 采用SpringLayout布局
        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        JLabel keyLabel = new JLabel("     密钥 (10bits): ");
        keyField = new JTextField(15);

        JLabel inputLabel = new JLabel("     输入 ( 1byte): ");
        inputField = new JTextField(15);

        JLabel outputLabel = new JLabel("     输出结果:       ");
        outputField = new JTextField(15);
        outputField.setEditable(false);

        // 创建操作选择按钮
        encryptRadio = new JRadioButton("加密", true);
        decryptRadio = new JRadioButton("解密");
        modeGroup = new ButtonGroup();
        modeGroup.add(encryptRadio);
        modeGroup.add(decryptRadio);

        actionButton = new JButton("执行");
        generateKeyButton = new JButton("随机生成密钥");
        backButton = new JButton("返回");

        // 添加组件到窗口
        add(keyLabel);
        add(keyField);
        add(inputLabel);
        add(inputField);
        add(outputLabel);
        add(outputField);
        add(encryptRadio);
        add(decryptRadio);
        add(actionButton);
        add(generateKeyButton);
        add(backButton);

        // 使用 SpringLayout 进行布局
        layout.putConstraint(SpringLayout.WEST, keyLabel, 10, SpringLayout.WEST, this.getContentPane());
        layout.putConstraint(SpringLayout.NORTH, keyLabel, 10, SpringLayout.NORTH, this.getContentPane());
        layout.putConstraint(SpringLayout.WEST, keyField, 10, SpringLayout.EAST, keyLabel);
        layout.putConstraint(SpringLayout.NORTH, keyField, 0, SpringLayout.NORTH, keyLabel);

        layout.putConstraint(SpringLayout.WEST, inputLabel, 10, SpringLayout.WEST, this.getContentPane());
        layout.putConstraint(SpringLayout.NORTH, inputLabel, 20, SpringLayout.SOUTH, keyLabel);
        layout.putConstraint(SpringLayout.WEST, inputField, 10, SpringLayout.EAST, inputLabel);
        layout.putConstraint(SpringLayout.NORTH, inputField, 0, SpringLayout.NORTH, inputLabel);

        layout.putConstraint(SpringLayout.WEST, outputLabel, 10, SpringLayout.WEST, this.getContentPane());
        layout.putConstraint(SpringLayout.NORTH, outputLabel, 20, SpringLayout.SOUTH, inputLabel);
        layout.putConstraint(SpringLayout.WEST, outputField, 10, SpringLayout.EAST, outputLabel);
        layout.putConstraint(SpringLayout.NORTH, outputField, 0, SpringLayout.NORTH, outputLabel);

        layout.putConstraint(SpringLayout.WEST, encryptRadio, 85, SpringLayout.WEST, this.getContentPane());
        layout.putConstraint(SpringLayout.NORTH, encryptRadio, 20, SpringLayout.SOUTH, outputLabel);
        layout.putConstraint(SpringLayout.WEST, decryptRadio, 40, SpringLayout.EAST, encryptRadio);
        layout.putConstraint(SpringLayout.NORTH, decryptRadio, 20, SpringLayout.SOUTH, outputLabel);

        layout.putConstraint(SpringLayout.WEST, actionButton, 25, SpringLayout.WEST, this.getContentPane());
        layout.putConstraint(SpringLayout.NORTH, actionButton, 20, SpringLayout.SOUTH, encryptRadio);

        layout.putConstraint(SpringLayout.WEST, generateKeyButton, 20, SpringLayout.EAST, actionButton);
        layout.putConstraint(SpringLayout.NORTH, generateKeyButton, 20, SpringLayout.SOUTH, encryptRadio);

        layout.putConstraint(SpringLayout.WEST, backButton, 20, SpringLayout.EAST, generateKeyButton);
        layout.putConstraint(SpringLayout.NORTH, backButton, 20, SpringLayout.SOUTH, encryptRadio);

        // 执行按钮事件监听
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key = keyField.getText();
                String inputText = inputField.getText();

                if (encryptRadio.isSelected()) {
                    if (inputText.length() == 1) {
                        char character = inputText.charAt(0);
                        String binaryPlaintext = String.format("%8s", Integer.toBinaryString(character)).replace(' ', '0');
                        inputText = binaryPlaintext;
                    }

                    if (validateInput(key, inputText, 10, 8)) {
                        String[] keys = functions.generateKeys(key);
                        String ciphertextBinary = functions.encrypt(inputText, keys);
                        int decimalValue = Integer.parseInt(ciphertextBinary, 2);
                        String ciphertext = Character.toString((char) decimalValue);
                        outputField.setText(ciphertext);
                    } else {
                        JOptionPane.showMessageDialog(S_Des_ASCII.this, "请检查输入格式与长度是否满足要求。", "输入错误", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    if (inputText.length() == 1) {
                        char character = inputText.charAt(0);
                        String binaryCiphertext = String.format("%8s", Integer.toBinaryString(character)).replace(' ', '0');
                        inputText = binaryCiphertext;
                    }

                    if (validateInput(key, inputText, 10, 8)) {
                        String[] keys = functions.generateKeys(key);
                        String binaryPlaintext = functions.decrypt(inputText, keys);
                        int decimalValue = Integer.parseInt(binaryPlaintext, 2);
                        String plaintext = Character.toString((char) decimalValue);
                        outputField.setText(plaintext);
                    } else {
                        JOptionPane.showMessageDialog(S_Des_ASCII.this, "请输入正确的10位密钥和1字节密文。", "输入错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // 随机生成密钥按钮的事件监听
        generateKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String randomKey = generateRandomKey(10);
                keyField.setText(randomKey);
                JOptionPane.showMessageDialog(S_Des_ASCII.this, "生成的随机密钥: " + randomKey, "随机密钥生成", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // 返回按钮的事件监听
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainPage().setVisible(true);
                dispose();
            }
        });
    }

    // 验证输入是否是二进制且长度正确
    private boolean validateInput(String key, String text, int keyLength, int textLength) {
        return key.matches("[01]{" + keyLength + "}") && text.matches("[01]{" + textLength + "}");
    }

    // 生成随机的二进制密钥
    private String generateRandomKey(int length) {
        Random random = new Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < length; i++) {
            key.append(random.nextInt(2)); // 生成0或1
        }
        return key.toString();
    }

}

//主页面
public class MainPage extends JFrame {
    private JButton enDeButton; // 将加密和解密合并为一个按钮
    private JButton decodeButton;
    private JButton ASCIIButton;

    public MainPage() {
        // 设置窗口标题
        setTitle("S-DES 加密解密");
        setSize(340, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建自定义的背景面板
        JPanel backgroundPanel = new JPanel() {
            ImageIcon backgroundImage = new ImageIcon("bg.jpg"); // 替换为你的图片路径
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 绘制背景图片
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout()); // 使用 GridBagLayout 布局

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 设置按钮之间的间距
        gbc.anchor = GridBagConstraints.CENTER;

        // 创建按钮
        Dimension buttonSize = new Dimension(120, 30); // 统一的按钮大小
        enDeButton = new JButton("加密/解密");
        enDeButton.setPreferredSize(buttonSize);

        ASCIIButton = new JButton("ASCII加密/解密");
        ASCIIButton.setPreferredSize(buttonSize);

        decodeButton = new JButton("暴力破解");
        decodeButton.setPreferredSize(buttonSize);

        // 添加按钮到背景面板
        gbc.gridx = 0;
        gbc.gridy = 0;
        backgroundPanel.add(enDeButton, gbc);

        gbc.gridy = 1;
        backgroundPanel.add(ASCIIButton, gbc);

        gbc.gridy = 2;
        backgroundPanel.add(decodeButton, gbc);

        // 设置按钮事件监听
        enDeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new S_Des_Normal().setVisible(true); // 调用合并后的加解密界面
                dispose();
            }
        });

        ASCIIButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new S_Des_ASCII().setVisible(true);
                dispose();
            }
        });

        decodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new S_Des_Crack().setVisible(true);
                dispose();
            }
        });

        // 将背景面板添加到窗口
        setContentPane(backgroundPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainPage().setVisible(true);
            }
        });
    }
}