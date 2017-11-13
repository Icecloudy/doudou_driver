package com.doudou.driver.ui.profile.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.doudou.driver.BaseActivity;
import com.doudou.driver.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity {


    @BindView(R.id.tvLoginPwd)
    TextView tvLoginPwd;
    @BindView(R.id.tvPayPwd)
    TextView tvPayPwd;

    private String title;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_password);
        setTitle(R.string.change_password);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.tvLoginPwd, R.id.tvPayPwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvLoginPwd:
                title = tvLoginPwd.getText().toString();
                break;
            case R.id.tvPayPwd:
                title = tvPayPwd.getText().toString();
                break;
        }
        Intent intent = new Intent(this,SetPasswordActivity.class);
        intent.putExtra("title",title);
        startActivity(intent);
        finish();
    }

}
