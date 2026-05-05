package com.pao.laboratory05.playlist;

import java.util.Arrays;

public class Playlist {
    private String name;
    private Song[] songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new Song[0];
    }

    public String getName() {
        return this.name;
    }

    public void addSong(Song song) {
        Song[] newSongs = new Song[this.songs.length + 1];
        System.arraycopy(this.songs, 0, newSongs, 0, this.songs.length);

        newSongs[newSongs.length - 1] = song;
        this.songs = newSongs;
    }

    public void printSortedByTitle() {
        Song[] newSongs = this.songs.clone();
        Arrays.sort(newSongs);

        System.out.println(Arrays.toString(newSongs).replace(", S", "\nS"));
    }

    public void printSortedByDuration() {
        Song[] newSongs = this.songs.clone();
        Arrays.sort(newSongs, new SongDurationComparator());
        System.out.println(Arrays.toString(newSongs).replace(", S", "\nS"));

    }

    public int getTotalDuration() {
        int total = 0;
        for(var song : this.songs) {
            total += song.durationSeconds();
        }
        return total;
    }
}
