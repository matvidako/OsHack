package hr.ms.oshack.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import hr.ms.oshack.R;

public abstract class BaseActivity extends AppCompatActivity {

    Resources resources;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.menu_drawer)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        addView(getLayoutResId());
        ButterKnife.bind(this);
        resources = getResources();
        setSupportActionBar(toolbar);
    }

    protected void addView(int layoutId) {
        LinearLayout rootView = ButterKnife.findById(this, R.id.root);
        getLayoutInflater().inflate(layoutId, rootView, true);
    }

    abstract protected int getLayoutResId();

    abstract protected int getMenuResId();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getMenuResId() != 0) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(getMenuResId(), menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

}

