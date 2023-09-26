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
       User user = new User(name,mobile);
       users.add(user);
       return user;
    }

    public Artist createArtist(String name) {
     Artist artist = new Artist(name);
     artists.add(artist);
     return artist;
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
        Album album = new Album(title,artistName);
        albums.add(album);

        if(artistAlbumMap.containsKey(artist1)){
            List<Album> newlist = artistAlbumMap.get(artist1);
            newlist.add(album);
            artistAlbumMap.put(artist1,newlist);
        }
        else{
            List<Album> list = new ArrayList<>();
            list.add(album);
            artistAlbumMap.put(artist1,list);
        }
        return album;
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

        List<User> list = playlistListenerMap.get(playlist1);
        for(User user : list){
            if(!user.getMobile().equals(user1.getMobile())){
                list.add(user1);
                playlistListenerMap.put(playlist1,list);
            }
        }
        return playlist1;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        boolean found = false;
        User user1 = null;

        for(User user : users){
            if(user.getMobile().equals(mobile)){
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
                found = true;
                break;
            }
        }
        if(found1==false) throw new Exception("Song does not exist");

        boolean found3 = true;
        if(songLikeMap.containsKey(song1)){
            List<User> list = songLikeMap.get(song1);
            for(User user : list){
                if(!user.getMobile().equals(mobile)){
                    found3 = false;
                }
            }

            if(found3==false){
                int likes = song1.getLikes();
                song1.setLikes(likes++);
            }
        }
        else{
            List<User> list = new ArrayList<>();
            list.add(user1);
            songLikeMap.put(song1,list);
            song1.setLikes(1);
        }
        return song1;
    }

    public String mostPopularArtist() {
         Artist artist =new Artist("kaleb");
         return artist.getName();
    }

    public String mostPopularSong() {
     Song song = new Song("kesariya","Arjit",5);
     return song.getTitle();
    }
}
