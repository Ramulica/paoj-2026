package com.pao.laboratory05.playlist;

public record Song(String title, String artist, int durationSeconds)
        implements Comparable<Song> {

    // ✅ instance method
    public String formattedDuration() {
        return durationSeconds / 60 + ":" + String.format("%02d", durationSeconds % 60);
    }

    // ✅ Comparable — natural order: alphabetical by title
    @Override
    public int compareTo(Song other) {
        return this.title.compareTo(other.title);
    }
}
        // compareTo: sortare după titlu (alfabetic)
        // Hint: String are deja compareTo — folosește-l
