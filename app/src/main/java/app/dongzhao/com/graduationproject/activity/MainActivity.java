package app.dongzhao.com.graduationproject.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.dongzhao.com.graduationproject.InitNews;
import app.dongzhao.com.graduationproject.R;
import app.dongzhao.com.graduationproject.domain.GlobalConstants;
import app.dongzhao.com.graduationproject.domain.NewsMenu;
import app.dongzhao.com.graduationproject.utils.PrefUtil;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //自定义对话框
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private ListView mListView;

    /**
     * Activity生命周期的方法
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        builder = new AlertDialog.Builder(this);
        //找到新闻显示的界面
        mListView = (ListView) findViewById( R.id.lv_listview);
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 100;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = new TextView(MainActivity.this);
                tv.setText("helloword");
                return tv;
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        //浮动按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.more_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view,"没有更多内容", Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();
                //这里打开一个笔记界面,判断是否设置过密码
                if (isPwdSetted()) {
                    //登录密码
                    showEnterDialog();
                } else {
                    //设置密码
                    showSetupDialog();
                }
            }
        });
        //抽屉布局设置监听,监听抽屉的点击事件
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
       //监听拖拽
        drawer.setDrawerListener(toggle);
        //状态同步
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView设置监听
        navigationView.setNavigationItemSelectedListener(this);
        // 请求服务器获取数据
        initNews();
        //初始化新闻显示

    }

    /**
     * 加载新闻数据
     */
    private void initNews() {
        //拿到对象
        InitNews initNew = new InitNews();
        //网络请求数据
        initNew.getDataFromServer(MainActivity.this);
        //回调方法
        initNew.getNewsData(new InitNews.NewsData() {
            @Override
            public void sendNewsData(NewsMenu news) {
                ArrayList<NewsMenu.NewsTabData> newsDetail = news.data.get(0).children;
                //拿到详细数据地址
                String url = GlobalConstants.SERVER_URL + newsDetail.get(0).url;

            }
        });
    }


    /**
     * 进入输入密码对话框
     */
    private void showEnterDialog() {
        View view = View.inflate(this, R.layout.dialog_enter_pwd, null);
        builder.setView(view);
        final EditText et_pwd = (EditText) view.findViewById(R.id.et_dialog_pwd);
        Button bt_dialog_ok = (Button) view.findViewById(R.id.bt_dialog_ok);
        Button bt_dialog_cancle = (Button) view.findViewById(R.id.bt_dialog_cancle);
        //取消按钮
        bt_dialog_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //确定按钮
        bt_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户输入的密码
                String newPwd = et_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(newPwd)) {
                    Toast.makeText(MainActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //获取用户设置的密码
                String oldPwd = PrefUtil.getString(MainActivity.this, "pwd", null);
                //比较原来设置的密码和这次输入的密码是否一致.
                if (newPwd.equals(oldPwd)) {
                    dialog.dismiss();
                    Intent intent = new Intent(MainActivity.this, DiaryActivity.class);
                    startActivity(intent);
                    return;
                } else {
                    Toast.makeText(MainActivity.this, "喵~ 密码错误", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
            }
        });
        dialog = builder.show();
    }

    /**
     * 设置密码对话框
     */
    private void showSetupDialog() {
        View view = View.inflate(this, R.layout.dialog_setup_pwd, null);
        builder.setView(view);
        final EditText et_pwd = (EditText) view.findViewById(R.id.et_dialog_pwd);
        final EditText et_pwd_confirm = (EditText) view.findViewById(R.id.et_dialog_pwd_confirm);
        Button bt_dialog_ok = (Button) view.findViewById(R.id.bt_dialog_ok);
        Button bt_dialog_cancle = (Button) view.findViewById(R.id.bt_dialog_cancle);
        bt_dialog_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bt_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = et_pwd.getText().toString().trim();
                String pwd_confirm = et_pwd_confirm.getText().toString().trim();
                if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd_confirm)) {
                    Toast.makeText(MainActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pwd.equals(pwd_confirm)) {
                    Toast.makeText(MainActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                //保存登录记录
                PrefUtil.setString(MainActivity.this, "pwd", pwd);
                //关闭对话框
                dialog.dismiss();
                //弹出输入密码对话框
                showEnterDialog();
            }
        });
        //显示对话框,把对话框的引用赋给类的成员变量
        dialog = builder.show();
    }

    /**
     * 判断是否设置过密码
     */
    private boolean isPwdSetted() {
        String pwd = PrefUtil.getString(this, "pwd", null);
        if (TextUtils.isEmpty(pwd)) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 重写返回键
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 创建扩展空间
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 右上角扩展空间,显示更多功能
     * 黑工程首页,黑工程教务处,等WebView界面
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home_page) {
//            Toast.makeText(MainActivity.this, "黑工程教务处首页", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, NewsDetailActicity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.teach_system) {
//            Toast.makeText(MainActivity.this, "教务系统", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 侧滑菜单
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
//            Intent intent  = new Intent(MainActivity.this,TakePhotoActivity.class);
//            startActivity(intent);

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            showShare();

        } else if (id == R.id.nav_send) {

        }
        //点击后取消显示侧滑菜单
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //极光分享平台
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("喵~ 分享的快乐,你懂得~ \n www.hgc.com");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("喵~  分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

}
