package com.doudou.driver.ui.login;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.doudou.driver.BaseActivity;
import com.doudou.driver.R;
import com.doudou.driver.SysApplication;
import com.doudou.driver.ui.login.model.DriverRegBean;
import com.doudou.driver.utils.StringUtil;
import com.doudou.driver.utils.ToastUtil;
import com.doudou.driver.view.MyBottomSheetDialog;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * phone 手机号
 * username 真实姓名
 * drivingn 驾驶证号
 * startdate 驾驶证初次领取日期(0000-00-00)
 * driving 驾驶证照片
 * carnum 车牌号 选择+输入
 * brand 车型 如野马T80
 * holder 车辆所有人
 * carregdate 车辆注册时间(0000-00-00)
 * color 车身颜色
 * carphoto 车辆照片
 * paypass 密码
 */
public class DriverAuthTwoActivity extends BaseActivity {


    @BindView(R.id.editRealName)
    EditText editRealName;
    @BindView(R.id.editDriverNum)
    EditText editDriverNum;
    @BindView(R.id.editDriverDate)
    TextView editDriverDate;
    @BindView(R.id.imgDriverCard)
    ImageView imgDriverCard;
    @BindView(R.id.imgDriverLicense)
    ImageView imgDriverLicense;
    @BindView(R.id.las_step)
    AppCompatButton lasStep;
    @BindView(R.id.next_step)
    AppCompatButton nextStep;

    String phone;
    String username;
    String drivingNum;
    String startdate;

    private File file;
    private String saveDir = Environment.getExternalStorageDirectory().getPath() + "/lexing_image";// 拍照图片保存路径
    File mars_file = new File(saveDir);
    private Bitmap photo;

    private String img_driver_url = "";
    private String img_license_url = "";
    private int imgUrl; //1.img_driver_url  2.img_license_url
    private DatePickerDialog datePicker;
    DriverRegBean regBean;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        SysApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_driver_auth_two);
        ButterKnife.bind(this);
        setTitle(R.string.driver_auth);
        initData();
    }

    private void initData() {
        phone = getIntent().getExtras().getString("phone");
        datePicker = new DatePickerDialog(this, android.app.AlertDialog.BUTTON_POSITIVE, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                String month = m > 8 ? ((m + 1) + "") : ("0" + (m + 1));
                String day = d > 9 ? (d + "") : ("0" + d);
                if (StringUtil.compare_date(StringUtil.getHttpRequestTimestamp(),y + "-" + month + "-" + day)>0){
                    editDriverDate.setText(y + "-" + month + "-" + day);
                }else{
                    showMsg("日期不能选择今天之后，请重新选择");
                }
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    @OnClick({R.id.imgDriverCard, R.id.las_step, R.id.next_step, R.id.editDriverDate,R.id.imgDriverLicense})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgDriverCard:
                imgUrl = 1;
                ImgDialog();
                break;
            case R.id.imgDriverLicense:
                imgUrl = 2;
                ImgDialog();
                break;
            case R.id.las_step:
                finish();
                break;
            case R.id.next_step:
                initView();
                break;
            case R.id.editDriverDate:
                datePicker.show();
                break;
        }
    }

    private void initView() {
        username = editRealName.getText().toString();
        drivingNum = editDriverNum.getText().toString();
        startdate = editDriverDate.getText().toString();
        regBean = new DriverRegBean();
        regBean.setPhone(phone);
        regBean.setUsername(username);
        regBean.setDrivingn(drivingNum);
        regBean.setStartdate(startdate);
        regBean.setDriving(img_driver_url);
        regBean.setLicense(img_license_url);
        if (TextUtils.isEmpty(username)) {
            ToastUtil.showToast(this, "请输入姓名");
        } else if (TextUtils.isEmpty(drivingNum)) {
            ToastUtil.showToast(this, "请输入驾驶证编号");
        } else if (TextUtils.isEmpty(startdate)) {
            ToastUtil.showToast(this, "请选择你初次领取驾驶证日期");
        } else if (TextUtils.isEmpty(img_driver_url)) {
            ToastUtil.showToast(this, "请上传驾驶证照片");
        }else if (TextUtils.isEmpty(img_license_url)) {
            ToastUtil.showToast(this, "请上传行驶证照片");
        } else {
            Intent intent = new Intent(this, DriverAuthThreeActivity.class);
            intent.putExtra("DriverRegBean", JSON.toJSONString(regBean));
            startActivity(intent);
        }
    }

    /**
     * 选择身份证照片窗口
     */
    private void ImgDialog() {
        new MyBottomSheetDialog(this).build().setFirstItem("拍照", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    int permissionResult = ContextCompat.checkSelfPermission(DriverAuthTwoActivity.this, Manifest.permission.CAMERA);
                    if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                        openCamera();
                    } else if (permissionResult == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(DriverAuthTwoActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                    }
                } else {
                    openCamera();
                }
            }
        }).setSecondItem("从手机相册选择", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    int permissionResult = ContextCompat.checkSelfPermission(DriverAuthTwoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                        selectPhoto();
                    } else if (permissionResult == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(DriverAuthTwoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
                    }
                } else {
                    selectPhoto();
                }
            }
        }).show();

    }

    private static final int PERMISSION_REQUEST_STORAGE = 200;
    private static final int PERMISSION_REQUEST_CAMERA = 201;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE: {
                int permissionResult = grantResults[0];
                if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                    //扫描照片
                    selectPhoto();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.album_dialog_permission_failed)
                            .setMessage(R.string.album_permission_storage_failed_hint)
                            .setPositiveButton(R.string.album_dialog_sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                break;
            }
            case PERMISSION_REQUEST_CAMERA: {
                int permissionResult = grantResults[0];
                if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.album_dialog_permission_failed)
                            .setMessage(R.string.album_permission_camera_failed_hint)
                            .setPositiveButton(R.string.album_dialog_sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    /**
     * 拍照
     */
    public void openCamera() {

        destoryImage();
        if (checkSDcard()) {
            // 先创建父目录，如果新创建一个文件的时候，父目录没有存在，那么必须先创建父目录，再新建文件。
            if (!mars_file.exists()) {
                mars_file.mkdirs();
            }
            file = new File(saveDir, ".jpg");
            file.delete();
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "照片创建失败!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
        }

    }

    // 选择照片
    public void selectPhoto() {
        if (checkSDcard()) {
            // 先创建父目录，如果新创建一个文件的时候，父目录没有存在，那么必须先创建父目录，再新建文件。
            if (!mars_file.exists()) {
                mars_file.mkdirs();
            }
            file = new File(saveDir, ".jpg");
            file.delete();
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "照片创建失败!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
        innerIntent.setType("image/*"); // 查看类型
        // StringIMAGE_UNSPECIFIED="image/*";详细的类型在com.google.android.mms.ContentType中
        Intent wrapperIntent = Intent.createChooser(innerIntent, null);
        startActivityForResult(wrapperIntent, SELECT_PHOTO_REQUEST_CODE);
    }

    private void destoryImage() {
        if (photo != null) {
            photo.recycle();
            photo = null;
        }
    }

    /**
     * 检查SD卡是否存在
     *
     * @return
     */
    public final boolean checkSDcard() {
        boolean flag = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (!flag) {
            Toast.makeText(this, "请插入手机存储卡再使用本功能", Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

    public static final int TAKE_PHOTO_REQUEST_CODE = 1;
    public static final int SELECT_PHOTO_REQUEST_CODE = 2;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == TAKE_PHOTO_REQUEST_CODE || requestCode == SELECT_PHOTO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = null;
                if (requestCode == SELECT_PHOTO_REQUEST_CODE) {
                    uri = intent.getData();
                } else if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
                    if (file != null && file.exists()) {
                        BitmapFactory.Options option = new BitmapFactory.Options();
                        option.inSampleSize = 2;
                        try {
                            photo = BitmapFactory.decodeFile(file.getPath(), option);
                            File myCaptureFile = new File(saveDir + "/" + file.getName());
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                            photo.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                            bos.flush();
                            bos.close();
                            // Log.d(TAG, saveDir + "/"+file.getName());
                            uri = Uri.fromFile(new File(saveDir + "/" + file.getName()));
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }
                if (uri != null) {

                    Bitmap bm = getBitmapFromUri(uri);
                    // 转换成图片，保存到sdcard中
                    bm = ratio(bm, 720, 480);
                    String npicName = "" + System.currentTimeMillis();
                    saveMyBitmap(bm, npicName);

                }

            }
        }

    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {

            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Compress image by size, this will modify image width/height. Used to get thumbnail
     *
     * @param image
     * @param pixelW target pixel of width
     * @param pixelH target pixel of height
     * @return
     */
    public Bitmap ratio(Bitmap image, float pixelW, float pixelH) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if (os.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        // 压缩好比例大小后再进行质量压缩
        // return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }

    // Bitmap 转换成jpeg并保存
    public void saveMyBitmap(Bitmap mBitmap, String bitName) {
        File f = new File(saveDir + "/" + bitName + ".jpg");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);// 30 是压缩率，表示压缩70%; 如果不压缩是100，表示压缩率为0
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
            if (imgUrl==1){
                img_driver_url = saveDir + "/" + bitName + ".jpg";
                imgDriverCard.setImageBitmap(createImageThumbnail(img_driver_url));
            }else if (imgUrl==2){
                img_license_url = saveDir + "/" + bitName + ".jpg";
                imgDriverLicense.setImageBitmap(createImageThumbnail(img_license_url));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap createImageThumbnail(String filePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);

        opts.inSampleSize = computeSampleSize(opts, -1, 128 * 128);
        opts.inJustDecodeBounds = false;

        try {
            bitmap = BitmapFactory.decodeFile(filePath, opts);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bitmap;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

}
