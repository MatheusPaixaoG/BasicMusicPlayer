import java.util.Vector;
import java.util.concurrent.locks.Lock;

import javax.swing.*;

public class Adicionar extends Thread {
    private final Lock locker;
    private final musica music;
    private final DefaultListModel<musica> listMusics;
    private final JProgressBar addRemoveProgressBar;
    private final JList listOfMusics;

    public Adicionar(Lock loki, String nome_musica, DefaultListModel<musica> musicList, JProgressBar progressBar, JList list) {
        this.addRemoveProgressBar = progressBar;
        this.listMusics = musicList;
        this.listOfMusics = list;
        this.locker = loki;
        this.music = new musica(nome_musica);
    }

    public void run() {

            locker.lock();
            try {
                for (int i = 0; i <= 100; i++) {
                    addRemoveProgressBar.setValue(i);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                listMusics.addElement(music);
                addRemoveProgressBar.setValue(0);
                //listOfMusics.updateUI();
            } catch (Exception e) {
                System.out.println("An error ocurred.");
                e.printStackTrace();
            } finally {
                locker.unlock();

        }
    }
}
