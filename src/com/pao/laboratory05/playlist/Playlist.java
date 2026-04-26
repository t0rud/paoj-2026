package com.pao.laboratory05.playlist;

import java.util.Arrays;

public class Playlist {
    private String name;
    private Song[] songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new Song[0]; // Initializat cu un array gol
    }

    public String getName() {
        return name;
    }

    public void addSong(Song song) {
        // Pattern-ul de resize
        Song[] tmp = new Song[songs.length + 1];
        System.arraycopy(songs, 0, tmp, 0, songs.length);
        tmp[tmp.length - 1] = song;
        songs = tmp;
    }

    public void printSortedByTitle() {
        //Clonat inaninte de sort yaay!!
        Song[] copy = songs.clone();
        Arrays.sort(copy); // Foloseste Comparable-ul din Song

        for (Song song : copy) {
            System.out.println(song);
        }
    }

    public void printSortedByDuration() {
        Song[] copy = songs.clone();
        Arrays.sort(copy, new SongDurationComparator()); // Foloseste Comparator-ul

        for (Song song : copy) {
            System.out.println(song);
        }
    }

    public int getTotalDuration() {
        int total = 0;
        for (Song song : songs) {
            total += song.durationSeconds(); // Apelam getter-ul generat de record
        }
        return total;
    }
}