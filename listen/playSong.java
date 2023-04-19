package com.example.listen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class playSong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    TextView textView;
    ImageView pre;
    ImageView play;
    ImageView next;
//    ImageView puase;
    ArrayList<File>songs;
    MediaPlayer mediaPlayer;
    String textContent;
    int position;
    SeekBar seekBar;
    Thread updateSeek;
    ImageView fav;
    int f=0;
    int repeat_range=0;
    ImageView music;
    ImageView list;
    ImageView repeat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textView=findViewById(R.id.textView);
        pre=findViewById(R.id.pre);
        next=findViewById(R.id.next);
        play=findViewById(R.id.play);
        seekBar=findViewById(R.id.seekBar);
        list=findViewById(R.id.list);
        fav=findViewById(R.id.fav);
        music=findViewById(R.id.music);
        repeat=findViewById(R.id.repeat);

        HashMap <String,Integer> map =new HashMap<>();

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        songs=(ArrayList) bundle.getParcelableArrayList("songlist");
        textContent=intent.getStringExtra("currSong");
        textView.setText(textContent);
        textView.setSelected(true);
        position=intent.getIntExtra("position",0);
        for(File s:songs){
            map.put(s.getName(),0);
        }
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer=MediaPlayer.create(this, uri);
        mediaPlayer.start();
        if(position%2==0){
            music.setImageResource(R.drawable.ic_baseline_music_note_24);
        }else {
            music.setImageResource(R.drawable.music);
        }
        seekBar.setMax(mediaPlayer.getDuration());


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        updateSeek=new Thread(){
            @Override
            public void run() {
                int currentPosition=0;
                try{
                    while (currentPosition<mediaPlayer.getDuration()){
                        currentPosition=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }

                }catch (Exception e){
                    e.printStackTrace();

                }
            }
        };
        updateSeek.start();

        play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else{
                    play.setImageResource(R.drawable.puase);
                    mediaPlayer.start();
                }

            }
        });

        pre.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0){
                    position=position-1;
                }else{
                    position=songs.size()-1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.puase);
                seekBar.setMax(mediaPlayer.getDuration());
                textContent=songs.get(position).getName().toString();
                textView.setText(textContent);
                repeat.setImageResource(R.drawable.repeat);
                fav.setImageResource(R.drawable.fav);
                if(position%2==0){
                    music.setImageResource(R.drawable.ic_baseline_music_note_24);
                }else {
                    music.setImageResource(R.drawable.music);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=songs.size()-1){
                    position=position+1;
                }else{
                    position=0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.puase);
                seekBar.setMax(mediaPlayer.getDuration());
                textContent=songs.get(position).getName().toString();
                textView.setText(textContent);
                repeat.setImageResource(R.drawable.repeat);
                fav.setImageResource(R.drawable.fav);
                if(position%2==0){
                    music.setImageResource(R.drawable.ic_baseline_music_note_24);
                }else {
                    music.setImageResource(R.drawable.music);
                }
            }
        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(f==0){
                    Toast.makeText(playSong.this, "Successfully Added ", Toast.LENGTH_SHORT).show();
                    String name=songs.get(position).getName();
                    fav.setImageResource(R.drawable.favdark);


//
//                    String s = name + "zz";
//                    Toast.makeText(playSong.this, s, Toast.LENGTH_SHORT).show();
//
//                    songs.get(position).getName().replace(name,s);
                    f=1;

                }else{
                    Toast.makeText(playSong.this, "Romoved from Favourite", Toast.LENGTH_SHORT).show();
                    fav.setImageResource(R.drawable.fav);
                    f=0;
                }

            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                onCreate(new Bundle());

            }
        });
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(repeat_range==0){
                    Toast.makeText(playSong.this, "Set Loop", Toast.LENGTH_SHORT).show();
                    repeat.setImageResource(R.drawable.repeat2);
                    repeat_range=1;
                    mediaPlayer.setLooping(true);


                }else{
                    Toast.makeText(playSong.this, "Release Loop", Toast.LENGTH_SHORT).show();
                    repeat.setImageResource(R.drawable.repeat);
                    repeat_range=0;
                }

            }
        });





    }
}