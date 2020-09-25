package sk.itsovy.android.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Game hangmanGame;

    private ImageView imageView;
    private TextView textView;
    private EditText editText;

    private final int [] gallowIds = {
            R.drawable.gallows0,
            R.drawable.gallows1,
            R.drawable.gallows2,
            R.drawable.gallows3,
            R.drawable.gallows4,
            R.drawable.gallows5,
            R.drawable.gallows6,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageViewGallows);
        textView = findViewById(R.id.textViewGuessedWord);
        editText = findViewById(R.id.editTextLetter);
        hangmanGame = new HangmanGame(new Random());
        textView.setText(hangmanGame.getGuessedCharacters());
        restartGame();
    }

    public void onGallowsClick(View view) {
        if (hangmanGame.isWon() || hangmanGame.getAttemptsLeft()==0){
            restartGame();
            return;
        }

        CharSequence text = editText.getText();
        if (text == null || text.length() == 0) {
            Toast.makeText(this, "Insert a letter",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        char letter = Character.toLowerCase(text.charAt(0));

        if (letter < 'a' || letter > 'z') {
            Toast.makeText(this, "Only alphabetical allowed",
                    Toast.LENGTH_SHORT).show();
            editText.setText("");
            return;
        }


        boolean success = hangmanGame.guess(letter);
        editText.setText("");

        if (success){
            textView.setText(hangmanGame.getGuessedCharacters());
           if( hangmanGame.isWon()){
               alertWonGame();
           }


        }else{
            int gallowsIndx=Game.DEFAULT_ATTEMPTS_LEFT-hangmanGame.getAttemptsLeft();
            imageView.setImageResource(gallowIds[gallowsIndx]);

            if (hangmanGame.getAttemptsLeft()==0){
                //PREHRA
                alertLostGame();
                textView.setText(hangmanGame.getChallengeWord());
            }
        }
    }

    private void alertLostGame() {
        ColorFilter filter = new LightingColorFilter(Color.RED, Color.BLACK);
        imageView.setColorFilter(filter);
    }

    private void alertWonGame() {
        ColorFilter filter = new LightingColorFilter(Color.GREEN, Color.BLACK);
        imageView.setColorFilter(filter);
    }
    private void restartGame(){
        hangmanGame = new HangmanGame(new Random());
        textView.setText(hangmanGame.getGuessedCharacters());
        imageView.setColorFilter(null);
        imageView.setImageResource(gallowIds[0]);
    }
}