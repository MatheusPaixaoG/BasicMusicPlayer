import java.util.Random;
import java.util.Vector;

import javax.swing.*;

public class Player extends Thread {
    private int indexMusicaSelecionada;
    private final JLabel label;
    private final JLabel musicStatusLabel;
    private JList listOfMusics;
    private final DefaultListModel<musica> listMusics;
    private int acao;
    private boolean aleatorio;
    private Random rand = new Random();
    public JProgressBar musicProgressBar;
    public boolean tocandoOuPausado;
    private musica musicaTocando;

    public Player(JLabel label, int musicaSelecionada, JLabel musicStatus, DefaultListModel<musica> listMusics, JList listOfMusics, int acao, boolean aleatorio, JProgressBar progressBar, boolean tocandoOuPausado) {
        this.tocandoOuPausado = tocandoOuPausado;
        this.musicProgressBar = progressBar;
        this.aleatorio = aleatorio;
        this.acao = acao;
        this.listOfMusics = listOfMusics;
        this.indexMusicaSelecionada = musicaSelecionada;
        this.listMusics = listMusics;
        this.musicStatusLabel = musicStatus;
        this.label = label;
    }

    public void run() {
        if (acao == 1) { // Se acao for 1, a ação realizada será voltar a música
            Voltar();
        }
        else if (acao == 2) { // Se acao for 2, a ação realizada será avaçar a música
            Avancar();
        }
        else if (acao == 3) { // Se acao for 3, dá play na música
            // Música tocando
            Play();
        }
        else if (acao == 4) { // Se acao for 4, pausa a música
            Pause();
        }
    }

    public void Avancar() {
        if (listMusics.size() == 0) {
            label.setText("Música atual: nenhuma");
        }
        else {
            if (aleatorio) { // a reprodução será aleatória
                String musicName = label.getText().substring(14);
                int random_music_index = rand.nextInt(listMusics.size());
                String nextMusicName = listMusics.elementAt(random_music_index).nome;
                while (musicName.equals(nextMusicName)) {
                    random_music_index = rand.nextInt(listMusics.size());
                    nextMusicName = listMusics.elementAt(random_music_index).nome;
                }
                label.setText("Música atual: " + listMusics.elementAt(random_music_index).nome);
                Interface.currentMusicIndex = random_music_index;
            }
            else { // a reprodução será sequencial
                label.setText("Música atual: " + listMusics.elementAt((Interface.currentMusicIndex + 1) % listMusics.size()).nome);
                Interface.currentMusicIndex = (Interface.currentMusicIndex + 1) % listMusics.size();
            }
            musicaTocando = listMusics.get(indexMusicaSelecionada);
            musicaTocando.tocandoOuPausada = false;
            musicStatusLabel.setText("Status da música: pausada");
            listOfMusics.setSelectedIndex(Interface.currentMusicIndex);
            indexMusicaSelecionada = listOfMusics.getSelectedIndex();
        }
    }

    public void Voltar() {
        if (listMusics.size() == 0) {
            label.setText("Música atual: nenhuma");
        }
        else {
            if (aleatorio) { // a reprodução será aleatória
                String musicName = label.getText().substring(14);
                int random_music_index = rand.nextInt(listMusics.size());
                String nextMusicName = listMusics.elementAt(random_music_index).nome;
                while (musicName.equals(nextMusicName)) {
                    random_music_index = rand.nextInt(listMusics.size());
                    nextMusicName = listMusics.elementAt(random_music_index).nome;
                }
                label.setText("Música atual: " + listMusics.elementAt(random_music_index).nome);
                Interface.currentMusicIndex = random_music_index;
            }
            else { // a reprodução será sequencial
                label.setText("Música atual: " + listMusics.elementAt(((Interface.currentMusicIndex - 1) + listMusics.size()) % listMusics.size()).nome);
                Interface.currentMusicIndex = ((Interface.currentMusicIndex - 1) + listMusics.size()) % listMusics.size();
            }
        }
    }

    public void Play() {
        // Música tocando
        label.setText("Música atual: " + listMusics.get(indexMusicaSelecionada).nome);
        Interface.currentMusicIndex = indexMusicaSelecionada;
        musicStatusLabel.setText("Status da música: tocando");
        musicaTocando = listMusics.get(indexMusicaSelecionada);
        musicaTocando.musicProgressBar = musicProgressBar;
        musicaTocando.tocandoOuPausada = true;
        musicaTocando.tocarMusica();
    }

    public void Pause() {
        // Música pausada
        label.setText("Música atual: " + listMusics.get(indexMusicaSelecionada).nome);
        musicaTocando = listMusics.get(indexMusicaSelecionada);
        musicaTocando.tocandoOuPausada = false;
        musicStatusLabel.setText("Status da música: pausada");
    }
}
