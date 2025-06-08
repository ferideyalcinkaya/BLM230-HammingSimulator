package mimari;

import javax.swing.*;
import javax.swing.SpinnerNumberModel;
import java.awt.*;
import java.util.Arrays;

public class MainUI {

    private JFrame frame;
    private JTextField inputField;
    private JTextArea outputArea;
    private JSpinner errorSpinner;
    private HammingCode hc;

    // Memory panel and bit labels
    private JPanel memoryPanel;
    private JLabel[] memBits;

    // Store the last encoded codeword and current (possibly mutated) codeword
    private int[] lastEncoded;
    private int[] currentCoded;

    public MainUI() {
        // Frame setup
        frame = new JFrame("Hamming SEC-DED Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout(10, 10));

        // Top panel: input, load to memory, encode
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        top.add(new JLabel("Veri (binary):"));
        inputField = new JTextField(20);
        top.add(inputField);
        JButton loadBtn = new JButton("Load to Memory");
        top.add(loadBtn);
        JButton encodeBtn = new JButton("Encode");
        top.add(encodeBtn);

        // === YENİ: Hamming Code Nasıl Oluşur? butonu ===
        JButton explainBtn = new JButton("Hamming Code Nasıl Oluşur?");
        top.add(explainBtn);

        frame.add(top, BorderLayout.NORTH);

        // Memory panel
        memoryPanel = new JPanel();
        memoryPanel.setBorder(BorderFactory.createTitledBorder("Memory"));
        frame.add(memoryPanel, BorderLayout.WEST);

        // Center panel: inject, decode
        JPanel mid = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        mid.add(new JLabel("Hata Pozisyonu:"));
        errorSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1, 1));
        mid.add(errorSpinner);
        JButton injectBtn = new JButton("Inject Error");
        injectBtn.setEnabled(false);
        mid.add(injectBtn);
        JButton decodeBtn = new JButton("Decode");
        decodeBtn.setEnabled(false);
        mid.add(decodeBtn);
        frame.add(mid, BorderLayout.CENTER);

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setRows(5);
        JScrollPane scroll = new JScrollPane(outputArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        frame.add(scroll, BorderLayout.SOUTH);

        // Action listeners
        loadBtn.addActionListener(e -> {
            onLoadMemory();
            injectBtn.setEnabled(false);
            decodeBtn.setEnabled(false);
        });

        encodeBtn.addActionListener(e -> {
            onEncode();
            injectBtn.setEnabled(true);
            decodeBtn.setEnabled(false);
        });

        injectBtn.addActionListener(e -> {
            onInject();
            decodeBtn.setEnabled(true);
        });

        decodeBtn.addActionListener(e -> onDecode());

        // === YENİ: Açıklama butonu ===
        explainBtn.addActionListener(e -> showHammingExplanation());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void onLoadMemory() {
        String text = inputField.getText().trim();
        // Sadece 0/1 kontrolü
        if (!text.matches("[01]+")) {
            JOptionPane.showMessageDialog(frame, "Sadece 0 ve 1 girin!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Memory panel'i temizle ve etiketi oluştur
        memoryPanel.removeAll();
        int m = text.length();
        memBits = new JLabel[m];
        for (int i = 0; i < m; i++) {
            memBits[i] = new JLabel(String.valueOf(text.charAt(i)), SwingConstants.CENTER);
            memBits[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            memBits[i].setPreferredSize(new Dimension(25, 25));
            memBits[i].setOpaque(true);
            memBits[i].setBackground(Color.WHITE);
            memoryPanel.add(memBits[i]);
        }
        memoryPanel.revalidate();
        memoryPanel.repaint();

        hc = new HammingCode(m);
        lastEncoded = null;
        currentCoded = null;
        outputArea.setText("Memory loaded: " + text);
    }

    private void onEncode() {
        if (hc == null) {
            JOptionPane.showMessageDialog(frame, "Önce Load to Memory yapın!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int m = memBits.length;
        int[] data = new int[m];
        for (int i = 0; i < m; i++) data[i] = Integer.parseInt(memBits[i].getText());
        lastEncoded = hc.encode(data);
        currentCoded = Arrays.copyOf(lastEncoded, lastEncoded.length);

        // Update memory panel with coded bits
        memoryPanel.removeAll();
        memBits = new JLabel[currentCoded.length];
        for (int i = 0; i < currentCoded.length; i++) {
            memBits[i] = new JLabel(String.valueOf(currentCoded[i]), SwingConstants.CENTER);
            memBits[i].setBorder(BorderFactory.createLineBorder(Color.BLUE));
            memBits[i].setPreferredSize(new Dimension(25, 25));
            memBits[i].setOpaque(true);
            memBits[i].setBackground(Color.WHITE);
            memoryPanel.add(memBits[i]);
        }
        memoryPanel.revalidate();
        memoryPanel.repaint();

        SpinnerNumberModel model = (SpinnerNumberModel) errorSpinner.getModel();
        model.setMinimum(1);
        model.setMaximum(currentCoded.length);
        model.setStepSize(1);
        model.setValue(1);

        outputArea.setText("Encoded: " + arrayToString(currentCoded));
    }

    private void onInject() {
        if (currentCoded == null) {
            JOptionPane.showMessageDialog(frame, "Önce Encode yapın!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int pos = (int) errorSpinner.getValue();
        if (pos < 1 || pos > currentCoded.length) {
            JOptionPane.showMessageDialog(frame, "Geçerli bir pozisyon seçin!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        currentCoded[pos - 1] ^= 1;

        // Update memory UI: highlight mutated bits
        for (int i = 0; i < currentCoded.length; i++) {
            memBits[i].setText(String.valueOf(currentCoded[i]));
            if (lastEncoded[i] != currentCoded[i]) {
                memBits[i].setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            } else {
                memBits[i].setBorder(BorderFactory.createLineBorder(Color.BLUE));
            }
        }

        outputArea.setText("Error Injected at " + pos + ":\n" + arrayToString(currentCoded));
    }

    private void onDecode() {
        if (currentCoded == null) {
            JOptionPane.showMessageDialog(frame, "Önce Encode yapın!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (hc.isDoubleError(currentCoded)) {
            outputArea.setText("Double Error Detected! Cannot correct.");
            return;
        }
        int errPos = hc.getErrorPosition(currentCoded);
        int[] decoded = hc.decode(currentCoded);

        // Update memory with decoded bits
        memoryPanel.removeAll();
        memBits = new JLabel[decoded.length];
        for (int i = 0; i < decoded.length; i++) {
            memBits[i] = new JLabel(String.valueOf(decoded[i]), SwingConstants.CENTER);
            memBits[i].setBorder(BorderFactory.createLineBorder(Color.GREEN));
            memBits[i].setPreferredSize(new Dimension(25, 25));
            memBits[i].setOpaque(true);
            memBits[i].setBackground(Color.WHITE);
            memoryPanel.add(memBits[i]);
        }
        memoryPanel.revalidate();
        memoryPanel.repaint();

        outputArea.setText(
            "Detected Error Position: " + errPos + "\n" +
            "Decoded Data: " + arrayToString(decoded)
        );
    }

    private String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int b : arr) sb.append(b);
        return sb.toString();
    }

    // === YENİ: Açıklama Penceresi ===
    private void showHammingExplanation() {
        if (hc == null) {
            JOptionPane.showMessageDialog(frame, "Önce Load to Memory yapın!", "Uyarı", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("🔎 Hamming Kod Oluşumu (SEC-DED)\n");
        sb.append("──────────────────────────────\n");
        sb.append("1. Kullanıcının girdiği veri: ").append(hc.getDataBitCount()).append(" bit\n");
        sb.append("2. Parity bit sayısı: ").append(hc.getParityBitCount()).append("\n");
        sb.append("3. Toplam kod uzunluğu (veri+parity+overall): ").append(hc.getTotalCodeLength()).append("\n\n");
        sb.append("📌 Parity bitleri her zaman 2^k pozisyonuna (1, 2, 4, 8...) yerleştirilir.\n\n");
        sb.append("Her parity biti, aşağıdaki pozisyonlardaki bitleri kontrol eder:\n");
        for (int p = 0; p < hc.getParityBitCount(); p++) {
            int pPos = (1 << p);
            sb.append("- Parity Bit ").append(p + 1)
              .append(" (Pozisyon ").append(pPos).append("): ");
            for (int i = 1; i < hc.getTotalCodeLength(); i++) {
                if (((i + 1) & pPos) != 0) {
                    sb.append(i + 1).append(" ");
                }
            }
            sb.append("\n");
        }
        sb.append("\n🔹 Parity bitlerinin hesaplanması:\n");
        sb.append("Her parity biti, kontrol ettiği bitlerin değerinin toplamı çiftse 0, tekse 1 olur.\n\n");
        sb.append("🔹 Son bit (overall parity), kodun tamamındaki 1'lerin toplamının çift/tek olmasına göre belirlenir.\n");
        sb.append("Bu sayede tek hata düzeltilir, çift hata algılanır.\n");

        JTextArea explainText = new JTextArea(sb.toString());
        explainText.setEditable(false);
        explainText.setLineWrap(true);
        explainText.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(explainText);
        scrollPane.setPreferredSize(new Dimension(400, 320));

        JOptionPane.showMessageDialog(frame, scrollPane, "Hamming Code Nasıl Oluşur?", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainUI::new);
    }
}
