import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Thread;

import gamefield.GameField;

public class Life {
    private Frame mainFrame;
    private Panel inputPanel;
    private Panel gameControlPanel;
    private Panel gameFieldPanel;
    public GameField gameField;
    private int rowsNumber = 10;
    private int colsNumber = 10;
    private int cellSize = 10;
    private final int mainFrameInitialWidth = 300;
    private final int mainFrameInitialHeight = 250;
    private boolean playing = false;
    private int msBetweenGenerations = 100;
    private Button startButton;

    Life() {
        this.mainFrame = createMainFrame();
        this.inputPanel = createInputPanel();
        this.gameControlPanel = createGameControlPanel();
        this.gameFieldPanel = createGameFieldPanel();
        gameControlPanel.setVisible(false);
        gameFieldPanel.setVisible(false);
        mainFrame.add(inputPanel);
        mainFrame.add(gameFieldPanel);
        mainFrame.add(gameControlPanel);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
    }

    private Frame createMainFrame() {
        Frame mainFrame = new Frame();
        mainFrame.setTitle("Life");
        mainFrame.setSize(mainFrameInitialWidth, mainFrameInitialHeight);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                stop();
                mainFrame.dispose();
            }
        });
        mainFrame.setLayout(new BoxLayout(mainFrame, BoxLayout.Y_AXIS));
        return mainFrame;
    }

    private Panel createInputPanel() {
        //Panel inputPanel = new Panel(new FlowLayout());
        Panel inputPanel = new Panel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        
        Panel colsPanel = new Panel(new FlowLayout());
        Label colsInputLabel = new Label("Количество столбцов");
        TextField colsInput = new TextField("" + colsNumber);
        colsPanel.add(colsInputLabel);
        colsPanel.add(colsInput);

        Panel rowsPanel = new Panel(new FlowLayout());
        Label rowsInputLabel = new Label("Количество строк");
        TextField rowsInput = new TextField("" + rowsNumber);
        rowsPanel.add(rowsInputLabel);
        rowsPanel.add(rowsInput);

        Panel cellSizePanel = new Panel(new FlowLayout());
        Label cellSizeLabel = new Label("Размер клетки");
        TextField cellSizeInput = new TextField("" + cellSize);
        cellSizePanel.add(cellSizeLabel);
        cellSizePanel.add(cellSizeInput);

        Panel msBetweenGenPanel = new Panel(new FlowLayout());
        Label msBetweenGenLabel = new Label("Задержка между поколениями, мс");
        TextField msBetweenGenInput = new TextField("" + msBetweenGenerations);
        msBetweenGenPanel.add(msBetweenGenLabel);
        msBetweenGenPanel.add(msBetweenGenInput);

        Panel btnPanel = new Panel(new FlowLayout());
        Button createButton = new Button("Создать");
        btnPanel.add(createButton);

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                colsNumber = Integer.parseInt(colsInput.getText());
                rowsNumber = Integer.parseInt(rowsInput.getText());
                cellSize = Integer.parseInt(cellSizeInput.getText());
                msBetweenGenerations = Integer.parseInt(msBetweenGenInput.getText());
                gameField = createGameField();
                gameFieldPanel.add(gameField);
                inputPanel.setVisible(false);
                gameFieldPanel.setVisible(true);
                gameControlPanel.setVisible(true);
                startButton.setLabel("Старт");
                mainFrame.setSize(gameField.getWidth() + 150, gameField.getHeight() + 150);
                mainFrame.paintAll(mainFrame.getGraphics());
            }
        });

        inputPanel.add(colsPanel);
        inputPanel.add(rowsPanel);
        inputPanel.add(cellSizePanel);
        inputPanel.add(msBetweenGenPanel);
        inputPanel.add(btnPanel);
        return inputPanel;
    }

    private Panel createGameControlPanel() {
        Panel controlPanel = new Panel(new FlowLayout());
        Button startButton = new Button("Старт");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(playing){
                    stop();
                    gameField.setEditBlocked(false);
                    startButton.setLabel("Старт");
                } else {
                    gameField.setEditBlocked(true);
                    play();
                    startButton.setLabel("Стоп");
                }
            }
        });
        this.startButton = startButton;

        Button goBackButton = new Button("Назад");
        goBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stop();
                gameFieldPanel.remove(gameField);
                gameControlPanel.setVisible(false);
                inputPanel.setVisible(true);
                mainFrame.setSize(mainFrameInitialWidth, mainFrameInitialHeight);
                mainFrame.paintAll(mainFrame.getGraphics());
            }
        });
        controlPanel.add(startButton);
        controlPanel.add(goBackButton);
        return controlPanel;
    }

    private Panel createGameFieldPanel() {
        Panel gameFieldPanel = new Panel(new FlowLayout());
        return gameFieldPanel;
    }

    private GameField createGameField() {
        GameField gameField = new GameField(colsNumber, rowsNumber, cellSize);
        return gameField;
    }

    public static void main(String[] args) {
        new Life();
    }

    public void play() {
        playing = true;
        Thread gt = newGameThread();
        gt.start();
    }

    public void stop() {
        playing = false;
    }

    private Thread newGameThread() {
        return new Thread() {
            @Override
            public void run() {
                while(playing){
                    try {
                        Thread.sleep(msBetweenGenerations);
                    } catch(InterruptedException err) {}
                    if(playing)
                        gameField.nextGeneration();
                }
            }
        };
    }
}
