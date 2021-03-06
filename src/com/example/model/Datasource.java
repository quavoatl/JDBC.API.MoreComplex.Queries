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

    public static final String ORDER_QUERY_ALBUM = " ORDER BY " + TABLE_ALBUMS + "." +
            COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

    //select artists.name, albums.name, songs.track from songs
    //inner join albums on songs.album = albums._id
    //inner join artists on albums.artist = artists._id
    //WHERE songs.title = "Go Your Own Way"
    //ORDER BY albums.name COLLATE NOCASE DESC
    public static final String QUERY_ARTIST_FOR_SONG_START =
            "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + "," + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME +
                    "," + TABLE_SONGS + "." + COLUMN_SONG_TRACK + " FROM SONGS " +
                    "INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS +
                    "." + COLUMN_ALBUM_ID + " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST +
                    " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID + " WHERE " + TABLE_SONGS + "." + COLUMN_SONG_TITLE +
                    " = \"";
    public static final String ORDER_QUERY_ARTIST_FOR_SONG =
            " ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";
    ///CREATE VIEW IF NOT EXISTS artist_list AS
//SELECT artists.name, albums.name AS album, songs.track, songs.title FROM songs
//inner join albums on songs.album = albums._id
//inner join artists on albums.artist = artists._id
//order by artists.name
    public static final String VIEW_NAME = "artist_list";
    public static final String CREATE_VIEW_ARTIST_LIST =
            "CREATE VIEW IF NOT EXISTS " + VIEW_NAME + " AS SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + "," + TABLE_ALBUMS +
                    "." + COLUMN_ALBUM_NAME + " AS " + COLUMN_SONG_ALBUM + ", " + TABLE_SONGS +
                    "." + COLUMN_SONG_TRACK + ", " + TABLE_SONGS + "." + COLUMN_SONG_TITLE + " FROM " + TABLE_SONGS +
                    " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." +
                    COLUMN_ALBUM_ID +
                    " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " +
                    TABLE_ARTISTS + "." + COLUMN_ARTIST_ID + " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME;

    //SELECT name, album, track from artist_list where title = "Phantom Lord"

    public static final String QUERY_VIEW_SONG_INFO =
            "SELECT " + COLUMN_ARTIST_NAME + ", " + COLUMN_SONG_ALBUM + ", " + COLUMN_SONG_TRACK + " FROM " + VIEW_NAME +
                    " WHERE " + COLUMN_SONG_TITLE + " = \"";

    public static final String QUERY_VIEW_FOR_ARTIST_SONG_INFO_PREP =
            "SELECT " + COLUMN_ARTIST_NAME + ", " + COLUMN_SONG_ALBUM + ", " + COLUMN_SONG_TRACK + " FROM " + VIEW_NAME +
                    " WHERE " + COLUMN_SONG_TITLE + " = ?";

    public static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS + "(" + COLUMN_ARTIST_NAME + ") VALUES(?)";

    public static final String INSERT_ALBUMS = "INSERT INTO " + TABLE_ALBUMS + "(" + COLUMN_ALBUM_NAME + ", " + COLUMN_ALBUM_ARTIST +
            ") VALUES(?, ?)";

    public static final String INSERT_SONGS = "INSERT INTO " + TABLE_SONGS + "(" + COLUMN_SONG_TRACK + ", " + COLUMN_SONG_TITLE +
            "," + COLUMN_SONG_ALBUM + ") VALUES(?, ?, ?)";

    public static final String QUERY_ARTIST_ID = "SELECT " + COLUMN_ARTIST_ID + " FROM " + TABLE_ARTISTS +
            " WHERE " + COLUMN_ARTIST_NAME + " = ?";
    public static final String QUERY_ALBUM_ID = "SELECT " + COLUMN_ALBUM_ID + " FROM " + TABLE_ALBUMS +
            " WHERE " + COLUMN_ALBUM_NAME + " = ?";


    private Connection conn;

    private PreparedStatement querySongInfoView;

    private PreparedStatement insertIntoArtist;
    private PreparedStatement insertIntoAlbums;
    private PreparedStatement insertIntoSongs;

    private PreparedStatement queryArtistID;
    private PreparedStatement queryAlbumID;

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            querySongInfoView = conn.prepareStatement(QUERY_VIEW_FOR_ARTIST_SONG_INFO_PREP);
            insertIntoArtist = conn.prepareStatement(INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS);
            insertIntoAlbums = conn.prepareStatement(INSERT_ALBUMS, Statement.RETURN_GENERATED_KEYS);
            insertIntoSongs = conn.prepareStatement(INSERT_SONGS);
            queryArtistID = conn.prepareStatement(QUERY_ARTIST_ID);
            queryAlbumID = conn.prepareStatement(QUERY_ALBUM_ID);

            return true;
        } catch (SQLException e) {
            System.out.println("Something went wrong " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (querySongInfoView != null) {
                querySongInfoView.close();
            }
            if (insertIntoArtist != null) {
                insertIntoArtist.close();
            }
            if (insertIntoAlbums != null) {
                insertIntoAlbums.close();
            }
            if (insertIntoSongs != null) {
                insertIntoSongs.close();
            }
            if (queryArtistID != null) {
                queryArtistID.close();
            }
            if (queryAlbumID != null) {
                queryAlbumID.close();
            }

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
        sb.append("\"");
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(ORDER_QUERY_ALBUM);
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

    public List<SongArtist> queryArtistForSong(String songName, int sortOrder) {

        StringBuilder sb = new StringBuilder(QUERY_ARTIST_FOR_SONG_START);
        sb.append(songName);
        sb.append("\"");
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(ORDER_QUERY_ARTIST_FOR_SONG);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        System.out.println("SQL statement : " + sb.toString());

        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sb.toString())) {

            List<SongArtist> artistForSong = new ArrayList<>();

            while (resultSet.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(resultSet.getString(1));
                songArtist.setAlbumName(resultSet.getString(2));
                songArtist.setTrack(resultSet.getInt(3));
                artistForSong.add(songArtist);
            }
            return artistForSong;


        } catch (SQLException e) {
            System.out.println("Something went wrong..." + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void querySongsMetaData() {
        String sql = "SELECT * FROM " + TABLE_SONGS;

        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int numColumns = metaData.getColumnCount();

            for (int i = 1; i <= numColumns; i++) {
                System.out.format("Column %d in the songs table is names %s\n",
                        i, metaData.getColumnName(i));
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong..." + e.getMessage());
        }
    }


    public boolean createViewForArtist() {

        try (Statement statement = conn.createStatement()) {
            statement.execute(CREATE_VIEW_ARTIST_LIST);
            System.out.println("Created the view");
            return true;

        } catch (SQLException e) {
            System.out.println("Something went wrong..." + e.getMessage());
            e.printStackTrace();
            System.out.println("Error");
            return false;
        }
    }

    public List<SongArtist> queryViewForSong(String songName) {

        try {
            //Prepared Statement
            querySongInfoView.setString(1, songName);
            ResultSet resultSet = querySongInfoView.executeQuery();

            List<SongArtist> nameAlbumTrack = new ArrayList<>();
            while (resultSet.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(resultSet.getString(1));
                songArtist.setAlbumName(resultSet.getString(2));
                songArtist.setTrack(resultSet.getInt(3));
                nameAlbumTrack.add(songArtist);
            }
            return nameAlbumTrack;

        } catch (SQLException e) {
            System.out.println("Something went wrong..." + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private int insertAlbum(String name, int artistID) throws SQLException {

        queryAlbumID.setString(1, name);
        ResultSet resultSet = queryAlbumID.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt(1);
        } else {
            insertIntoAlbums.setString(1, name);
            insertIntoAlbums.setInt(2, artistID);
            int affectedRows = insertIntoAlbums.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Something went wrong...Couldn't insert album");
            }

            ResultSet getGeneratedKey = insertIntoAlbums.getGeneratedKeys();
            if (getGeneratedKey.next()) {
                return getGeneratedKey.getInt(1);
            } else {
                throw new SQLException("Something went wrong...Couldn't get the _id");
            }
        }
    }

    private int insertArtist(String name) throws SQLException {

        queryArtistID.setString(1, name);
        ResultSet resultSet = queryArtistID.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt(1);
        } else {
            insertIntoArtist.setString(1, name);
            int affectedRows = insertIntoArtist.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Something went wrong...Couldn't insert artist");
            }

            ResultSet getGeneratedKey = insertIntoArtist.getGeneratedKeys();
            if (getGeneratedKey.next()) {
                return getGeneratedKey.getInt(1);
            } else {
                throw new SQLException("Something went wrong...Couldn't get the _id");
            }
        }
    }

    public int insertSong(String title, String artist, String album, int track) {

        //public static final String INSERT_SONGS = "INSERT INTO " + TABLE_SONGS + "(" + COLUMN_SONG_TRACK + ", " + COLUMN_SONG_TITLE +
        //            "," + COLUMN_SONG_ALBUM + ") VALUES(?, ?, ?)";

        try {
            int artistID = insertArtist(artist);
            int albumID = insertAlbum(album, artistID);

            conn.setAutoCommit(false);
            insertIntoSongs.setInt(1, track);
            insertIntoSongs.setString(2, title);
            insertIntoSongs.setInt(3, albumID);

            int affectedRows = insertIntoSongs.executeUpdate();

            if (affectedRows == 1) {
                conn.commit();
                System.out.println("Successfully added the song");
            } else {
                throw new SQLException("Something went wrong...song insertion failed" );
            }

        } catch (SQLException e) {
            System.out.println("Something went wrong..." + e.getMessage());
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Rolling back...");
                e.printStackTrace();
            }
        } finally {

            try {
                conn.setAutoCommit(true);
                System.out.println("Setting auto commint back to true");

            } catch (SQLException e) {
                System.out.println("Something went wrong..." + e.getMessage());
                e.printStackTrace();
            }
        }

    }
}
