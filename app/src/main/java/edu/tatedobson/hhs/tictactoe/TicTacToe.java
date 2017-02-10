package edu.tatedobson.hhs.tictactoe;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
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

import java.util.concurrent.TimeUnit;

public class TicTacToe extends AppCompatActivity implements android.view.View.OnTouchListener{

    private TicTacToeGame mGame;
    private TextView mInfoTextView;
    private boolean mGameOver;
    private int playerWins = 0;
    private int ties = 0;
    private int androidWins = 0;
    private BoardView mBoardView;

    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        if (savedInstanceState == null) {
            startNewGame();
        }
        else {
            mGame.setBoardState(savedInstanceState.getCharArray("board"));
            mGameOver = savedInstanceState.getBoolean("mGameOver");
            playerWins = savedInstanceState.getInt("mHumanWins");
            androidWins = savedInstanceState.getInt("mComputerWins");
            ties = savedInstanceState.getInt("ties");
            mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
            mGame.setDifficultyLevel(savedInstanceState.getInt("mDifficulty"));
        }


        startNewGame();
    }




    private void startNewGame() {
        mBoardView = (BoardView) findViewById(R.id.board);
        mGameOver = false;
        mGame = new TicTacToeGame();
        mInfoTextView = (TextView)findViewById(R.id.information);

        mBoardView.setOnTouchListener(this);
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

        if(mGame.getBoardOccupant(pos) == mGame.OPEN_SPOT && !mGameOver) {
            setMove(TicTacToeGame.HUMAN_PLAYER, pos);
            if(mGame.checkForWinner() == 0) {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        mGameOver = true;
                        setMove(TicTacToeGame.COMPUTER_PLAYER, mGame.getComputerMove());
                        mGameOver = false;
                    }
                }, 500);
            }
        }

        // So we aren't notified of continued events when finger is moved
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.x_sound);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.o_sound);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
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

        if(player == TicTacToeGame.HUMAN_PLAYER) {
            mHumanMediaPlayer.start();
        } else {
            mComputerMediaPlayer.start();
        }

        switch(mGame.checkForWinner()) {
            case 1:
                mInfoTextView.setText(R.string.result_tie);
                ties++;
                TextView t = (TextView)findViewById(R.id.ties);
                t.setText(getString(R.string.ties) + ties);
                mGameOver = true;
                break;
            case 2:
                mInfoTextView.setText(R.string.result_human_wins);
                playerWins++;
                TextView h = (TextView)findViewById(R.id.human_wins);
                h.setText(getString(R.string.human) + playerWins);
                mGameOver = true;
                break;
            case 3:
                mInfoTextView.setText(R.string.result_computer_wins);
                androidWins++;
                TextView a = (TextView)findViewById(R.id.android_wins);
                a.setText(getString(R.string.android) + androidWins);
                mGameOver = true;
                break;
        }
    }

    public TicTacToeGame getGame() {
        return mGame;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharArray("board", mGame.getBoardState());
        outState.putBoolean("mGameOver", mGameOver);
        outState.putInt("mHumanWins", playerWins);
        outState.putInt("mComputerWins", androidWins);
        outState.putInt("mTies", ties);
        outState.putCharSequence("info", mInfoTextView.toString());
        outState.putInt("mDifficulty", mGame.getDifficultyLevel());
    }
}
