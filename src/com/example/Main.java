package com.example;

        import com.example.model.Artist;
        import com.example.model.Datasource;
        import com.example.model.SongArtist;

        import java.util.List;
        import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Datasource datasource = new Datasource();
        if(!datasource.open()) {
            System.out.println("Can't open datasource");
            return;
        }

        List<Artist> artists = datasource.queryArtist2(Datasource.ORDER_BY_NONE);
        if(artists == null) {
            System.out.println("no artists");
            return;
        }
        for(Artist artist : artists) {
            System.out.println("ID = " + artist.getId() + ", Name = " + artist.getName());
        }

        List<String> albumsForArtist = datasource.queryAlbumsForArtist("Carole King",Datasource.ORDER_BY_NONE);
        for(String album : albumsForArtist) {
            System.out.println(album);
        }
        
        List<SongArtist> artistForSong = datasource.queryArtistForSong("Go Your Own Way",Datasource.ORDER_BY_ASC);

        for(SongArtist songArtist : artistForSong) {
            if(songArtist == null) {
                System.out.println("Could not find the artist for this song...");
                return;
            }
            System.out.println("Artist : " + songArtist.getArtistName() + ", Album : " + songArtist.getAlbumName()
                                + ", Track : " + songArtist.getTrack());
        }

        System.out.println(Datasource.CREATE_VIEW_ARTIST_LIST);
        datasource.createViewForArtist();

        datasource.querySongsMetaData();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a song : ");
        String sql = scanner.nextLine();

        artistForSong = datasource.queryViewForSong(sql);

        if(artistForSong.isEmpty()) {
            System.out.println("Can't find what you are searching for...");
            return;
        }
        for(SongArtist artistForSongg : artistForSong) {
            System.out.println("FROM VIEW Artist : " + artistForSongg.getArtistName() + ", Album : " + artistForSongg.getAlbumName()
                    + ", Track : " + artistForSongg.getTrack() );
        }
            
        datasource.close();
    }
}
