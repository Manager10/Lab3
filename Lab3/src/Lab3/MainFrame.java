package Lab3;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class MainFrame extends JFrame
{
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;

    private Double[] coefficients;

    private JFileChooser fileChooser = null;

    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToGraphicsMenuItem;
    private JMenuItem searchValueMenuItem;
    private JMenuItem aboutMenuItem;

    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;

    private Box hBoxResult;
    // Визуализатор ячеек таблицы
    private GornerTableCellRenderer renderer = new GornerTableCellRenderer();
    // Модель данных с результатами вычислений
    private GornerTableModel data;
    public MainFrame(Double[] coefficients) {
// Обязательный вызов конструктора предка
        super("Табулирование многочлена на отрезке по схеме Горнера");
// Запомнить во внутреннем поле переданные коэффициенты
        this.coefficients = coefficients;

        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();

        setLocation((kit.getScreenSize().width - WIDTH) / 2,
                (kit.getScreenSize().height - HEIGHT) / 2);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);

        JMenu tableMenu = new JMenu("Таблица");
        menuBar.add(tableMenu);

        JMenu aboutMenu = new JMenu("Справка");
        menuBar.add(aboutMenu);

        Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл") {

            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
// Если экземпляр диалогового окна "Открыть файл" ещѐ не создан,
// то создать его
                    fileChooser = new JFileChooser();
// и инициализировать текущей директорией
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    saveToTextFile(fileChooser.getSelectedFile());
            }
        };
        saveToTextMenuItem = fileMenu.add(saveToTextAction);
        saveToTextMenuItem.setEnabled(false);

        Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    // и инициализировать текущей директорией
                    fileChooser.setCurrentDirectory(new File("."));
                }
                // Показать диалоговое окно
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) ;
                saveToGraphicsFile(fileChooser.getSelectedFile());
            }
        };

        saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
        saveToGraphicsMenuItem.setEnabled(false);
    }
        protected void saveToGraphicsFile(File selectedFile)
        {
            try
            {
                // Создать новый байтовый поток вывода, направленный в указанный файл
                DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));
                // Записать в поток вывода попарно значение X в точке, значение многочлена в точке
                for (int i = 0; i < data.getRowCount(); i++)
                {
                    out.writeDouble((Double) data.getValueAt(i, 0));
                    out.writeDouble((Double) data.getValueAt(i, 1));
                }
                // Закрыть поток вывода
                out.close();
            }
            catch (Exception e)
            {
                // Исключительную ситуацию "ФайлНеНайден" в данном случае можно не обрабатывать,
                // так как мы файл создаѐм, а не открываем для чтения
            }
        }

        protected void saveToTextFile(File selectedFile)
        {
            try
            {
                // Создать новый символьный поток вывода, направленный в указанный файл
                PrintStream out = new PrintStream(selectedFile);
                // Записать в поток вывода заголовочные сведения
                out.println("Результаты табулирования многочлена по схеме Горнера");
                out.print("Многочлен: ");
                for (int i = 0; i < coefficients.length; i++)
                {
                    out.print(coefficients[i] + "*X^" +
                            (coefficients.length - i - 1));
                    if (i != coefficients.length - 1)
                        out.print(" + ");
                }
                out.println("");
                out.println("Интервал от " + data.getFrom() + " до " +
                        data.getTo() + " с шагом " + data.getStep());
                out.println("====================================================");
                // Записать в поток вывода значения в точках
                for (int i = 0; i < data.getRowCount(); i++)
                {
                    out.println("Значение в точке " + data.getValueAt(i, 0)
                            + " равно " + data.getValueAt(i, 1));
                }
                // Закрыть поток
                out.close();
            }
            catch (FileNotFoundException e)
            {
                // Исключительную ситуацию "ФайлНеНайден" можно не
                // обрабатывать, так как мы файл создаѐм, а не открываем
            }
        }
}

