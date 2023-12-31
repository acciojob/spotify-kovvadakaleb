package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        boolean found=false;
        for(User user : users){
            if(user.getMobile().equals(mobile)){
                found=true;
            }
        }
        if(found==false) {
            User user = new User(name,mobile);
            users.add(user);
            return user;
        }


       return new User();
    }

    public Artist createArtist(String name) {
        boolean found=false;
        for(Artist artist : artists){
            if(artist.getName().equals(name)){
                found=true;
            }
        }

        if(found==false){
            Artist artist = new Artist(name);
            artists.add(artist);
            return artist;
        }




     return new Artist();
    }

    public Album createAlbum(String title, String artistName) {
        boolean found = false;
        Artist artist1 = null;
        for(Artist artist : artists){
            if(artist.getName().equals(artistName)){
                artist1 = artist;
                found = true;
                break;
            }
        }
        if(found==false){
            Artist artist = new Artist(artistName);
            artist1 = artist;
            artists.add(artist);
        }
        boolean found1=false;
        for(Album album : albums){
            if(album.getTitle().equals(title)){
                found1=true;
                break;
            }
        }
        Album album1 = null;
        if(found1==false){
            Album album = new Album(title,artistName);
            album1 = album;
            albums.add(album);
        }


        if(artistAlbumMap.containsKey(artist1)){
            List<Album> newlist = artistAlbumMap.get(artist1);
            newlist.add(album1);
            artistAlbumMap.put(artist1,newlist);
        }
        else{
            List<Album> list = new ArrayList<>();
            list.add(album1);
            artistAlbumMap.put(artist1,list);
        }
        return album1;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        boolean found = false;
        Album album1 = null;
        for(Album album : albums){
            if(album.getTitle().equals(albumName)){
                 album1 = album;
                found=true;
                break;
            }
        }
        if(found==false) throw new Exception("Album does not exist");

        Song song  = new Song(title,albumName,length);
        songs.add(song);

        if(albumSongMap.containsKey(album1)){
           List<Song> oldlist = albumSongMap.get(album1);
           oldlist.add(song);
           albumSongMap.put(album1,oldlist);
       }
       else{
           List<Song> newlist = new ArrayList<>();
           newlist.add(song);
           albumSongMap.put(album1,newlist);
       }
        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        boolean found = false;
        User user1 = null;
        for(User user : users){
            if(user.getMobile().equals(mobile)){
                user1 = user;
                found=true;
                break;
            }
        }
        if(found==false) throw new Exception("User does not exist");

        Playlist playlist = new Playlist(title);
        playlists.add(playlist);
        creatorPlaylistMap.put(user1,playlist);
        List<User> list = new ArrayList<>();
        list.add(user1);
        playlistListenerMap.put(playlist,list);
        List<Song> newlist = new ArrayList<>();
        for(Song song : songs){
            if(song.getLength()==length){
                newlist.add(song);
            }
        }
        playlistSongMap.put(playlist,newlist);


        if(userPlaylistMap.containsKey(user1)){
            List<Playlist> oldlist = userPlaylistMap.get(user1);
            oldlist.add(playlist);
            userPlaylistMap.put(user1,oldlist);
        }
        else{
            List<Playlist> newList1 = new ArrayList<>();
            newList1.add(playlist);
            userPlaylistMap.put(user1,newList1);
        }
        return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        boolean found = false;
        User user1 = null;
        for(User user : users){
            if(user.getMobile().equals(mobile)){
               user1 = user;
                found=true;
                break;
            }
        }
        if(found==false) throw new Exception("User does not exist");
        Playlist playlist = new Playlist(title);
        creatorPlaylistMap.put(user1,playlist);
        List<User> list = new ArrayList<>();
        list.add(user1);
        playlistListenerMap.put(playlist,list);
        List<Song> newlist = new ArrayList<>();
        for(String songTitle : songTitles){
            for(Song song : songs){
                if(song.getTitle().equals(songTitle)){
                    newlist.add(song);
                }
            }
        }
        playlistSongMap.put(playlist,newlist);

        if(userPlaylistMap.containsKey(user1)){
            List<Playlist> oldlist = userPlaylistMap.get(user1);
            oldlist.add(playlist);
            userPlaylistMap.put(user1,oldlist);
        }
        else{
            List<Playlist> newList1 = new ArrayList<>();
            newList1.add(playlist);
            userPlaylistMap.put(user1,newList1);
        }
        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        boolean found = false;
        User user1 = null;
        for(User user : users){
            if(user.getMobile().equals(mobile)){
                user1 = user;
                found=true;
                break;
            }
        }
        if(found==false) throw new Exception("User does not exist");

        boolean found1 = false;
        Playlist playlist1 = null;
        for(Playlist  playlist : playlists){
            if(playlist.getTitle().equals(playlistTitle)){
                playlist1 = playlist;
                found1=true;
                break;
            }
        }

        if(found1==false) throw new Exception("Playlist does not exist");

        boolean found2 = false;
        List<User> list = playlistListenerMap.get(playlist1);
        for(User user : list){
            if(user.getMobile().equals(mobile)){
               found2=true;
            }
        }
        if(found2==false){
            list.add(user1);
            playlistListenerMap.put(playlist1,list);
        }
        return playlist1;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        boolean found = false;
        User user1 = null;

        for(User user : users){
            if(user.getMobile().equals(mobile)){
                user1 = user;
                found = true;
                break;
            }
        }
        if(found==false) throw new Exception("User does not exist");

        boolean found1 = false;
        Song song1 = null;

        for(Song song : songs){
            if(song.getTitle().equals(songTitle)){
                song1 = song;
                found1= true;
                break;
            }
        }
        if(found1==false) throw new Exception("Song does not exist");

        boolean found3 = false;
        boolean found4 = false;
        boolean found5 = false;
        if(songLikeMap.containsKey(song1)){
            List<User> list = songLikeMap.get(song1);
            for(User user : list){
                if(user.getMobile().equals(mobile)){
                    found3 = true;
                    break;
                }
            }

            if(found3==false){
               found4 = true;
               list.add(user1);
                songLikeMap.put(song1,list);
                int likes = song1.getLikes();
                song1.setLikes(likes=likes+1);
            }

        }
        else{
            found5=true;
            List<User> newlist = new ArrayList<>();
            newlist.add(user1);
            songLikeMap.put(song1,newlist);
            song1.setLikes(1);
        }
          Artist artist = null;

          for (Map.Entry<Artist, List<Album>> entry : artistAlbumMap.entrySet()) {
              List<Album> albums = entry.getValue();
              for (Album album : albums) {
                  if (album.getTitle().equals(song1.getAlbumName())) {
                      artist = entry.getKey();
                      break;
                  }
              }
              if (artist != null) {
                  break;
              }
          }
          if(found4==true || found5==true) {
              int likes = artist.getLikes();
              artist.setLikes(likes = likes + 1);
          }

          return song1;
    }

    public String mostPopularArtist() {
         Artist artist1 = null;
         int most = Integer.MIN_VALUE;
         for(Artist artist : artists){
             if(artist.getLikes()>most){
                 most = artist.getLikes();
                 artist1 = artist;
             }
         }
         return artist1.getName();
    }

    public String mostPopularSong() {
      Song song1 = null;
      int most = Integer.MIN_VALUE;
     for(Song song : songs){
         if(song.getLikes()>most){
             most = song.getLikes();
             song1 = song;
         }
     }
     return song1.getTitle();
    }
}
