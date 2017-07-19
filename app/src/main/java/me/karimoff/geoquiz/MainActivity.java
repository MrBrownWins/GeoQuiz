package me.karimoff.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private TextView scoreView;
    private TextView sentenceView;
    private int score = 0;
    private  int answerIndex;
    ArrayList<String> sentencesList = new ArrayList<>();
    ArrayList<Boolean> answersList = new ArrayList<>();

    public ArrayList<String> getSentencesList() {
        return sentencesList;
    }

    public void setSentencesList(ArrayList<String> sentencesList) {
        this.sentencesList = sentencesList;
    }

    public ArrayList<Boolean> getAnswersList() {
        return answersList;
    }

    public void setAnswersList(ArrayList<Boolean> answersList) {
        this.answersList = answersList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchSentences();

        scoreView = (TextView) findViewById(R.id.score);
        sentenceView = (TextView) findViewById(R.id.sentence);

        Button trueButton = (Button) findViewById(R.id.true_id);
        Button falseButton = (Button) findViewById(R.id.false_id);

        OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandle(view);
            }
        };

        scoreView.setText(getString(R.string.score_text, score));
        sentenceView.setText(getRandomSentence());

        trueButton.setOnClickListener(listener);
        falseButton.setOnClickListener(listener);

    }

    public void clickHandle(View view) {
        Boolean answer = false;

        if (view.getId() == R.id.true_id  ){
            answer = true;
        }

        if (answer == answersList.get(answerIndex)){
            score++;
            scoreView.setText(getString(R.string.score_text, score));

            sentenceView.setText(getRandomSentence());

            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
        } else {
            score--;
            scoreView.setText(getString(R.string.score_text, score));

            Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    public String getRandomSentence(){
        Random random = new Random();
        answerIndex = random.nextInt(getSentencesList().size());
        return getSentencesList().get(answerIndex);
    }

    public void fetchSentences(){

        ArrayList<String> sentencesList = getSentencesList();
        ArrayList<Boolean> answersList = getAnswersList();

        String json = null;

        try {
            InputStream jsonStream = getResources().openRawResource(R.raw.sentences);
            int size = jsonStream.available();
            byte[] buffer = new byte[size];
            jsonStream.read(buffer);
            jsonStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray sentences = jsonObject.getJSONArray("sentences");

            for (int i = 0; i < sentences.length(); i++) {
                JSONObject tempObject = sentences.getJSONObject(i);
                sentencesList.add(tempObject.getString("sentence"));
                answersList.add(tempObject.getBoolean("answer"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
