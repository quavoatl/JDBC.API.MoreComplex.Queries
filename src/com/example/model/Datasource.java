package com.example.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\Zece 64\\" +
            "Desktop\\Java11\\JDBC Music\\" + DB_NAME;
    public static final String TABLE_ALBUMS = "albums";

    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";
    public static final int INDEX_ALBUM_ID = 1;
    public static final int INDEX_ALBUM_NAME = 2;
    public static final int INDEX_ALBUM_ARTIST = 3;

    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_ID = "_id";
    public static final String COLUMN_SONG_TRACK = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";
    public static final int INDEX_SONG_ID = 1;
    public static final int INDEX_SONG_TRACK = 2;
    public static final int INDEX_SONG_TITLE = 3;
    public static final int INDEX_SONG_ALBUM = 4;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    //SQL code:
    //SELECT albums.name from albums
    //INNER JOIN artists on albums.artist = artists._id
    //WHERE artists.name = "Carole King"
    //ORDER BY albums.name COLLATE NOCASE ASC

    public static final String QUERY_ALBUM_FOR_ARTIST_START =
            "SELECT " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " FROM " + TABLE_ALBUMS +
                    " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " +
                    TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
                    " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + " = \"";

    public static final String ORDER_BY_QUERY = "\" ORDER BY " + TABLE_ALBUMS + "." +
                              COLUMN_ALBUM_NAME + " COLLATE NOCASE ";


    private Connection conn;

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch (SQLException e) {
            System.out.println("Something went wrong " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Could not close the connection " + e.getMessage());
        }
    }

    public List<Artist> queryArtist() {
        //using finally pentru a inchide ResultSet/Statement dupa procesare

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + TABLE_ARTISTS);

            List<Artist> artists = new ArrayList<>();
            while (resultSet.next()) {
                Artist artist = new Artist();
                artist.setId(resultSet.getInt(INDEX_ARTIST_ID));
                artist.setName(resultSet.getString(INDEX_ARTIST_NAME));
                artists.add(artist);
            }
            return artists;

        } catch (SQLException e) {
            System.out.println("Query failed" + e.getMessage());
            return null;

        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing ResultSet..." + e.getMessage());
            }

            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing Statement..." + e.getMessage());
            }
        }
    }

    public List<Artist> queryArtist2(int sortOrder) {
        //using try with resources, ResultSet/Statement vor fi inchise automat dupa executare

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ARTISTS);
        if (sortOrder != ORDER_BY_NONE) {

            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sb.toString())) {

            List<Artist> artists = new ArrayList<>();
            while (resultSet.next()) {
                Artist artist = new Artist();
                artist.setId(resultSet.getInt(INDEX_ARTIST_ID));
                artist.setName(resultSet.getString(INDEX_ARTIST_NAME));
                artists.add(artist);
            }
            return artists;

        } catch (SQLException e) {
            System.out.println("Something went wrong..." + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<String> queryAlbumsForArtist(String artist, int sortOrder) {

        StringBuilder sb = new StringBuilder(QUERY_ALBUM_FOR_ARTIST_START);
        sb.append(artist);
        if(sortOrder != ORDER_BY_NONE) {
            sb.append(ORDER_BY_QUERY);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        System.out.println("SQL : " + sb.toString());

        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sb.toString())) {

            List<String> albums = new ArrayList<>();
            while (resultSet.next()) {
                albums.add(resultSet.getString(1));
            }
            return albums;

        } catch (SQLException e) {
            System.out.println("Something went wrong..." + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


}

