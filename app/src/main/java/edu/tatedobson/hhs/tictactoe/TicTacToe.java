package edu.tatedobson.hhs.tictactoe;

import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TicTacToe extends AppCompatActivity implements android.view.View.OnClickListener{

    private TicTacToeGame mGame;
    private Button mBoardButtons[];
    private TextView mInfoTextView;
    private boolean mGameOver;
    private int playerWins = 0;
    private int ties = 0;
    private int androidWins = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        mBoardButtons = new Button[9];
        mBoardButtons[0] = (Button)findViewById(R.id.zero);
        mBoardButtons[1] = (Button)findViewById(R.id.one);
        mBoardButtons[2] = (Button)findViewById(R.id.two);
        mBoardButtons[3] = (Button)findViewById(R.id.three);
        mBoardButtons[4] = (Button)findViewById(R.id.four);
        mBoardButtons[5] = (Button)findViewById(R.id.five);
        mBoardButtons[6] = (Button)findViewById(R.id.six);
        mBoardButtons[7] = (Button)findViewById(R.id.seven);
        mBoardButtons[8] = (Button)findViewById(R.id.eight);

        mGameOver = false;
        mGame = new TicTacToeGame();
        mInfoTextView = (TextView)findViewById(R.id.information);

        startNewGame();
    }

    private void startNewGame() {
        mGame.clearBoard();
        for(Button x : mBoardButtons) {
            x.setText("");
            x.setEnabled(true);
            x.setOnClickListener(this);
        }
        mGameOver = false;
        mInfoTextView.setText(R.string.first_human);
    }

    public void onClick(View v) {
        if(v.isEnabled() && !mGameOver) {
            for(int i = 0; i < mBoardButtons.length; i++) {
                if(mBoardButtons[i].getId() == v.getId()) {
                    setMove(TicTacToeGame.HUMAN_PLAYER, i);
                }
            }
            if(mGame.checkForWinner() == 0) {
                setMove(mGame.COMPUTER_PLAYER, mGame.getComputerMove());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                break;
            case R.id.action_difficulty:
                DialogFragment newFragment = new DifficultyDialog();
                newFragment.show(getSupportFragmentManager(), "dialog");
                startNewGame();
        }
        return true;
    }

    private void setMove(char player, int location) {

        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        if (player == TicTacToeGame.HUMAN_PLAYER) {
            mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
            mInfoTextView.setText(R.string.turn_computer);
        }
        else {
            mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
            mInfoTextView.setText(R.string.turn_human);
        }

        switch(mGame.checkForWinner()) {
            case 1:
                mInfoTextView.setText(R.string.result_tie);
                ties++;
                TextView t = (TextView)findViewById(R.id.ties);
                t.setText("Ties: " + ties);
                mGameOver = true;
                break;
            case 2:
                mInfoTextView.setText(R.string.result_human_wins);
                playerWins++;
                TextView h = (TextView)findViewById(R.id.human_wins);
                h.setText("Human: " + playerWins);
                mGameOver = true;
                break;
            case 3:
                mInfoTextView.setText(R.string.result_computer_wins);
                androidWins++;
                TextView a = (TextView)findViewById(R.id.android_wins);
                a.setText("Android: " + androidWins);
                mGameOver = true;
                break;
        }
    }

    public TicTacToeGame getGame() {
        return mGame;
    }
}
