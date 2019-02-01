package dev.wilburomae.bookapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import dev.wilburomae.bookapp.R;
import dev.wilburomae.bookapp.adapters.ReaderPagerAdapter;
import dev.wilburomae.bookapp.dataaccesslayer.XmlReader;

public class ReaderActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private SearchDialogFragment mSearchDialogFragment;
    private ColorDialogFragment mColorDialogFragment;
    private FloatingActionButton mHighlightButton;
    private TextView mToolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reader);

        Intent prevIntent = getIntent();
        Bundle extras = prevIntent.getExtras();
        int position = extras.getInt("POSITION");

        Toolbar toolbar = (Toolbar) findViewById(R.id.reader_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbarText = (TextView) findViewById(R.id.reader_toolbar_text);
        setToolbarText("");

        mHighlightButton = findViewById(R.id.reader_fab_highlight);
        mHighlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColorDialogFragment = new ColorDialogFragment();
                mColorDialogFragment.show(getSupportFragmentManager(), "colorDlg");
            }
        });

        ReaderPagerAdapter mPagerAdapter = new ReaderPagerAdapter(this, XmlReader.getChapters());
        mViewPager = findViewById(R.id.reader_viewPager);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(position);

    }

    public void setToolbarText(String text) {
        mToolbarText.setText(text);
    }

    public void showHighlightButton(boolean toShow) {
        if (toShow) mHighlightButton.show();
        else mHighlightButton.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.constant_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                AboutDialogFragment aboutDialogFragment = new AboutDialogFragment();
                aboutDialogFragment.show(getSupportFragmentManager(), "aboutDlg");
                break;
//            case R.id.menu_settings:
            // TODO implement settings
//                break;
            case R.id.menu_search:
                mSearchDialogFragment = new SearchDialogFragment();
                mSearchDialogFragment.show(getSupportFragmentManager(), "searchDlg");
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getClass().equals(AppCompatImageView.class)) {
            mColorDialogFragment.getDialog().cancel();
            ((ReaderPagerAdapter) mViewPager.getAdapter()).permanentHighlight(v.getId());
        } else if (v.getClass().equals(CardView.class)) {
            mSearchDialogFragment.getDialog().cancel();
            mViewPager.setCurrentItem(Integer.parseInt(v.getTag().toString()));
        }
    }
}
