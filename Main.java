import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.*;
import java.util.*;

public class Main extends JFrame implements ActionListener {

    Locale[] locales = {
            Locale.forLanguageTag("ru-RU"),
            Locale.forLanguageTag("en-US"),
            Locale.forLanguageTag("de-DE")
    };

    String[] langNames = {
            "Русский (RU)", "English (US)", "Deutsch (DE)"
    };

    int lang = 0;
    int person = 0;

    String[] titles = {
            "Награждение победителей олимпиады",
            "Olympiad winners awards",
            "Preisverleihung der Olympiade"
    };

    String[] congrTitles = {
            "Поздравитель", "Congratulator", "Gratulant"
    };

    String[] giftTitles = {
            "Подарки", "Gifts", "Geschenke"
    };

    String[] chooseText = {
            "Выбрать подарки ▼",
            "Choose gifts ▼",
            "Geschenke wählen ▼"
    };

    String[] concertTitles = {
            "Концерт", "Concert", "Konzert"
    };

    String[] clientTitles = {
            "Постоянный клиент", "Regular customer", "Stammkunde"
    };

    String[] yesText = {"Да", "Yes", "Ja"};
    String[] noText = {"Нет", "No", "Nein"};

    String[] costText = {
            "Стоимость: ", "Cost: ", "Kosten: "
    };

    String[] saveText = {
            "Сохранить заказ в файл",
            "Save order to file",
            "Bestellung speichern"
    };

    String[] savedText = {
            "Заказ сохранён в order.txt",
            "Order saved to order.txt",
            "Bestellung in order.txt gespeichert"
    };

    String[] errorText = {
            "Ошибка записи в файл",
            "File write error",
            "Fehler beim Schreiben"
    };

    String[][] congrNames = {
            {"Дед Мороз", "Клоун", "Мэр города"},
            {"Santa Claus", "Clown", "City mayor"},
            {"Weihnachtsmann", "Clown", "Bürgermeister"}
    };

    String[][] giftNames = {
            {"Кубок", "Книга", "Медаль"},
            {"Cup", "Book", "Medal"},
            {"Pokal", "Buch", "Medaille"}
    };

    int[] congrPrices = {500, 300, 700};
    int[] giftPrices = {400, 150, 250};

    int concertPrice = 1000;

    // 0 — кубок, 1 — книга, 2 — медаль.
    int[][] availableGifts = {
            {0, 1},
            {2},
            {0, 1, 2}
    };

    JComboBox<String> langBox = new JComboBox<>(langNames);

    JLabel dateLong = new JLabel();
    JLabel dateShort = new JLabel();

    JPanel congrPanel = new JPanel(new GridLayout(1, 3));
    JPanel giftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JPanel concertPanel = new JPanel(new GridLayout(1, 2));
    JPanel clientPanel = new JPanel(new GridLayout(1, 2));

    JCheckBox[] congrBoxes = new JCheckBox[3];
    JCheckBox[] giftBoxes = new JCheckBox[3];

    JButton giftButton = new JButton();
    JPopupMenu giftMenu = new JPopupMenu();

    JRadioButton concertYes = new JRadioButton();
    JRadioButton concertNo = new JRadioButton();

    JRadioButton clientYes = new JRadioButton();
    JRadioButton clientNo = new JRadioButton();

    JLabel costLabel = new JLabel();
    JButton saveButton = new JButton();

    javax.swing.Timer timer = new javax.swing.Timer(1000, this);

    double total = 0;

    public Main() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // Картинки сверху.
        JPanel photos = new JPanel();

        photos.setBackground(new Color(255, 245, 220));

        photos.add(new JLabel(new ImageIcon("images/cat.png")));
        photos.add(new JLabel(new ImageIcon("images/sun.png")));
        photos.add(new JLabel(new ImageIcon("images/meme.png")));

        add(photos, BorderLayout.NORTH);

        // Основная часть окна.
        JPanel form = new JPanel(new GridLayout(9, 1, 5, 5));

        form.setBorder(
                BorderFactory.createEmptyBorder(0, 10, 10, 10)
        );

        add(form, BorderLayout.CENTER);

        // Поздравители и подарки.
        ButtonGroup congrGroup = new ButtonGroup();

        for (int i = 0; i < 3; i++) {
            congrBoxes[i] = new JCheckBox();
            congrBoxes[i].addActionListener(this);

            congrGroup.add(congrBoxes[i]);
            congrPanel.add(congrBoxes[i]);

            giftBoxes[i] = new JCheckBox();
            giftBoxes[i].addActionListener(this);
        }

        congrBoxes[0].setSelected(true);

        giftButton.addActionListener(this);
        giftPanel.add(giftButton);

        updateGifts();

        // Выбор концерта.
        ButtonGroup concertGroup = new ButtonGroup();

        concertGroup.add(concertYes);
        concertGroup.add(concertNo);

        concertNo.setSelected(true);

        concertYes.addActionListener(this);
        concertNo.addActionListener(this);

        concertPanel.add(concertYes);
        concertPanel.add(concertNo);

        // Постоянный клиент.
        ButtonGroup clientGroup = new ButtonGroup();

        clientGroup.add(clientYes);
        clientGroup.add(clientNo);

        clientNo.setSelected(true);

        clientYes.addActionListener(this);
        clientNo.addActionListener(this);

        clientPanel.add(clientYes);
        clientPanel.add(clientNo);

        langBox.addActionListener(this);
        saveButton.addActionListener(this);

        // Размещаем элементы в основной части.
        form.add(langBox);
        form.add(dateLong);
        form.add(dateShort);
        form.add(congrPanel);
        form.add(giftPanel);
        form.add(concertPanel);
        form.add(clientPanel);
        form.add(costLabel);
        form.add(saveButton);

        // Немного оформления.
        getContentPane().setBackground(new Color(255, 245, 220));

        dateLong.setHorizontalAlignment(SwingConstants.CENTER);
        dateShort.setHorizontalAlignment(SwingConstants.CENTER);

        costLabel.setHorizontalAlignment(SwingConstants.CENTER);
        costLabel.setFont(new Font("Dialog", Font.BOLD, 18));

        saveButton.setBackground(new Color(220, 235, 210));

        setTexts();
        countCost();
        updateTime();

        timer.start();

        setSize(680, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void setTexts() {
        setTitle(titles[lang]);

        congrPanel.setBorder(
                BorderFactory.createTitledBorder(congrTitles[lang])
        );

        giftPanel.setBorder(
                BorderFactory.createTitledBorder(giftTitles[lang])
        );

        concertPanel.setBorder(
                BorderFactory.createTitledBorder(
                        concertTitles[lang] + " — " + concertPrice + " BYN"
                )
        );

        clientPanel.setBorder(
                BorderFactory.createTitledBorder(clientTitles[lang])
        );

        for (int i = 0; i < 3; i++) {
            congrBoxes[i].setText(
                    congrNames[lang][i] + " — " + congrPrices[i] + " BYN"
            );

            giftBoxes[i].setText(
                    giftNames[lang][i] + " — " + giftPrices[i] + " BYN"
            );
        }

        giftButton.setText(chooseText[lang]);

        concertYes.setText(yesText[lang]);
        concertNo.setText(noText[lang]);

        clientYes.setText(yesText[lang]);
        clientNo.setText(noText[lang]);

        saveButton.setText(saveText[lang]);
    }

    void updateGifts() {
        giftMenu.removeAll();

        for (int i = 0; i < 3; i++) {
            giftBoxes[i].setSelected(false);
        }

        for (int number : availableGifts[person]) {
            giftMenu.add(giftBoxes[number]);
        }
    }

    void updateTime() {
        Date now = new Date();

        DateFormat longFormat = DateFormat.getDateTimeInstance(
                DateFormat.LONG,
                DateFormat.LONG,
                locales[lang]
        );

        DateFormat shortFormat = DateFormat.getDateTimeInstance(
                DateFormat.SHORT,
                DateFormat.SHORT,
                locales[lang]
        );

        dateLong.setText(longFormat.format(now));
        dateShort.setText(shortFormat.format(now));
    }

    void countCost() {
        total = congrPrices[person];

        for (int i = 0; i < 3; i++) {
            if (giftBoxes[i].isSelected()) {
                total = total + giftPrices[i];
            }
        }

        if (concertYes.isSelected()) {
            total = total + concertPrice;
        }

        if (clientYes.isSelected()) {
            total = total * 0.9;
        }

        showCost();
    }

    void showCost() {
        NumberFormat nf = NumberFormat.getNumberInstance(locales[lang]);

        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        costLabel.setText(costText[lang] + nf.format(total) + " BYN");
    }

    void saveOrder() {
        try (FileWriter out = new FileWriter(
                "order.txt", StandardCharsets.UTF_8)) {

            out.write(titles[lang] + "\n");
            out.write(dateLong.getText() + "\n\n");

            out.write(
                    congrTitles[lang] + ": " + congrNames[lang][person] + "\n"
            );

            out.write(giftTitles[lang] + ":");

            for (int i = 0; i < 3; i++) {
                if (giftBoxes[i].isSelected()) {
                    out.write(" " + giftNames[lang][i] + ";");
                }
            }

            out.write("\n");

            if (concertYes.isSelected()) {
                out.write(concertTitles[lang] + ": " + yesText[lang] + "\n");
            } else {
                out.write(concertTitles[lang] + ": " + noText[lang] + "\n");
            }

            if (clientYes.isSelected()) {
                out.write(
                        clientTitles[lang] + ": " + yesText[lang] + " (-10%)\n"
                );
            } else {
                out.write(clientTitles[lang] + ": " + noText[lang] + "\n");
            }

            out.write(costLabel.getText() + "\n");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    errorText[lang],
                    titles[lang],
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                savedText[lang],
                titles[lang],
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == langBox) {
            lang = langBox.getSelectedIndex();

            setTexts();
            updateTime();
            showCost();

        } else if (source == giftButton) {
            giftMenu.show(giftButton, 0, giftButton.getHeight());

        } else if (source == saveButton) {
            saveOrder();

        } else if (source == timer) {
            updateTime();

        } else {
            for (int i = 0; i < 3; i++) {
                if (source == congrBoxes[i] && person != i) {
                    person = i;
                    updateGifts();
                }
            }

            countCost();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}