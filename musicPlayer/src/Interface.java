import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interface implements ActionListener, ListSelectionListener {
    private JPanel panel;
    private JButton voltarButton;
    private JButton pauseButton;
    private JButton playButton;
    private JButton avancarButton;
    private JButton removeMusicButton;
    private JButton addMusicButton;
    private JButton aleatorioButton;
    private JProgressBar addRemoveProgressBar;
    private JList<musica> musicList;
    private JLabel currentMusic;
    private JLabel musicStatus;
    private JLabel aleatorioStatus;
    public JProgressBar musicProgressBar;
    public static DefaultListModel<musica> playlist = new DefaultListModel<musica>();
    public static int currentMusicIndex = 0; // Mantém registro do índice atual da música a ser tocada
    private boolean aleatorio = false; // Começa por sequencial
    static JTextField textField;
    static JFrame addRemoveFrame;
    static JButton musicNameButton;
    static JLabel label;

    public Interface() {
        // Inicializando os componentes botão
        musicNameButton = new JButton("Enviar");
        // Inicializando a JList dinâmica
        musicList.setModel(playlist);
        musicList.setSelectedIndex(0);
        // Seta o listener de clique
        musicList.addListSelectionListener(this);
        addMusicButton.addActionListener(this);
        removeMusicButton.addActionListener(this);
        playButton.addActionListener(this);
        pauseButton.addActionListener(this);
        avancarButton.addActionListener(this);
        voltarButton.addActionListener(this);
        musicNameButton.addActionListener(this);
        aleatorioButton.addActionListener(this);
        // Seta uma ação a ser disparada quando o botão for clicado
        addMusicButton.setActionCommand("add_music");
        removeMusicButton.setActionCommand("remove_music");
        playButton.setActionCommand("play_music");
        pauseButton.setActionCommand("pause_music");
        avancarButton.setActionCommand("avancar_music");
        voltarButton.setActionCommand("voltar_music");
        musicNameButton.setActionCommand("add_remove_music");
        aleatorioButton.setActionCommand("aleatorio");

        // Inicializando um componente JTextField com 16 colunas
        textField = new JTextField(16);

        // Inicializando um componente de barra de progresso
        addRemoveProgressBar.setStringPainted(true);
        // Seta o valor inicial 0
        addRemoveProgressBar.setValue(0);
        musicProgressBar.setValue(0);


        // Inicializando um componente de texto na aba para adicionar ou remover músicas
        label = new JLabel("Digite o nome da música para adicionar");

        // Inicializando um componente panel
        JPanel addRemovePanel = new JPanel();
        // Cria margens em todas as direções
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));

        // Adicionando componentes no painel de adicionar e remover músicas
        addRemovePanel.add(label);
        addRemovePanel.add(textField);
        addRemovePanel.add(musicNameButton);

        // Inicializa a janela da GUI.
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        addRemoveFrame = new JFrame();
        addRemoveFrame.add(addRemovePanel);

        // Seta o titulo da janela.
        frame.setTitle("Player de música");
        addRemoveFrame.setTitle("Janela de adicionar ou remover músicas");
        // Seta o tamanho da janela.
        frame.setSize(700, 700);
        frame.setVisible(true);

        addRemoveFrame.setSize(300, 300);
    }

    public static String nome_musica; // Música atualmente sendo reproduzida
    public static boolean add;
    public static boolean remove;
    public static Lock musicListLock = new ReentrantLock(); // Lock para a lista de músicas
    public static int musicaSelecionada;
    public static Thread tempPlayer; // Serve para obter uma referência à thread player iniciada para que seja possível pausá-la
    public static boolean tocandoOuPausado = false; // Variável para saber se a música atual está tocando ou se está pausada

    public static void main(String[] args) throws InterruptedException {
        System.out.println("INÍCIO DO PROGRAMA");

        new Interface();

        System.out.println("FIM DO PROGRAMA");

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        // TODO Auto-generated method stub
        musicaSelecionada = (int) musicList.getSelectedIndex();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if ("add_music".equals(command)) {
            addRemoveFrame.setVisible(true); // Torna visível a janela de adicionar músicas
            add = true;
            remove = false;
        }
        else if ("remove_music".equals(command)) {
            add = false;
            remove = true;
            Thread remover = new Remover(musicListLock, playlist, addRemoveProgressBar, musicList, musicaSelecionada, currentMusic);
            remover.start();
        }
        else if ("play_music".equals(command)) {
            tocandoOuPausado = true;
            Thread player = new Player(currentMusic, musicaSelecionada, musicStatus, playlist, musicList, 3, aleatorio, musicProgressBar, tocandoOuPausado);
            player.start();
            tempPlayer = player;
        }
        else if ("pause_music".equals(command)) {
            tocandoOuPausado = false;
            //tempPlayer;
            Thread pauser = new Player(currentMusic, musicaSelecionada, musicStatus, playlist, musicList, 4, aleatorio, musicProgressBar, tocandoOuPausado);
            pauser.start();
        }
        else if ("avancar_music".equals(command)) {
            Thread avancar = new Player(currentMusic, musicaSelecionada, musicStatus, playlist, musicList, 2, aleatorio, musicProgressBar, tocandoOuPausado);
            avancar.start();
        }
        else if ("voltar_music".equals(command)) {
            Thread voltar = new Player(currentMusic, musicaSelecionada, musicStatus, playlist, musicList, 1, aleatorio, musicProgressBar, tocandoOuPausado);
            voltar.start();
        }
        else if ("add_remove_music".equals(command)) {
            nome_musica = textField.getText();
            Thread adicionar = new Adicionar(musicListLock, nome_musica, playlist, addRemoveProgressBar, musicList);
            adicionar.start();

            textField.setText("");
            addRemoveFrame.setVisible(false);
        }
        else if ("aleatorio".equals(command)) {
            aleatorio = !aleatorio;
            String modoDeReproducao = "";
            if (aleatorio) {
                modoDeReproducao = "aleatório";
            }
            else {
                modoDeReproducao = "sequencial";
            }
            aleatorioStatus.setText("Modo de reprodução: " + modoDeReproducao);
        }
    }

}



