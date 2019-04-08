package com.example;

        import com.example.model.Artist;
        import com.example.model.Datasource;
        import com.example.model.SongArtist;

        import java.util.List;

public class Main {

    public static void main(String[] args) {
        Datasource datasource = new Datasource();
        if(!datasource.open()) {
            System.out.println("Can't open datasource");
            return;
        }

        List<Artist> artists = datasource.queryArtist2(Datasource.ORDER_BY_DESC);
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

        List<SongArtist> artistForSong = datasource.queryArtistForSong("Go Your Own Way",Datasource.ORDER_BY_NONE);

        for(SongArtist songArtist : artistForSong) {
            if(songArtist == null) {
                return;
            }
            System.out.println("Artist : " + songArtist.getArtistName() + ", Album : " + songArtist.getAlbumName()
                                + ", Track : " + songArtist.getTrack());
        }
        
        datasource.querySongsMetaData();
        datasource.close();
    }
}
