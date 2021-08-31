import javax.swing.*;
import java.util.Random;

public class musica {
    String nome;
    int duracao;
    Random rand = new Random();
    public JProgressBar musicProgressBar;
    public boolean tocandoOuPausada;

    public musica(String name) {
        this.nome = name;
        this.duracao = (rand.nextInt(10) + 5); // a duração será entre 5 e 14 segundos
    }

    public String toString()
    {
        return this.nome;
    }

    public void tocarMusica() {
        float percentMusica = 100/duracao;
        boolean musicaTerminou = false;
        // int time = 0;
        for(int time = 0; time <= duracao; time++) {
            if (tocandoOuPausada) {
                int percentProgressBar = (int) Math.round(Math.floor(time * percentMusica));
                musicProgressBar.setValue(percentProgressBar);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                musicaTerminou = true;
            }
            else {
                musicaTerminou = false;
                break;
            }
        }
        if (musicaTerminou) {
            musicProgressBar.setValue(0);
        }

    }

}