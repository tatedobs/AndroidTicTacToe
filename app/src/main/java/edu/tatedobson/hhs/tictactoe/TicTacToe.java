package edu.tatedobson.hhs.tictactoe;

import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TicTacToe extends AppCompatActivity implements android.view.View.OnTouchListener{

    private TicTacToeGame mGame;
    private TextView mInfoTextView;
    private boolean mGameOver;
    private int playerWins = 0;
    private int ties = 0;
    private int androidWins = 0;
    private BoardView mBoardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        mBoardView = (BoardView) findViewById(R.id.board);


        mGameOver = false;
        mGame = new TicTacToeGame();
        mInfoTextView = (TextView)findViewById(R.id.information);

        mBoardView.setOnTouchListener(this);

        startNewGame();
    }

    private void startNewGame() {
        mBoardView.setGame(mGame);
        mGame.clearBoard();
        mGameOver = false;
        mInfoTextView.setText(R.string.first_human);
        mBoardView.invalidate();
    }

    public boolean onTouch(View v, MotionEvent event) {
        // Determine which cell was touched
        int col = (int) event.getX() / mBoardView.getBoardCellWidth();
        int row = (int) event.getY() / mBoardView.getBoardCellHeight();
        int pos = row * 3 + col;

        if(mGame.getBoardOccupant(pos) == ' ' && !mGameOver) {
            setMove(TicTacToeGame.HUMAN_PLAYER, pos);
            if(mGame.checkForWinner() == 0) {
                setMove(TicTacToeGame.COMPUTER_PLAYER, mGame.getComputerMove());
            }
        }

        // So we aren't notified of continued events when finger is moved
        return false;
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

        mBoardView.invalidate();
        mGame.setMove(player, location);

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
