package musicPlayer;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    private static Clip clip;
    private static AudioInputStream audioStream;
    private static int currentSongIndex = 0;
    private static String[] playlist = {
            "src/musicPlayer/F16 - The Grey Room _ Golden Palms.wav",
            "src/musicPlayer/make the visible invisible - Alge.wav"
    };
    private static JLabel name;
    
    public static void main(String[] args) {
        // Set up the frame for the GUI
        JFrame frame = new JFrame("Music Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());

        name = new JLabel("Now Playing: " + new File(playlist[currentSongIndex]).getName());
        frame.add(name);

        JButton playButton = new JButton("Play");
        JButton stopButton = new JButton("Stop");
        JButton resetButton = new JButton("Reset");
        JButton nextButton = new JButton("Next");
        JButton prevButton = new JButton("Previous");
        JButton quitButton = new JButton("Quit");

        frame.add(playButton);
        frame.add(stopButton);
        frame.add(resetButton);
        frame.add(nextButton);
        frame.add(prevButton);
        frame.add(quitButton);
        initializeAudio();
        
        // Play button 
        playButton.addActionListener(e -> clip.start());
        //Stop button
        stopButton.addActionListener(e -> clip.stop());
        //Reset button
        resetButton.addActionListener(e -> clip.setMicrosecondPosition(0));
        //Next button
        nextButton.addActionListener(e -> {
            currentSongIndex = (currentSongIndex + 1) % playlist.length;
            switchSong();
        });
        //Previous button
        prevButton.addActionListener(e -> {
            currentSongIndex = (currentSongIndex - 1 + playlist.length) % playlist.length;
            switchSong();
        });
        //Quit button 
        quitButton.addActionListener(e -> {
            clip.close();
            System.exit(0);
        });
        frame.setVisible(true);
    }

    private static void initializeAudio() {
        try {
            File file = new File(playlist[currentSongIndex]);
            audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file");
        } catch (LineUnavailableException e) {
            System.out.println("Cannot access audio source");
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Audio file not supported");
        } catch (IOException e) {
            System.out.println("Error loading audio file");
        }
    }

    private static void switchSong() {
        clip.close();
        File file = new File(playlist[currentSongIndex]);
        audioStream = null;
        try {
            audioStream = AudioSystem.getAudioInputStream(file);
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error loading new audio: ");
            return; 
        }

        // Update label to display just the file name
        name.setText("Now Playing: " + file.getName());
        clip.start();
    }
}