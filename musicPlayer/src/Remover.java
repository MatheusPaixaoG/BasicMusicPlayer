import java.util.Vector;
import java.util.concurrent.locks.Lock;

import javax.swing.*;

public class Remover extends Thread {
    private final Lock locker;
    private final int indexMusicaSelecionada;
    private final DefaultListModel<musica> listMusics;
    private final JProgressBar addRemoveProgressBar;
    private final JLabel label;
    private final JList listOfMusics;

    public Remover(Lock loki, DefaultListModel<musica> musicList, JProgressBar progressBar, JList listOfMusics, int musicaSelecionada, JLabel label) {
        this.label = label;
        this.listOfMusics = listOfMusics;
        this.addRemoveProgressBar = progressBar;
        this.locker = loki;
        this.indexMusicaSelecionada = musicaSelecionada;
        this.listMusics = musicList;
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
                int tempIndex = 0;
                for (int m = 0; m < listMusics.size(); m++) {
                    musica music = listMusics.get(m);
                    if (music.nome == listMusics.get(indexMusicaSelecionada).nome) {
                        break;
                    }
                    tempIndex++;
                }
                int sizeOfListBeforeRemove = listMusics.size();
                listMusics.removeElementAt(indexMusicaSelecionada);
                int sizeOfListAfterRemove = listMusics.size();
                if (sizeOfListBeforeRemove != sizeOfListAfterRemove) {
                    if (tempIndex <= Interface.currentMusicIndex) {
                        Interface.currentMusicIndex--;
                        if (tempIndex == Interface.currentMusicIndex) {
                            if (listMusics.size() == 0) {
                                //Sem musica tocada
                                label.setText("Música atual: nenhuma");
                            }
                            else {
                                //Atualizar a musica atual sendo tocada
                                label.setText("Música atual: " + listMusics.get(Interface.currentMusicIndex));
                            }
                        }
                    }


                }
                addRemoveProgressBar.setValue(0);
                // listOfMusics.updateUI();
            } catch (Exception e) {
                System.out.println("An error ocurred.");
                e.printStackTrace();
            } finally {
                locker.unlock();
            }

    }
}

